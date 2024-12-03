package com.admin4tutor.bot.service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.admin4tutor.bot.dto.Tutor;

@Service
public class SessionManager {

    private final Map <Long, UserSession> userSessions = new ConcurrentHashMap<>();
    private final AnswerProcessor answerProcessor;
    private final UserSessionFactory userSessionFactory;

    public SessionManager (@Lazy AnswerProcessor answerProcessor, UserSessionFactory userSessionFactory){
        this.answerProcessor = answerProcessor;
        this.userSessionFactory = userSessionFactory;
    }

    public void startSession(long chatId, long telegramId){
        UserSession session = userSessionFactory.createUserSession(Stage.ASKING_FOR_ROLE, telegramId);
        userSessions.put(chatId, session);
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
            case Stage.ASKING_FOR_ANOTHER_AVAILABILITY_DAY -> answerProcessor.processAnotherAvailabilityDayAnswer(chatId, answer, session);
            case Stage.ASKING_FOR_PRICE -> answerProcessor.processPriceAnswer(chatId, answer, session);
            case Stage.ASKING_FOR_SCHEDULE_DAY -> answerProcessor.processScheduleDayAnswer(chatId, answer, session);
            case Stage.ASKING_FOR_SCHEDULE_TIME -> answerProcessor.processScheduleTimeAnswer(chatId, answer, session);
            case Stage.ASKING_FOR_ANOTHER_SCHEDULE_DAY -> answerProcessor.processAnotherScheduleDayAnswer(chatId, answer, session);
            case Stage.ASKING_FOR_EMAIL -> answerProcessor.processEmailAnswer(chatId, answer, session);
            case Stage.ASKING_FOR_PHONE_NUMBER -> answerProcessor.processPhoneNumberAnswer(chatId, answer, session);
            case Stage.ASKING_FOR_BIOGRAPHY -> answerProcessor.processBiographyAnswer(chatId, answer, session);
            case Stage.CHECKING_QUESTIONNAIRE_RESULTS -> answerProcessor.processChekingQuestionnaireAnswer(chatId, answer, session);
            case Stage.ASKING_FOR_TUTOR -> answerProcessor.processTutorAnswer(chatId, answer, session);
            case Stage.VIEWING_TUTOR_PAGE -> answerProcessor.processTutorView(chatId, answer, session);
            case Stage.WAITING_FOR_TUTOR_CONFIRMATION -> answerProcessor.processEager(chatId);
            case Stage.CONFIRMING_REGISTRATION -> answerProcessor.processConfirmation(chatId, answer, session);
            case Stage.NOTIFYING_REGISTRATION_RESULTS -> answerProcessor.processRegistrationResults(chatId, answer, session);
            case Stage.MAIN_MENU -> System.out.println();   
        }
    }

    public void startSessionForTutorConfirmation(Tutor tutor){
        Long telegramId = tutor.getTelegramId();
        UserSession session = userSessionFactory.createUserSession(Stage.CONFIRMING_REGISTRATION, telegramId);
        session.setUser(tutor);
        userSessions.put(telegramId, session);
    }

    public UserSession getUserSession(long chatId){
        return userSessions.get(chatId);
    }

    public boolean containsUserSession(long chatId){
        return userSessions.containsKey(chatId);
    }
}
