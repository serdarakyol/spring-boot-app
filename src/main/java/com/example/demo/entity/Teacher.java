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
import lombok.NonNull;
import lombok.Setter;

@Entity
@Table(name = "Teacher")
@AllArgsConstructor
public class Teacher {
    @Id
    @GeneratedValue
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private int teacherId;
    @NonNull
    @Getter @Setter private String teachName;
    @Getter @Setter private LocalDate dob;
    @Getter @Setter private String teacherEmail;
    @Transient
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    @Setter private Integer age;

    public Integer getAge() {
        return Period.between(this.dob, LocalDate.now()).getYears();
    }
}
