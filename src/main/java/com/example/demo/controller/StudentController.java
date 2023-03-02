package com.example.demo.controller;

import java.util.List;

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

import com.example.demo.dto.StudentDTO;
import com.example.demo.entity.Student;
import com.example.demo.mapper.StudentMapper;
import com.example.demo.serviceIml.StudentServiceImpl;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping(path = "api/v1/student")
public class StudentController {

    private final StudentServiceImpl studentServiceImpl;

    private final StudentMapper studentMapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public String registerNewStudent(@RequestBody Student student) {
        studentServiceImpl.addNewStudent(student);
        return BodyResponses.CREATED;
    }

    @PutMapping(path = "{studentId}")
    public String updateStudent(@PathVariable("studentId") int studentId, @RequestBody Student updateStudent) {
        studentServiceImpl.updateStudent(studentId, updateStudent);

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
    public StudentDTO getStudentById(@PathVariable("studentId") int studentId) {
        Student student = studentServiceImpl.getStudentById(studentId);
        return studentMapper.studentToDto(student);
    }

    @GetMapping(path = "by-email/{studentEmail}")
    public StudentDTO getStudentByEmail(@PathVariable(value = "studentEmail") String email) {
        Student student = studentServiceImpl.getStudentByEmail(email);
        return studentMapper.studentToDto(student);
    }

    @GetMapping
    public List<StudentDTO> getStudents() {
        List<Student> students = studentServiceImpl.getStudents();
        return studentMapper.studentToDto(students);
    }
}
