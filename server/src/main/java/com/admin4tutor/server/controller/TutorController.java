package com.admin4tutor.server.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.admin4tutor.server.model.Language;
import com.admin4tutor.server.model.entities.Availability;
import com.admin4tutor.server.model.entities.Schedule;
import com.admin4tutor.server.model.entities.Tutor;
import com.admin4tutor.server.service.StudentService;
import com.admin4tutor.server.service.TutorService;


@RequestMapping("/tutors")
@RestController
public class TutorController{

    private final TutorService tutorService;

    public TutorController(TutorService databaseService){
        this.tutorService = databaseService;
    }
    
    @PostMapping
    public ResponseEntity<String> createTutor(@RequestBody Tutor tutor) {
        tutorService.addTutor(tutor);
        return new ResponseEntity<>("Tutor received, waiting for availabilities.", 
        HttpStatus.ACCEPTED);
    }
    
    @PostMapping("/availabilities")
    public ResponseEntity<String> addAvailabilities(@RequestParam Long telegramId,
    @RequestBody List<Availability> availabilities){
        tutorService.saveAvailabilities(telegramId, availabilities);
        return new ResponseEntity<>("Tutor has been successfully saved to the database", 
        HttpStatus.CREATED);
    }

    @PostMapping("/search")
    public ResponseEntity<List<Tutor>> getSuitableTutors(@RequestParam Language language, 
    @RequestParam Long telegramId, @RequestBody List<Schedule> lessons){
        List<Tutor> tutors = tutorService.getAvailableTutorsByLanguage(lessons, language);
        StudentService.SCHEDULES.put(telegramId, lessons);
        return new ResponseEntity<>(tutors, HttpStatus.OK);
    }
    
    @DeleteMapping("/delete/{telegramId}")
    public ResponseEntity<String> deleteTutor(@PathVariable Long telegramId){
        Tutor tutor = tutorService.getTutorByTelegramId(telegramId);
        if(tutor == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        tutorService.deleteTutor(tutor);
        return new ResponseEntity<>("Tutor and all associated entitites " + 
        "have been removed from the database", HttpStatus.OK);
    }
}