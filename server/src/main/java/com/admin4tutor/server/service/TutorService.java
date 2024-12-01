package com.admin4tutor.server.service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.admin4tutor.server.model.entities.Availability;
import com.admin4tutor.server.model.entities.Tutor;
import com.admin4tutor.server.service.repositories.AvailabilityRepository;
import com.admin4tutor.server.service.repositories.TutorRepository;

@Service
public class TutorService {

    private final Map<Long, Tutor> pendingTutors = new ConcurrentHashMap<>();

    @Autowired
    private TutorRepository tutorRepository;
    @Autowired
    private AvailabilityRepository availabilityRepository;

    public void addTutor(Long telegramId, Tutor tutor){
        pendingTutors.put(telegramId, tutor);
    }

    @Transactional
    public Tutor saveAvailabilities(Long telegramId, List<Availability> availabilities){
        Tutor tutor = pendingTutors.get(telegramId);
        tutorRepository.save(tutor);
        availabilities.forEach(x -> x.setTutor(tutor));
        availabilityRepository.saveAll(availabilities);
        return pendingTutors.remove(telegramId);
    }

}
