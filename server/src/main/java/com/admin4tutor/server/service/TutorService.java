package com.admin4tutor.server.service;

import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.admin4tutor.server.model.Language;
import com.admin4tutor.server.model.entities.Availability;
import com.admin4tutor.server.model.entities.Schedule;
import com.admin4tutor.server.model.entities.Tutor;
import com.admin4tutor.server.service.repositories.AvailabilityRepository;
import com.admin4tutor.server.service.repositories.TutorRepository;

@Service
public class TutorService {

    private static final Map<Long, Tutor> PENDING_TUTORS = new ConcurrentHashMap<>();

    @Autowired
    private TutorRepository tutorRepository;
    @Autowired
    private AvailabilityRepository availabilityRepository;

    public void addTutor(Long telegramId, Tutor tutor){
        PENDING_TUTORS.put(telegramId, tutor);
    }

    @Transactional
    public Tutor saveAvailabilities(Long telegramId, List<Availability> availabilities){
        Tutor tutor = PENDING_TUTORS.get(telegramId);
        tutorRepository.save(tutor);
        availabilities.forEach(x -> x.setTutor(tutor));
        availabilityRepository.saveAll(availabilities);
        return PENDING_TUTORS.remove(telegramId);
    }

    public List<Tutor> getAvailableTutorsByLanguage(List<Schedule> schedules, Language language){
        List<Tutor> tutors = tutorRepository.findByLanguageWithAvailabilities(language);
        if(tutors.isEmpty()) return tutors;
        tutorLoop: for(int i = 0; i < tutors.size(); i++){
            Tutor tutor = tutors.get(i);
            for(Schedule lesson: schedules){
                if(!isTutorAvailableForLesson(tutor, lesson)){
                    tutors.remove(tutor);
                    continue tutorLoop;
                }
            }
        }
        return tutors;
    }

    public Tutor getTutorByTelegramId(Long telegramId){
        return tutorRepository.findByTelegramIdWithAvailabilities(telegramId);
    }

    @Transactional(propagation = Propagation.MANDATORY)
    void updateTutorAvailabilities(Tutor tutor, List<Schedule> schedules){
        List<Availability> availabilities = tutor.getAvailabilities();
        for (var lesson : schedules) {
            Availability interval = availabilities.stream()
                .filter(a -> a.getDayOfWeek().equals(lesson.getDayOfWeek()) &&
                !a.getStartTime().isAfter(lesson.getStartTime()) &&
                !a.getEndTime().isBefore(lesson.getStartTime().plusHours(1)))
                .findFirst().get();
            adjustAvailability(interval, lesson, availabilities);
        }
        availabilityRepository.saveAll(availabilities);
    }

    private boolean isTutorAvailableForLesson(Tutor tutor, Schedule lesson) {
        LocalTime endTime = lesson.getStartTime().plusHours(1);
        return tutor.getAvailabilities().stream().anyMatch(availability ->
        availability.getDayOfWeek().equals(lesson.getDayOfWeek()) &&
        !lesson.getStartTime().isBefore(availability.getStartTime()) &&
        !endTime.isAfter(availability.getEndTime()));
    }

    private void adjustAvailability(Availability interval, Schedule lesson, 
    List<Availability> availabilities){
        LocalTime lessonStart = lesson.getStartTime();
        LocalTime lessonEnd = lessonStart.plusHours(1);
        if (interval.getStartTime().equals(lessonStart) && interval.getEndTime().equals(lessonEnd))
            availabilities.remove(interval);
        else if (interval.getStartTime().equals(lessonStart))
            interval.setStartTime(lessonEnd);
        else if (interval.getEndTime().equals(lessonEnd))
            interval.setEndTime(lessonStart);
        else if (interval.getStartTime().isBefore(lessonStart) && interval.getEndTime().isAfter(lessonEnd)) {
            Availability newInterval = new Availability();
            newInterval.setDayOfWeek(interval.getDayOfWeek());
            newInterval.setStartTime(lessonEnd);
            newInterval.setEndTime(interval.getEndTime());
            newInterval.setTutor(interval.getTutor());
            interval.setEndTime(lessonStart);
            availabilities.add(newInterval);
        }
    }
}
