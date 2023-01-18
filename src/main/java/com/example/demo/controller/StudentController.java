package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entity.Student;
import com.example.demo.serviceIml.StudentServiceImpl;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping(path = "api/v1/student")
public class StudentController {

    private final StudentServiceImpl studentServiceImpl;

    @Autowired
    public StudentController(StudentServiceImpl studentServiceImpl) {
        this.studentServiceImpl = studentServiceImpl;
    }

    @GetMapping
    public List<Student> getStudents() {
        return studentServiceImpl.getStudents();
    }

    @GetMapping(path = "by-id/{studentId}")
    public Student getStudentById(@PathVariable("studentId") Long studentId) {
        return studentServiceImpl.getStudentById(studentId);
    }

    @GetMapping(path = "by-email/{studentEmail}")
    public Student getStudentByEmail(@PathVariable(value = "studentEmail") String email) {
        return studentServiceImpl.getStudentByEmail(email);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public String registerNewStudent(@RequestBody Student student) {
        studentServiceImpl.addNewStudent(student);
        return BodyResponses.CREATED;
    }
    @DeleteMapping(path = "{studentId}")
    public String deleteStudent(@PathVariable("studentId") Long studentId) {
        studentServiceImpl.deleteStudent(studentId);
        return BodyResponses.DELETED;
    }

    @PutMapping(path = "{studentId}")
    public String updateStudent(
            @PathVariable("studentId") Long studentId,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String email) {
        studentServiceImpl.updateStudent(studentId, name, email);
        
        return BodyResponses.UPDATED;
    }
}
