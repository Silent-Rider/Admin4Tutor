package com.admin4tutor.bot.dto;

public enum DayOfWeek {
    MONDAY("Понедельник"),
    TUESDAY("Вторник"),
    WEDNESDAY("Среда"),
    THURSDAY("Четверг"),
    FRIDAY("Пятница"),
    SATURDAY("Суббота"),
    SUNDAY("Воскресенье");

    private final String value;

    private DayOfWeek(String value){
        this.value = value;
    }

    public String getValue(){
        return value;
    }
}
