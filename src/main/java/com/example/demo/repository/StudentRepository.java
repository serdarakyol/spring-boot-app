package com.example.demo.repository;

import java.util.Optional;

import jakarta.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.Student;

@Repository
public interface StudentRepository extends JpaRepository<Student, Integer> {
    @Query("SELECT s FROM Student s WHERE s.email = ?1")
    Optional<Student> findByEmail(String email);

    @Modifying
    @Transactional
    @Query("DELETE FROM Student s WHERE s.email = :email")
    void deleteByEmail(@Param("email") String email);

    @Query("SELECT COUNT(s.email) = 1 FROM Student s WHERE s.email = :email")
    boolean isExistByEmail(@Param("email") String email);
}
