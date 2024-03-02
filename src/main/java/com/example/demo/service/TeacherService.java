package com.example.demo.service;

import java.util.List;
import java.util.Set;

import com.example.demo.dto.CourseDTO;
import com.example.demo.dto.TeacherDTO;

public interface TeacherService {
    public final String teacherNotExistMsg = "Teacher does not exist with ";
    public final String teacherExistMsg = "Teacher already exist with ";
    public final String teacherSuccessfullyDeleteMsg = "Teacher successfully deleted with ";
    public final String teacherSuccessfullyFound = "Teacher successfully found: ";
    
    public String addNewTeacher(TeacherDTO teacher);
    public String deleteTeacherById(int teacherId);
    public String deleteByEmail(String teacherEmail);
    public String updateTeacherById(String teacherEmail, TeacherDTO Updateteacher);
    public TeacherDTO getTeacherById(int teacherId);
    public TeacherDTO getTeacherByEmail(String teacherId);
    public List<TeacherDTO> getTeachers();
    public String enrollCourse(int teacherId, Set<CourseDTO> courses);
}
