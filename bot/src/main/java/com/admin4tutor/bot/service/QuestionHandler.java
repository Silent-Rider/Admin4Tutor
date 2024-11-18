package com.admin4tutor.bot.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import com.admin4tutor.bot.TelegramBot;
import com.admin4tutor.bot.model.DayOfWeek;
import com.admin4tutor.bot.model.Language;
import com.admin4tutor.bot.model.Tutor;
import com.admin4tutor.bot.model.User;

public class QuestionHandler {
    private final TelegramBot bot;

    public QuestionHandler(TelegramBot bot){
        this.bot = bot;
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
        bot.sendMessage(chatId, "Введите через пробел вашу фамилию и имя", null);
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
            TelegramBot.logger.error("Lost day of week value");
            return;
        }
        String text = String.format("%s: Укажите один или более интервалов доступности " + 
            "в формате \"ЧЧ:ММ-ЧЧ:ММ\", перечисляя их через запятую. 🕰%n", 
            dayOfWeek.getValue()) + " Пример: 10:45-14:00, 20:00-22:30";
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
        if(daysOfWeek.isEmpty()) session.setCurrentDays(Arrays.asList(DayOfWeek.values()));
        InlineKeyboardButton ready = new InlineKeyboardButton("Готово");
        ready.setCallbackData("READY");
        keyboard.add(Collections.singletonList(ready));
        keyboardMarkup.setKeyboard(keyboard);
        String text = "Выберите еще один день недели для указания интервалов доступности," +
        " либо нажмите \"Готово\"";
        bot.sendMessage(chatId, text, keyboardMarkup);
    }

    void askForScheduleDay(long chatId, User user){
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
        String text = "Выберите желаемый день недели для проведения занятия";
        bot.sendMessage(chatId, text, keyboardMarkup);
    }

    void askForScheduleTime(long chatId, String answer){
        DayOfWeek dayOfWeek = null;
        for(DayOfWeek day: DayOfWeek.values())
            if(day.toString().equals(answer)) dayOfWeek = day;
        if(dayOfWeek == null){
            TelegramBot.logger.error("Lost day of week value");
            return;
        }
        String text = String.format("%s: Укажите время начала занятия " + 
            "в формате \"ЧЧ:ММ\". 🕰", dayOfWeek.getValue()) + "\nПример: 17:00";
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
        if(daysOfWeek.isEmpty()) session.setCurrentDays(Arrays.asList(DayOfWeek.values()));
        InlineKeyboardButton ready = new InlineKeyboardButton("Готово");
        ready.setCallbackData("READY");
        keyboard.add(Collections.singletonList(ready));
        keyboardMarkup.setKeyboard(keyboard);
        String text = "Выберите еще один день недели для проведения занятий," +
        " либо нажмите \"Готово\"";
        bot.sendMessage(chatId, text, keyboardMarkup);
    }
    
    void askForTutor(long chatId, UserSession session){
        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        List<Tutor> tutors = session.getSuitableTutors();
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        tutors.forEach(x -> keyboard.add(Collections.singletonList(new InlineKeyboardButton(x.getName()))));
        keyboardMarkup.setKeyboard(keyboard);
        String text = "Выберите подходящего репетитора из списка";
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

    void askForEmail(long chatId){
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        keyboardMarkup.setResizeKeyboard(true);
        keyboardMarkup.setOneTimeKeyboard(true);
        KeyboardRow row = (KeyboardRow)Collections.singletonList(new KeyboardButton("Пропустить"));
        List <KeyboardRow> keyboard = Collections.singletonList(row);
        keyboardMarkup.setKeyboard(keyboard);
        bot.sendMessage(chatId, "Напишите ваш адрес электронной почты", keyboardMarkup);
    }

    void askForPhoneNumber(long chatId){
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        keyboardMarkup.setResizeKeyboard(true);
        keyboardMarkup.setOneTimeKeyboard(true);
        KeyboardRow row = (KeyboardRow)Collections.singletonList(new KeyboardButton("Пропустить"));
        List <KeyboardRow> keyboard = Collections.singletonList(row);
        keyboardMarkup.setKeyboard(keyboard);
        bot.sendMessage(chatId, "Напишите ваш номер телефона", keyboardMarkup);
    }
}
