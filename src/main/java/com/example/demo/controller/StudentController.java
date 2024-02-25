package com.example.demo.controller;

import java.time.Instant;
import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.StudentDTO;
import com.example.demo.entity.Student;
import com.example.demo.response.Response;
import com.example.demo.response.ResponseEnum;
import com.example.demo.service.StudentService;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;

@Tag(name = "Student", description = "Student module")
@RestController
@AllArgsConstructor
@RequestMapping(path = "api/v1/student")
public class StudentController {

    private final StudentService studentService;

    @PostMapping
    public Response<String> registerNewStudent(@RequestBody StudentDTO student) {
        return Response.<String>builder()
                .data(studentService.addNewStudent(student))
                .statusCode(ResponseEnum.SUCCESS.getStatusCode())
                .statusMessage(ResponseEnum.SUCCESS.getStatusMessage())
                .timestamp(Instant.now().toString()).build();
    }

    @PutMapping(path = "{studentEmail}")
    public Response<String> updateStudent(@PathVariable("studentEmail") String studentEmail, @RequestBody Student updateStudent) {
        return Response.<String>builder()
                .data(studentService.updateStudent(studentEmail, updateStudent))
                .statusCode(ResponseEnum.SUCCESS.getStatusCode())
                .statusMessage(ResponseEnum.SUCCESS.getStatusMessage())
                .timestamp(Instant.now().toString()).build();
    }

    @DeleteMapping(path = "by-id/{studentId}")
    public Response<String> deleteStudentById(@PathVariable("studentId") int studentId) {
        return Response.<String>builder()
                .data(studentService.deleteStudentById(studentId))
                .statusCode(ResponseEnum.SUCCESS.getStatusCode())
                .statusMessage(ResponseEnum.SUCCESS.getStatusMessage())
                .timestamp(Instant.now().toString()).build();
    }

    @DeleteMapping(path = "by-email/{studentEmail}")
    public Response<String> deleteStudentByEmail(@PathVariable("studentEmail") String studentEmail) {
        return Response.<String>builder()
                .data(studentService.deleteStudentByEmail(studentEmail))
                .statusCode(ResponseEnum.SUCCESS.getStatusCode())
                .statusMessage(ResponseEnum.SUCCESS.getStatusMessage())
                .timestamp(Instant.now().toString()).build();
    }

    @GetMapping(path = "by-id/{studentId}")
    public Response<StudentDTO> getStudentById(@PathVariable("studentId") int studentId) {
        return Response.<StudentDTO>builder()
                .data(studentService.getStudentById(studentId))
                .statusCode(ResponseEnum.SUCCESS.getStatusCode())
                .statusMessage(ResponseEnum.SUCCESS.getStatusMessage())
                .timestamp(Instant.now().toString()).build();
    }

    @GetMapping(path = "by-email/{studentEmail}")
    public Response<StudentDTO> getStudentByEmail(@PathVariable(value = "studentEmail") String email) {
        return Response.<StudentDTO>builder()
                .data(studentService.getStudentByEmail(email))
                .statusCode(ResponseEnum.SUCCESS.getStatusCode())
                .statusMessage(ResponseEnum.SUCCESS.getStatusMessage())
                .timestamp(Instant.now().toString()).build();
    }

    @GetMapping
    public Response<List<StudentDTO>> getStudents() {
        return Response.<List<StudentDTO>>builder()
                .data(studentService.getStudents())
                .statusCode(ResponseEnum.SUCCESS.getStatusCode())
                .statusMessage(ResponseEnum.SUCCESS.getStatusMessage())
                .timestamp(Instant.now().toString()).build();
    }

}
