package com.example.demo.dto;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@NonNull
@Getter
@Setter
public class TeacherDTO {
    private String teacherName;
    private String teacherEmail;
    private Integer teacherAge;
}
