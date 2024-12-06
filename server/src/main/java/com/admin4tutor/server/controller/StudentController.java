package com.admin4tutor.server.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.admin4tutor.server.model.entities.Student;
import com.admin4tutor.server.service.EnrollmentService;

@RequestMapping("/students")
@RestController
public class StudentController {

    private final EnrollmentService enrollmentService;

    public StudentController(EnrollmentService studentService){
        this.enrollmentService = studentService;
    }
    
    @PostMapping("/enroll")
    public ResponseEntity<String> enrollStudent(@RequestParam Long tutorTelegramId,
    @RequestBody Student student) {
        Long telegramId = student.getTelegramId();
        if(!LessonTemplate.SCHEDULES.containsKey(telegramId)) 
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        enrollmentService.enrollStudent(tutorTelegramId, student, LessonTemplate.SCHEDULES.get(telegramId));
        return new ResponseEntity<>("Student has been successfully saved to the database", 
        HttpStatus.CREATED);
    }
}
