package com.admin4tutor.bot.handlers;

import com.admin4tutor.bot.TelegramBot;

public class StageHandler {
    private final TelegramBot bot;
    private final Role role;

    public StageHandler(TelegramBot bot, Role role){
        this.bot = bot;
        this.role = role;
    }

    public enum Role{
        STUDENT,
        TUTOR
    }
}
