package com.admin4tutor.bot.service;

import com.admin4tutor.bot.model.User;

public class UserSession {
    
    private Stage stage;
    private User user;

    UserSession(Stage stage, User user){
        this.stage = stage;
        this.user = user;
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
}
