package com.example.demo.entity;

import java.time.LocalDate;
import java.time.Period;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;


import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "Student")
@NoArgsConstructor
public class Student extends User {
    public Student (String name, String email, LocalDate dob) {
        setName(name);
        setEmail(email);
        setDob(dob);
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

    @Override
    public Integer getAge() {
        return Period.between(getDob(), LocalDate.now()).getYears();
    }

    @Override
    public String toString() {
        return "Student(id=" + getId() +
               ", name="  + getName() + 
               ", email=" + getEmail() + 
               ", dob=" + getDob() + 
               ", age=" + getAge() + 
               ")";
    }
}
