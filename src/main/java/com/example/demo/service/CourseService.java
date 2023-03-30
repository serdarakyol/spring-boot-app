package com.example.demo.service;

import com.example.demo.entity.Course;

public interface CourseService {

    public void addNewCourse(final Course course);
    public void updateCourse(String courseId, final Course course);
}
