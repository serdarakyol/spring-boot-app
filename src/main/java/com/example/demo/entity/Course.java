package com.example.demo.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "Course")
@RequiredArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String courseId;
    @NonNull
    private String courseName;
    @NonNull
    private Integer courseCredit;
    @NonNull
    private Integer courseTeacherId;
}
