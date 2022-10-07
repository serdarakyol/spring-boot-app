package com.example.demo.Student;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class StudentService {
    public List<Student> getStudent(){
		return List.of(
			new Student(1L,
						"Serdar",
						"serdarakyol55@outlook.com",
						LocalDate.of(1995, Month.MAY, 5),
						21
			)
		);
	}
}
