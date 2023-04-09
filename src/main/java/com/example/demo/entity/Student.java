package com.example.demo.entity;

import java.time.LocalDate;
import java.time.Period;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "Student")
@RequiredArgsConstructor
@NoArgsConstructor
@ToString
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int studentId;
    @NonNull @Getter @Setter
    private String studentName;
    @NonNull @Getter @Setter
    private String studentEmail;
    @NonNull @Getter @Setter
    private LocalDate studentDOB;
    @Transient
    private Integer studentAge;

    public Integer getStudentAge() {
        return Period.between(this.studentDOB, LocalDate.now()).getYears();
    }

    @ManyToMany
    @JoinTable(
        name = "enrolled", 
        joinColumns = @JoinColumn(name = "student_id"),
        inverseJoinColumns = @JoinColumn(name = "course_id")
    )
    @Getter @Setter 
    @JsonIgnore
    private Set<Course> enrolledCourses = new HashSet<>();
}
