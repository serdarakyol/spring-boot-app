package com.example.demo.mapper;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.example.demo.dto.CourseDTO;
import com.example.demo.dto.TeacherDTO;
import com.example.demo.entity.Course;
import com.example.demo.entity.Teacher;

@Mapper(componentModel = "spring")
public interface TeacherMapper {

    TeacherMapper INSTANCE = Mappers.getMapper(TeacherMapper.class);

    @Mapping(target = "enrolledCourses", source = "teachingCourses")
    TeacherDTO toDTO(Teacher teacher);

    Teacher toEntity(TeacherDTO teacherDTO);

    default Set<CourseDTO> coursesToDTOS(Set<Course> courses) {
        return courses == null || courses.size() == 0 ? new HashSet<>()
                : courses.stream().map(course -> CourseMapper.INSTANCE.toDTO(course)).collect(Collectors.toSet());
    }

}
