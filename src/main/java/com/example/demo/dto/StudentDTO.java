package com.example.demo.dto;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

public class StudentDTO {
    @NonNull @Getter @Setter private String studentName;
    @NonNull @Getter @Setter private String studentEmail;
    @NonNull @Getter @Setter private Integer studentAge;
}
