package com.example.demo.serviceIml;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
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

import com.example.demo.dto.CourseDTO;
import com.example.demo.entity.Course;
import com.example.demo.exception.BadRequestException;
import com.example.demo.mapper.CourseMapper;
import com.example.demo.repository.CourseRepository;

@ExtendWith(MockitoExtension.class)
public class CourseServiceImlTest {

    @Mock
    private CourseRepository courseRepository;

    @InjectMocks
    private CourseServiceIml courseServiceIml;

    @InjectMocks
    private CourseMapper courseMapper = spy(CourseMapper.INSTANCE);

    private Course course;
    private CourseDTO courseDTO;

    @Captor
    private ArgumentCaptor<String> idCaptor;

    private String nonExistId = "61a4acb0-123b-4641-a722-448fd3a15b60";

    // Given
    @BeforeEach
    void setUp() {
        course = Course.builder().id("61a4acb0-123b-4641-a722-448fd3a95b60").courseName("Data Structure")
                .courseCredit(3).isActive(true).build();

        courseDTO = CourseDTO.builder().courseName(course.getCourseName()).courseCredit(course.getCourseCredit())
                .build();
    }

    @Test
    void testAddNewCourse() {
        // Given - When
        courseServiceIml.addNewCourse(courseDTO);

        // Then
        ArgumentCaptor<Course> courseArgumentCaptor = ArgumentCaptor.forClass(Course.class);
        verify(courseRepository).save(courseArgumentCaptor.capture());
        Course savedCourse = courseArgumentCaptor.getValue();
        assertNotNull(savedCourse);
        assertEquals(savedCourse.getCourseName(), course.getCourseName());
        assertEquals(savedCourse.getCourseCredit(), course.getCourseCredit());
    }

    @Test
    void testAddNewCourseInvalidCourseName() {
        // Given
        courseDTO.setCourseName("   ");

        // When
        assertThrows(BadRequestException.class, () -> {
            courseServiceIml.addNewCourse(courseDTO);
        });

        // Then
        verify(courseRepository, never()).save(any());
    }

    @Test
    void testAddNewCourseInvalidCredit() {
        // Given
        courseDTO.setCourseCredit(-5);

        // When
        BadRequestException exp = assertThrows(BadRequestException.class, () -> {
            courseServiceIml.addNewCourse(courseDTO);
        });

        // Then
        assertEquals("course_credit must be greater than 0", exp.getMessage());
    }

    @Test
    void testUpdateCourse() {
        // Given
        CourseDTO expectedCourseDTO = CourseDTO.builder().courseCredit(3).courseName("Data Structure").build();
        String expectedResponse = "Successfully updated";

        // When
        when(courseRepository.findById(course.getId())).thenReturn(Optional.of(course));
        String response = courseServiceIml.updateCourse(course.getId(), expectedCourseDTO);

        // Then
        assertEquals(expectedCourseDTO.getCourseName(), course.getCourseName());
        assertEquals(expectedCourseDTO.getCourseCredit(), course.getCourseCredit());
        assertEquals(expectedResponse, response);
    }

    @Test
    void testUpdateCourseNotFound() {
        // Given - When
        when(courseRepository.findById(nonExistId)).thenReturn(Optional.empty());
        BadRequestException exp = assertThrows(BadRequestException.class, () -> {
            courseServiceIml.updateCourse(nonExistId, courseDTO);
        });

        // Then
        assertEquals("Course is not exist with ID: " + nonExistId, exp.getMessage());
    }

    @Test
    void testUpdateCourseInvalidCourseName() {
        // Given
        courseDTO.setCourseName("   ");

        // When
        when(courseRepository.findById(course.getId())).thenReturn(Optional.of(course));
        BadRequestException exp = assertThrows(BadRequestException.class, () -> {
            courseServiceIml.updateCourse(course.getId(), courseDTO);
        });

        // Then
        assertEquals("course_name can not be null, empty or only whitespace", exp.getMessage());
    }

    @Test
    void testUpdateCourseInvalidCredit() {
        // Given
        courseDTO.setCourseCredit(-1);

        // When
        when(courseRepository.findById(course.getId())).thenReturn(Optional.of(course));
        BadRequestException exp = assertThrows(BadRequestException.class, () -> {
            courseServiceIml.updateCourse(course.getId(), courseDTO);
        });

        // Then
        assertEquals("course_credit must be greater than 0", exp.getMessage());
    }

    @Test
    void testDeleteCourseById() {
        // Given
        when(courseRepository.findByIdAndIsActiveTrue(course.getId())).thenReturn(Optional.of(course));

        // When
        courseServiceIml.deleteCourseById(course.getId());

        // Then
        verify(courseRepository, times(1)).findByIdAndIsActiveTrue(idCaptor.capture());
        ArgumentCaptor<Course> argumentCaptorCourse = ArgumentCaptor.forClass(Course.class);
        verify(courseRepository).save(argumentCaptorCourse.capture());
        Course capturedCourse = argumentCaptorCourse.getValue();
        assertEquals(false, capturedCourse.getIsActive());
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
        when(courseRepository.findById(course.getId())).thenReturn(Optional.of(course));

        // When
        CourseDTO actualCourse = courseServiceIml.getCourseById(course.getId());

        // Then
        assertNotNull(actualCourse);
        assertEquals(course.getCourseName(), actualCourse.getCourseName());
        assertEquals(course.getCourseCredit(), actualCourse.getCourseCredit());
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
        List<CourseDTO> expectedCourses = new ArrayList<>();
        expectedCourses.add(courseDTO);
        List<Course> actualCourses = new ArrayList<>();
        actualCourses.add(course);

        // When
        when(courseRepository.findAll()).thenReturn(actualCourses);
        List<CourseDTO> actualResponse = courseServiceIml.getCourses();

        // Then
        assertNotNull(actualResponse);
        assertEquals(1, actualResponse.size());
        assertEquals(expectedCourses.get(0).getCourseName(), actualResponse.get(0).getCourseName());
        assertEquals(expectedCourses.get(0).getCourseCredit(), actualResponse.get(0).getCourseCredit());

    }
}
