package com.admin4tutor.server.service;

import org.springframework.stereotype.Service;

import com.admin4tutor.server.service.repositories.AvailabilityRepository;
import com.admin4tutor.server.service.repositories.LessonRepository;
import com.admin4tutor.server.service.repositories.StudentRepository;
import com.admin4tutor.server.service.repositories.TutorRepository;

@Service
public class DatabaseService {
    
    private AvailabilityRepository availabilityRepository;
    private LessonRepository lessonRepository;
    private StudentRepository studentRepository;
    private TutorRepository tutorRepository;

    public void saveTutor(){
        
    }
    
}
