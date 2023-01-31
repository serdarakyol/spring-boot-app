package com.example.demo.repository;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.Student;

@Repository
public interface StudentRepository extends JpaRepository<Student, Integer> {
    @Query("SELECT s FROM Student s WHERE s.studentEmail = ?1")
    Optional<Student> findStudentByEmail(String email);

    @Modifying
    @Transactional
    @Query("DELETE FROM Student s WHERE s.studentEmail = :studentEmail")
    void deleteStudentByEmail(String studentEmail);
}
