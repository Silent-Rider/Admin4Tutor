package com.admin4tutor.server.web;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
public class TutorController{
    
    @PostMapping("/tutors")
    public String displayStartPage(@RequestBody String tutor) {
        System.out.println(tutor);
        return null;
    }
    
}