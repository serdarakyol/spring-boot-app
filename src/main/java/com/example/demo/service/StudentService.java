package com.example.demo.service;

import java.util.List;

import com.example.demo.entity.Student;

public interface StudentService {

    public void addNewStudent(Student student);
    public void deleteStudent(Long studentId);
    public void updateStudent(Long studentId, String name, String email);
    public Student getStudentById(Long studentId);
    public List<Student> getStudents();

}
