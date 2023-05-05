package com.example.demo.service;

import java.util.List;

import com.example.demo.entity.Teacher;

public interface TeacherService {
    public final String teacherNotExistMsg = "Teacher does not exist with ";
    public final String teacherExistMsg = "Teacher already exist with ";
    public final String teacherSuccessfullyDeleteMsg = "Teacher successfully deleted with ";
    public final String teacherSuccessfullyFound = "Teacher successfully found: ";
    
    public void addNewTeacher(Teacher teacher);
    public void deleteTeacherById(int teacherId);
    public void deleteByEmail(String teacherEmail);
    public void updateTeacherById(String teacherEmail, Teacher Updateteacher);
    public Teacher getTeacherById(int teacherId);
    public Teacher getTeacherByEmail(String teacherId);
    public List<Teacher> getTeachers();
}
