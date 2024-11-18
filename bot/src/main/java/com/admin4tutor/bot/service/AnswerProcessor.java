package com.admin4tutor.bot.service;

import com.admin4tutor.bot.TelegramBot;
import com.admin4tutor.bot.model.DayOfWeek;
import com.admin4tutor.bot.model.Student;
import com.admin4tutor.bot.model.Tutor;
import com.admin4tutor.bot.model.User;

public class AnswerProcessor {
    
    private final TelegramBot bot;
    private final QuestionHandler questionHandler;
    private DayOfWeek currentDayOfWeek;

    public AnswerProcessor(TelegramBot bot){
        this.bot = bot;
        questionHandler = new QuestionHandler(bot);
    }

    void processRoleAnswer(long chatId, String answer, UserSession session){
        switch (answer) {
            case "🧑🏼‍🎓 Я студент" -> {
                session.setUser(new Student());
                session.setStage(Stage.ASKING_FOR_LANGUAGE);
                questionHandler.askForLanguage(chatId, session.getUser());
            }
            case "🧑🏻‍🏫 Я репетитор" -> {
                session.setUser(new Tutor());
                session.setStage(Stage.ASKING_FOR_LANGUAGE);
                questionHandler.askForLanguage(chatId, session.getUser());
            }
            default -> bot.sendMessage(chatId,
                        "Пожалуйста, выберите один из предложенных вариантов", bot.currentKeyboard);
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
                bot.sendMessage(chatId,"Пожалуйста, выберите один из предложенных вариантов",
                bot.currentKeyboard);
                return;
            }
        }
        session.setStage(Stage.ASKING_FOR_NAME);
        questionHandler.askForName(chatId);
    }
    
    void processNameAnswer(long chatId, String answer, UserSession session){
        if(answer.matches("[А-Яа-я]+? [А-Яа-я]+?")) session.getUser().setName(answer);
        else {
            bot.sendMessage(chatId, "Неверный формат имени. " +
            "Пожалуйста, введите фамилию и имя через пробел", bot.currentKeyboard);
            return;
        }
        session.setStage(Stage.ASKING_FOR_DATE_OF_BIRTH);
        questionHandler.askForDateOfBirth(chatId);
    }
    
    void processDateOfBirthAnswer(long chatId, String answer, UserSession session){
        if(answer.matches("\\d{2}\\.\\d{2}\\.\\d{4}")) session.getUser().setDateOfBirth(answer);
        else {
            bot.sendMessage(chatId, "Неверный формат даты. " + 
            "Пожалуйста, напишите вашу дату рождения в формате \"ДД.ММ.ГГГГ\"", bot.currentKeyboard);
            return;
        }
        User user = session.getUser();
        if(user instanceof Tutor){
            session.setStage(Stage.ASKING_FOR_AVAILABILITY_DAY);
            questionHandler.askForAvailabilityDay(chatId);
        } else {
            session.setStage(Stage.ASKING_FOR_SCHEDULE);
            questionHandler.askForSchedule(chatId, session.getUser());
        }
    }
    
    void processAvailabilityDayAnswer(long chatId, String answer, UserSession session){
        switch(answer) {
            case "MONDAY" -> currentDayOfWeek = DayOfWeek.MONDAY;
            case "TUESDAY" -> currentDayOfWeek = DayOfWeek.TUESDAY;
            case "WEDNESDAY" -> currentDayOfWeek = DayOfWeek.WEDNESDAY;
            case "THURSDAY" -> currentDayOfWeek = DayOfWeek.THURSDAY;
            case "FRIDAY" -> currentDayOfWeek = DayOfWeek.FRIDAY;
            case "SATURDAY" -> currentDayOfWeek = DayOfWeek.SATURDAY;
            case "SUNDAY" -> currentDayOfWeek = DayOfWeek.SUNDAY;
            default -> {
                bot.sendMessage(chatId,"Пожалуйста, выберите один из дней недели",
                bot.currentKeyboard);
                return;
            }
        }
        session.setStage(Stage.ASKING_FOR_AVAILABILITY_INTERVALS);
        questionHandler.askForAvailabilityIntervals(chatId, answer);
    }

    void processAvailabilityIntervalsAnswer(long chatId, String answer, UserSession session){
        Tutor tutor = (Tutor) session.getUser();
        if(answer.matches("^([0-1][0-9]|2[0-3]):[0-5][0-9]-([0-1][0-9]|2[0-3]):[0-5][0-9]" + 
        "(, ?([0-1][0-9]|2[0-3]):[0-5][0-9]-([0-1][0-9]|2[0-3]):[0-5][0-9])*$"))
        tutor.getAvailability().put(currentDayOfWeek, answer);
        else {
            String text = String.format("Неверный формат интервалов" + 
            "\\n%s: Укажите один или более интервалов доступности " + 
            "в формате \"ЧЧ:ММ-ЧЧ:ММ\", перечисляя их через запятую. ", 
            currentDayOfWeek.getValue()) + "Пример: 10:45-14:00, 20:00-22:30";
            bot.sendMessage(chatId, text, bot.currentKeyboard);
            return;
        }
        //session.setStage(Stage.ASKING_FOR_AVAILABILITY_DAY);
        //questionHandler.askForAvailabilityDay(chatId);
        String result = tutor.getName() + " " + tutor.getClass().getSimpleName() + " " +
        tutor.getDateOfBirth() + " " + tutor.getLanguage() + " ";
        for(DayOfWeek day: tutor.getAvailability().keySet())
            result += day.getValue() + ":" + tutor.getAvailability().get(day) + " ";
        bot.sendMessage(chatId, result, bot.currentKeyboard);
    }

    void processScheduleAnswer(long chatId, String answer, UserSession session){
        System.out.println();
    }
}
