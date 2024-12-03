package com.admin4tutor.bot.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import com.admin4tutor.bot.TelegramBot;
import com.admin4tutor.bot.dto.DayOfWeek;
import com.admin4tutor.bot.dto.Language;
import com.admin4tutor.bot.dto.Student;
import com.admin4tutor.bot.dto.Tutor;
import com.admin4tutor.bot.dto.User;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class QuestionHandler {
    
    private final TelegramBot bot;
    private final SessionManager sessionManager;

    public QuestionHandler(TelegramBot bot, SessionManager sessionManager){
        this.bot = bot;
        this.sessionManager = sessionManager;
    }

    void askForLanguage(long chatId, User user){
        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        for(int i = 0; i < 4; i++) keyboard.add(new ArrayList<>());
        Language[] languages = Language.values();
        int j = 0;
        for(int i = 0; i < languages.length; i++){
            InlineKeyboardButton button = new InlineKeyboardButton(languages[i].getValue());
            button.setCallbackData(languages[i].toString());
            keyboard.get(j).add(button);
            if((i+1) % 2 == 0) j++;
        }
        keyboardMarkup.setKeyboard(keyboard);
        String text = user instanceof Tutor ? "Выберите язык для преподавания" : "Выберите язык для изучения";
        bot.sendMessage(chatId, text, keyboardMarkup);
    }

    void askForName(long chatId){
        bot.sendMessage(chatId, "Введите вашу фамилию и имя", null);
    }

    void askForDateOfBirth(long chatId){
        bot.sendMessage(chatId, "Напишите вашу дату рождения в формате \"ДД.ММ.ГГГГ\" 🗓", null);
    }

    void askForAvailabilityDay(long chatId){
        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        for(int i = 0; i < 4; i++) keyboard.add(new ArrayList<>());
        DayOfWeek[] daysOfWeek = DayOfWeek.values();
        int j = 0;
        for(int i = 0; i < daysOfWeek.length; i++){
            InlineKeyboardButton button = new InlineKeyboardButton(daysOfWeek[i].getValue());
            button.setCallbackData(daysOfWeek[i].toString());
            keyboard.get(j).add(button);
            if((i+1) % 2 == 0) j++;
        }
        keyboardMarkup.setKeyboard(keyboard);
        String text = "Выберите день недели для указания интервалов доступности";
        bot.sendMessage(chatId, text, keyboardMarkup);
    }

    void askForAvailabilityIntervals(long chatId, String answer){
        DayOfWeek dayOfWeek = null;
        for(DayOfWeek day: DayOfWeek.values())
            if(day.toString().equals(answer)) dayOfWeek = day;
        if(dayOfWeek == null){
            log.error("Lost day of week value");
            return;
        }
        String text = String.format("%s: Укажите один или более интервалов доступности " + 
            "в формате \"ЧЧ:ММ-ЧЧ:ММ\", перечисляя их через запятую 🕰%n", 
            dayOfWeek.getValue()) + "Пример: 10:45-14:00, 20:00-22:30";
        bot.sendMessage(chatId, text, null);
    }

    void askForAnotherAvailabilityDay(long chatId, DayOfWeek previoisDayOfWeek, UserSession session){
        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        for(int i = 0; i < 4; i++) keyboard.add(new ArrayList<>());
        List<DayOfWeek> daysOfWeek = session.getCurrentDays();
        daysOfWeek.remove(previoisDayOfWeek);
        int j = 0;
        for(int i = 0; i < daysOfWeek.size(); i++){
            InlineKeyboardButton button = new InlineKeyboardButton(daysOfWeek.get(i).getValue());
            button.setCallbackData(daysOfWeek.get(i).toString());
            keyboard.get(j).add(button);
            if((i+1) % 2 == 0) j++;
        }
        if(daysOfWeek.isEmpty())
            session.setCurrentDays(new ArrayList<>(Arrays.asList(DayOfWeek.values())));
        InlineKeyboardButton ready = new InlineKeyboardButton("Готово");
        ready.setCallbackData("READY");
        keyboard.add(Collections.singletonList(ready));
        keyboardMarkup.setKeyboard(keyboard);
        String text = "Выберите еще один день недели для указания интервалов доступности," +
        " либо нажмите \"Готово\"";
        bot.sendMessage(chatId, text, keyboardMarkup);
    }

    void askForPrice(long chatId){
        bot.sendMessage(chatId, "Укажите желаемую цену за занятие в рублях 💰", null);
    }

    void askForScheduleDay(long chatId){
        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        for(int i = 0; i < 4; i++) keyboard.add(new ArrayList<>());
        DayOfWeek[] daysOfWeek = DayOfWeek.values();
        int j = 0;
        for(int i = 0; i < daysOfWeek.length; i++){
            InlineKeyboardButton button = new InlineKeyboardButton(daysOfWeek[i].getValue());
            button.setCallbackData(daysOfWeek[i].toString());
            keyboard.get(j).add(button);
            if((i+1) % 2 == 0) j++;
        }
        keyboardMarkup.setKeyboard(keyboard);
        String text = "Составьте удобное для вас расписание. " + 
        "Выберите желаемый день недели для проведения занятия";
        bot.sendMessage(chatId, text, keyboardMarkup);
    }

    void askForScheduleTime(long chatId, String answer){
        DayOfWeek dayOfWeek = null;
        for(DayOfWeek day: DayOfWeek.values())
            if(day.toString().equals(answer)) dayOfWeek = day;
        if(dayOfWeek == null){
            log.error("Lost day of week value");
            return;
        }
        String text = String.format("%s: Укажите время начала занятия " + 
            "в формате \"ЧЧ:ММ\" 🕰", dayOfWeek.getValue()) + "\nПример: 17:00";
        bot.sendMessage(chatId, text, null);
    }

    void askForAnotherScheduleDay(long chatId, DayOfWeek previoisDayOfWeek, UserSession session){
        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        for(int i = 0; i < 4; i++) keyboard.add(new ArrayList<>());
        List<DayOfWeek> daysOfWeek = session.getCurrentDays();
        daysOfWeek.remove(previoisDayOfWeek);
        int j = 0;
        for(int i = 0; i < daysOfWeek.size(); i++){
            InlineKeyboardButton button = new InlineKeyboardButton(daysOfWeek.get(i).getValue());
            button.setCallbackData(daysOfWeek.get(i).toString());
            keyboard.get(j).add(button);
            if((i+1) % 2 == 0) j++;
        }
        if(daysOfWeek.isEmpty())
            session.setCurrentDays(new ArrayList<>(Arrays.asList(DayOfWeek.values())));
        InlineKeyboardButton ready = new InlineKeyboardButton("Готово");
        ready.setCallbackData("READY");
        keyboard.add(Collections.singletonList(ready));
        keyboardMarkup.setKeyboard(keyboard);
        String text = "Выберите еще один день недели для проведения занятий," +
        " либо нажмите \"Готово\"";
        bot.sendMessage(chatId, text, keyboardMarkup);
    }

    void askForEmail(long chatId){
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        keyboardMarkup.setResizeKeyboard(true);
        keyboardMarkup.setOneTimeKeyboard(true);
        KeyboardRow row = new KeyboardRow(Collections.singletonList(new KeyboardButton("Пропустить")));
        List <KeyboardRow> keyboard = Collections.singletonList(row);
        keyboardMarkup.setKeyboard(keyboard);
        bot.sendMessage(chatId, "Напишите ваш адрес электронной почты ✉️", keyboardMarkup);
    }

    void askForPhoneNumber(long chatId){
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        keyboardMarkup.setResizeKeyboard(true);
        keyboardMarkup.setOneTimeKeyboard(true);
        KeyboardRow row = new KeyboardRow(Collections.singletonList(new KeyboardButton("Пропустить")));
        List <KeyboardRow> keyboard = Collections.singletonList(row);
        keyboardMarkup.setKeyboard(keyboard);
        bot.sendMessage(chatId, "Напишите ваш номер телефона 📞", keyboardMarkup);
    }

    void askForBiography(long chatId){
        bot.sendMessage(chatId, "Напишите свою краткую биографию 📙", null);
    }

    void askForCheckingQuestionnaireResults(long chatId, User user){
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        keyboardMarkup.setResizeKeyboard(true);
        keyboardMarkup.setOneTimeKeyboard(true);
        KeyboardButton ready = new KeyboardButton("Готово");
        KeyboardButton refill = new KeyboardButton("Заполнить анкету заново");
        KeyboardRow row = new KeyboardRow(){{ add(ready); add(refill); }};
        List <KeyboardRow> keyboard = Collections.singletonList(row);
        keyboardMarkup.setKeyboard(keyboard);
        switch (user) {
            case Tutor tutor -> {
                var availability = tutor.getAvailability();
                String result = user.toString() + "\n🆓 Доступность 🆓";
                for(var day: availability.keySet())
                    result += "\n" + day.getValue() + ": " + (availability.get(day).isEmpty() ? "занят"
                     : availability.get(day).toString().replaceAll("[\\[\\]]", ""));
                bot.sendMessage(chatId, result, keyboardMarkup);
            }
            case Student student -> bot.sendMessage(chatId, student.toString(), keyboardMarkup);
            default -> {
                log.error("Lost type of user");
            }
        }
    }

    void notifyTutorAboutRegistration(long tutorId){
        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        InlineKeyboardButton mainMenu = new InlineKeyboardButton("Главное меню");
        mainMenu.setCallbackData("Главное меню");
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>(Collections.singletonList(
            Collections.singletonList(mainMenu)));
        keyboardMarkup.setKeyboard(keyboard);
        bot.sendMessage(tutorId, "Вы успешно зарегистрированы как репетитор", keyboardMarkup);
    }

    void askForTutor(long chatId, UserSession session){
        List<Tutor> tutors = session.getSuitableTutors();
        if(tutors.isEmpty()){
            ReplyKeyboardMarkup simpleKeyboardMarkup = new ReplyKeyboardMarkup();
            KeyboardRow row1 = new KeyboardRow(){{ add(new KeyboardButton("Изменить расписание")); }};
            KeyboardRow row2 = new KeyboardRow(){{ add(new KeyboardButton("Вернуться в начало")); }};
            simpleKeyboardMarkup.setKeyboard(Arrays.asList(row1, row2));
            bot.sendMessage(chatId,"К сожалению, " +
            "подходящих по вашему расписанию репетиторов нет", simpleKeyboardMarkup);
            return;
        }
        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        tutors.forEach(x -> {
            InlineKeyboardButton button = new InlineKeyboardButton(x.getName());
            button.setCallbackData(x.getName());
            keyboard.add(Collections.singletonList(button));
        });
        keyboardMarkup.setKeyboard(keyboard);
        String text = "Выберите одного репетитора из списка подходящих по вашему расписанию";
        bot.sendMessage(chatId, text, keyboardMarkup);
    }

    void viewTutorPage(long chatId, Tutor tutor){
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        keyboardMarkup.setResizeKeyboard(true);
        keyboardMarkup.setOneTimeKeyboard(true);
        KeyboardButton register = new KeyboardButton("Записаться");
        KeyboardButton backToList = new KeyboardButton("Вернуться к списку");
        KeyboardRow row = new KeyboardRow(){{add(register); add(backToList);}};
        List <KeyboardRow> keyboard = Collections.singletonList(row);
        keyboardMarkup.setKeyboard(keyboard);
        bot.sendMessage(chatId, tutor.toString(), keyboardMarkup);
    }

    void askTutorForConfirmation(Tutor chosenTutor, Student student){
        Long tutorTelegramId = chosenTutor.getTelegramId();
        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        String confirmText = "Подтвердить запись";
        String rejectText = "Отклонить запись";
        InlineKeyboardButton confirm = new InlineKeyboardButton(confirmText);
        InlineKeyboardButton reject = new InlineKeyboardButton(rejectText);
        confirm.setCallbackData(confirmText + "/" + student.getTelegramId());
        reject.setCallbackData(rejectText + "/" + student.getTelegramId());
        List<List<InlineKeyboardButton>> keyboard = Collections.singletonList(List.of(confirm, reject));
        keyboardMarkup.setKeyboard(keyboard);
        if(sessionManager.containsUserSession(tutorTelegramId)){
            UserSession tutorSession = sessionManager.getUserSession(tutorTelegramId);
            tutorSession.setStage(Stage.CONFIRMING_REGISTRATION);
        } else sessionManager.startSessionForTutorConfirmation(chosenTutor);
        String text = "К вам на занятия хочет записаться студент. Подтвердите или отклоните запись\n" 
        + student.toString();
        bot.sendMessage(tutorTelegramId, text , keyboardMarkup);
    }

    void notifyStudentAboutConfirmation(long studentId){
        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        InlineKeyboardButton mainMenu = new InlineKeyboardButton("Главное меню");
        mainMenu.setCallbackData("Главное меню");
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>(Collections.singletonList(
            Collections.singletonList(mainMenu)));
        keyboardMarkup.setKeyboard(keyboard);
        UserSession session = sessionManager.getUserSession(studentId);
        session.setStage(Stage.NOTIFYING_REGISTRATION_RESULTS);
        session.sendUser();
        bot.sendMessage(studentId, "Репетитор подтвердил вашу запись. " + 
        "Вы успешно зарегистрированы", keyboardMarkup);
    }

    void notifyStudentAboutRejection(long studentId){
        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        InlineKeyboardButton mainMenu = new InlineKeyboardButton("К списку репетиторов");
        mainMenu.setCallbackData("К списку репетиторов");
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>(Collections.singletonList(
            Collections.singletonList(mainMenu)));
        keyboardMarkup.setKeyboard(keyboard);
        UserSession session = sessionManager.getUserSession(studentId);
        session.setStage(Stage.NOTIFYING_REGISTRATION_RESULTS);
        bot.sendMessage(studentId, "К сожалению, репетитор не подтвердил вашу запись", keyboardMarkup);
    }

    void launchMainMenu(long chatId, UserSession session){
        
    }

}
