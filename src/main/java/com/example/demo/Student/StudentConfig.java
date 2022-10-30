package com.example.demo.Student;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StudentConfig {
    @Bean
    CommandLineRunner commandLineRunner(StudentRepository repository) {
        return args -> {
            Student serdar = new Student(
                    "Serdar",
                    "serdarakyol55@outlook.com",
                    LocalDate.of(1234, Month.MAY, 5));

            Student anotherStudent = new Student(
                    "anotherStudent",
                    "anotherStudent@outlook.com",
                    LocalDate.of(1235, Month.MAY, 4));

            repository.saveAll(List.of(serdar, anotherStudent));
        };
    };
}
