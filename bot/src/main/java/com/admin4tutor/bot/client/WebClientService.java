package com.admin4tutor.bot.client;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import com.admin4tutor.bot.dto.Availability;
import com.admin4tutor.bot.dto.Lesson;
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
                List<Availability> availabilities = DTOUtils.getAvailabilities(tutor);
                sendTutor(tutor);
                sendAvailabilities(availabilities, tutor.getTelegramId());
            }
            case Student student -> {
                sendStudent(student);
            }
            default -> log.error("Lost type of user");
        }
    }

    public List<Tutor> getSuitableTutors(Student student){
        List<Lesson> lessons = DTOUtils.getLessons(student);
        List<Tutor> tutors = null;
        try{
            tutors = webClient.post()
            .uri( uriBuilder -> {
                return uriBuilder
                .path(ServerPaths.TUTORS_URI)
                .path(ServerPaths.GET)
                .queryParam("language", student.getLanguage())
                .build(); })
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(lessons)
            .accept(MediaType.APPLICATION_JSON)
            .retrieve()
            .bodyToFlux(Tutor.class)
            .collectList()
            .block();
            log.info("List of tutors has been successfully received from server");
        } catch(WebClientRequestException e){
            log.error("Error while receiving list of tutors: " + e.getMessage());
        }
        return tutors;
    }

    private void sendTutor(Tutor tutor){ 
        try {
            webClient.post()
            .uri(ServerPaths.TUTORS_URI + ServerPaths.POST)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(tutor)
            .retrieve()
            .toBodilessEntity()
            .block();
            log.info("New tutor successfully send to webserver");
        } catch (WebClientResponseException e) {
            log.error("Error while sending tutor data: " + e.getMessage());
        }
    }

    private void sendAvailabilities(List<Availability> availabilities, Long telegramId){
        try {
            webClient.post()
            .uri(uriBuilder -> {
                return uriBuilder
                .path(ServerPaths.AVAILABILITIES_URI)
                .queryParam("telegramId", telegramId)
                .build(); })
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(availabilities)
            .retrieve()
            .toBodilessEntity()
            .block();
            log.info("Tutor's availabilities successfully send to webserver");
        } catch (WebClientResponseException e) {
            log.error("Error while sending tutor's availabilities: " + e.getMessage());
        }
    }

    private void sendStudent(Student student){
        System.out.println(student);
    }


}
