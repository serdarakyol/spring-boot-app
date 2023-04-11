package com.example.demo.service;

import java.util.List;

import com.example.demo.entity.Student;

public interface StudentService {
    public void addNewStudent(Student student);
    public void deleteStudentById(int studentId);
    public void deleteStudentByEmail(String studentEmail);
    public void updateStudent(String studentEmail, Student updateStudent);
    public Student getStudentById(int studentId);
    public Student getStudentByEmail(String email);
    public List<Student> getStudents();
    public void enrollToCourse(int studentId, String courseId);
}
