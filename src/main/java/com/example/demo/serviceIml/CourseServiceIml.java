package com.example.demo.serviceIml;

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
        Optional<Course> optionalCourse = courseRepository.findById(courseNameNotValid);
        if (optionalCourse.isPresent()) {
            log.error(courseNotExist + courseId);
            throw new BadRequestException(courseNotExist + courseId);
        }

        Course currentCourse = optionalCourse.get();

        // Check course name
        String courseName = updateCourse.getCourseName();
        if (courseName == null || courseName.trim().length() == 0) {
            log.error("CourseName can not be blank");
            throw new BadRequestException("CourseName can not be blank");
        }
        currentCourse.setCourseName(updateCourse.getCourseName());

        // Check course credit
        if (updateCourse.getCourseCredit() < 0 || updateCourse.getCourseCredit().equals(null)) {
            log.error("CourseCredit can not be lover than zero");
            throw new BadRequestException("CourseCredit can not be lover than zero");
        }
        currentCourse.setCourseCredit(updateCourse.getCourseCredit());

        log.info("Course updated: {}", updateCourse.toString());
    }
}
