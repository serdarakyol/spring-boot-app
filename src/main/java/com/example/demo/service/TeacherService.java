package com.example.demo.service;

import java.time.LocalDate;
import java.util.List;

import com.example.demo.entity.Teacher;

public interface TeacherService {
    public void addNewTeacher(Teacher teacher);
    public void deleteTeacherById(int teacherId);
    public void deleteTeacherByMail(String teacherEmail);
    public void updateTeacherById(int teacherId, String teacherName, String teacherEmail, LocalDate teacherDOB);
    public Teacher getTeacherById(int teacherId);
    public Teacher getTeacherByEmail(String teacherId);
    public List<Teacher> getTeachers();
}
