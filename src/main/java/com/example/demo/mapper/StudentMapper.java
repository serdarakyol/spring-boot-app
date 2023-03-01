package com.example.demo.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.example.demo.dto.StudentDTO;
import com.example.demo.entity.Student;

@Mapper(componentModel = "spring")
public interface StudentMapper {
    StudentMapper INSTANCE = Mappers.getMapper(StudentMapper.class);

    @Mapping(source = "studentName", target = "studentName")
    @Mapping(source = "studentEmail", target = "studentEmail")
    @Mapping(source = "studentAge", target = "studentAge")
    StudentDTO studentToDto(Student student);

    List<StudentDTO> studentToDto(List<Student> students);
}
