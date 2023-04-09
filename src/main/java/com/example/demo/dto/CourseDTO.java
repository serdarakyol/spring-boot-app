package com.example.demo.dto;

import java.util.Set;

import com.example.demo.entity.Student;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@NonNull
@Getter
@Setter
public class CourseDTO {
    private String courseName;
    private Integer courseCredit;
    private Set<Student> enrolledStudents;
}
