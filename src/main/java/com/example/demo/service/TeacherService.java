package com.example.demo.service;

import java.util.List;

import com.example.demo.entity.Teacher;

public interface TeacherService {
    public void addNewTeacher(Teacher teacher);
    public List<Teacher> getTeachers();
}
