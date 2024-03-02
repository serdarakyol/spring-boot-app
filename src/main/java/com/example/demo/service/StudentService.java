package com.example.demo.service;

import java.util.List;

import com.example.demo.dto.StudentDTO;

public interface StudentService {

    public final String studentNotExistMsg = "Student doesn't exist with ";
    public final String studentSuccessfullyDeleteMsg = "Student successfully deleted with ";
    public final String studentSuccessfullyFoundMsg = "Student successfully found: ";
    public final String studentExist = "Student already exist with ";

    public String addNewStudent(StudentDTO student);
    public String deleteStudentById(int studentId);
    public String deleteStudentByEmail(String studentEmail);
    public String updateStudent(String studentEmail, StudentDTO updateStudent);
    public StudentDTO getStudentById(int studentId);
    public StudentDTO getStudentByEmail(String email);
    public List<StudentDTO> getStudents();

}
