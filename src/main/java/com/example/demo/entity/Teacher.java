package com.example.demo.entity;

import java.time.LocalDate;
import java.time.Period;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "Teacher")
@AllArgsConstructor
public class Teacher {
    @Id
    @GeneratedValue
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private int teacherId;
    @Getter @Setter private String teacherName;
    @Getter @Setter private String teacherEmail;
    @Getter @Setter private LocalDate dob;
    @Transient
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    @Setter private Integer teacherAge;

    public Teacher() {}

    public Integer getTeacherAge() {
        return Period.between(this.dob, LocalDate.now()).getYears();
    }
}
