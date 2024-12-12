package com.admin4tutor.server.service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.admin4tutor.server.model.entities.Schedule;
import com.admin4tutor.server.model.entities.Student;
import com.admin4tutor.server.model.entities.Tutor;
import com.admin4tutor.server.service.repositories.ScheduleRepository;
import com.admin4tutor.server.service.repositories.StudentRepository;

@Service
public class StudentService {

    public static final Map<Long, List<Schedule>> SCHEDULES = new ConcurrentHashMap<>();

    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private ScheduleRepository scheduleRepository;
    
    @Transactional(propagation = Propagation.MANDATORY)
    void addStudent(Student student, Tutor tutor){
        student.setTutor(tutor);
        studentRepository.save(student);
        List<Schedule> schedules = SCHEDULES.get(student.getTelegramId());
        schedules.forEach(x -> x.setStudent(student));
        scheduleRepository.saveAll(schedules);
    }

    public Student getStudentByTelegramId(Long telegramId){
        return studentRepository.findByTelegramIdWithSchedules(telegramId);
    }

    @Transactional(propagation = Propagation.MANDATORY)
    void deleteStudent(Student student){
        SCHEDULES.remove(student.getTelegramId());
        studentRepository.delete(student);
    }
}
