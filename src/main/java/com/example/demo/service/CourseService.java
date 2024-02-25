package com.example.demo.service;

import java.util.List;

import com.example.demo.dto.CourseDTO;

public interface CourseService {

    public String addNewCourse(CourseDTO course);
    public String updateCourse(String courseId, CourseDTO course);
    public String deleteCourseById(String courseId);
    public CourseDTO getCourseById(String courseId);
    public List<CourseDTO> getCourses();
}
