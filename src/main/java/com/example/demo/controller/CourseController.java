package com.example.demo.controller;

import java.util.List;
import java.util.stream.Collectors;

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
import com.example.demo.entity.Course;
import com.example.demo.mapper.CourseMapper;
import com.example.demo.serviceIml.CourseServiceIml;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping(path = "api/v1/course")
public class CourseController {

    private final CourseServiceIml courseServiceImpl;

    private final CourseMapper courseMapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public String registerNewCourse(@RequestBody CourseDTO course) {
        courseServiceImpl.addNewCourse(course);
        return BodyResponses.CREATED;
    }

    @PatchMapping(path = "{courseId}")
    public String updateCourse(@PathVariable("courseId") String courseId, @RequestBody Course updateCourse) {
        courseServiceImpl.updateCourse(courseId, updateCourse);

        return BodyResponses.UPDATED;
    }

    @DeleteMapping(path = "{courseId}")
    public String deleteCourseById(@PathVariable("courseId") String courseId){
        courseServiceImpl.deleteCourseById(courseId);
        return BodyResponses.DELETED;
    }

    @GetMapping(path = "{courseId}")
    public CourseDTO getCourseById(@PathVariable("courseId") String courseId){
        Course course = courseServiceImpl.getCourseById(courseId);
        return courseMapper.toDTO(course);
    }

    @GetMapping
    public List<CourseDTO> getCourses(){
        List<Course> courses = courseServiceImpl.getCourses();
        return courses.stream().map(c -> courseMapper.toDTO(c)).collect(Collectors.toList());
    }
}
