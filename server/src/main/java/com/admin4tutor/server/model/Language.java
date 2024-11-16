package com.admin4tutor.server.model;

public enum Language {
    ENGLISH("Английский"),
    GERMAN("Немецкий"),
    FRENCH("Французский"),
    SPANISH("Испанский"),
    ITALIAN("Итальянский"),
    CHINESE("Китайский"),
    JAPANESE("Японский"),
    KOREAN("Корейский");

    private final String value;

    private Language(String value){
        this.value = value;
    }

    @Override
    public String toString(){
        return value;
    }
}
