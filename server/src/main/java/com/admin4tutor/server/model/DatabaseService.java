package com.admin4tutor.server.model;

import org.springframework.stereotype.Service;

import com.admin4tutor.server.model.repositories.AvailabilityRepository;
import com.admin4tutor.server.model.repositories.LessonRepository;
import com.admin4tutor.server.model.repositories.StudentRepository;
import com.admin4tutor.server.model.repositories.TutorRepository;

@Service
public class DatabaseService {
    
    private AvailabilityRepository availabilityRepository;
    private LessonRepository lessonRepository;
    private StudentRepository studentRepository;
    private TutorRepository tutorRepository;

    
}
