package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.TeacherDTO;
import com.example.demo.entity.Teacher;
import com.example.demo.mapper.TeacherMapper;
import com.example.demo.serviceIml.TeacherServiceImpl;

@RestController
@RequestMapping(path = "api/v1/teacher")
public class TeacherController {

    @Autowired
    private TeacherServiceImpl teacherServiceImpl;

    @Autowired
    private TeacherMapper teacherMapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public String registerNewTeacher(@RequestBody Teacher teacher) {
        teacherServiceImpl.addNewTeacher(teacher);
        return BodyResponses.CREATED;
    }

    @PutMapping(path = "{teacherId}")
    public String updateTeacherById(@PathVariable("teacherId") int teacherId, @RequestBody Teacher teacher) {
        teacherServiceImpl.updateTeacherById(
                teacherId,
                teacher.getTeacherName(),
                teacher.getTeacherEmail(),
                teacher.getTeacherDOB());
        return BodyResponses.UPDATED;
    }

    @DeleteMapping(path = "by-id/{teacherId}")
    public String deleteTeacher(@PathVariable("teacherId") int teacherId) {
        teacherServiceImpl.deleteTeacherById(teacherId);
        return BodyResponses.DELETED;
    }

    @DeleteMapping(path = "by-email/{teacherEmail}")
    public String deleteTeacher(@PathVariable("teacherEmail") String teacherEmail) {
        teacherServiceImpl.deleteTeacherByMail(teacherEmail);
        return BodyResponses.DELETED;
    }

    @GetMapping(path = "by-id/{teacherId}")
    public TeacherDTO getTeacherById(@PathVariable("teacherId") int teacherId) {
        Teacher teacher = teacherServiceImpl.getTeacherById(teacherId);
        return teacherMapper.teacherToDto(teacher);
    }

    @GetMapping(path = "by-email/{teacherEmail}")
    public TeacherDTO getTeacherById(@PathVariable("teacherEmail") String teacherEmail) {
        Teacher teacher = teacherServiceImpl.getTeacherByEmail(teacherEmail);
        return teacherMapper.teacherToDto(teacher);
    }

    @GetMapping
    public List<TeacherDTO> getTeachers() {
        List<Teacher> teachers = teacherServiceImpl.getTeachers();
        return teacherMapper.teacherToDto(teachers);
    }
}
