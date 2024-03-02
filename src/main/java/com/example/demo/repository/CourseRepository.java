package com.example.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.Course;

@Repository
public interface CourseRepository extends JpaRepository<Course, String>{

    Optional<Course> findByIdAndIsActiveTrue(String id);

    Optional<Course> findByCourseNameAndIsActiveTrue(String courseName);
}
