package com.example.demo.serviceIml;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
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

    // Given
    @BeforeEach
    void setUp() {
        testCourse = new Course("Data Structure", 10, 200);
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
        testCourse.setCourseCredit(-5);;

        // When
        assertThrows(BadRequestException.class, () -> {
            courseServiceIml.addNewCourse(testCourse);
        });
        
        // Then
        verify(courseRepository, never()).save(any());
    }

    //@Test
    //void testUpdateCourse() {
    //    // Given
    //    testCourse.setCourseId("test-uuid-info");
    //    Course newCourse = new Course("Advanced programming technique", 5, 1);
    //    testCourse.setCourseName(newCourse.getCourseName());
    //    testCourse.setCourseCredit(newCourse.getCourseCredit());
    //    
    //    // When
    //    System.out.println(testCourse.getCourseId());
    //    when(courseRepository.findById(testCourse.getCourseId())).thenReturn(Optional.of(testCourse));
    //    courseServiceIml.updateCourse(testCourse.getCourseId(), newCourse);
//
    //    // Then
    //    assertEquals(testCourse.getCourseName(), newCourse.getCourseName());
    //    assertEquals(testCourse.getCourseCredit(), newCourse.getCourseCredit());
    //}
}
