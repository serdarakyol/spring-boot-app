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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entity.Teacher;
import com.example.demo.serviceIml.TeacherServiceImpl;

@RestController
@RequestMapping(path = "api/v1")
public class TeacherController {

    private final TeacherServiceImpl teacherServiceImpl;

    @Autowired
    public TeacherController(TeacherServiceImpl teacherServiceImpl) {
        this.teacherServiceImpl = teacherServiceImpl;
    }

    @PostMapping(path = "teacher")
    @ResponseStatus(HttpStatus.CREATED)
    public String registerNewTeacher(@RequestBody Teacher teacher) {
        teacherServiceImpl.addNewTeacher(teacher);
        return BodyResponses.CREATED;
    }

    @PutMapping(path = "teacher/{teacherId}")
    public String updateTeacherById(
        @PathVariable("teacherId") int teacherId,
        @RequestParam(required = false) String teacherName,
        @RequestParam(required = false) String teacherEmail
    ) {
        teacherServiceImpl.updateTeacherById(teacherId, teacherName, teacherEmail);
        return BodyResponses.UPDATED;
    }

    @DeleteMapping(path = "teacher/{teacherId}")
    public String deleteTeacher(@PathVariable("teacherId") int teacherId) {
        teacherServiceImpl.deleteTeacherById(teacherId);
        return BodyResponses.DELETED;
    }

    @GetMapping(path = "teacher/by-id/{teacherId}")
    public Teacher getTeacherById(@PathVariable("teacherId") int teacherId) {
        return teacherServiceImpl.getTeacherById(teacherId);
    }

    @GetMapping(path = "teacher/by-email/{teacherEmail}")
    public Teacher getTeacherById(@PathVariable("teacherEmail") String teacherEmail) {
        return teacherServiceImpl.getTeacherByEmail(teacherEmail);
    }

    @GetMapping(path = "teachers")
    public List<Teacher> getTeachers() {
        return teacherServiceImpl.getTeachers();
    }
    
}
