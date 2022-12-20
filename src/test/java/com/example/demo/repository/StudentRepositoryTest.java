package com.example.demo.repository;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.example.demo.entity.Student;

@DataJpaTest
public class StudentRepositoryTest {

    @Autowired
    private StudentRepository studentRepository;

    @Test
    void testFindStudentByEmail() {
        Student student = new Student(
            "Serdar",
            "serdarakyol55@outlook.com",
            LocalDate.parse("2000-03-29")
        );

        studentRepository.save(student);
        
        /*Student studentRecord = studentRepository.findStudentByEmail("serdarakyol55@outlook.com").get();

        assertEquals(student.getName(), studentRecord.getName());
        assertEquals(student.getEmail(), studentRecord.getEmail());
        assertEquals(student.getAge(), studentRecord.getAge());*/
    }
}
