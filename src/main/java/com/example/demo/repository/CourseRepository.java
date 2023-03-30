package com.example.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.Course;

@Repository
public interface CourseRepository extends JpaRepository<Course, String>{
    @Query("SELECT c FROM Course c WHERE c.courseId = ?1")
    Optional<Course> findById(String courseId);
}
