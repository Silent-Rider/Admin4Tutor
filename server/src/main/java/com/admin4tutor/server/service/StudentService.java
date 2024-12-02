package com.admin4tutor.server.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.admin4tutor.server.controller.LessonTemplate;
import com.admin4tutor.server.model.entities.Student;
import com.admin4tutor.server.model.entities.Tutor;
import com.admin4tutor.server.service.repositories.LessonRepository;
import com.admin4tutor.server.service.repositories.StudentRepository;
import com.admin4tutor.server.service.repositories.TutorRepository;

@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private LessonRepository lessonRepository;
    @Autowired
    private TutorRepository tutorRepository;
    
    @Transactional
    public void addStudent(Long tutorTelegramId, Student student, List<LessonTemplate> schedule){
        Tutor tutor = tutorRepository.findByTelegramIdWithAvailabilities(tutorTelegramId);
        
    }
}
