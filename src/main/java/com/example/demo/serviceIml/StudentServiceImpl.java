package com.example.demo.serviceIml;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.controller.BodyResponses;
import com.example.demo.controller.CommonResponses;
import com.example.demo.dto.StudentDTO;
import com.example.demo.entity.Student;
import com.example.demo.exception.BadRequestException;
import com.example.demo.exception.NotFoundException;
import com.example.demo.mapper.StudentMapper;
import com.example.demo.repository.StudentRepository;
import com.example.demo.service.StudentService;
import com.example.demo.utils.Utils;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {

    static Logger log = LoggerFactory.getLogger(StudentServiceImpl.class);

    @Autowired
    private final StudentRepository studentRepository;

    @Autowired
    private final StudentMapper studentMapper;

    @Override
    public String addNewStudent(StudentDTO studentDTO) {
        String studentEmail = studentDTO.getEmail();
        if (studentRepository.isExistByEmail(studentEmail)) {
            log.error(studentExist + "E-MAIL: " + studentEmail);
            throw new BadRequestException(studentExist + "E-MAIL: " + studentEmail);
        }

        // check if mail is valid
        if (!Utils.isMailValid(studentDTO.getEmail())) {
            log.error(CommonResponses.emailNotValidMsg);
            throw new BadRequestException(CommonResponses.emailNotValidMsg);
        }

        if (studentDTO.getName().length() < 2) {
            log.error(CommonResponses.nameNotValidMsg);
            throw new BadRequestException(CommonResponses.nameNotValidMsg);
        }
        Student student = studentMapper.toEntity(studentDTO);
        studentRepository.save(student);
        log.info("New student saved with ID: {}", student.getId());
        return BodyResponses.CREATED;
    }

    @Transactional
    @Override
    public String updateStudent(String studentEmail, final Student updateStudent) {
        Optional<Student> student = studentRepository.findByEmail(studentEmail);
        // check if the student is exist
        if (!student.isPresent()) {
            log.error(studentNotExistMsg + "E-mail: " + studentEmail);
            throw new NotFoundException(studentNotExistMsg + "E-mail: " + studentEmail);
        }

        Student currentStudent = student.get();

        // Student name processing
        if (updateStudent.getName().length() < 2) {
            log.error(CommonResponses.nameNotValidMsg);
            throw new BadRequestException(CommonResponses.nameNotValidMsg);
        }
        currentStudent.setName(updateStudent.getName());

        // Check student email if valid
        if (!Utils.isMailValid(updateStudent.getEmail())) {
            log.error(CommonResponses.emailNotValidMsg);
            throw new BadRequestException(CommonResponses.emailNotValidMsg);
        }

        // check if e-mail taken
        if (!studentEmail.equals(updateStudent.getEmail())) {
            if (studentRepository.isExistByEmail(updateStudent.getEmail())) {
                log.error(CommonResponses.emailTakenMsg);
                throw new BadRequestException(CommonResponses.emailTakenMsg);
            }
            currentStudent.setEmail(updateStudent.getEmail());
        }

        // no need process for the DOB becase it's @NonNull in the entity class
        currentStudent.setDob(updateStudent.getDob());
        log.info("Student updated: {} ", currentStudent.toString());
        return BodyResponses.UPDATED;
    }

    @Override
    public String deleteStudentById(int studentId) {
        if (!studentRepository.existsById(studentId)) {
            log.error(studentNotExistMsg + "ID: " + studentId);
            throw new NotFoundException(studentNotExistMsg + "ID: " + studentId);
        }
        studentRepository.deleteById(studentId);
        log.info(studentSuccessfullyDeleteMsg + "ID: " + studentId);
        return BodyResponses.DELETED;
    }

    @Override
    public String deleteStudentByEmail(String studentEmail) {
        if (!studentRepository.isExistByEmail(studentEmail)) {
            log.error(studentNotExistMsg + "E-MAIL: " + studentEmail);
            throw new NotFoundException(studentNotExistMsg + "E-MAIL: " + studentEmail);
        }
        studentRepository.deleteByEmail(studentEmail);
        log.info(studentSuccessfullyDeleteMsg + "E-MAIL: " + studentEmail);
        return BodyResponses.DELETED;
    }

    @Override
    public StudentDTO getStudentById(int studentId) {
        Optional<Student> studentRecord = studentRepository.findById(studentId);
        if (!studentRecord.isPresent()) {
            log.error(studentNotExistMsg + "ID: " + studentId);
            throw new NotFoundException(studentNotExistMsg + "ID: " + studentId);
        }
        Student student = studentRecord.get();
        log.info(studentSuccessfullyFoundMsg + student.toString());
        return studentMapper.toDTO(student);
    }

    @Override
    public StudentDTO getStudentByEmail(String studentEmail) {
        Optional<Student> studentOptional = studentRepository.findByEmail(studentEmail);
        if (!studentOptional.isPresent()) {
            log.error(studentNotExistMsg + "E-MAIL: " + studentEmail);
            throw new NotFoundException(studentNotExistMsg + "E-MAIL: " + studentEmail);
        }
        Student student = studentOptional.get();
        log.info(studentSuccessfullyFoundMsg + student.toString());
        return studentMapper.toDTO(student);
    }

    @Override
    public List<StudentDTO> getStudents() {
        log.info("getStudents: All students are called.");
        return studentRepository.findAll().stream().map(s -> studentMapper.toDTO(s)).toList();
    }

}
