package com.example.demo.controller;

import java.time.Instant;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.CourseDTO;
import com.example.demo.response.Response;
import com.example.demo.response.ResponseEnum;
import com.example.demo.service.CourseService;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping(path = "api/v1/course")
public class CourseController {

    private final CourseService courseService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Response<String> registerNewCourse(@RequestBody CourseDTO course) {
        courseService.addNewCourse(course);
        return Response.<String>builder()
                .data(BodyResponses.CREATED)
                .statusCode(ResponseEnum.SUCCESS.getStatusCode())
                .statusMessage(ResponseEnum.SUCCESS.getStatusMessage())
                .timestamp(Instant.now().toString()).build();
    }

    @PatchMapping(path = "{courseId}")
    public Response<String> updateCourse(@PathVariable("courseId") String courseId, @RequestBody CourseDTO courseDTO) {
        courseService.updateCourse(courseId, courseDTO);

        return Response.<String>builder()
                .data(BodyResponses.UPDATED)
                .statusCode(ResponseEnum.SUCCESS.getStatusCode())
                .statusMessage(ResponseEnum.SUCCESS.getStatusMessage())
                .timestamp(Instant.now().toString()).build();
    }

    @DeleteMapping(path = "{courseId}")
    public String deleteCourseById(@PathVariable("courseId") String courseId) {
        courseService.deleteCourseById(courseId);
        return BodyResponses.DELETED;
    }

    @GetMapping(path = "{courseId}")
    public Response<CourseDTO> getCourseById(@PathVariable("courseId") String courseId) {
        return Response.<CourseDTO>builder()
                .data(courseService.getCourseById(courseId))
                .statusCode(ResponseEnum.SUCCESS.getStatusCode())
                .statusMessage(ResponseEnum.SUCCESS.getStatusMessage())
                .timestamp(Instant.now().toString()).build();
    }

    @GetMapping
    public Response<List<CourseDTO>> getCourses() {
        return Response.<List<CourseDTO>>builder()
                .data(courseService.getCourses())
                .statusCode(ResponseEnum.SUCCESS.getStatusCode())
                .statusMessage(ResponseEnum.SUCCESS.getStatusMessage())
                .timestamp(Instant.now().toString())
                .build();
    }
}
