package com.example.demo.entity;

import java.time.LocalDate;
import java.time.Period;

import com.example.demo.entity.factory.User;

import jakarta.persistence.Entity;
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
