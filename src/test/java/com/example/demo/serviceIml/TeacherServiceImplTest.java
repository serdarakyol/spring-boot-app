package com.example.demo.serviceIml;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
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
import com.example.demo.dto.TeacherDTO;
import com.example.demo.entity.Teacher;
import com.example.demo.exception.BadRequestException;
import com.example.demo.exception.NotFoundException;
import com.example.demo.mapper.TeacherMapper;
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

    @InjectMocks
    private TeacherMapper courseMapper = spy(TeacherMapper.INSTANCE);

    private Teacher teacher;
    private TeacherDTO teacherDTO;

    @BeforeEach
    void setUp() {
        teacher = Teacher.builder().name("Test Teacher").email("testteacher@example.com").dob(LocalDate.parse("1990-01-01")).build();
        teacherDTO = TeacherDTO.builder().name(teacher.getName()).email(teacher.getEmail()).age(34).build();
    }

    @Test
    void testAddNewTeacher() {
        // Given
        when(teacherRepository.isExistByEmail(teacher.getEmail())).thenReturn(false);

        // When
        String actualResponse = teacherServiceImpl.addNewTeacher(teacherDTO);

        // Then
        ArgumentCaptor<Teacher> teacherArgumentCaptor = ArgumentCaptor.forClass(Teacher.class);
        // That verfies if teacherRepository.save called
        verify(teacherRepository).save(teacherArgumentCaptor.capture());
        Teacher teacherRecord = teacherArgumentCaptor.getValue();
        assertEquals(teacher.getName(), teacherRecord.getName());
        assertEquals(teacher.getEmail(), teacherRecord.getEmail());
        assertEquals("Successfully created", actualResponse);
    }

    @Test
    void testAddNewTeacherAlreadyExists() {
        // Given
        String teacherMail = teacher.getEmail();
        when(teacherRepository.isExistByEmail(teacherMail)).thenReturn(true);

        // When
        assertThrows(BadRequestException.class, () -> {
            teacherServiceImpl.addNewTeacher(teacherDTO);
        });

        // Then [Checks if teacherRepository.save() method called or not]
        verify(teacherRepository, never()).save(any());

    }

    @Test
    void testAddNewTeacherInvalidName() {
        // Given
        teacherDTO.setName("T");

        // When
        assertThrows(BadRequestException.class, () -> {
            teacherServiceImpl.addNewTeacher(teacherDTO);
        });

        // Then
        verify(teacherRepository, never()).save(any());
    }

    @Test
    void testAddNewTeacherInvalidEmail() {
        // Given
        teacherDTO.setEmail("invalid_email");

        // When
        assertThrows(BadRequestException.class, () -> {
            teacherServiceImpl.addNewTeacher(teacherDTO);
        });

        // Then
        verify(teacherRepository, never()).save(any());
    }

    @Test
    void testUpdateTeacherById() {
        // Given
        teacherDTO.setName("Updated Name");
        teacherDTO.setEmail("update@test.com");
        teacherDTO.setDob(LocalDate.parse("2000-01-01"));

        when(teacherRepository.findByEmail(teacher.getEmail())).thenReturn(Optional.of(teacher));
        when(teacherRepository.isExistByEmail(teacherDTO.getEmail())).thenReturn(false);

        // When
        String actualReponse = teacherServiceImpl.updateTeacherById(teacher.getEmail(), teacherDTO);

        // Then
        ArgumentCaptor<Teacher> teacherArgumentCaptor = ArgumentCaptor.forClass(Teacher.class);
        verify(teacherRepository).save(teacherArgumentCaptor.capture());
        Teacher teacherRecord = teacherArgumentCaptor.getValue();
        assertEquals(teacherRecord.getName(), teacher.getName());
        assertEquals(teacherRecord.getEmail(), teacher.getEmail());
        assertEquals(teacherRecord.getAge(), teacher.getAge());
        assertEquals(teacherRecord.getDob(), teacher.getDob());
        assertEquals("Successfully updated", actualReponse);
    }

    @Test
    void testUpdateTeacherByIdEmailTaken() {
        // given
        teacherDTO.setName("Updated Name");
        teacherDTO.setEmail("update@test.com");
        teacherDTO.setDob(LocalDate.parse("2000-01-01"));
        when(teacherRepository.findByEmail(teacher.getEmail())).thenReturn(Optional.of(teacher));
        when(teacherRepository.isExistByEmail(teacherDTO.getEmail())).thenReturn(true);

        // when
        Throwable exception = assertThrows(BadRequestException.class, () -> {
            teacherServiceImpl.updateTeacherById(teacher.getEmail(), teacherDTO);
        });

        // then
        assertEquals(CommonResponses.emailTakenMsg, exception.getMessage());
    }

    @Test
    void testUpdateTeacherAlreadyExist() {
        // Given
        teacherDTO.setName("Updated Name");
        teacherDTO.setEmail("update@test.com");
        teacherDTO.setDob(LocalDate.parse("2000-01-01"));

        when(teacherRepository.findByEmail(teacher.getEmail())).thenReturn(Optional.empty());

        // When
        Throwable exception = assertThrows(NotFoundException.class, () -> {
            teacherServiceImpl.updateTeacherById(teacher.getEmail(), teacherDTO);
        });

        // Then
        String expectedMessage = "Teacher does not exist with E-MAIL: " + teacher.getEmail();

        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    void testUpdateTeacherInvalidName() {
        // Given
        teacherDTO.setName("X");
        teacherDTO.setEmail("update@test.com");
        teacherDTO.setDob(LocalDate.parse("2000-01-01"));

        when(teacherRepository.findByEmail(teacher.getEmail())).thenReturn(Optional.of(teacher));

        // When
        Throwable exception = assertThrows(BadRequestException.class, () -> {
            teacherServiceImpl.updateTeacherById(teacher.getEmail(), teacherDTO);
        });

        // Then
        assertEquals(CommonResponses.nameNotValidMsg, exception.getMessage());

    }

    @Test
    void testUpdateTeacherInvalidEmail() {
        // Given
        teacherDTO.setName("Updated Name");
        teacherDTO.setEmail("test@a.com");
        teacherDTO.setDob(LocalDate.parse("2000-01-01"));

        when(teacherRepository.findByEmail(teacher.getEmail())).thenReturn(Optional.of(teacher));

        Throwable exception = assertThrows(BadRequestException.class, () -> {
            teacherServiceImpl.updateTeacherById(teacher.getEmail(), teacherDTO);
        });

        // then
        assertEquals(CommonResponses.emailNotValidMsg, exception.getMessage());

    }

    @Test
    void testGetTeacherById() {
        // Given
        int teacherID = 1;
        when(teacherRepository.findById(teacherID)).thenReturn(Optional.of(teacher));

        // When
        TeacherDTO actualTeacherDTO = teacherServiceImpl.getTeacherById(teacherID);

        // Then
        assertNotNull(actualTeacherDTO);
        assertEquals("Test Teacher", actualTeacherDTO.getName());
        assertEquals("testteacher@example.com", actualTeacherDTO.getEmail());
        assertEquals(LocalDate.parse("1990-01-01"), actualTeacherDTO.getDob());

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
        when(teacherRepository.findByEmail(teacher.getEmail())).thenReturn(Optional.of(teacher));

        // When
        TeacherDTO actualTeacherDTO = teacherServiceImpl.getTeacherByEmail(teacher.getEmail());

        // Then
        assertNotNull(actualTeacherDTO);
        assertEquals("Test Teacher", actualTeacherDTO.getName());
        assertEquals("testteacher@example.com", actualTeacherDTO.getEmail());
        assertEquals(LocalDate.parse("1990-01-01"), actualTeacherDTO.getDob());

        verify(teacherRepository, times(1)).findByEmail(teacher.getEmail());
        verifyNoMoreInteractions(teacherRepository);
    }

    @Test
    void testGetTeacherByEmailNotFound() {
        // Given
        when(teacherRepository.findByEmail(teacher.getEmail())).thenReturn(Optional.empty());

        // When & Assert
        assertThrows(NotFoundException.class, () -> teacherServiceImpl.getTeacherByEmail(teacher.getEmail()));

        verify(teacherRepository, times(1)).findByEmail(teacher.getEmail());
        verifyNoMoreInteractions(teacherRepository);
    }

    @Test
    void testGetTeachers() {
        List<Teacher> teachers = new ArrayList<Teacher>();
        Teacher test2 = new Teacher("test2", "test2@example.com", LocalDate.parse("1995-01-01"));
        Teacher test3 = new Teacher("test3", "test2@example.com", LocalDate.parse("1991-01-01"));
        teachers.add(teacher);
        teachers.add(test2);
        teachers.add(test3);

        when(teacherRepository.findAll()).thenReturn(teachers);

        List<TeacherDTO> actualTeachers = teacherServiceImpl.getTeachers();

        assertEquals(3, actualTeachers.size());
        assertEquals(actualTeachers.get(0).getName(), teacher.getName());
        assertEquals(actualTeachers.get(0).getEmail(), teacher.getEmail());
        assertEquals(34, actualTeachers.get(0).getAge());
        assertEquals(actualTeachers.get(1).getName(), test2.getName());
        assertEquals(actualTeachers.get(1).getEmail(), test2.getEmail());
        assertEquals(29, actualTeachers.get(1).getAge());
        assertEquals(actualTeachers.get(2).getName(), test3.getName());
        assertEquals(actualTeachers.get(2).getEmail(), test3.getEmail());
        assertEquals(33, actualTeachers.get(2).getAge());
    }

    @Test
    void testDeleteTeacherByEmail() {
        // Given
        String validEmail = teacher.getEmail();
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
