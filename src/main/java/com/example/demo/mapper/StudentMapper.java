package com.example.demo.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.example.demo.dto.StudentDTO;
import com.example.demo.entity.Student;

@Mapper(componentModel = "spring")
public interface StudentMapper {

    StudentMapper INSTANCE = Mappers.getMapper(StudentMapper.class);

    StudentDTO toDTO(Student student);

    Student toEntity(StudentDTO studentDTO);

}
