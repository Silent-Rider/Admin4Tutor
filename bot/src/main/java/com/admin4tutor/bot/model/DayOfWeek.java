package com.admin4tutor.bot.model;

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

    @Override
    public String toString(){
        return value;
    }
}
