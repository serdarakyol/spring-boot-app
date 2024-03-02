package com.example.demo.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.example.demo.dto.CourseDTO;
import com.example.demo.entity.Course;

@Mapper(componentModel = "spring")
public interface CourseMapper {
    CourseMapper INSTANCE = Mappers.getMapper(CourseMapper.class);

    CourseDTO toDTO(Course course);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "isActive", ignore = true)
    Course toEntity(CourseDTO course);

}
