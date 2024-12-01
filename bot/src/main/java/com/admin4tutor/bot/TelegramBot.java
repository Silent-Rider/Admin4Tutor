package com.admin4tutor.bot;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import com.admin4tutor.bot.service.SessionManager;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class TelegramBot extends TelegramLongPollingBot {

    private final String botToken;
    private final String botUsername;
    private final SessionManager sessionManager;

    public TelegramBot (@Value("${telegram.bot.token}") String botToken,
    @Value("${telegram.bot.username}")String botUsername,  @Lazy SessionManager sessionManager) {
        super(new DefaultBotOptions(), botToken);
        this.botToken = botToken;
        this.botUsername = botUsername;
        this.sessionManager = sessionManager;
    }
    
    @Override
    public void onUpdateReceived(Update update) {
        CompletableFuture.runAsync(() -> processUpdate(update)).exceptionally(e -> {
                log.error("Exception during async processing of update", e);
                return null;
            });
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    private void processUpdate(Update update){
        if(update.hasMessage() && update.getMessage().hasText()) {
            Message message = update.getMessage();
            long chatId = message.getChatId();
            String text = message.getText();
            long telegramId = message.getFrom().getId();
            if(text.equals("/start")){
                sessionManager.startSession(chatId, telegramId);
                startConversation(chatId);
            } else {
                sessionManager.handleUserAnswer(chatId, text);
            }
        } else if(update.hasCallbackQuery()){
            CallbackQuery query = update.getCallbackQuery();
            long chatId = query.getMessage().getChatId();
            String text = query.getData();
            sessionManager.handleUserAnswer(chatId, text);
        }
    }

    public void sendMessage(long chatId, String text, ReplyKeyboard keyboardMarkup){
        SendMessage message = new SendMessage(String.valueOf(chatId), text);
        if(keyboardMarkup != null){
            message.setReplyMarkup(keyboardMarkup);
            sessionManager.getUserSession(chatId).setCurrentKeyboard(keyboardMarkup);
        } else {
            ReplyKeyboardRemove remove = new ReplyKeyboardRemove(true);
            message.setReplyMarkup(remove);
            sessionManager.getUserSession(chatId).setCurrentKeyboard(remove);
        }
        try{
            execute(message);
        } catch(TelegramApiException e){
            log.error("TelegramApiException occurred while sending a message to user: {}", chatId, e);
        }
    }

    public void startConversation(long chatId){
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        keyboardMarkup.setResizeKeyboard(true);
        keyboardMarkup.setOneTimeKeyboard(true);
        KeyboardRow row = new KeyboardRow() {{ add(new KeyboardButton("🧑🏼‍🎓 Я студент"));
        add(new KeyboardButton("🧑🏻‍🏫 Я репетитор")); }};
        List <KeyboardRow> keyboard = Collections.singletonList(row);
        keyboardMarkup.setKeyboard(keyboard);
        sendMessage(chatId, "Кто вы?", keyboardMarkup);
    }
}