package com.example.demo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class CourseDTO {

    @JsonProperty("course_name")
    private String courseName;

    @JsonProperty("course_credit")
    private Integer courseCredit;

}
