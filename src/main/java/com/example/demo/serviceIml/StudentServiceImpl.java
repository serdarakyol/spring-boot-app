package com.example.demo.serviceIml;

import java.util.List;

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
            String errorMsg = Utils.stringMerger(studentExist, "E-MAIL: ", studentEmail);
            log.error(errorMsg);
            throw new BadRequestException(errorMsg);
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
        log.info("New student saved with ID: {}", studentDTO.toString());
        return BodyResponses.CREATED;
    }

    @Transactional
    @Override
    public String updateStudent(String studentEmail, StudentDTO updateStudent) {
        Student studentDB = studentRepository.findByEmail(studentEmail).orElseThrow(() -> {
            String errorMsg = Utils.stringMerger(studentNotExistMsg, "E-mail: ", studentEmail);
            log.error(errorMsg);
            throw new NotFoundException(errorMsg);
        });

        // Student name processing
        if (updateStudent.getName().length() < 2) {
            log.error(CommonResponses.nameNotValidMsg);
            throw new BadRequestException(CommonResponses.nameNotValidMsg);
        }
        studentDB.setName(updateStudent.getName());

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
            studentDB.setEmail(updateStudent.getEmail());
        }

        // no need process for the DOB becase it's @NonNull in the entity class
        studentDB.setDob(updateStudent.getDob());
        log.info("Student updated with E-mail: {} ", studentEmail);
        return BodyResponses.UPDATED;
    }

    @Override
    public String deleteStudentById(int studentId) {
        if (!studentRepository.existsById(studentId)) {
            String errorMsg = Utils.stringMerger(studentNotExistMsg, "ID: ", String.valueOf(studentId));
            log.error(errorMsg);
            throw new NotFoundException(errorMsg);
        }
        studentRepository.deleteById(studentId);
        log.info(studentSuccessfullyDeleteMsg, "ID: ", String.valueOf(studentId));
        return BodyResponses.DELETED;
    }

    @Override
    public String deleteStudentByEmail(String studentEmail) {
        if (!studentRepository.isExistByEmail(studentEmail)) {
            String errorMsg = Utils.stringMerger(studentNotExistMsg, "E-mail: ", studentEmail);
            log.error(errorMsg);
            throw new NotFoundException(errorMsg);
        }
        studentRepository.deleteByEmail(studentEmail);
        log.info(studentSuccessfullyDeleteMsg, "E-MAIL: ", studentEmail);
        return BodyResponses.DELETED;
    }

    @Override
    public StudentDTO getStudentById(int studentId) {
        Student student = studentRepository.findById(studentId).orElseThrow(() -> {
            String errorMsg = Utils.stringMerger(studentNotExistMsg, "ID: ", String.valueOf(studentId));
            log.error(errorMsg);
            throw new NotFoundException(errorMsg);
        });

        log.info(studentSuccessfullyFoundMsg, String.valueOf(studentId));
        return studentMapper.toDTO(student);
    }

    @Override
    public StudentDTO getStudentByEmail(String studentEmail) {
        Student student = studentRepository.findByEmail(studentEmail).orElseThrow(() -> {
            String errorMsg = Utils.stringMerger(studentNotExistMsg, "E-mail: ", studentEmail);
            log.error(errorMsg);
            throw new NotFoundException(errorMsg);
        });

        log.info(studentSuccessfullyFoundMsg, studentEmail);
        return studentMapper.toDTO(student);
    }

    @Override
    public List<StudentDTO> getStudents() {
        log.info("getStudents: All students are called.");
        return studentRepository.findAll().stream().map(s -> studentMapper.toDTO(s)).toList();
    }

}
