package com.admin4tutor.bot.dto;

import lombok.Getter;

public enum Language {
    ENGLISH("🇬🇧 Английский"),
    GERMAN("🇩🇪 Немецкий"),
    FRENCH("🇫🇷 Французский"),
    SPANISH("🇪🇸 Испанский"),
    ITALIAN("🇮🇹 Итальянский"),
    CHINESE("🇨🇳 Китайский"),
    JAPANESE("🇯🇵 Японский"),
    KOREAN("🇰🇷 Корейский");

    @Getter
    private final String value;

    private Language(String value){
        this.value = value;
    }
}
