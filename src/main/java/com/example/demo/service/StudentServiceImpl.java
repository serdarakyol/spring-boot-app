package com.example.demo.service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.entity.Student;
import com.example.demo.exception.BadRequestException;
import com.example.demo.exception.NotFoundException;
import com.example.demo.repository.StudentRepository;
import com.example.demo.utils.Utils;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService{
	private final String emailTakenMsg= "E-mail is taken. Please add another E-mail";
	private final String emailNotValidMsg = "The E-mail is not valid. Please write a valid e-mail";
	private final String studentNotExistMsg = "Student doesn't exist, student ID: ";

	private final StudentRepository studentRepository;
    private Utils emailValidator = new Utils();

	/*
	Note to me!!!
	@RequiredArgsConstructor does the same thing like below code
	@Autowired
	public StudentService(StudentRepository studentRepository) {
		this.studentRepository = studentRepository;
	}
	*/
	@Override
	public List<Student> getStudents() {
		return studentRepository.findAll();
	}

	@Override
	public Student getStudentById(Long studentId) {
		Student student = studentRepository.findById(studentId)
				.orElseThrow(() -> new NotFoundException(studentNotExistMsg + studentId));
		return student;
	}

	@Override
	public void addNewStudent(Student student) {
		Optional<Student> studentOptional = studentRepository.findStudentByEmail(student.getEmail());
		if (studentOptional.isPresent()) {
			throw new BadRequestException(emailTakenMsg);
		}

		// check if mail is valid
		boolean isMailValid = emailValidator.isMailValid(student.getEmail());
		if (!isMailValid){
			throw new BadRequestException(emailNotValidMsg);
		}
		studentRepository.save(student);
	}

	@Override
	public void deleteStudent(Long studentId) {
		boolean isExist = studentRepository.existsById(studentId);
		if (!isExist) {
			throw new NotFoundException(studentNotExistMsg + studentId);
		}
		studentRepository.deleteById(studentId);
	}

	@Transactional
	@Override
	public void updateStudent(Long studentId, String name, String email) {
		Student student = studentRepository.findById(studentId)
				.orElseThrow(() -> new NotFoundException(studentNotExistMsg + studentId));

		if (name != null && name.length() > 0 && !Objects.equals(student.getName(), name)) {
			student.setName(name);
		}

		if (!emailValidator.isMailValid(email)) {
			throw new BadRequestException(emailNotValidMsg);
		}

		if (email != null && email.length() > 0 && !Objects.equals(student.getEmail(), email)) {
			Optional<Student> studentOptional = studentRepository.findStudentByEmail(email);
			// check if e-mail taken
			if (studentOptional.isPresent()) {
				throw new BadRequestException(emailTakenMsg);
			}
			student.setEmail(email);
		}
	}

}
