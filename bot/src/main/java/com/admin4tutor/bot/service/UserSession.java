package com.admin4tutor.bot.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;

import com.admin4tutor.bot.model.DayOfWeek;
import com.admin4tutor.bot.model.Language;
import com.admin4tutor.bot.model.Tutor;
import com.admin4tutor.bot.model.User;

public class UserSession {
    
    private Stage stage;
    private User user;
    private List<DayOfWeek> currentDays = new ArrayList<>();
    private ReplyKeyboard currentKeyboard;
    private List<Tutor> suitableTutors;
    private DayOfWeek currentDayOfWeek;

    UserSession(Stage stage, User user){
        this.stage = stage;
        this.user = user;
        for(var day: DayOfWeek.values())
            currentDays.add(day);
    }

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<DayOfWeek> getCurrentDays() {
        return currentDays;
    }

    public void setCurrentDays(List<DayOfWeek> currentDays) {
        this.currentDays = currentDays;
    }

    public ReplyKeyboard getCurrentKeyboard() {
        return currentKeyboard;
    }

    public void setCurrentKeyboard(ReplyKeyboard currentKeyboard) {
        this.currentKeyboard = currentKeyboard;
    }

    public List<Tutor> getSuitableTutors() {
        if(suitableTutors == null) suitableTutors = getSuitableTutorsFromDatabase();
        return suitableTutors;
    }

    public void setSuitableTutors(List<Tutor> suitableTutors) {
        this.suitableTutors = suitableTutors;
    }
    //PLUG!!!
    private List<Tutor> getSuitableTutorsFromDatabase(){
        Tutor tutor = new Tutor();
        tutor.setName("Клименко Кирилл");
        tutor.setDateOfBirth("30.10.2001");
        tutor.setLanguage(Language.ENGLISH);
        tutor.setEmail("silent.30.rider.10@gmail.com");
        tutor.setPhoneNumber("+79529170764");
        tutor.setBiography("Cool guy");
        return Collections.singletonList(tutor);
    }

    public DayOfWeek getCurrentDayOfWeek() {
        return currentDayOfWeek;
    }

    public void setCurrentDayOfWeek(DayOfWeek currentDayOfWeek) {
        this.currentDayOfWeek = currentDayOfWeek;
    }
}
