package com.example.demo.dto;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@NonNull
@Getter
@Setter
public class TeacherDTO {
    private String name;
    private String email;
    private Integer age;
}
