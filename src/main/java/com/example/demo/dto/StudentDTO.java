package com.example.demo.dto;

import java.util.Set;

import com.example.demo.entity.Course;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@NonNull
@Getter
@Setter
public class StudentDTO {
    private String studentName;
    private String studentEmail;
    private Integer studentAge;
    private Set<Course> courses;
}
