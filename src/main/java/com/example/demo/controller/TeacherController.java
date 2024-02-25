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

import com.example.demo.dto.TeacherDTO;
import com.example.demo.response.Response;
import com.example.demo.response.ResponseEnum;
import com.example.demo.service.TeacherService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "api/v1/teacher")
public class TeacherController {

    private final TeacherService teacherService;

    @PostMapping
    public Response<String> registerNewTeacher(@RequestBody TeacherDTO teacherDTO) {
        return Response.<String>builder()
                .data(teacherService.addNewTeacher(teacherDTO))
                .statusCode(ResponseEnum.SUCCESS.getStatusCode())
                .statusMessage(ResponseEnum.SUCCESS.getStatusMessage())
                .timestamp(Instant.now().toString())
                .build();
    }

    @PutMapping(path = "{teacherEmail}")
    public Response<String> updateTeacherById(@PathVariable("teacherEmail") String teacherEmail, @RequestBody TeacherDTO teacherDTO) {
        return Response.<String>builder()
                .data(teacherService.updateTeacherById(teacherEmail, teacherDTO))
                .statusCode(ResponseEnum.SUCCESS.getStatusCode())
                .statusMessage(ResponseEnum.SUCCESS.getStatusMessage())
                .timestamp(Instant.now().toString())
                .build();
    }

    @DeleteMapping(path = "by-id/{teacherId}")
    public Response<String> deleteTeacher(@PathVariable("teacherId") int teacherId) {
        return Response.<String>builder()
                .data(teacherService.deleteTeacherById(teacherId))
                .statusCode(ResponseEnum.SUCCESS.getStatusCode())
                .statusMessage(ResponseEnum.SUCCESS.getStatusMessage())
                .timestamp(Instant.now().toString())
                .build();
    }

    @DeleteMapping(path = "by-email/{teacherEmail}")
    public Response<String> deleteTeacher(@PathVariable("teacherEmail") String teacherEmail) {
        return Response.<String>builder()
                .data(teacherService.deleteByEmail(teacherEmail))
                .statusCode(ResponseEnum.SUCCESS.getStatusCode())
                .statusMessage(ResponseEnum.SUCCESS.getStatusMessage())
                .timestamp(Instant.now().toString())
                .build();
    }

    @GetMapping(path = "by-id/{teacherId}")
    public Response<TeacherDTO> getTeacherById(@PathVariable("teacherId") int teacherId) {
        return Response.<TeacherDTO>builder()
                .data(teacherService.getTeacherById(teacherId))
                .statusCode(ResponseEnum.SUCCESS.getStatusCode())
                .statusMessage(ResponseEnum.SUCCESS.getStatusMessage())
                .timestamp(Instant.now().toString()).build();
    }

    @GetMapping(path = "by-email/{teacherEmail}")
    public Response<TeacherDTO> getTeacherById(@PathVariable("teacherEmail") String teacherEmail) {
        return Response.<TeacherDTO>builder()
                .data(teacherService.getTeacherByEmail(teacherEmail))
                .statusCode(ResponseEnum.SUCCESS.getStatusCode())
                .statusMessage(ResponseEnum.SUCCESS.getStatusMessage())
                .timestamp(Instant.now().toString()).build();
    }

    @GetMapping
    public Response<List<TeacherDTO>> getTeachers() {
        return Response.<List<TeacherDTO>>builder()
                .data(teacherService.getTeachers())
                .statusCode(ResponseEnum.SUCCESS.getStatusCode())
                .statusMessage(ResponseEnum.SUCCESS.getStatusMessage())
                .timestamp(Instant.now().toString()).build();
    }
}
