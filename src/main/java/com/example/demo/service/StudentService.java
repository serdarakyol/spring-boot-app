package com.example.demo.service;

import java.util.List;

import com.example.demo.entity.Student;

public interface StudentService {
    public void addNewStudent(Student student);
    public void deleteStudent(int studentId);
    public void updateStudent(int studentId, String name, String email);
    public Student getStudentById(int studentId);
    public Student getStudentByEmail(String email);
    public List<Student> getStudents();
}
