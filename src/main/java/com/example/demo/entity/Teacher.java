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
@Setter
@Getter
@Table(name = "Teacher")
@NoArgsConstructor
public class Teacher extends User {
    @Builder
    public Teacher(String name, String email, LocalDate dob) {
        setName(name);
        setEmail(email);
        setDob(dob);
    }

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "teaching_courses", joinColumns = @JoinColumn(name = "id_teacher"), inverseJoinColumns = @JoinColumn(name = "id_course"))
    private Set<Course> teachingCourses;

    @Override
    public Integer getAge() {
        return Period.between(getDob(), LocalDate.now()).getYears();
    }

}
