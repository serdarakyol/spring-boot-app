package com.example.demo.entity;

import java.time.LocalDate;
import java.time.Period;
import java.util.Set;

import com.example.demo.entity.factory.User;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import lombok.NoArgsConstructor;

@Entity
@Table(name = "Teacher")
@NoArgsConstructor
public class Teacher extends User{
    public Teacher(String name, String email, LocalDate dob) {
        setName(name);
        setEmail(email);
        setDob(dob);
    }

    @OneToMany(mappedBy = "teacher", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Enrolled> teachingCourses;

    @Override
    public Integer getAge() {
        return Period.between(getDob(), LocalDate.now()).getYears();
    }

}
