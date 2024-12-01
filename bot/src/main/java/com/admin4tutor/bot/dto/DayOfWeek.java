package com.admin4tutor.bot.dto;

import lombok.Getter;

public enum DayOfWeek {
    MONDAY("Понедельник"),
    TUESDAY("Вторник"),
    WEDNESDAY("Среда"),
    THURSDAY("Четверг"),
    FRIDAY("Пятница"),
    SATURDAY("Суббота"),
    SUNDAY("Воскресенье");

    @Getter
    private final String value;

    private DayOfWeek(String value){
        this.value = value;
    }
}
