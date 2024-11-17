package com.admin4tutor.bot.service;

import java.util.ArrayList;
import java.util.List;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import com.admin4tutor.bot.TelegramBot;
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

    void askForName(long chatId, User user){
        bot.sendMessage(chatId, "Напишите вашу фамилию и имя", null);
        System.out.println(user.getClass() + " " + user.getLanguage());
    }

    void askForSchedule(long chatId, User user){
        bot.sendMessage(chatId, "Напишите удобное для вас расписание", null);
        System.out.println(user.getClass() + " " + user.getLanguage());
    }

}
