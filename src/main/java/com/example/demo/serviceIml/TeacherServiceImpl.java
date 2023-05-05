package com.example.demo.serviceIml;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.controller.CommonResponses;
import com.example.demo.entity.Teacher;
import com.example.demo.exception.BadRequestException;
import com.example.demo.exception.NotFoundException;
import com.example.demo.repository.TeacherRepository;
import com.example.demo.service.TeacherService;
import com.example.demo.utils.Utils;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TeacherServiceImpl implements TeacherService {

    static Logger log = LoggerFactory.getLogger(TeacherServiceImpl.class);

    @Autowired
    private final TeacherRepository teacherRepository;

    @Override
    public void addNewTeacher(Teacher teacher) {
        String teacherEmail = teacher.getEmail();
        if (teacherRepository.isExistByEmail(teacherEmail)) {
            log.error(teacherExistMsg + "E-MAIL: " + teacherEmail);
            throw new BadRequestException(teacherExistMsg + "E-MAIL: " + teacherEmail);
        }

        // check if mail is valid
        if (!Utils.isMailValid(teacher.getEmail())) {
            log.error(CommonResponses.emailNotValidMsg);
            throw new BadRequestException(CommonResponses.emailNotValidMsg);
        }

        if (teacher.getName().length() < 2) {
            log.error(CommonResponses.nameNotValidMsg);
            throw new BadRequestException(CommonResponses.nameNotValidMsg);
        }

        teacherRepository.save(teacher);
        log.info("New teacher saved: {}", teacher.toString());
    }

    @Transactional
    @Override
    public void updateTeacherById(String teacherEmail, Teacher updatedTeacher) {
        Optional<Teacher> teacher = teacherRepository.findByEmail(teacherEmail);
        // check if the teacher is exist
        if (!teacher.isPresent()) {
            log.error(teacherNotExistMsg + "E-MAIL: " + teacherEmail);
            throw new NotFoundException(teacherNotExistMsg + "E-MAIL: " + teacherEmail);
        }

        Teacher currentTeacher = teacher.get();

        // Teacher name processing
        if (updatedTeacher.getName().length() < 2) {
            log.error(CommonResponses.nameNotValidMsg);
            throw new BadRequestException(CommonResponses.nameNotValidMsg);
        }
        currentTeacher.setName(updatedTeacher.getName());

        // Check teacher email if valid
        if (!Utils.isMailValid(updatedTeacher.getEmail())) {
            log.error(CommonResponses.emailNotValidMsg);
            throw new BadRequestException(CommonResponses.emailNotValidMsg);
        }

        // check if e-mail taken
        if (!teacherEmail.equals(updatedTeacher.getEmail())) {
            if (teacherRepository.isExistByEmail(updatedTeacher.getEmail())) {
                log.error(CommonResponses.emailTakenMsg);
                throw new BadRequestException(CommonResponses.emailTakenMsg);
            }
            currentTeacher.setEmail(updatedTeacher.getEmail());
        }

        // no need process for the DOB becase it's @NonNull in the entity class
        currentTeacher.setDob(updatedTeacher.getDob());
        log.info("Teacher updated: {}", currentTeacher.toString());
    }

    @Override
    public void deleteTeacherById(int teacherId) {
        if (!teacherRepository.existsById(teacherId)) {
            log.error(teacherNotExistMsg + "ID: " + teacherId);
            throw new NotFoundException(teacherNotExistMsg + "ID: " + teacherId);
        }
        teacherRepository.deleteById(teacherId);
        log.info(teacherSuccessfullyDeleteMsg + "ID: " + teacherId);
    }

    @Override
    public void deleteByEmail(String teacherEmail) {
        if (!teacherRepository.isExistByEmail(teacherEmail)) {
            log.error(teacherNotExistMsg + "E-MAIL: " + teacherEmail);
            throw new NotFoundException(teacherNotExistMsg + "E-MAIL: " + teacherEmail);
        }
        teacherRepository.deleteByEmail(teacherEmail);
        log.info(teacherSuccessfullyDeleteMsg + "E-MAIL: " + teacherEmail);
    }

    @Override
    public Teacher getTeacherById(int teacherId) {
        Optional<Teacher> teacherRecord = teacherRepository.findById(teacherId);
        if (!teacherRecord.isPresent()) {
            log.error(teacherNotExistMsg + "ID: " + teacherId);
            throw new NotFoundException(teacherNotExistMsg + "ID: " + teacherId);
        }
        Teacher teacher = teacherRecord.get();
        log.info(teacherSuccessfullyFound + teacher.toString());
        return teacher;
    }

    @Override
    public Teacher getTeacherByEmail(String teacherEmail) {
        Optional<Teacher> teacherOptional = teacherRepository.findByEmail(teacherEmail);
        if (!teacherOptional.isPresent()) {
            log.error(teacherNotExistMsg + "E-MAIL: " + teacherEmail);
            throw new NotFoundException(teacherNotExistMsg + "E-MAIL: " + teacherEmail);
        }
        Teacher teacher = teacherOptional.get();
        log.info(teacherSuccessfullyFound + teacher.toString());
        return teacher;
    }

    @Override
    public List<Teacher> getTeachers() {
        log.info("getTeachers: All teachers are called.");
        return teacherRepository.findAll();
    }
}
