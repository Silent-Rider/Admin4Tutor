package com.admin4tutor.bot.service;

import com.admin4tutor.bot.TelegramBot;
import com.admin4tutor.bot.model.DayOfWeek;
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
                        "Пожалуйста, выберите один из предложенных вариантов", session.getCurrentKeyboard());
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
                session.getCurrentKeyboard());
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
            "Пожалуйста, введите фамилию и имя через пробел", session.getCurrentKeyboard());
            return;
        }
        session.setStage(Stage.ASKING_FOR_DATE_OF_BIRTH);
        questionHandler.askForDateOfBirth(chatId);
    }
    
    void processDateOfBirthAnswer(long chatId, String answer, UserSession session){
        if(answer.matches("\\d{2}\\.\\d{2}\\.\\d{4}")) session.getUser().setDateOfBirth(answer);
        else {
            bot.sendMessage(chatId, "Неверный формат даты. " + 
            "Пожалуйста, напишите вашу дату рождения в формате \"ДД.ММ.ГГГГ\"", session.getCurrentKeyboard());
            return;
        }
        User user = session.getUser();
        if(user instanceof Tutor){
            session.setStage(Stage.ASKING_FOR_AVAILABILITY_DAY);
            questionHandler.askForAvailabilityDay(chatId);
        } else {
            session.setStage(Stage.ASKING_FOR_SCHEDULE_DAY);
            questionHandler.askForScheduleDay(chatId, session.getUser());
        }
    }
    
    void processAvailabilityDayAnswer(long chatId, String answer, UserSession session){
        switch(answer) {
            case "MONDAY" -> session.setCurrentDayOfWeek(DayOfWeek.MONDAY);
            case "TUESDAY" -> session.setCurrentDayOfWeek(DayOfWeek.TUESDAY);
            case "WEDNESDAY" -> session.setCurrentDayOfWeek(DayOfWeek.WEDNESDAY);
            case "THURSDAY" -> session.setCurrentDayOfWeek(DayOfWeek.THURSDAY);
            case "FRIDAY" -> session.setCurrentDayOfWeek(DayOfWeek.FRIDAY);
            case "SATURDAY" -> session.setCurrentDayOfWeek(DayOfWeek.SATURDAY);
            case "SUNDAY" -> session.setCurrentDayOfWeek(DayOfWeek.SUNDAY);
            default -> {
                bot.sendMessage(chatId,"Пожалуйста, выберите один из дней недели",
                session.getCurrentKeyboard());
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
        tutor.getAvailability().put(session.getCurrentDayOfWeek(), answer);
        else {
            String text = String.format("Неверный формат интервалов" + 
            "%s: Укажите один или более интервалов доступности " + 
            "в формате \"ЧЧ:ММ-ЧЧ:ММ\", перечисляя их через запятую. 🕰%n", 
            session.getCurrentDayOfWeek().getValue()) + "Пример: 10:45-14:00, 20:00-22:30";
            bot.sendMessage(chatId, text, session.getCurrentKeyboard());
            return;
        }
        session.setStage(Stage.ASKING_FOR_ANOTHER_AVAILABILITY_DAY);
        questionHandler.askForAnotherAvailabilityDay(chatId, session.getCurrentDayOfWeek(), session);
    }

    void processAnotherAvailabilityDayAnswer(long chatId, String answer, UserSession session){
        switch(answer) {
            case "MONDAY" -> session.setCurrentDayOfWeek(DayOfWeek.MONDAY);
            case "TUESDAY" -> session.setCurrentDayOfWeek(DayOfWeek.TUESDAY);
            case "WEDNESDAY" -> session.setCurrentDayOfWeek(DayOfWeek.WEDNESDAY);
            case "THURSDAY" -> session.setCurrentDayOfWeek(DayOfWeek.THURSDAY);
            case "FRIDAY" -> session.setCurrentDayOfWeek(DayOfWeek.FRIDAY);
            case "SATURDAY" -> session.setCurrentDayOfWeek(DayOfWeek.SATURDAY);
            case "SUNDAY" -> session.setCurrentDayOfWeek(DayOfWeek.SUNDAY);
            case "READY" -> {
                session.setStage(Stage.ASKING_FOR_EMAIL);
                questionHandler.askForEmail(chatId);
                return;
            }
            default -> {
                bot.sendMessage(chatId,"Пожалуйста, " + 
                "выберите один из дней недели, либо нажмите \"Готово\"",
                session.getCurrentKeyboard());
                return;
            }
        }
        session.setStage(Stage.ASKING_FOR_AVAILABILITY_INTERVALS);
        questionHandler.askForAvailabilityIntervals(chatId, answer);
    }

    void processScheduleDayAnswer(long chatId, String answer, UserSession session){
        switch(answer) {
            case "MONDAY" -> session.setCurrentDayOfWeek(DayOfWeek.MONDAY);
            case "TUESDAY" -> session.setCurrentDayOfWeek(DayOfWeek.TUESDAY);
            case "WEDNESDAY" -> session.setCurrentDayOfWeek(DayOfWeek.WEDNESDAY);
            case "THURSDAY" -> session.setCurrentDayOfWeek(DayOfWeek.THURSDAY);
            case "FRIDAY" -> session.setCurrentDayOfWeek(DayOfWeek.FRIDAY);
            case "SATURDAY" -> session.setCurrentDayOfWeek(DayOfWeek.SATURDAY);
            case "SUNDAY" -> session.setCurrentDayOfWeek(DayOfWeek.SUNDAY);
            default -> {
                bot.sendMessage(chatId,"Пожалуйста, выберите один из дней недели",
                session.getCurrentKeyboard());
                return;
            }
        }
        session.setStage(Stage.ASKING_FOR_SCHEDULE_TIME);
        questionHandler.askForScheduleTime(chatId, answer);
    }

    void processScheduleTimeAnswer(long chatId, String answer, UserSession session){
        Student student = (Student) session.getUser();
        if(answer.matches("^([0-1][0-9]|2[0-3]):[0-5][0-9]$"))
        student.getSchedule().put(session.getCurrentDayOfWeek(), answer);
        else {
            String text = "Неверный формат времени" + String.format("%s: Укажите время начала занятия " + 
            "в формате \"ЧЧ:ММ\". 🕰", session.getCurrentDayOfWeek().getValue()) + "\nПример: 17:00";
            bot.sendMessage(chatId, text, session.getCurrentKeyboard());
            return;
        }
        session.setStage(Stage.ASKING_FOR_ANOTHER_SCHEDULE_DAY);
        questionHandler.askForAnotherScheduleDay(chatId, session.getCurrentDayOfWeek(), session);
    }

    void processAnotherScheduleDayAnswer(long chatId, String answer, UserSession session){
        switch(answer) {
            case "MONDAY" -> session.setCurrentDayOfWeek(DayOfWeek.MONDAY);
            case "TUESDAY" -> session.setCurrentDayOfWeek(DayOfWeek.TUESDAY);
            case "WEDNESDAY" -> session.setCurrentDayOfWeek(DayOfWeek.WEDNESDAY);
            case "THURSDAY" -> session.setCurrentDayOfWeek(DayOfWeek.THURSDAY);
            case "FRIDAY" -> session.setCurrentDayOfWeek(DayOfWeek.FRIDAY);
            case "SATURDAY" -> session.setCurrentDayOfWeek(DayOfWeek.SATURDAY);
            case "SUNDAY" -> session.setCurrentDayOfWeek(DayOfWeek.SUNDAY);
            case "READY" -> {
                session.setStage(Stage.ASKING_FOR_TUTOR);
                questionHandler.askForTutor(chatId, session);
                return;
            }
            default -> {
                bot.sendMessage(chatId,"Пожалуйста, " + 
                "выберите один из дней недели, либо нажмите \"Готово\"",
                session.getCurrentKeyboard());
                return;
            }
        }
        session.setStage(Stage.ASKING_FOR_SCHEDULE_TIME);
        questionHandler.askForScheduleTime(chatId, answer);
    }

    void processTutorAnswer(long chatId, String answer, UserSession session){
        Tutor tutor = null;
        for(var suitableTutor: session.getSuitableTutors())
            if(suitableTutor.getName().equals(answer)){
                tutor = suitableTutor;
                break;
            }
        if(tutor == null){
            bot.sendMessage(chatId,"Пожалуйста, выберите одного из предложенных репетиторов",
            session.getCurrentKeyboard());
            return;
        }
        session.setStage(Stage.VIEWING_TUTOR_PAGE);
        questionHandler.viewTutorPage(chatId, tutor);
    }

    void processTutorView(long chatId, String answer, UserSession session){
        switch (answer) {
            case "Записаться" -> {
                session.setStage(Stage.ASKING_FOR_EMAIL);
                questionHandler.askForEmail(chatId);
            }
            case "Вернуться к списку" -> {
                session.setStage(Stage.ASKING_FOR_TUTOR);
                questionHandler.askForTutor(chatId, session);
            }
            default -> bot.sendMessage(chatId,
                        "Пожалуйста, выберите один из предложенных вариантов", session.getCurrentKeyboard());
        }
    }

    void processEmailAnswer(long chatId, String answer, UserSession session){
        if(!answer.equals("Пропустить")) session.getUser().setEmail(answer);
        session.setStage(Stage.ASKING_FOR_PHONE_NUMBER);
        questionHandler.askForPhoneNumber(chatId);
    }

    void processPhoneNumberAnswer(long chatId, String answer, UserSession session){
        if(!answer.equals("Пропустить")) session.getUser().setEmail(answer);
        if(session.getUser() instanceof Tutor){
            session.setStage(Stage.ASKING_FOR_BIOGRAPHY);
            questionHandler.askForBiography(chatId);
        } else{
            session.setStage(Stage.CREATED_ACCOUNT);
            printInformation(chatId, session.getUser());
        }
    }

    void processBiographyAnswer(long chatId, String answer, UserSession session){
        Tutor tutor = (Tutor) session.getUser();
        if(answer.length() > 15) tutor.setBiography(answer);
        else {
            bot.sendMessage(chatId,"Биография слишком короткая. Напишите не менее 15 символов", 
            session.getCurrentKeyboard());
            return;
        }
        session.setStage(Stage.CREATED_ACCOUNT);
        printInformation(chatId, session.getUser());

    }
    //Just for testing. Remove after!
    void printInformation(long chatId, User user){
        bot.sendMessage(chatId, user.toString(), null);
    }
}
