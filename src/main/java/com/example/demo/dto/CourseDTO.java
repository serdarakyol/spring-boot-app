package com.example.demo.dto;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@NonNull
@Getter
@Setter
public class CourseDTO {
    private String courseName;
    private Integer courseCredit;
}
