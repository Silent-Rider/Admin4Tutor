package com.admin4tutor.bot.client;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import com.admin4tutor.bot.dto.Availability;
import com.admin4tutor.bot.dto.Language;
import com.admin4tutor.bot.dto.Student;
import com.admin4tutor.bot.dto.Tutor;
import com.admin4tutor.bot.dto.User;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class WebClientService {

    private final WebClient webClient;

    public WebClientService(WebClient webClient){
        this.webClient = webClient;
    }

    public void sendUser(User user){
        switch(user){
            case Tutor tutor -> {
                sendTutor(tutor);
                sendAvailabilities(tutor);
            }
            case Student student -> sendStudent(student);
            default -> log.error("Lost type of user");
        }
    }

    private void sendTutor(Tutor tutor){ 
        try {
            webClient.post().
            uri(ServerPaths.TUTORS_URI).
            contentType(MediaType.APPLICATION_JSON).
            bodyValue(tutor).
            retrieve().
            toBodilessEntity().
            block();
            log.info("New tutor successfully send to webserver");
        } catch (WebClientResponseException e) {
            log.error("Error while sending tutor data: " + e.getMessage());
        }
    }

    private void sendAvailabilities(Tutor tutor){
        List<Availability> availabilities = new ArrayList<>();
        for(var dayOfWeek: tutor.getAvailability().keySet()){
            List<String> intervals = tutor.getAvailability().get(dayOfWeek);
            if(intervals.isEmpty()) continue;
            for(var interval: intervals){
                String[] times = interval.split("-");
                Availability availability = Availability.builder().startTime(times[0]).
                endTime(times[1]).dayOfWeek(dayOfWeek).build();
                availabilities.add(availability);
            }
        }
        try {
            webClient.post().
            uri(uriBuilder -> {
                return uriBuilder
                .path(ServerPaths.AVAILABILITIES_URI)
                .queryParam("telegramId", tutor.getTelegramId())
                .build(); }).
            contentType(MediaType.APPLICATION_JSON).
            bodyValue(availabilities).
            retrieve().
            toBodilessEntity().
            block();
            log.info("Tutor's availabilities successfully send to webserver");
        } catch (WebClientResponseException e) {
            log.error("Error while sending tutor's availabilities: " + e.getMessage());
        }
    }

    private void sendStudent(Student student){
        System.out.println(student);
    }

    //PLUG!!!
    public List<Tutor> getSuitableTutorsFromDatabase(Long chatId, Long telegramId){
        Tutor tutor = new Tutor(555, telegramId);
        tutor.setName("Клименко Кирилл");
        tutor.setDateOfBirth("30.10.2001");
        tutor.setLanguage(Language.ENGLISH);
        tutor.setEmail("silent.30.rider.10@gmail.com");
        tutor.setPhoneNumber("+79529170764");
        tutor.setPrice(1000);
        tutor.setBiography("Cool guy");
        return Collections.singletonList(tutor);
    }
}
