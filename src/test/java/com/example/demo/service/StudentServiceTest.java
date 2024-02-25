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
import com.example.demo.serviceIml.StudentServiceImpl;

@ExtendWith(MockitoExtension.class)
public class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;
    private StudentServiceImpl studentService;
    // Given
    private Student student = new Student(
        "test2",
        "test2@outlook.com",
        LocalDate.parse("2000-01-23")
    );

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        studentService = new StudentServiceImpl(studentRepository, null);
        // When
        //studentService.addNewStudent(student);
    }

    @Test
    void testAddNewStudentSuccess() {
        // Then
        ArgumentCaptor<Student> studentArgumentCaptor = ArgumentCaptor.forClass(Student.class);
        verify(studentRepository).save(studentArgumentCaptor.capture());
        Student studentRecord = studentArgumentCaptor.getValue();
        assertThat(studentRecord).isEqualTo(student);
        assertThat(student.getAge()).isEqualTo(23);
    }
}
