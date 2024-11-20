package com.admin4tutor.bot;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
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

import com.admin4tutor.bot.client.WebClientService;
import com.admin4tutor.bot.service.AnswerProcessor;
import com.admin4tutor.bot.service.SessionManager;

@Component
public class TelegramBot extends TelegramLongPollingBot {

    public static final Logger logger = LoggerFactory.getLogger(TelegramBot.class);
    private final String botToken;
    private final String botUsername;
    private final SessionManager sessionManager;
    private final WebClientService webClientService;

    public TelegramBot (@Value("${telegram.bot.token}") String botToken,
    @Value("${telegram.bot.username}")String botUsername,
    WebClientService webClientService) {
        super(new DefaultBotOptions(), botToken);
        this.botToken = botToken;
        this.botUsername = botUsername;
        this.webClientService = webClientService;
        this.sessionManager = new SessionManager(new AnswerProcessor(this), this.webClientService);
    }
    
    @Override
    public void onUpdateReceived(Update update) {
        CompletableFuture.runAsync(() -> processUpdate(update)).exceptionally(e -> {
                logger.error("Exception during async processing of update", e);
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
            logger.error("TelegramApiException occurred while sending a message to user: {}", chatId, e);
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