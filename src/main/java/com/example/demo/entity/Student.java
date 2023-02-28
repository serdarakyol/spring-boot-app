package com.example.demo.entity;

import java.time.LocalDate;
import java.time.Period;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotNull;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

@Entity
@Table(name = "Student")
@RequiredArgsConstructor
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private int studentId;
    @NonNull @Getter @Setter private String studentName;
    @NonNull @Getter @Setter private String studentEmail;
    @NonNull @Getter @Setter private LocalDate studentDOB;
    @Transient
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private Integer studentAge;

    public Student() {}

    public Integer getStudentAge() {
        return Period.between(this.studentDOB, LocalDate.now()).getYears();
    }
}
