package com.example.demo.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entity.Course;
import com.example.demo.serviceIml.CourseServiceIml;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping(path = "api/v1/course")
public class CourseController {

    private final CourseServiceIml courseServiceImpl;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public String registerNewCourse(@RequestBody Course course) {
        courseServiceImpl.addNewCourse(course);
        return BodyResponses.CREATED;
    }

    @PutMapping(path = "{courseId}")
    public String updateCourse(@PathVariable("courseId") String courseId, @RequestBody Course updateCourse) {
        courseServiceImpl.updateCourse(courseId, updateCourse);

        return BodyResponses.UPDATED;
    }

    @DeleteMapping(path = "{courseId}")
    public String deleteCourseById(@PathVariable("courseId") String courseId){
        courseServiceImpl.deleteCourseById(courseId);
        return BodyResponses.DELETED;
    }
}
