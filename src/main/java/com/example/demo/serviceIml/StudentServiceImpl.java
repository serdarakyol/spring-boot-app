package com.example.demo.serviceIml;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.controller.CommonResponses;
import com.example.demo.entity.Student;
import com.example.demo.exception.BadRequestException;
import com.example.demo.exception.NotFoundException;
import com.example.demo.repository.StudentRepository;
import com.example.demo.service.StudentService;
import com.example.demo.utils.Utils;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService{
	private final String studentNotExistByIdMsg = "Student doesn't exist, student ID: ";
	private final String studentNotExistByEmailMsg = "Student doesn't exist, student E-MAIL: ";

	private final StudentRepository studentRepository;

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
	public Student getStudentById(int studentId) {
		Student student = studentRepository.findById(studentId)
				.orElseThrow(() -> new NotFoundException(studentNotExistByIdMsg + studentId));
		return student;
	}

	@Override
	public Student getStudentByEmail(String studentEmail){
		Optional<Student> studentOptional = studentRepository.findStudentByEmail(studentEmail);
		if (studentOptional.isEmpty()) {
			throw new NotFoundException(studentNotExistByEmailMsg + studentEmail);
		}
		return studentOptional.get();
	}

	@Override
	public void addNewStudent(Student student) {
		Optional<Student> studentOptional = studentRepository.findStudentByEmail(student.getStudentEmail());
		if (studentOptional.isPresent()) {
			throw new BadRequestException(CommonResponses.emailTakenMsg);
		}

		// check if mail is valid
		if (!Utils.isMailValid(student.getStudentEmail())){
			throw new BadRequestException(CommonResponses.emailNotValidMsg);
		}
		studentRepository.save(student);
	}

	@Override
	public void deleteStudentById(int studentId) {
		boolean isStudentExist = studentRepository.existsById(studentId);
		if (!isStudentExist) {
			throw new NotFoundException(studentNotExistByIdMsg + studentId);
		}
		studentRepository.deleteById(studentId);
	}

	@Override
	public void deleteStudentByEmail(String studentEmail) {
		boolean isStudentExist = studentRepository.findStudentByEmail(studentEmail).isPresent();
		if (!isStudentExist) {
			throw new NotFoundException(studentNotExistByIdMsg + studentEmail);
		}

		studentRepository.deleteStudentByEmail(studentEmail);
	}

	@Transactional
	@Override
	public void updateStudent(int studentId, String studentName, String studentEmail) {
		Student student = studentRepository.findById(studentId)
				.orElseThrow(() -> new NotFoundException(studentNotExistByIdMsg + studentId));

		if (studentName != null && studentName.length() > 0 && !Objects.equals(student.getStudentName(), studentName)) {
			student.setStudentName(studentName);
		}

		if (!Utils.isMailValid(studentEmail)) {
			throw new BadRequestException(CommonResponses.emailNotValidMsg);
		}

		if (studentEmail != null && studentEmail.length() > 0 && !Objects.equals(student.getStudentEmail(), studentEmail)) {
			Optional<Student> studentOptional = studentRepository.findStudentByEmail(studentEmail);
			// check if e-mail taken
			if (studentOptional.isPresent()) {
				throw new BadRequestException(CommonResponses.emailTakenMsg);
			}
			student.setStudentEmail(studentEmail);
		}
	}

}
