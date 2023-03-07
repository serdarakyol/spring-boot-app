package com.example.demo.serviceIml;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
public class StudentServiceImpl implements StudentService {
    private final String studentNotExistMsg = "Student doesn't exist with ";
    private final String studentSuccessfullyDeleteMsg = "Student successfully deleted with ";
    private final String studentSuccessfullyFoundMsg = "Student successfully found: ";
    private final String studentExist = "Student already exist with ";

    static Logger log = LoggerFactory.getLogger(StudentServiceImpl.class);

    @Autowired
    private final StudentRepository studentRepository;

    @Override
    public void addNewStudent(final Student student) {
        String studentEmail = student.getStudentEmail();
        if (studentRepository.isExistByEmail(studentEmail)) {
            log.error(studentExist + "E-MAIL: " + studentEmail);
            throw new BadRequestException(studentExist + "E-MAIL: " + studentEmail);
        }

        // check if mail is valid
        if (!Utils.isMailValid(student.getStudentEmail())) {
            log.error(CommonResponses.emailNotValidMsg);
            throw new BadRequestException(CommonResponses.emailNotValidMsg);
        }

        if (student.getStudentName().length() < 2) {
            log.error(CommonResponses.nameNotValidMsg);
            throw new BadRequestException(CommonResponses.nameNotValidMsg);
        }

        studentRepository.save(student);
        log.info("New student saved: {}", student.toString());
    }

    @Transactional
    @Override
    public void updateStudent(int studentId, final Student updateStudent) {
        // check if the student is exist
        if (!studentRepository.existsById(studentId)) {
            log.error(studentNotExistMsg + "ID: " + studentId);
            throw new NotFoundException(studentNotExistMsg + "ID: " + studentId);
        }

        Student currentStudent = studentRepository.findById(studentId).get();

        // Student name processing
        if (updateStudent.getStudentName().length() < 2) {
            log.error(CommonResponses.nameNotValidMsg);
            throw new BadRequestException(CommonResponses.nameNotValidMsg);
        }
        currentStudent.setStudentName(updateStudent.getStudentName());

        // Check student email if valid
        if (!Utils.isMailValid(updateStudent.getStudentEmail())) {
            log.error(CommonResponses.emailNotValidMsg);
            throw new BadRequestException(CommonResponses.emailNotValidMsg);
        }

        // check if e-mail taken
        if (studentRepository.isExistByEmail(updateStudent.getStudentEmail())) {
            log.error(CommonResponses.emailTakenMsg);
            throw new BadRequestException(CommonResponses.emailTakenMsg);
        }
        currentStudent.setStudentEmail(updateStudent.getStudentEmail());

        // no need process for the DOB becase it's @NonNull in the entity class
        currentStudent.setStudentDOB(updateStudent.getStudentDOB());
        log.info("Student updated: {} ", currentStudent.toString());
    }

    @Override
    public void deleteStudentById(int studentId) {
        if (!studentRepository.existsById(studentId)) {
            log.error(studentNotExistMsg + "ID: " + studentId);
            throw new NotFoundException(studentNotExistMsg + "ID: " + studentId);
        }
        studentRepository.deleteById(studentId);
        log.info(studentSuccessfullyDeleteMsg + "ID: " + studentId);
    }

    @Override
    public void deleteStudentByEmail(String studentEmail) {
        if (!studentRepository.isExistByEmail(studentEmail)) {
            log.error(studentNotExistMsg + "E-MAIL: " + studentEmail);
            throw new NotFoundException(studentNotExistMsg + "E-MAIL: " + studentEmail);
        }
        studentRepository.deleteByEmail(studentEmail);
        log.info(studentSuccessfullyDeleteMsg + "E-MAIL: " + studentEmail);
    }

    @Override
    public Student getStudentById(int studentId) {
        Optional<Student> studentRecord = studentRepository.findById(studentId);
        if (!studentRecord.isPresent()) {
            log.error(studentNotExistMsg + "ID: " + studentId);
            throw new NotFoundException(studentNotExistMsg + "ID: " + studentId);
        }
        Student student = studentRecord.get();
        log.info(studentSuccessfullyFoundMsg + student.toString());
        return student;
    }

    @Override
    public Student getStudentByEmail(String studentEmail) {
        Optional<Student> studentOptional = studentRepository.findByEmail(studentEmail);
        if (!studentOptional.isPresent()) {
            log.error(studentNotExistMsg + "E-MAIL: " + studentEmail);
            throw new NotFoundException(studentNotExistMsg + "E-MAIL: " + studentEmail);
        }
        Student student = studentOptional.get();
        log.info(studentSuccessfullyFoundMsg + student.toString());
        return student;
    }

    @Override
    public List<Student> getStudents() {
        log.info("All students are called.");
        return studentRepository.findAll();
    }
}

/*
 * Note to me!!!
 * 
 * @RequiredArgsConstructor does the same thing like below code
 * 
 * @Autowired
 * public StudentService(StudentRepository studentRepository) {
 * this.studentRepository = studentRepository;
 * }
 */
