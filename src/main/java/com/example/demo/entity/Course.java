package com.example.demo.entity;

import java.util.Set;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
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
public class Course {
    @Id @Getter @Setter
    @GeneratedValue(strategy = GenerationType.UUID)
    private String courseId;
    @NonNull @Setter @Getter
    private String courseName;
    @NonNull @Setter @Getter
    private Integer courseCredit;
    @NonNull @Setter @Getter
    private Integer courseTeacherId;

    @ManyToMany
    @JoinTable(
        name = "student_enrolled",
        joinColumns = @JoinColumn(name = "course_id"),
        inverseJoinColumns = @JoinColumn(name = "student_id")
    )
    @Getter
    private Set<Student> enrolledStudents;
}
