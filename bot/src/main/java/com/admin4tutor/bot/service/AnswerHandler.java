package com.admin4tutor.bot.service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class AnswerHandler {

    private final Map <Long, UserSession> userSessions = new ConcurrentHashMap<>();
    private final StageHandler stageHandler;

    public AnswerHandler (StageHandler stageHandler){
        this.stageHandler = stageHandler;
    }

    public void startSession(long chatId){
        userSessions.put(chatId, new UserSession(Stage.ASKING_FOR_ROLE, null));
    }

    public void handleUserAnswer(long chatId, String answer){
        UserSession session = userSessions.get(chatId);
        switch(session.getStage()){
            case Stage.ASKING_FOR_ROLE -> System.out.println();
            case Stage.CHOOSING_LANGUAGE -> System.out.println();
            case Stage.PLANNING_SCHEDULE -> System.out.println();
            case Stage.ASKING_FOR_NAME -> System.out.println();
            case Stage.ASKING_FOR_EMAIL -> System.out.println();
            case Stage.ASKING_FOR_PHONE_NUMBER -> System.out.println();
            case Stage.ASKING_FOR_AVAILABILITY -> System.out.println();
            case Stage.ASKING_FOR_BIOGRAPHY -> System.out.println();
        }
    }

    public UserSession getUserSession(long chatId){
        return userSessions.get(chatId);
    }
}
