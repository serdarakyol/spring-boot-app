package com.example.demo.serviceIml;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.entity.Course;
import com.example.demo.exception.BadRequestException;
import com.example.demo.repository.CourseRepository;
import com.example.demo.service.CourseService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
@RequiredArgsConstructor
public class CourseServiceIml implements CourseService {
    private final String courseNotExist = "Course is not exist with ID: ";
    private final String courseNameNotValid = "courseName can not be null, empty or only whitespace";
    private final String creditNotValid = "courseCredit must be equal or greater than 0";
    private final String courseDeleted = "Course successfully deleted with ID: ";
    private final String courseFound = "Course successfully found: ";

    private final CourseRepository courseRepository;

    @Override
    public void addNewCourse(Course course) {
        String courseName = course.getCourseName();
        if (courseName == null || courseName.trim().length() == 0) {
            log.error(courseNameNotValid);
            throw new BadRequestException(courseNameNotValid);
        }

        if (course.getCourseCredit() < 0) {
            log.error(creditNotValid);
            throw new BadRequestException(creditNotValid);
        }

        courseRepository.save(course);
        log.info("New course saved: {}", course.toString());
    }

    @Transactional
    @Override
    public void updateCourse(String courseId, final Course updateCourse) {
        Optional<Course> optionalCourse = courseRepository.findById(courseId);
        if (!optionalCourse.isPresent()) {
            log.error(courseNotExist + courseId);
            throw new BadRequestException(courseNotExist + courseId);
        }

        Course currentCourse = optionalCourse.get();

        // Check course name
        String courseName = updateCourse.getCourseName();
        if (!courseName.equals(currentCourse.getCourseName())) {
            if (courseName == null || courseName.trim().length() == 0) {
                log.error(courseNameNotValid);
                throw new BadRequestException(courseNameNotValid);
            }
            currentCourse.setCourseName(updateCourse.getCourseName());
        }

        // Check course credit
        if (updateCourse.getCourseCredit() != currentCourse.getCourseCredit()) {
            if (updateCourse.getCourseCredit() < 0 || updateCourse.getCourseCredit().equals(null)) {
                log.error(creditNotValid);
                throw new BadRequestException(creditNotValid);
            }
            currentCourse.setCourseCredit(updateCourse.getCourseCredit());
        }

        log.info("Course updated: {}", updateCourse.toString());
    }

    @Override
    public void deleteCourseById(String courseId) {
        if (!courseRepository.existsById(courseId)) {
            log.error(courseNotExist + courseId);
            throw new BadRequestException(courseNotExist + courseId);
        }
        courseRepository.deleteById(courseId);
        log.info(courseDeleted + courseId);
    }

    @Override
    public Course getCourseById(String courseId) {
        Optional<Course> courseOpt = courseRepository.findById(courseId);
        if (!courseOpt.isPresent()) {
            log.error(courseNotExist + courseId);
            throw new BadRequestException(courseNotExist + courseId);
        }

        Course course = courseOpt.get();
        log.info(courseFound + course.toString());
        return course;
    }

    @Override
    public List<Course> getCourses() {
        log.info("All courses are called.");
        return courseRepository.findAll();
    }
}
