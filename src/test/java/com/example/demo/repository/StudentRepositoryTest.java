package com.example.demo.repository;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import com.example.demo.entity.Student;

@DataJpaTest
public class StudentRepositoryTest {

    @Autowired
    private StudentRepository studentRepository;

    @Test
    void testFindStudentByEmail() {
        // Given
        Student student = new Student(
            "Serdar",
            "serdarakyol55@outlook.com",
            LocalDate.parse("2000-03-29")
        );
        studentRepository.save(student);
        
        // When
        Student studentRecord = studentRepository.findByEmail("serdarakyol55@outlook.com").get();

        // Then
        assertThat(studentRecord).isEqualTo(student);
    }
}
