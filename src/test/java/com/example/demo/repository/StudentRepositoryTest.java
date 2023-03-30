package com.example.demo.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.example.demo.entity.Student;

@ExtendWith(MockitoExtension.class)
@DataJpaTest
class StudentRepositoryTest {

    @MockBean
    private StudentRepository studentRepository;

    @Mock
    private Student student;

    private final String studentEmail = "john.doe@example.com";

    @BeforeEach
    void setUp() {
        student.setStudentEmail(studentEmail);
    }

    @Test
    void testFindByEmailShouldReturnOptionalOfStudentWhenStudentExists() {
        when(studentRepository.findByEmail(studentEmail)).thenReturn(Optional.of(student));

        Optional<Student> foundStudent = studentRepository.findByEmail(studentEmail);

        assertThat(foundStudent).isPresent().contains(student);
    }

    @Test
    void testFindByEmailShouldReturnEmptyOptionalWhenStudentDoesNotExist() {
        when(studentRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        Optional<Student> foundStudent = studentRepository.findByEmail(studentEmail);

        assertThat(foundStudent).isEmpty();
    }

    @Test
    void testDeleteByEmailShouldDeleteStudentWhenStudentExists() {
        studentRepository.deleteByEmail(studentEmail);

        assertThat(studentRepository.findByEmail(studentEmail)).isEmpty();
    }

    @Test
    void testIsExistByEmailShouldReturnTrueWhenStudentExists() {
        when(studentRepository.isExistByEmail(studentEmail)).thenReturn(true);

        boolean exists = studentRepository.isExistByEmail(studentEmail);

        assertThat(exists).isTrue();
    }

    @Test
    void testIsExistByEmailShouldReturnFalseWhenStudentDoesNotExist() {
        when(studentRepository.isExistByEmail(anyString())).thenReturn(false);

        boolean exists = studentRepository.isExistByEmail(studentEmail);

        assertThat(exists).isFalse();
    }
}
