package com.example.demo.service;

import java.util.List;

import com.example.demo.dto.CourseDTO;

public interface CourseService {

    public void addNewCourse(CourseDTO course);
    public void updateCourse(String courseId, CourseDTO course);
    public void deleteCourseById(String courseId);
    public CourseDTO getCourseById(String courseId);
    public List<CourseDTO> getCourses();
}
