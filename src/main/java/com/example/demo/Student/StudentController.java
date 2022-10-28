package com.example.demo.Student;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping(path = "api/v1/student")
public class StudentController {

    private final StudentService studentService;
    
    @Autowired
    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping
	public List<Student> getStudent(){
		return studentService.getStudent();
	}

    @PostMapping(value="path")
    public void registerNewStudent(@RequestBody Student student){
        studentService.addNewStudent(student);
    }

}
