package com.example.demo.Student;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class StudentService {

	private final StudentRepository studentRepository;

	@Autowired
	public StudentService(StudentRepository studentRepository) {
		this.studentRepository = studentRepository;
	}

	public List<Student> getStudents() {
		return studentRepository.findAll();
	}

	public ResponseEntity<String> addNewStudent(Student student) {
		Optional<Student> studentOptional = studentRepository.findStudentByEmail(student.getEmail());
		if (studentOptional.isPresent()) {
			return new ResponseEntity<String>("User exist", HttpStatus.BAD_REQUEST); // IllegalStateException("email taken");
		}
		studentRepository.save(student);
		System.out.println(student);
		return new ResponseEntity<String>("User created", HttpStatus.CREATED);
	}

	public void deleteStudent(Long studentId) {
		boolean isExist = studentRepository.existsById(studentId);
		if (!isExist) {
			throw new IllegalStateException("Student with ID " + studentId + " doesn't exist");
		}
		studentRepository.deleteById(studentId);
	}

	@Transactional
	public void updateStudent(Long studentId, String name, String email) {
		Student student = studentRepository.findById(studentId)
				.orElseThrow(() -> new IllegalStateException("Student with ID " + studentId + " doesn't exist"));

		if (name != null && name.length() > 0 && !Objects.equals(student.getName(), name)) {
			student.setName(name);
		}

		if (email != null && email.length() > 0 && !Objects.equals(student.getEmail(), email)) {
			Optional<Student> studentOptional = studentRepository.findStudentByEmail(email);
			// check if e-mail taken
			if (studentOptional.isPresent()) {
				throw new IllegalStateException("E-mail taken");
			}
			student.setEmail(email);
		}
	}

}
