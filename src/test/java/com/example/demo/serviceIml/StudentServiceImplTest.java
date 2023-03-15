package com.example.demo.serviceIml;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.demo.controller.CommonResponses;
import com.example.demo.entity.Student;
import com.example.demo.exception.BadRequestException;
import com.example.demo.exception.NotFoundException;
import com.example.demo.repository.StudentRepository;

@ExtendWith(MockitoExtension.class)
class StudentServiceImplTest {

    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private StudentServiceImpl studentServiceImpl;

    @Captor
    private ArgumentCaptor<Integer> integerCaptor;

    @Captor
    private ArgumentCaptor<String> emailCaptor;

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

        // Then
        ArgumentCaptor<Student> studentArgumentCaptor = ArgumentCaptor.forClass(Student.class);
        // That verfies if studentRepository.save called
        verify(studentRepository).save(studentArgumentCaptor.capture());
        Student studentRecord = studentArgumentCaptor.getValue();
        assertEquals(studentRecord, testStudent);
        assertEquals(testStudent.getStudentAge(), 23);
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

    @Test
    void testUpdateStudent() {
        // Given
        Student updatedStudent = new Student();
        updatedStudent.setStudentName("Jane Doe");
        updatedStudent.setStudentEmail("janedoe@example.com");
        updatedStudent.setStudentDOB(LocalDate.parse("2001-01-01"));

        when(studentRepository.findByEmail(testStudent.getStudentEmail())).thenReturn(Optional.of(testStudent));
        when(studentRepository.isExistByEmail(updatedStudent.getStudentEmail())).thenReturn(false);

        // When
        studentServiceImpl.updateStudent(testStudent.getStudentEmail(), updatedStudent);

        // Then
        assertEquals(testStudent.getStudentName(), updatedStudent.getStudentName());
        assertEquals(testStudent.getStudentDOB(), updatedStudent.getStudentDOB());
        assertEquals(testStudent.getStudentEmail(), updatedStudent.getStudentEmail());
        assertEquals(testStudent.getStudentDOB(), updatedStudent.getStudentDOB());
    }

    @Test
    void testUpdateStudentEMailTaken() {
        // Given
        Student updatedStudent = new Student();
        updatedStudent.setStudentName("Jane Doe");
        updatedStudent.setStudentEmail("janedoe@example.com");
        updatedStudent.setStudentDOB(LocalDate.of(2001, 1, 1));

        when(studentRepository.findByEmail(testStudent.getStudentEmail())).thenReturn(Optional.of(testStudent));
        when(studentRepository.isExistByEmail(updatedStudent.getStudentEmail())).thenReturn(true);

        // When
        Throwable exception = assertThrows(BadRequestException.class, () -> {
            studentServiceImpl.updateStudent(testStudent.getStudentEmail(), updatedStudent);
        });

        // Then
        assertEquals(CommonResponses.emailTakenMsg, exception.getMessage());
    }

