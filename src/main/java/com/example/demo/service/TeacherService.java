package com.example.demo.service;

import java.util.List;

import com.example.demo.entity.Teacher;

public interface TeacherService {
    public void addNewTeacher(Teacher teacher);
    public void updateTeacherById(int teacherId, String teacherName, String teacherEmail);
    public void deleteTeacherById(int teacherId);
    public Teacher getTeacherById(int teacherId);
    public List<Teacher> getTeachers();
}
