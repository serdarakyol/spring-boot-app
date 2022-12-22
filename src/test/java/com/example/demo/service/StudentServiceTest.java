package com.example.demo.service;

import static org.mockito.Mockito.verify;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.demo.entity.Student;
import com.example.demo.repository.StudentRepository;

@ExtendWith(MockitoExtension.class)
public class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;
    private StudentService studentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        studentService = new StudentService(studentRepository);
    }

    @Test
    void testAddNewStudentSuccess() {
        // Given
        Student student = new Student(
            "test",
            "test@outlook.com",
            LocalDate.parse("2000-03-29")
        );

        // When
        studentService.addNewStudent(student);

        // Then
        ArgumentCaptor<Student> studentArgumentCaptor = ArgumentCaptor.forClass(Student.class);
        verify(studentRepository).save(studentArgumentCaptor.capture());
        Student record = studentArgumentCaptor.getValue();
        assertThat(record).isEqualTo(student);
    }

    @Test
    void testDeleteStudent() {

    }

    @Test
    void testGetStudentById() {

    }

    @Test
    void testGetStudents() {
        studentService.getStudents();
        verify(studentRepository).findAll();
    }

    @Test
    void testUpdateStudent() {

    }
}
