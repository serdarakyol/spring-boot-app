package com.example.demo.serviceIml;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

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
import com.example.demo.entity.Teacher;
import com.example.demo.exception.BadRequestException;
import com.example.demo.exception.NotFoundException;
import com.example.demo.repository.TeacherRepository;

@ExtendWith(MockitoExtension.class)
public class TeacherServiceImplTest {

    @Mock
    private TeacherRepository teacherRepository;

    @InjectMocks
    private TeacherServiceImpl teacherServiceImpl;

    @Captor
    private ArgumentCaptor<String> emailCaptor;

    @Captor
    private ArgumentCaptor<Integer> integerCaptor;

    private Teacher testTeacher;

    @BeforeEach
    void setUp() {
        testTeacher = new Teacher();
        testTeacher.setName("Test Teacher");
        testTeacher.setEmail("testteacher@example.com");
        testTeacher.setDob(LocalDate.parse("1990-01-01"));
    }

    @Test
    void testAddNewTeacher() {
        // Given
        when(teacherRepository.isExistByEmail(testTeacher.getEmail())).thenReturn(false);

        // When
        teacherServiceImpl.addNewTeacher(testTeacher);

        // Then
        ArgumentCaptor<Teacher> teacherArgumentCaptor = ArgumentCaptor.forClass(Teacher.class);
        // That verfies if teacherRepository.save called
        verify(teacherRepository).save(teacherArgumentCaptor.capture());
        Teacher teacherRecord = teacherArgumentCaptor.getValue();
        assertEquals(teacherRecord, testTeacher);
        assertEquals(testTeacher.getAge(), 33);
    }

    @Test
    void testAddNewTeacherAlreadyExists() {
        // Given
        String teacherMail = testTeacher.getEmail();
        when(teacherRepository.isExistByEmail(teacherMail)).thenReturn(true);

        // When
        assertThrows(BadRequestException.class, () -> {
            teacherServiceImpl.addNewTeacher(testTeacher);
        });

        // Then [Checks if teacherRepository.save() method called or not]
        verify(teacherRepository, never()).save(any());

    }

    @Test
    void testAddNewTeacherInvalidName() {
        // Given
        testTeacher.setName("T");

        // When
        assertThrows(BadRequestException.class, () -> {
            teacherServiceImpl.addNewTeacher(testTeacher);
        });

        // Then
        verify(teacherRepository, never()).save(any());
    }

    @Test
    void testAddNewTeacherInvalidEmail() {
        // Given
        testTeacher.setEmail("invalid_email");

        // When
        assertThrows(BadRequestException.class, () -> {
            teacherServiceImpl.addNewTeacher(testTeacher);
        });

        // Then
        verify(teacherRepository, never()).save(any());
    }

    @Test
    void testUpdateTeacherById() {
        // Given
        Teacher updatedTeacher = new Teacher("Updated Name", "update@test.com", LocalDate.parse("2000-01-01"));

        when(teacherRepository.findByEmail(testTeacher.getEmail())).thenReturn(Optional.of(testTeacher));
        when(teacherRepository.isExistByEmail(updatedTeacher.getEmail())).thenReturn(false);

        // When
        teacherServiceImpl.updateTeacherById(testTeacher.getEmail(), updatedTeacher);

        // Then
        assertEquals(testTeacher.getName(), updatedTeacher.getName());
        assertEquals(testTeacher.getEmail(), updatedTeacher.getEmail());
        assertEquals(testTeacher.getAge(), updatedTeacher.getAge());
        assertEquals(testTeacher.getDob(), updatedTeacher.getDob());
    }

    @Test
    void testUpdateTeacherByIdEmailTaken() {
        // given
        Teacher updatedTeacher = new Teacher("Updated Name", "update@test.com", LocalDate.parse("2000-01-01"));

        when(teacherRepository.findByEmail(testTeacher.getEmail())).thenReturn(Optional.of(testTeacher));
        when(teacherRepository.isExistByEmail(updatedTeacher.getEmail())).thenReturn(true);

        // when
        Throwable exception = assertThrows(BadRequestException.class, () -> {
            teacherServiceImpl.updateTeacherById(testTeacher.getEmail(), updatedTeacher);
        });

        // then
        assertEquals(CommonResponses.emailTakenMsg, exception.getMessage());
    }

