package com.example.demo.entity;

import java.time.LocalDate;
import java.time.Period;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

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

    public Student() {

    }

    public Integer getStudentAge() {
        return Period.between(this.studentDOB, LocalDate.now()).getYears();
    }
}
