package com.example.demo.serviceIml;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.Student;
import com.example.demo.exception.BadRequestException;
import com.example.demo.repository.StudentRepository;


@ExtendWith(MockitoExtension.class)
@Repository
class StudentServiceImplTest {

    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private StudentServiceImpl studentServiceImpl;

    private Student testStudent;

    @BeforeEach
    void setUp() {
        testStudent = new Student("Test One", "test.one@example.com", LocalDate.parse("2000-01-01"));
    }

    @Test
    void testAddNewStudent() {
        // Given
        when(studentRepository.isExistByEmail(testStudent.getStudentEmail())).thenReturn(false);

        // When
        studentServiceImpl.addNewStudent(testStudent);

        // Then [That verfies if studentRepository.save called or not]
        verify(studentRepository).save(testStudent);
    }

    @Test
    void testAddNewStudentAlreadyExists() {
        // Given
        when(studentRepository.isExistByEmail(testStudent.getStudentEmail())).thenReturn(true);

        // When
        assertThrows(BadRequestException.class, () -> {
            studentServiceImpl.addNewStudent(testStudent);
        });

        // Then [Checks if studentRepository.save() method called or not]
        verify(studentRepository, never()).save(any());
    }

    @Test
    void testAddNewStudentInvalidEmail() {
        // Given
        testStudent.setStudentEmail("invalid_email");

        // When
        assertThrows(BadRequestException.class, () -> {
            studentServiceImpl.addNewStudent(testStudent);
        });
        
        // Then
        verify(studentRepository, never()).save(any());
    }

    @Test
    void testAddNewStudentInvalidName() {
        // Given
        testStudent.setStudentName("J");

        // When
        assertThrows(BadRequestException.class, () -> {
            studentServiceImpl.addNewStudent(testStudent);
        });

        // Then
        verify(studentRepository, never()).save(any());
    }

}
