package com.example.demo.serviceIml;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.controller.CommonResponses;
import com.example.demo.entity.Teacher;
import com.example.demo.exception.BadRequestException;
import com.example.demo.repository.TeacherRepository;
import com.example.demo.service.TeacherService;
import com.example.demo.utils.Utils;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TeacherServiceImpl implements TeacherService {
    @Autowired
    private final TeacherRepository teacherRepository;
    private Utils customUtils = new Utils();
    

    @Override
    public void addNewTeacher(Teacher teacher) {
        Optional<Teacher> teacherOptional = teacherRepository.findTeacherByEmail(teacher.getTeacherEmail());
        if (teacherOptional.isPresent()) {
            throw new BadRequestException(CommonResponses.emailTakenMsg);
        }

        // check if mail is valid
        if ( !customUtils.isMailValid(teacher.getTeacherEmail()) ) {
            throw new BadRequestException(CommonResponses.emailNotValidMsg);
        }
        teacherRepository.save(teacher);
    }
}
