package com.example.demo.entity;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "Course")
@RequiredArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Course {
    @Id
    @Column(length = 36)
    @GeneratedValue(strategy = GenerationType.UUID)
    private String courseId;
    @NonNull
    @Column(length = 50)
    private String courseName;
    @NonNull
    private Integer courseCredit;
    @NonNull
    private Integer courseTeacherId;

    @ManyToMany(mappedBy = "enrolledCourses")
    @JsonIgnore
    private Set<Student> enrolledStudents = new HashSet<>();

    /*
     * I did not used lombok in here because it was throwing StackOverflowError. The
     * solution found on https://stackoverflow.com/a/54571390
     */
    public String toString() {
        return "Course(" +
                "CourseId=" + courseId +
                ", CourseName='" + courseName + '\'' +
                ", CourseCredit='" + courseCredit + '\'' +
                ", courseTeacherId=" + courseTeacherId +
                ')';
    }
}
