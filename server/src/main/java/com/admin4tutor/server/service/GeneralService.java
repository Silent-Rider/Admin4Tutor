package com.admin4tutor.server.service;

import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.admin4tutor.server.model.Status;
import com.admin4tutor.server.model.entities.Lesson;
import com.admin4tutor.server.model.entities.Schedule;
import com.admin4tutor.server.model.entities.Student;
import com.admin4tutor.server.model.entities.Tutor;
import com.admin4tutor.server.service.repositories.LessonRepository;

@Service
public class GeneralService {

    @Autowired
    private TutorService tutorService;
    @Autowired
    private StudentService studentService;
    @Autowired
    private LessonRepository lessonRepository;

    @Transactional
    public void enrollStudent(Long tutorTelegramId, Student student){
        Tutor tutor = tutorService.getTutorByTelegramId(tutorTelegramId);
        List<Schedule> schedules = StudentService.SCHEDULES.get(student.getTelegramId());
        List<Lesson> lessons = createLessons(schedules);
        studentService.addStudent(student, tutor);
        tutorService.updateTutorAvailabilities(tutor, schedules, true);
        lessons.forEach(lesson -> {
            lesson.setTutor(tutor);
            lesson.setStudent(student);
            lesson.setLanguage(tutor.getLanguage());
        });
        lessonRepository.saveAll(lessons);
    }

    @Transactional
    public void deleteStudent(Student student){
        List<Schedule> schedules = student.getSchedules();
        List<Lesson> scheduledLessons = lessonRepository.findByStudentAndStatus(student, Status.SCHEDULED);
        studentService.deleteStudent(student);
        Tutor tutor = student.getTutor();
        tutorService.updateTutorAvailabilities(tutor, schedules, false);
        lessonRepository.deleteAll(scheduledLessons);
    }

    private List<Lesson> createLessons(List<Schedule> schedules){
        List<Lesson> lessons = new ArrayList<>();
        LocalDate today = LocalDate.now();
        for(int i = 0; i < 4; i++){
            for(var schedule: schedules){
                Lesson lesson = new Lesson();
                lesson.setStartTime(schedule.getStartTime());
                lesson.setEndTime(schedule.getStartTime().plusHours(1));
                lesson.setStatus(Status.SCHEDULED);
                lesson.setLessonDate(today.with(TemporalAdjusters.next(schedule.getDayOfWeek())));
                lessons.add(lesson);
            }
            today = today.plusWeeks(1);
        }
        return lessons;
    }

}
