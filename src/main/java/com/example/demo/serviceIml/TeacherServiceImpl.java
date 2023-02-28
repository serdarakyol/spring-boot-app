package com.example.demo.serviceIml;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

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
    private final String teacherNotExistByIdMsg = "Teacher does not exist with ID: ";
    private final String teacherNotExistByEmailMsg = "Teacher does not exist with E-MAIL: ";

    @Autowired
    private final TeacherRepository teacherRepository;

    @Override
    public void addNewTeacher(Teacher teacher) {
        Optional<Teacher> teacherOptional = teacherRepository.findTeacherByTeacherEmail(teacher.getTeacherEmail());
        if (teacherOptional.isPresent()) {
            throw new BadRequestException(CommonResponses.emailTakenMsg);
        }

        // check if mail is valid
        if (!Utils.isMailValid(teacher.getTeacherEmail())) {
            throw new BadRequestException(CommonResponses.emailNotValidMsg);
        }
        teacherRepository.save(teacher);
    }

    @Transactional
    @Override
    public void updateTeacherById(int teacherId, String teacherName, String teacherEmail, LocalDate teacherDOB) {
        Teacher teacher = teacherRepository.findById(teacherId)
                .orElseThrow(() -> new NotFoundException(teacherNotExistByIdMsg + teacherId));

        // Teacher name processing
        if (teacherName == null || teacherName.length() < 2 || Objects.equals(teacher.getTeacherName(), teacherName)) {
            throw new BadRequestException(CommonResponses.nameNotValidMsg);
        }
        teacher.setTeacherName(teacherName);

        // Teacher email processing
        if (!Utils.isMailValid(teacherEmail)) {
            throw new BadRequestException(CommonResponses.emailNotValidMsg);
        }

        if (teacherEmail == null || teacherEmail.length() < 2 || Objects.equals(teacher.getTeacherEmail(), teacherEmail)) {
            throw new BadRequestException(CommonResponses.emailNotValidMsg);
        }

        Optional<Teacher> teacherOptional = teacherRepository.findTeacherByTeacherEmail(teacherEmail);
        // check if e-mail taken
        if (teacherOptional.isPresent()) {
            throw new BadRequestException(CommonResponses.emailTakenMsg);
        }
        teacher.setTeacherEmail(teacherEmail);

        // Teacher DOB processing
        if (teacherDOB == null || Objects.equals(teacherDOB, teacher.getTeacherDOB())) {
            throw new BadRequestException(CommonResponses.birthdayNotValidMsg);
        }
        teacher.setTeacherDOB(teacherDOB);
    }

    @Override
    public void deleteTeacherById(int teacherId) {
        boolean isExist = teacherRepository.existsById(teacherId);
        if (!isExist) {
            throw new NotFoundException(teacherNotExistByIdMsg);
        }
        teacherRepository.deleteById(teacherId);
    }

    @Override
    public void deleteTeacherByMail(String teacherEmail) {
        boolean isTeacherExist = teacherRepository.findTeacherByTeacherEmail(teacherEmail).isPresent();
        if (!isTeacherExist) {
            throw new NotFoundException(teacherNotExistByIdMsg);
        }
        teacherRepository.deleteTeacherByMail(teacherEmail);
    }

    @Override
    public Teacher getTeacherById(int teacherId) {
        Teacher teacher = teacherRepository.findById(teacherId)
                .orElseThrow(() -> new NotFoundException(teacherNotExistByIdMsg + teacherId));
        return teacher;
    }

    @Override
    public Teacher getTeacherByEmail(String teacherEmail) {
        Optional<Teacher> teacherOptional = teacherRepository.findTeacherByTeacherEmail(teacherEmail);
        if (teacherOptional.isEmpty()) {
            throw new NotFoundException(teacherNotExistByEmailMsg + teacherEmail);
        }
        return teacherOptional.get();
    }

    @Override
    public List<Teacher> getTeachers() {
        return teacherRepository.findAll();
    }
}
