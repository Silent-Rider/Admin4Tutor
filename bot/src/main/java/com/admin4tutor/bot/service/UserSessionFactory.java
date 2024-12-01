package com.admin4tutor.bot.service;

import org.springframework.stereotype.Service;

import com.admin4tutor.bot.client.WebClientService;

@Service
public class UserSessionFactory {

    private final WebClientService webClientService;
    
    public UserSessionFactory(WebClientService webClientService){
        this.webClientService = webClientService;
    }

    UserSession createUserSession(Stage stage, Long telegramId){
        return new UserSession(stage, telegramId, webClientService);
    }
}
