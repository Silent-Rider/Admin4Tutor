package com.admin4tutor.bot.client;

import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.admin4tutor.bot.dto.Language;
import com.admin4tutor.bot.dto.Tutor;
import com.admin4tutor.bot.dto.User;

@Service
public class WebClientService {

    private final WebClient webClient;

    public WebClientService(WebClient webClient){
        this.webClient = webClient;
    }

    public void sendUser(User user){
        
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
