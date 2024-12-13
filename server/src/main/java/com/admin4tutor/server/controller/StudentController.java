package com.admin4tutor.server.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.admin4tutor.server.model.entities.Student;
import com.admin4tutor.server.service.GeneralService;
import com.admin4tutor.server.service.StudentService;

@RequestMapping("/students")
@RestController
public class StudentController {

    private final GeneralService generalService;
    private final StudentService studentService;

    public StudentController(GeneralService generalService, StudentService studentService){
        this.generalService = generalService;
        this.studentService = studentService;
    }
    
    @PostMapping("/enroll")
    public ResponseEntity<String> enrollStudent(@RequestParam Long tutorTelegramId,
    @RequestBody Student student) {
        Long telegramId = student.getTelegramId();
        if(!StudentService.SCHEDULES.containsKey(telegramId)) 
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        generalService.enrollStudent(tutorTelegramId, student);
        return new ResponseEntity<>("Student has been successfully saved to the database", 
        HttpStatus.CREATED);
    }

    @DeleteMapping("/delete/{telegramId}")
    public ResponseEntity<String> deleteStudent(@PathVariable Long telegramId){
        Student student = studentService.getStudentByTelegramId(telegramId);
        if(student == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        generalService.deleteStudent(student);
        return new ResponseEntity<>("Student and all associated scheduled classes " + 
        "have been removed from the database", HttpStatus.OK);
    }
}
