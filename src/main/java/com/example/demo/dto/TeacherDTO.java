package com.example.demo.dto;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

public class TeacherDTO {
    @NonNull @Getter @Setter private String teacherName;
    @NonNull @Getter @Setter private String teacherEmail;
    @NonNull @Getter @Setter private Integer teacherAge;
}
