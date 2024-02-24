package com.example.demo.serviceIml;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.dto.CourseDTO;
import com.example.demo.entity.Course;
import com.example.demo.exception.BadRequestException;
import com.example.demo.mapper.CourseMapper;
import com.example.demo.repository.CourseRepository;
import com.example.demo.service.CourseService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@AllArgsConstructor
public class CourseServiceIml implements CourseService {
    private final String courseNotExist = "Course is not exist with ID: ";
    private final String courseNameNotValid = "course_ame can not be null, empty or only whitespace";
    private final String creditNotValid = "course_credit must be greater than 0";
    private final String courseDeleted = "Course successfully deleted with ID: ";

    private final CourseRepository courseRepository;

    private CourseMapper courseMapper;

    @Override
    public void addNewCourse(CourseDTO courseDTO) {
        if (!isValidCourseName(courseDTO.getCourseName())) {
            log.error(courseNameNotValid);
            throw new BadRequestException(courseNameNotValid);
        }

        if (courseDTO.getCourseCredit() < 1) {
            log.error(creditNotValid);
            throw new BadRequestException(creditNotValid);
        }
        Course course = courseMapper.toEntity(courseDTO);
        course.setIsActive(true);
        courseRepository.save(course);
        log.info("New course saved with ID: ".concat(course.getId()));
    }

    @Transactional
    @Override
    public void updateCourse(String courseId, CourseDTO courseDTO) {
        Course currentCourse = courseRepository.findById(courseId).orElseThrow(() -> {
            log.error(courseNotExist + courseId);
            throw new BadRequestException(courseNotExist + courseId);
        });

        // Check course name
        String courseName = courseDTO.getCourseName();
        if (!courseName.equals(currentCourse.getCourseName()) && !isValidCourseName(courseName)) {
            log.error(courseNameNotValid);
            throw new BadRequestException(courseNameNotValid);
        }
        currentCourse.setCourseName(courseDTO.getCourseName());

        // Check course credit
        if (courseDTO.getCourseCredit() != currentCourse.getCourseCredit()) {
            if (courseDTO.getCourseCredit() < 0 || courseDTO.getCourseCredit() == null) {
                log.error(creditNotValid);
                throw new BadRequestException(creditNotValid);
            }
            currentCourse.setCourseCredit(courseDTO.getCourseCredit());
        }

        log.info("Course successfully updated with ID: {}", currentCourse.getId());
    }

    @Override
    public void deleteCourseById(String courseId) {
        Course course = courseRepository.findByIdAndIsActiveTrue(courseId).orElseThrow(() -> {
            log.error(courseNotExist + courseId);
            throw new BadRequestException(courseNotExist + courseId);
        });
        course.setIsActive(Boolean.FALSE);
        courseRepository.save(course);
        log.info(courseDeleted + courseId);
    }

    @Override
    public CourseDTO getCourseById(String courseId) {
        Course course = courseRepository.findById(courseId).orElseThrow(() -> {
            log.error(courseNotExist + courseId);
            throw new BadRequestException(courseNotExist + courseId);
        });

        return courseMapper.toDTO(course);
    }

    @Override
    public List<CourseDTO> getCourses() {
        log.info("All courses are called.");
        List<Course> courses = courseRepository.findAll();
        return courses.stream().map(c -> courseMapper.toDTO(c)).toList();
    }

    private boolean isValidCourseName(String courseName) {
        return courseName.isBlank() || courseName == null ? false : true;
    }

    
}
