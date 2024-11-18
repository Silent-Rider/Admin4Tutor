package com.admin4tutor.bot.service;

import java.util.ArrayList;
import java.util.List;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

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
            "в формате \"ЧЧ:ММ-ЧЧ:ММ\", перечисляя их через запятую.", 
            dayOfWeek.getValue()) + " Пример: 10:45-14:00, 20:00-22:30";
        bot.sendMessage(chatId, text, null);
    }

    void askForSchedule(long chatId, User user){
        //String text = "Напишите удобное для вас расписание";
        //bot.sendMessage(chatId, text, null);
    }

}
