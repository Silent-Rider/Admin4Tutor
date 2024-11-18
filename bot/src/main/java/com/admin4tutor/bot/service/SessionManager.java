package com.admin4tutor.bot.service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SessionManager {

    private final Map <Long, UserSession> userSessions = new ConcurrentHashMap<>();
    private final AnswerProcessor answerProcessor;

    public SessionManager (AnswerProcessor answerProcessor){
        this.answerProcessor = answerProcessor;
    }

    public void startSession(long chatId){
        userSessions.put(chatId, new UserSession(Stage.ASKING_FOR_ROLE, null));
    }

    public void handleUserAnswer(long chatId, String answer){
        UserSession session = userSessions.get(chatId);
        switch(session.getStage()){
            case Stage.ASKING_FOR_ROLE -> answerProcessor.processRoleAnswer(chatId, answer, session);
            case Stage.ASKING_FOR_LANGUAGE -> answerProcessor.processLanguageAnswer(chatId, answer, session);
            case Stage.ASKING_FOR_NAME -> answerProcessor.processNameAnswer(chatId, answer, session);
            case Stage.ASKING_FOR_DATE_OF_BIRTH -> answerProcessor.processDateOfBirthAnswer(chatId, answer, session);
            case Stage.ASKING_FOR_AVAILABILITY_DAY -> answerProcessor.processAvailabilityDayAnswer(chatId, answer, session);
            case Stage.ASKING_FOR_AVAILABILITY_INTERVALS -> answerProcessor.processAvailabilityIntervalsAnswer(chatId, answer, session);
            case Stage.ASKING_FOR_SCHEDULE -> answerProcessor.processScheduleAnswer(chatId, answer, session);
            case Stage.ASKING_FOR_TUTOR -> System.out.println();
            case Stage.ASKING_FOR_EMAIL -> System.out.println();
            case Stage.ASKING_FOR_PHONE_NUMBER -> System.out.println();
            case Stage.ASKING_FOR_BIOGRAPHY -> System.out.println();
        }
    }

    public UserSession getUserSession(long chatId){
        return userSessions.get(chatId);
    }
}
