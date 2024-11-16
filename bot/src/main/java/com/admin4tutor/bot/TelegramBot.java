package com.admin4tutor.bot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import com.admin4tutor.bot.handlers.StageHandler;

@Component
public class TelegramBot extends TelegramLongPollingBot {

    private final Logger logger = LoggerFactory.getLogger(TelegramBot.class);
    private final Map<Long, Stage> userStages = new ConcurrentHashMap<>();
    private final String botToken;
    private final String botUsername;
    private StageHandler stageHandler;

    public TelegramBot(
        @Value("${telegram.bot.token}") String botToken, 
        @Value("${telegram.bot.username}")String botUsername) {
        super(new DefaultBotOptions(), botToken);
        this.botToken = botToken;
        this.botUsername = botUsername;
    }
    
    @Override
    public void onUpdateReceived(Update update) {
        if(update.hasMessage() && update.getMessage().hasText()) {
            Message message = update.getMessage();
            long chatId = message.getChatId();
            if(message.getText().equals("/start")){
                userStages.put(chatId, Stage.ASKING_FOR_ROLE);
                askForRole(chatId);
            } else if(userStages.containsKey(chatId)){
                switch(userStages.get(chatId)){
                    case ASKING_FOR_ROLE:
                    userStages.put(chatId, Stage.CHOOSING_LANGUAGE);
                }
            }
        }
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    private void askForRole(long chatId){
        SendMessage message = new SendMessage(String.valueOf(chatId), "Кто вы?");
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        keyboardMarkup.setResizeKeyboard(true); // делает клавиатуру компактной
        keyboardMarkup.setOneTimeKeyboard(true);

        KeyboardRow row = new KeyboardRow(2);
        row.add(new KeyboardButton("Я студент"));
        row.add(new KeyboardButton("Я репетитор"));

        List <KeyboardRow> keyboard = new ArrayList<>() {{ add(row); }};
        keyboardMarkup.setKeyboard(keyboard);
        message.setReplyMarkup(keyboardMarkup);

        try{
            execute(message);
        } catch(TelegramApiException e){
            logger.error("TelegramApiException occurred during asking user for role");
        }
    }
}