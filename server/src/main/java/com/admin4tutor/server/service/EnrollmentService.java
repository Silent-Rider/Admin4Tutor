package com.admin4tutor.server.service;

import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.admin4tutor.server.controller.LessonTemplate;
import com.admin4tutor.server.model.Status;
import com.admin4tutor.server.model.entities.Lesson;
import com.admin4tutor.server.model.entities.Student;
import com.admin4tutor.server.model.entities.Tutor;

@Service
public class EnrollmentService {

    @Autowired
    private TutorService tutorService;
    @Autowired
    private StudentService studentService;

    @Transactional
    public void enrollStudent(Long tutorTelegramId, Student student, List<LessonTemplate> schedule){
        Tutor tutor = tutorService.getTutorByTelegramId(tutorTelegramId);
        List<Lesson> lessons = createLessons(schedule);
        studentService.addStudent(student, lessons, tutor);
        tutorService.updateTutorAvailabilities(tutor, schedule);
    }

    private List<Lesson> createLessons(List<LessonTemplate> schedule){
        List<Lesson> lessons = new ArrayList<>();
        LocalDate today = LocalDate.now();
        for(int i = 0; i < 4; i++){
            for(var template: schedule){
                Lesson lesson = new Lesson();
                lesson.setStartTime(template.getStartTime());
                lesson.setEndTime(template.getStartTime().plusHours(1));
                lesson.setStatus(Status.SCHEDULED);
                lesson.setLessonDate(today.with(TemporalAdjusters.next(template.getDayOfWeek())));
                lessons.add(lesson);
            }
            today = today.plusWeeks(1);
        }
        return lessons;
    }

}
