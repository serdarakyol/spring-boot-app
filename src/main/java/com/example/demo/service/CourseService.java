package com.example.demo.service;

import java.util.List;

import com.example.demo.dto.CourseDTO;
import com.example.demo.entity.Course;

public interface CourseService {

    public void addNewCourse(CourseDTO course);
    public void updateCourse(String courseId, final Course course);
    public void deleteCourseById(String courseId);
    public Course getCourseById(String courseId);
    public List<Course> getCourses();
}
