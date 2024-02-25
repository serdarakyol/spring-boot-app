package com.example.demo.dto;

import java.time.LocalDate;
import java.util.Set;

import com.example.demo.entity.Course;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentDTO {

    @JsonProperty("name")
    private String name;

    @JsonProperty("email")
    private String email;

    @JsonProperty("age")
    private Integer age;

    @JsonProperty("dob")
    private LocalDate dob;

    @JsonProperty("enrolled_courses")
    private Set<Course> enrolledCourses;
}
