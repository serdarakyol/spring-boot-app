package com.example.demo.serviceIml;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
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
import com.example.demo.dto.StudentDTO;
import com.example.demo.entity.Student;
import com.example.demo.exception.BadRequestException;
import com.example.demo.exception.NotFoundException;
import com.example.demo.mapper.StudentMapper;
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

    @InjectMocks
    private StudentMapper courseMapper = spy(StudentMapper.INSTANCE);

    private Student student;
    private StudentDTO studentDTO;

    @BeforeEach
    void setUp() {
        student = Student.builder().name("Test One").email("test.one@example.com").dob(LocalDate.parse("2000-01-01")).build();
        studentDTO = StudentDTO.builder().name(student.getName()).age(24).email(student.getEmail()).build();
    }

    @Test
    void testAddNewStudent() {
        // Given
        when(studentRepository.isExistByEmail(student.getEmail())).thenReturn(false);

        // When
        String actualResponse = studentServiceImpl.addNewStudent(studentDTO);

        // Then
        assertEquals("Successfully created", actualResponse);
        
    }

    @Test
    void testAddNewStudentAlreadyExists() {
        // Given
        when(studentRepository.isExistByEmail(student.getEmail())).thenReturn(true);

        // When
        assertThrows(BadRequestException.class, () -> {
            studentServiceImpl.addNewStudent(studentDTO);
        });

        // Then [Checks if studentRepository.save() method called or not]
        verify(studentRepository, never()).save(any());
    }

    @Test
    void testAddNewStudentInvalidEmail() {
        // Given
        studentDTO.setEmail("invalid_email");

        // When
        assertThrows(BadRequestException.class, () -> {
            studentServiceImpl.addNewStudent(studentDTO);
        });

        // Then
        verify(studentRepository, never()).save(any());
    }

    @Test
    void testAddNewStudentInvalidName() {
        // Given
        studentDTO.setName("J");

        // When
        assertThrows(BadRequestException.class, () -> {
            studentServiceImpl.addNewStudent(studentDTO);
        });

        // Then
        verify(studentRepository, never()).save(any());
    }

    @Test
    void testUpdateStudent() {
        // Given
        StudentDTO updatedStudent = new StudentDTO();
        updatedStudent.setName("Jane Doe");
        updatedStudent.setEmail("janedoe@example.com");
        updatedStudent.setDob(LocalDate.parse("2001-01-01"));

        when(studentRepository.findByEmail(student.getEmail())).thenReturn(Optional.of(student));
        when(studentRepository.isExistByEmail(updatedStudent.getEmail())).thenReturn(false);

        // When
        studentServiceImpl.updateStudent(student.getEmail(), updatedStudent);

        // Then
        assertEquals(student.getName(), updatedStudent.getName());
        assertEquals(student.getDob(), updatedStudent.getDob());
        assertEquals(student.getEmail(), updatedStudent.getEmail());
        assertEquals(student.getDob(), updatedStudent.getDob());
    }

    @Test
    void testUpdateStudentEMailTaken() {
        // Given
        StudentDTO updatedStudent = new StudentDTO();
        updatedStudent.setName("Jane Doe");
        updatedStudent.setEmail("janedoe@example.com");
        updatedStudent.setDob(LocalDate.of(2001, 1, 1));

        when(studentRepository.findByEmail(student.getEmail())).thenReturn(Optional.of(student));
        when(studentRepository.isExistByEmail(updatedStudent.getEmail())).thenReturn(true);

        // When
        Throwable exception = assertThrows(BadRequestException.class, () -> {
            studentServiceImpl.updateStudent(student.getEmail(), updatedStudent);
        });

        // Then
        assertEquals(CommonResponses.emailTakenMsg, exception.getMessage());
    }

    @Test
    void testUpdateStudentAlreadyExist() {
        // Given
        StudentDTO updatedStudent = new StudentDTO();
        updatedStudent.setName("Jane Doe");
        updatedStudent.setEmail("janedoe@example.com");
        updatedStudent.setDob(LocalDate.of(2001, 1, 1));

        when(studentRepository.findByEmail(student.getEmail())).thenReturn(Optional.empty());

        // When
        Throwable exception = assertThrows(NotFoundException.class, () -> {
            studentServiceImpl.updateStudent(student.getEmail(), updatedStudent);
        });

        // Then
        String expectedMessage = "Student doesn't exist with E-mail: " + student.getEmail();

        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    void testUpdateStudentInvalidName() {
        // Given
        StudentDTO updatedStudent = new StudentDTO();
        updatedStudent.setName("a");
        updatedStudent.setEmail("janedoe@example.com");
        updatedStudent.setDob(LocalDate.of(2001, 1, 1));

        when(studentRepository.findByEmail(student.getEmail())).thenReturn(Optional.of(student));

        // When
        Throwable exception = assertThrows(BadRequestException.class, () -> {
            studentServiceImpl.updateStudent(student.getEmail(), updatedStudent);
        });

        // Then
        assertEquals(CommonResponses.nameNotValidMsg, exception.getMessage());

    }

    @Test
    void testUpdateStudentInvalidEmail() {
        // Given
        StudentDTO updatedStudent = new StudentDTO();
        updatedStudent.setName("Test");
        updatedStudent.setEmail("t@t.com");
        updatedStudent.setDob(LocalDate.of(2001, 1, 1));

        when(studentRepository.findByEmail(student.getEmail())).thenReturn(Optional.of(student));

        // When
        Throwable exception = assertThrows(BadRequestException.class, () -> {
            studentServiceImpl.updateStudent(student.getEmail(), updatedStudent);
        });

        // Then
        assertEquals(CommonResponses.emailNotValidMsg, exception.getMessage());
    }

    @Test
    public void testGetStudentById() {
        // Given
        int studentId = 1;
        when(studentRepository.findById(studentId)).thenReturn(Optional.of(student));

        // When
        StudentDTO studentInDB = studentServiceImpl.getStudentById(studentId);

        // Then
        assertNotNull(studentInDB);
        assertEquals("Test One", studentInDB.getName());
        assertEquals("test.one@example.com", studentInDB.getEmail());
        //assertEquals(LocalDate.parse("2000-01-01"), studentInDB.getDob());

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
        when(studentRepository.findByEmail(student.getEmail())).thenReturn(Optional.of(student));

        // When
        StudentDTO studentInDB = studentServiceImpl.getStudentByEmail(student.getEmail());

        // Then
        assertNotNull(studentInDB);
        assertEquals("Test One", studentInDB.getName());
        assertEquals("test.one@example.com", studentInDB.getEmail());
        //assertEquals(LocalDate.parse("2000-01-01"), studentInDB.getDob());

        verify(studentRepository, times(1)).findByEmail(student.getEmail());
        verifyNoMoreInteractions(studentRepository);
    }

    @Test
    void testGetStudentByEmailNotFound() {
        // Given
        when(studentRepository.findByEmail(student.getEmail())).thenReturn(Optional.empty());

        // When & Assert
        assertThrows(NotFoundException.class, () -> studentServiceImpl.getStudentByEmail(student.getEmail()));

        verify(studentRepository, times(1)).findByEmail(student.getEmail());
        verifyNoMoreInteractions(studentRepository);
    }

    @Test
    public void testGetStudents() {
        // Given
        List<Student> students = new ArrayList<Student>();
        Student test2 = new Student("test2", "test2@example.com", LocalDate.parse("1995-01-01"));
        Student test3 = new Student("test3", "test2@example.com", LocalDate.parse("1990-01-01"));
        students.add(student);
        students.add(test2);
        students.add(test3);

        when(studentRepository.findAll()).thenReturn(students);

        // When
        List<StudentDTO> result = studentServiceImpl.getStudents();

        // Then
        assertEquals(3, result.size());
        assertEquals(result.get(0).getName(), student.getName());
        assertEquals(result.get(0).getEmail(), student.getEmail());
        assertEquals(24, result.get(0).getAge());
        assertEquals(result.get(1).getName(), test2.getName());
        assertEquals(result.get(1).getEmail(), test2.getEmail());
        assertEquals(29, result.get(1).getAge());
        assertEquals(result.get(2).getName(), test3.getName());
        assertEquals(result.get(2).getEmail(), test3.getEmail());
        assertEquals(34, result.get(2).getAge());
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
        String validEmail = student.getEmail();
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
