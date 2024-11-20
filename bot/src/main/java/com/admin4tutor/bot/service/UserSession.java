package com.admin4tutor.bot.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;

import com.admin4tutor.bot.client.WebClientService;
import com.admin4tutor.bot.dto.DayOfWeek;
import com.admin4tutor.bot.dto.Tutor;
import com.admin4tutor.bot.dto.User;

public class UserSession {
    
    private final WebClientService webClientService;
    private final Long telegramId;
    private Stage stage;
    private User user;
    private List<Tutor> suitableTutors;
    private List<DayOfWeek> currentDays = new ArrayList<>(Arrays.asList(DayOfWeek.values()));
    private ReplyKeyboard currentKeyboard;
    private DayOfWeek currentDayOfWeek;
    private Tutor currentTutor;

    UserSession(Stage stage, User user, Long telegramId, WebClientService webClientService){
        this.stage = stage;
        this.user = user;
        this.telegramId = telegramId;
        this.webClientService = webClientService;
    }

    void sendUser(){
        webClientService.sendUser(user);
    }

    List<Tutor> getSuitableTutors() {
        if(suitableTutors == null) suitableTutors = webClientService.
        getSuitableTutorsFromDatabase(555L, telegramId);
        return suitableTutors;
    }

    Stage getStage() {
        return stage;
    }

    void setStage(Stage stage) {
        this.stage = stage;
    }

    User getUser() {
        return user;
    }

    void setUser(User user) {
        this.user = user;
    }

    List<DayOfWeek> getCurrentDays() {
        return currentDays;
    }

    void setCurrentDays(List<DayOfWeek> currentDays) {
        this.currentDays = currentDays;
    }

    ReplyKeyboard getCurrentKeyboard() {
        return currentKeyboard;
    }

    public void setCurrentKeyboard(ReplyKeyboard currentKeyboard) {
        this.currentKeyboard = currentKeyboard;
    }

    DayOfWeek getCurrentDayOfWeek() {
        return currentDayOfWeek;
    }

    void setCurrentDayOfWeek(DayOfWeek currentDayOfWeek) {
        this.currentDayOfWeek = currentDayOfWeek;
    }

    Tutor getCurrentTutor() {
        return currentTutor;
    }

    void setCurrentTutor(Tutor currentTutor) {
        this.currentTutor = currentTutor;
    }

    Long getTelegramId() {
        return telegramId;
    }
}
