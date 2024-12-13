package com.admin4tutor.bot.client;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import com.admin4tutor.bot.dto.Availability;
import com.admin4tutor.bot.dto.Schedule;
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

    public void deleteUser(User user){
        String uri;
        String successMessage = String.format("%s was successfully deleted from database",
        user.getClass().getSimpleName());
        String failureMessage = String.format("Error while attempting to make a %s delete request: ",
        user.getClass().getSimpleName().toLowerCase());
        if(user instanceof Tutor) uri = ServerEndpoints.TUTORS_URI;
        else uri = ServerEndpoints.STUDENTS_URI;
        try {
            webClient.delete()
            .uri(uriBuilder -> {
                return uriBuilder
                .path(uri + ServerEndpoints.DELETE + "/{telegramId}")
                .build(String.valueOf(user.getTelegramId()));
            })
            .retrieve()
            .toBodilessEntity()
            .block();
            log.info(successMessage);
        } catch (WebClientResponseException e) {
            log.error(failureMessage + e.getMessage());
        }
    }

    public List<Tutor> getSuitableTutors(Student student){
        List<Schedule> schedules = DTOUtils.getSchedules(student);
        List<Tutor> tutors = null;
        try{
            tutors = webClient.post()
            .uri( uriBuilder -> {
                return uriBuilder
                .path(ServerEndpoints.TUTORS_URI)
                .path(ServerEndpoints.SEARCH)
                .queryParam("language", student.getLanguage().toString())
                .queryParam("telegramId", student.getTelegramId())
                .build(); })
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(schedules)
            .accept(MediaType.APPLICATION_JSON)
            .retrieve()
            .bodyToFlux(Tutor.class)
            .collectList()
            .block();
            log.info("List of tutors has been successfully received from server");
        } catch(WebClientResponseException e){
            log.error("Error while receiving list of tutors: " + e.getMessage());
        }
        return tutors;
    }

    private void sendTutor(Tutor tutor){ 
        try {
            webClient.post()
            .uri(ServerEndpoints.TUTORS_URI)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(tutor)
            .retrieve()
            .toBodilessEntity()
            .block();
            log.info("New tutor successfully sent to webserver");
        } catch (WebClientResponseException e) {
            log.error("Error while sending tutor data: " + e.getMessage());
        }
    }

    private void sendAvailabilities(List<Availability> availabilities, Long telegramId){
        try {
            webClient.post()
            .uri(uriBuilder -> {
                return uriBuilder
                .path(ServerEndpoints.TUTORS_URI)
                .path(ServerEndpoints.AVAILABILITIES)
                .queryParam("telegramId", telegramId)
                .build(); })
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(availabilities)
            .retrieve()
            .toBodilessEntity()
            .block();
            log.info("Tutor's availabilities successfully sent to webserver");
        } catch (WebClientResponseException e) {
            log.error("Error while sending tutor's availabilities: " + e.getMessage());
        }
    }

    private void sendStudent(Student student){
        try{
            webClient.post()
            .uri(uriBuilder -> {
                return uriBuilder
                .path(ServerEndpoints.STUDENTS_URI)
                .path(ServerEndpoints.ENROLL)
                .queryParam("tutorTelegramId", student.getTutorId())
                .build();
            })
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(student)
            .retrieve()
            .toBodilessEntity()
            .block();
            log.info("New student successfully sent to webserver");
        } catch(WebClientResponseException e) {
            log.error("Error while sending student data: " + e.getMessage());
        }
    }
}