    @Test
    void testUpdateStudentAlreadyExist() {
        // Given
        Student updatedStudent = new Student();
        updatedStudent.setStudentName("Jane Doe");
        updatedStudent.setStudentEmail("janedoe@example.com");
        updatedStudent.setStudentDOB(LocalDate.of(2001, 1, 1));

        when(studentRepository.findByEmail(testStudent.getStudentEmail())).thenReturn(Optional.empty());

        // When
        Throwable exception = assertThrows(NotFoundException.class, () -> {
            studentServiceImpl.updateStudent(testStudent.getStudentEmail(), updatedStudent);
        });

        // Then
        String expectedMessage = "Student doesn't exist with E-mail: " + testStudent.getStudentEmail();

        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    void testUpdateStudentInvalidName() {
        // Given
        Student updatedStudent = new Student();
        updatedStudent.setStudentName("a");
        updatedStudent.setStudentEmail("janedoe@example.com");
        updatedStudent.setStudentDOB(LocalDate.of(2001, 1, 1));

        when(studentRepository.findByEmail(testStudent.getStudentEmail())).thenReturn(Optional.of(testStudent));

        // When
        Throwable exception = assertThrows(BadRequestException.class, () -> {
            studentServiceImpl.updateStudent(testStudent.getStudentEmail(), updatedStudent);
        });

        // Then
        assertEquals(CommonResponses.nameNotValidMsg, exception.getMessage());

    }

    @Test
    void testUpdateStudentInvalidEmail() {
        // Given
        Student updatedStudent = new Student();
        updatedStudent.setStudentName("Test");
        updatedStudent.setStudentEmail("t@t.com");
        updatedStudent.setStudentDOB(LocalDate.of(2001, 1, 1));

        when(studentRepository.findByEmail(testStudent.getStudentEmail())).thenReturn(Optional.of(testStudent));

        // When
        Throwable exception = assertThrows(BadRequestException.class, () -> {
            studentServiceImpl.updateStudent(testStudent.getStudentEmail(), updatedStudent);
        });

        // Then
        assertEquals(CommonResponses.emailNotValidMsg, exception.getMessage());
    }

    @Test
    public void testGetStudentById() {
        // Given
        int studentId = 1;
        when(studentRepository.findById(studentId)).thenReturn(Optional.of(testStudent));

        // When
        Student studentInDB = studentServiceImpl.getStudentById(studentId);

        // Then
        assertNotNull(studentInDB);
        assertEquals("Test One", studentInDB.getStudentName());
        assertEquals("test.one@example.com", studentInDB.getStudentEmail());
        assertEquals(LocalDate.parse("2000-01-01"), studentInDB.getStudentDOB());

        verify(studentRepository, times(1)).findById(studentId);
        verifyNoMoreInteractions(studentRepository);
    }

    @Test
    void testGetStudentByIdNotFound() {
        // Given
        int studentId = 1;
        when(studentRepository.findById(studentId)).thenReturn(Optional.empty());

        // When & Assert
        assertThrows(NotFoundException.class, () -> studentServiceImpl.getStudentById(studentId));

        verify(studentRepository, times(1)).findById(studentId);
        verifyNoMoreInteractions(studentRepository);
    }

    @Test
    void testGetStudentByEmail() {
        // Given
        when(studentRepository.findByEmail(testStudent.getStudentEmail())).thenReturn(Optional.of(testStudent));

        // When
        Student studentInDB = studentServiceImpl.getStudentByEmail(testStudent.getStudentEmail());

        // Then
        assertNotNull(studentInDB);
        assertEquals("Test One", studentInDB.getStudentName());
        assertEquals("test.one@example.com", studentInDB.getStudentEmail());
        assertEquals(LocalDate.parse("2000-01-01"), studentInDB.getStudentDOB());

        verify(studentRepository, times(1)).findByEmail(testStudent.getStudentEmail());
        verifyNoMoreInteractions(studentRepository);
    }

    @Test
    void testGetStudentByEmailNotFound() {
        // Given
        when(studentRepository.findByEmail(testStudent.getStudentEmail())).thenReturn(Optional.empty());

        // When & Assert
        assertThrows(NotFoundException.class, () -> studentServiceImpl.getStudentByEmail(testStudent.getStudentEmail()));

        verify(studentRepository, times(1)).findByEmail(testStudent.getStudentEmail());
        verifyNoMoreInteractions(studentRepository);
    }

    @Test
    public void testGetStudents() {
        // Given
        List<Student> students = new ArrayList<Student>();
        Student test2 = new Student("test2", "test2@example.com", LocalDate.parse("1995-01-01"));
        Student test3 = new Student("test3", "test2@example.com", LocalDate.parse("1990-01-01"));
        students.add(testStudent);
        students.add(test2);
        students.add(test3);

        when(studentRepository.findAll()).thenReturn(students);

        // When
        List<Student> result = studentServiceImpl.getStudents();

        // Then
        assertEquals(3, result.size());
        assertEquals(result.get(0), testStudent);
        assertEquals(result.get(1), test2);
        assertEquals(result.get(2), test3);
    }

    @Test
    void testDeleteStudentById() {
        // Given
        int existingStudentId = 1;
        when(studentRepository.existsById(existingStudentId)).thenReturn(true);

        // When
        studentServiceImpl.deleteStudentById(existingStudentId);

        // Then
        verify(studentRepository, times(1)).deleteById(integerCaptor.capture());
        int capturedStudentId = integerCaptor.getValue();
        assertEquals(existingStudentId, capturedStudentId);
    }

    @Test
    void testDeleteStudentByIdNotFound() {
        // Given
        int studentId = 1;
        when(studentRepository.existsById(studentId)).thenReturn(false);

        // When
        Throwable exception = assertThrows(NotFoundException.class, () -> {
            studentServiceImpl.deleteStudentById(studentId);
        });

        // Then
        String expectedError = "Student doesn't exist with ID: " + studentId;
        assertEquals(expectedError, exception.getMessage());
    }

    @Test
    public void testDeleteStudentByEmail() {
        // Given
        String validEmail = testStudent.getStudentEmail();
        when(studentRepository.isExistByEmail(validEmail)).thenReturn(true);

        // When
        studentServiceImpl.deleteStudentByEmail(validEmail);

        // Then
        verify(studentRepository, times(1)).deleteByEmail(emailCaptor.capture());
        String capturedEmail = emailCaptor.getValue();
        assert capturedEmail.equals(validEmail);
    }

    @Test
    public void testDeleteStudentByEmailNotFound() {
        // Given
        String invalidEmail = "a@a.c";
        when(studentRepository.isExistByEmail(invalidEmail)).thenReturn(false);

        // When and Assert
        assertThrows(NotFoundException.class, () -> studentServiceImpl.deleteStudentByEmail(invalidEmail));
    }
}