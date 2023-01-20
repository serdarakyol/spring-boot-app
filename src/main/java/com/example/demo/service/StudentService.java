package com.example.demo.service;

import java.util.List;

import com.example.demo.entity.Student;

public interface StudentService {

    public void addNewStudent(Student student);
    public void deleteStudent(Integer studentId);
    public void updateStudent(Integer studentId, String name, String email);
    public Student getStudentById(Integer studentId);
    public Student getStudentByEmail(String email);
    public List<Student> getStudents();

}
