package com.example.demo.service;

import java.util.List;

import com.example.demo.entity.Student;

public interface StudentService {
    public final String studentNotExistMsg = "Student doesn't exist with ";
    public final String studentSuccessfullyDeleteMsg = "Student successfully deleted with ";
    public final String studentSuccessfullyFoundMsg = "Student successfully found: ";
    public final String studentExist = "Student already exist with ";

    public void addNewStudent(Student student);
    public void deleteStudentById(int studentId);
    public void deleteStudentByEmail(String studentEmail);
    public void updateStudent(String studentEmail, Student updateStudent);
    public Student getStudentById(int studentId);
    public Student getStudentByEmail(String email);
    public List<Student> getStudents();
    public void enrollToCourse(int studentId, String courseId);
}
