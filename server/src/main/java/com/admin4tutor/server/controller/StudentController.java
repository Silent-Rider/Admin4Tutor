package com.admin4tutor.server.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.admin4tutor.server.model.entities.Student;
import com.admin4tutor.server.service.StudentService;

@RestController
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService){
        this.studentService = studentService;
    }
    
    @PostMapping("/students")
    public ResponseEntity<String> getStudent(@RequestParam Long tutorTelegramId,
    @RequestBody Student student) {
        Long telegramId = student.getTelegramId();
        if(!LessonTemplate.schedules.containsKey(telegramId)) 
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        studentService.addStudent(tutorTelegramId, student, LessonTemplate.schedules.get(telegramId));
        return new ResponseEntity<>("Student has been successfully saved to the database", 
        HttpStatus.CREATED);
    }

}
