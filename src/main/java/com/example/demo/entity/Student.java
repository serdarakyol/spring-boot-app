package com.example.demo.entity;

import java.time.LocalDate;
import java.time.Period;
import java.util.Set;

import com.example.demo.entity.factory.User;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "student")
@NoArgsConstructor
public class Student extends User {
    @Builder
    public Student(String name, String email, LocalDate dob) {
        setName(name);
        setEmail(email);
        setDob(dob);
    }

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "enrolled_courses", joinColumns = @JoinColumn(name = "id_student"), inverseJoinColumns = @JoinColumn(name = "id_course"))
    private Set<Course> enrolledCourses;

    @Override
    public Integer getAge() {
        return Period.between(getDob(), LocalDate.now()).getYears();
    }

}
