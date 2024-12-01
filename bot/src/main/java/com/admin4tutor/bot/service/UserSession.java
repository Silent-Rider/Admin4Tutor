package com.admin4tutor.bot.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;

import com.admin4tutor.bot.client.WebClientService;
import com.admin4tutor.bot.dto.DayOfWeek;
import com.admin4tutor.bot.dto.Tutor;
import com.admin4tutor.bot.dto.User;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

public class UserSession {
    
    private final WebClientService webClientService;
    
    @Getter(AccessLevel.PACKAGE)
    private final Long telegramId;
    
    @Getter(AccessLevel.PACKAGE) @Setter(AccessLevel.PACKAGE)
    private Stage stage;
    
    @Getter(AccessLevel.PACKAGE) @Setter(AccessLevel.PACKAGE)
    private User user;

    private List<Tutor> suitableTutors;

    @Getter(AccessLevel.PACKAGE) @Setter(AccessLevel.PACKAGE)
    private List<DayOfWeek> currentDays = new ArrayList<>(Arrays.asList(DayOfWeek.values()));

    @Getter @Setter
    private ReplyKeyboard currentKeyboard;
    
    @Getter(AccessLevel.PACKAGE) @Setter(AccessLevel.PACKAGE)
    private DayOfWeek currentDayOfWeek;

    @Getter(AccessLevel.PACKAGE) @Setter(AccessLevel.PACKAGE)
    private Tutor currentTutor;

    UserSession(Stage stage, Long telegramId, WebClientService webClientService){
        this.stage = stage;
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
}
