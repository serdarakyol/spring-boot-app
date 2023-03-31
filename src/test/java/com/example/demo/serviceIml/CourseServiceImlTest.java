package com.example.demo.serviceIml;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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

import com.example.demo.entity.Course;
import com.example.demo.exception.BadRequestException;
import com.example.demo.repository.CourseRepository;

@ExtendWith(MockitoExtension.class)
public class CourseServiceImlTest {

    @Mock
    private CourseRepository courseRepository;

    @InjectMocks
    private CourseServiceIml courseServiceIml;

    private Course testCourse;

    @Captor
    private ArgumentCaptor<String> idCaptor;

    String nonExistId = "61a4acb0-123b-4641-a722-448fd3a15b60";

    // Given
    @BeforeEach
    void setUp() {
        testCourse = new Course("Data Structure", 10, 200);
        testCourse.setCourseId("61a4acb0-123b-4641-a722-448fd3a95b60");
    }

    @Test
    void testAddNewCourse() {
        // When
        courseServiceIml.addNewCourse(testCourse);

        // Then
        ArgumentCaptor<Course> courseArgumentCaptor = ArgumentCaptor.forClass(Course.class);
        verify(courseRepository).save(courseArgumentCaptor.capture());
        Course courseRecord = courseArgumentCaptor.getValue();
        assertNotNull(courseRecord);
        assertEquals(courseRecord, testCourse);
        assertEquals(courseRecord.getCourseName(), testCourse.getCourseName());
        assertEquals(courseRecord.getCourseCredit(), testCourse.getCourseCredit());
        assertEquals(courseRecord.getCourseTeacherId(), testCourse.getCourseTeacherId());
    }

    @Test
    void testAddNewCourseInvalidCourseName() {
        // Given
        testCourse.setCourseName("   ");

        // When
        assertThrows(BadRequestException.class, () -> {
            courseServiceIml.addNewCourse(testCourse);
        });

        // Then
        verify(courseRepository, never()).save(any());
    }

    @Test
    void testAddNewCourseInvalidCredit() {
        // Given
        testCourse.setCourseCredit(-5);

        // When
        BadRequestException exp = assertThrows(BadRequestException.class, () -> {
            courseServiceIml.addNewCourse(testCourse);
        });

        // Then
        assertEquals("courseCredit must be equal or greater than 0", exp.getMessage());
    }

    @Test
    void testUpdateCourse() {
        // Given
        Course newCourse = new Course("Advanced programming technique", 5, 1);

        // When
        when(courseRepository.findById(testCourse.getCourseId())).thenReturn(Optional.of(testCourse));
        courseServiceIml.updateCourse(testCourse.getCourseId(), newCourse);

        // Then
        assertEquals(testCourse.getCourseName(), newCourse.getCourseName());
        assertEquals(testCourse.getCourseCredit(), newCourse.getCourseCredit());
    }

    @Test
    void testUpdateCourseNotFound() {
        // Given
        Course newCourse = new Course("Advanced programming technique", 5, 1);

        // When
        BadRequestException exp = assertThrows(BadRequestException.class, () -> {
            courseServiceIml.updateCourse(nonExistId, newCourse);
        });

        // Then
        assertEquals("Course is not exist with ID: " + nonExistId, exp.getMessage());
    }

    @Test
    void testUpdateCourseInvalidCourseName() {
        // Given
        Course newCourse = new Course("   ", 5, 1);

        // When
        when(courseRepository.findById(testCourse.getCourseId())).thenReturn(Optional.of(testCourse));
        BadRequestException exp = assertThrows(BadRequestException.class, () -> {
            courseServiceIml.updateCourse(testCourse.getCourseId(), newCourse);
        });

        // Then
        assertEquals("courseName can not be null, empty or only whitespace", exp.getMessage());
    }

    @Test
    void testUpdateCourseInvalidCredit() {
        // Given
        Course newCourse = new Course("Advanced programming technique", -5, 1);

        // When
        when(courseRepository.findById(testCourse.getCourseId())).thenReturn(Optional.of(testCourse));
        BadRequestException exp = assertThrows(BadRequestException.class, () -> {
            courseServiceIml.updateCourse(testCourse.getCourseId(), newCourse);
        });

        // Then
        assertEquals("courseCredit must be equal or greater than 0", exp.getMessage());
    }

    @Test
    void testDeleteCourseById() {
        // Given
        when(courseRepository.existsById(testCourse.getCourseId())).thenReturn(true);

        // When
        courseServiceIml.deleteCourseById(testCourse.getCourseId());

        // Then
        verify(courseRepository, times(1)).deleteById(idCaptor.capture());
        String capturedId = idCaptor.getValue();
        assertEquals(testCourse.getCourseId(), capturedId);
    }

    @Test
    void testDeleteCourseByIdNotFound() {
        // When
        BadRequestException exp = assertThrows(BadRequestException.class, () -> {
            courseServiceIml.deleteCourseById(nonExistId);
        });

        // Then
        assertEquals("Course is not exist with ID: " + nonExistId, exp.getMessage());
    }

    @Test
    void testGetCourseById() {
        // Given
        when(courseRepository.findById(testCourse.getCourseId())).thenReturn(Optional.of(testCourse));

        // When
        Course courseRecord = courseServiceIml.getCourseById(testCourse.getCourseId());

        // Then
        assertNotNull(courseRecord);
        assertEquals(courseRecord, testCourse);
        assertEquals(courseRecord.getCourseId(), testCourse.getCourseId());
        assertEquals(courseRecord.getCourseName(), testCourse.getCourseName());
        assertEquals(courseRecord.getCourseCredit(), testCourse.getCourseCredit());
        assertEquals(courseRecord.getCourseTeacherId(), testCourse.getCourseTeacherId());
    }

    @Test
    void testGetCourseByIdNotFound() {
        // When
        BadRequestException exp = assertThrows(BadRequestException.class, () -> {
            courseServiceIml.getCourseById(nonExistId);
        });

        // Then
        assertEquals("Course is not exist with ID: " + nonExistId, exp.getMessage());
    }

    @Test
    void testGetCourses() {
        // Given
        List<Course> actuals = new ArrayList<Course>();
        Course APT = new Course("Advanced programming technique", 5, 1);
        Course PL = new Course("Programming languages", 7, 2);
        actuals.add(PL);
        actuals.add(APT);
        actuals.add(testCourse);

        // When
        when(courseRepository.findAll()).thenReturn(actuals);
        List<Course> records = courseServiceIml.getCourses();

        // Then
        verify(courseRepository, times(1)).findAll();
        assertNotNull(records);
        System.out.println(records.size());
        assertEquals(3, records.size());
        for (int i = 0; i < records.size(); i++) {
            assertEquals(records.get(i).getCourseName(), actuals.get(i).getCourseName());
            assertEquals(records.get(i).getCourseCredit(), actuals.get(i).getCourseCredit());
            assertEquals(records.get(i).getCourseTeacherId(), actuals.get(i).getCourseTeacherId());
        }
    }
}
