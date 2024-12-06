package com.admin4tutor.server.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.admin4tutor.server.model.entities.Lesson;
import com.admin4tutor.server.model.entities.Student;
import com.admin4tutor.server.model.entities.Tutor;
import com.admin4tutor.server.service.repositories.LessonRepository;
import com.admin4tutor.server.service.repositories.StudentRepository;

@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private LessonRepository lessonRepository;
    
    @Transactional(propagation = Propagation.MANDATORY)
    void addStudent(Student student, List<Lesson> lessons, Tutor tutor){
        student.setTutor(tutor);
        studentRepository.save(student);
        lessons.forEach(lesson -> {
            lesson.setTutor(tutor);
            lesson.setStudent(student);
            lesson.setLanguage(tutor.getLanguage());
        });
        lessonRepository.saveAll(lessons);
    }
}
