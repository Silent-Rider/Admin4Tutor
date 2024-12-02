package com.admin4tutor.server.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.admin4tutor.server.model.Language;
import com.admin4tutor.server.model.entities.Availability;
import com.admin4tutor.server.model.entities.Tutor;
import com.admin4tutor.server.service.TutorService;



@RestController
public class TutorController{

    private final TutorService tutorService;

    public TutorController(TutorService databaseService){
        this.tutorService = databaseService;
    }
    
    @PostMapping("/tutors/post")
    public ResponseEntity<String> getTutor(@RequestBody Tutor tutor) {
        tutorService.addTutor(tutor.getTelegramId(), tutor);
        return new ResponseEntity<>("Tutor received, waiting for availabilities.", 
        HttpStatus.ACCEPTED);
    }
    
    @PostMapping("/availabilities")
    public ResponseEntity<String> getAvailabilities(@RequestParam Long telegramId,
    @RequestBody List<Availability> availabilities){
        tutorService.saveAvailabilities(telegramId, availabilities);
        return new ResponseEntity<>("Tutor has been successfully saved to the database", 
        HttpStatus.CREATED);
    }

    @PostMapping("/tutors/get")
    public ResponseEntity<List<Tutor>> sendTutors(@RequestParam Language language,
    @RequestBody List<LessonTemplate> lessons){
        List<Tutor> tutors = tutorService.getTutors(lessons, language);
        return new ResponseEntity<>(tutors, HttpStatus.OK);
    }    
}