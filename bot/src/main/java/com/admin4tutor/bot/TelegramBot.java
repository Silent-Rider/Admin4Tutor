package com.admin4tutor.bot;

import java.util.ArrayList;
import java.util.List;

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

import com.admin4tutor.bot.service.StageHandler;
import com.admin4tutor.bot.service.AnswerHandler;

@Component
public class TelegramBot extends TelegramLongPollingBot {

    private static final Logger logger = LoggerFactory.getLogger(TelegramBot.class);
    private final String botToken;
    private final String botUsername;
    private final AnswerHandler answerHandler;

    public TelegramBot(
        @Value("${telegram.bot.token}") String botToken, 
        @Value("${telegram.bot.username}")String botUsername) {
        super(new DefaultBotOptions(), botToken);
        this.botToken = botToken;
        this.botUsername = botUsername;
        this.answerHandler = new AnswerHandler(new StageHandler(this));
    }
    
    @Override
    public void onUpdateReceived(Update update) {
        if(update.hasMessage() && update.getMessage().hasText()) {
            Message message = update.getMessage();
            long chatId = message.getChatId();
            String text = message.getText();
            long telegramId = message.getFrom().getId();
            
            if(text.equals("/start")){
                answerHandler.startSession(chatId);
                askForRole(chatId);
            } else {
                answerHandler.handleUserAnswer(chatId, text);
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