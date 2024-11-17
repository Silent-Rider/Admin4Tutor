package com.admin4tutor.bot.service;

import com.admin4tutor.bot.TelegramBot;
import com.admin4tutor.bot.model.Student;
import com.admin4tutor.bot.model.Tutor;
import com.admin4tutor.bot.model.User;

public class AnswerProcessor {
    
    private final TelegramBot bot;
    private final QuestionHandler questionHandler;

    public AnswerProcessor(TelegramBot bot){
        this.bot = bot;
        questionHandler = new QuestionHandler(bot);
    }

    void processRoleAnswer(long chatId, String answer, UserSession session){
        switch (answer) {
            case "Я студент" -> {
                session.setUser(new Student());
                session.setStage(Stage.ASKING_FOR_LANGUAGE);
                questionHandler.askForLanguage(chatId, session.getUser());
            }
            case "Я репетитор" -> {
                session.setUser(new Tutor());
                session.setStage(Stage.ASKING_FOR_LANGUAGE);
                questionHandler.askForLanguage(chatId, session.getUser());
            }
            default -> bot.sendMessage(chatId,
                        "Пожалуйста, выберите один из предложенных вариантов.", null);
        }
    }

    void processLanguageAnswer(long chatId, String answer, UserSession session){
        switch(answer) {
            case "ENGLISH" -> session.getUser().setLanguage(answer);
            case "GERMAN" -> session.getUser().setLanguage(answer);
            case "FRENCH" -> session.getUser().setLanguage(answer);
            case "SPANISH" -> session.getUser().setLanguage(answer);
            case "ITALIAN" -> session.getUser().setLanguage(answer);
            case "CHINESE" -> session.getUser().setLanguage(answer);
            case "JAPANESE" -> session.getUser().setLanguage(answer);
            case "KOREAN" -> session.getUser().setLanguage(answer);
            default -> {
                bot.sendMessage(chatId,"Пожалуйста, выберите один из предложенных вариантов.",
                null);
                return;
            }
        }
        User user = session.getUser();
        if(user instanceof Tutor){
            session.setStage(Stage.ASKING_FOR_NAME);
            questionHandler.askForName(chatId, user);
        } 
        else {
            session.setStage(Stage.ASKING_FOR_SCHEDULE);
            questionHandler.askForSchedule(chatId, user);
        }
    }
    void processNameAnswer(long chatId, String answer, UserSession session){
        
    }
    void processScheduleAnswer(long chatId, String answer, UserSession session){
        
    }
}