    @Test
    void testUpdateTeacherAlreadyExist() {
        // Given
        Teacher updatedTeacher = new Teacher("Updated Name", "update@test.com", LocalDate.parse("2000-01-01"));

        when(teacherRepository.findByEmail(testTeacher.getEmail())).thenReturn(Optional.empty());

        // When
        Throwable exception = assertThrows(NotFoundException.class, () -> {
            teacherServiceImpl.updateTeacherById(testTeacher.getEmail(), updatedTeacher);
        });

        // Then
        String expectedMessage = "Teacher does not exist with E-MAIL: " + testTeacher.getEmail();

        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    void testUpdateTeacherInvalidName() {
        // Given
        Teacher updatedTeacher = new Teacher("x", "update@test.com", LocalDate.parse("2000-01-01"));

        when(teacherRepository.findByEmail(testTeacher.getEmail())).thenReturn(Optional.of(testTeacher));

        // When
        Throwable exception = assertThrows(BadRequestException.class, () -> {
            teacherServiceImpl.updateTeacherById(testTeacher.getEmail(), updatedTeacher);
        });

        // Then
        assertEquals(CommonResponses.nameNotValidMsg, exception.getMessage());

    }

    @Test
    void testUpdateTeacherInvalidEmail() {
        // Given
        Teacher updatedTeacher = new Teacher("Test", "test@a.com", LocalDate.parse("2000-01-01"));

        when(teacherRepository.findByEmail(testTeacher.getEmail())).thenReturn(Optional.of(testTeacher));

        Throwable exception = assertThrows(BadRequestException.class, () -> {
            teacherServiceImpl.updateTeacherById(testTeacher.getEmail(), updatedTeacher);
        });

        // then
        assertEquals(CommonResponses.emailNotValidMsg, exception.getMessage());

    }

    @Test
    void testGetTeacherById() {
        // Given
        int teacherID = 1;
        when(teacherRepository.findById(teacherID)).thenReturn(Optional.of(testTeacher));

        // When
        Teacher teacherInDB = teacherServiceImpl.getTeacherById(teacherID);

        // Then
        assertNotNull(teacherInDB);
        assertEquals("Test Teacher", teacherInDB.getName());
        assertEquals("testteacher@example.com", teacherInDB.getEmail());
        assertEquals(LocalDate.parse("1990-01-01"), teacherInDB.getDob());

        verify(teacherRepository, times(1)).findById(teacherID);
        verifyNoMoreInteractions(teacherRepository);
    }

    @Test
    void testGetTeacherByIdNotFound() {
        // Given
        int teacherID = 1;
        when(teacherRepository.findById(teacherID)).thenReturn(Optional.empty());

        // When & Assert
        assertThrows(NotFoundException.class, () -> teacherServiceImpl.getTeacherById(teacherID));

        verify(teacherRepository, times(1)).findById(teacherID);
        verifyNoMoreInteractions(teacherRepository);
    }

    @Test
    void testGetTeacherByEmail() {
        // Given
        when(teacherRepository.findByEmail(testTeacher.getEmail())).thenReturn(Optional.of(testTeacher));

        // When
        Teacher teacherInDB = teacherServiceImpl.getTeacherByEmail(testTeacher.getEmail());

        // Then
        assertNotNull(teacherInDB);
        assertEquals("Test Teacher", teacherInDB.getName());
        assertEquals("testteacher@example.com", teacherInDB.getEmail());
        assertEquals(LocalDate.parse("1990-01-01"), teacherInDB.getDob());

        verify(teacherRepository, times(1)).findByEmail(testTeacher.getEmail());
        verifyNoMoreInteractions(teacherRepository);
    }

    @Test
    void testGetTeacherByEmailNotFound() {
        // Given
        when(teacherRepository.findByEmail(testTeacher.getEmail())).thenReturn(Optional.empty());

        // When & Assert
        assertThrows(NotFoundException.class, () -> teacherServiceImpl.getTeacherByEmail(testTeacher.getEmail()));

        verify(teacherRepository, times(1)).findByEmail(testTeacher.getEmail());
        verifyNoMoreInteractions(teacherRepository);
    }

    @Test
    void testGetTeachers() {
        List<Teacher> teachers = new ArrayList<Teacher>();
        Teacher test2 = new Teacher("test2", "test2@example.com", LocalDate.parse("1995-01-01"));
        Teacher test3 = new Teacher("test3", "test2@example.com", LocalDate.parse("1990-01-01"));
        teachers.add(testTeacher);
        teachers.add(test2);
        teachers.add(test3);

        when(teacherRepository.findAll()).thenReturn(teachers);

        List<Teacher> result = teacherServiceImpl.getTeachers();

        assertEquals(3, result.size());
        assertEquals(result.get(0), testTeacher);
        assertEquals(result.get(1), test2);
        assertEquals(result.get(2), test3);
    }

    @Test
    void testDeleteTeacherByEmail() {
        // Given
        String validEmail = testTeacher.getEmail();
        when(teacherRepository.isExistByEmail(validEmail)).thenReturn(true);

        // When
        teacherServiceImpl.deleteByEmail(validEmail);

        // Then
        verify(teacherRepository, times(1)).deleteByEmail(emailCaptor.capture());
        String capturedEmail = emailCaptor.getValue();
        assertEquals(validEmail, capturedEmail);
    }

    @Test
    public void testDeleteTeacherByEmailNotFound() {
        // Given
        String invalidEmail = "a@a.c";
        when(teacherRepository.isExistByEmail(invalidEmail)).thenReturn(false);

        // When and Assert
        assertThrows(NotFoundException.class, () -> teacherServiceImpl.deleteByEmail(invalidEmail));
    }

    @Test
    void testDeleteTeacherById() {
        // Given
        int existingTeacherID = 1;
        when(teacherRepository.existsById(existingTeacherID)).thenReturn(true);

        // When
        teacherServiceImpl.deleteTeacherById(existingTeacherID);

        // Then
        verify(teacherRepository, times(1)).deleteById(integerCaptor.capture());
        int capturedTeacherId = integerCaptor.getValue();
        assertEquals(existingTeacherID, capturedTeacherId);
    }

    @Test
    void testDeleteTeacherByIdNotFound() {
        // Given
        int TeacherID = 1;
        when(teacherRepository.existsById(TeacherID)).thenReturn(false);

        // When
        Throwable exception = assertThrows(NotFoundException.class, () -> {
            teacherServiceImpl.deleteTeacherById(TeacherID);
        });

        // Then
        String expectedError = "Teacher does not exist with ID: " + TeacherID;
        assertEquals(expectedError, exception.getMessage());
    }
}
