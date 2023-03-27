package com.example.demo.entity;

import java.time.LocalDate;
import java.time.Period;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "Teacher")
@RequiredArgsConstructor
@NoArgsConstructor
@ToString
public class Teacher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int teacherId;
    @NonNull @Getter @Setter private String teacherName;
    @NonNull @Getter @Setter private String teacherEmail;
    @NonNull @Getter @Setter private LocalDate teacherDOB;
    @Transient
    @Setter private Integer teacherAge;

    public Integer getTeacherAge() {
        return Period.between(this.teacherDOB, LocalDate.now()).getYears();
    }
}
