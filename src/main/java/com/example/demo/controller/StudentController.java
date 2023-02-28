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

import com.example.demo.entity.Student;
import com.example.demo.serviceIml.StudentServiceImpl;

@RestController
@RequestMapping(path = "api/v1/student")
public class StudentController {

    private final StudentServiceImpl studentServiceImpl;

    @Autowired
    public StudentController(StudentServiceImpl studentServiceImpl) {
        this.studentServiceImpl = studentServiceImpl;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public String registerNewStudent(@RequestBody Student student) {
        studentServiceImpl.addNewStudent(student);
        return BodyResponses.CREATED;
    }

    @PutMapping(path = "{studentId}")
    public String updateStudent(@PathVariable("studentId") int studentId, @RequestBody Student student) {
        studentServiceImpl.updateStudent(
                studentId,
                student.getStudentName(),
                student.getStudentEmail(),
                student.getStudentDOB());

        return BodyResponses.UPDATED;
    }

    @DeleteMapping(path = "by-id/{studentId}")
    public String deleteStudentById(@PathVariable("studentId") int studentId) {
        studentServiceImpl.deleteStudentById(studentId);
        return BodyResponses.DELETED;
    }

    @DeleteMapping(path = "by-email/{studentEmail}")
    public String deleteStudentByEmail(@PathVariable("studentEmail") String studentEmail) {
        studentServiceImpl.deleteStudentByEmail(studentEmail);
        return BodyResponses.DELETED;
    }

    @GetMapping(path = "by-id/{studentId}")
    public Student getStudentById(@PathVariable("studentId") int studentId) {
        return studentServiceImpl.getStudentById(studentId);
    }

    @GetMapping(path = "by-email/{studentEmail}")
    public Student getStudentByEmail(@PathVariable(value = "studentEmail") String email) {
        return studentServiceImpl.getStudentByEmail(email);
    }

    @GetMapping
    public List<Student> getStudents() {
        return studentServiceImpl.getStudents();
    }
}
