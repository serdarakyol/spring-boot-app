package com.example.demo.entity;

import java.time.LocalDate;
import java.time.Period;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "Teacher")
@RequiredArgsConstructor
public class Teacher {
    @Id
    @GeneratedValue
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private int teacherId;
    @NonNull @Getter @Setter private String teacherName;
    @NonNull @Getter @Setter private String teacherEmail;
    @NonNull @Getter @Setter private LocalDate teacherDOB;
    @Transient
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    @Setter private Integer teacherAge;

    public Teacher() {}

    public Integer getTeacherAge() {
        return Period.between(this.teacherDOB, LocalDate.now()).getYears();
    }
}
