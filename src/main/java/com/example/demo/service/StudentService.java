package com.example.demo.service;

import java.util.List;

import com.example.demo.entity.Student;

public interface StudentService {
    public void addNewStudent(Student student);
    public void deleteStudentById(int studentId);
    public void deleteStudentByEmail(String studentEmail);
    public void updateStudent(int studentId, String studentName, String studentEmail);
    public Student getStudentById(int studentId);
    public Student getStudentByEmail(String email);
    public List<Student> getStudents();
}
