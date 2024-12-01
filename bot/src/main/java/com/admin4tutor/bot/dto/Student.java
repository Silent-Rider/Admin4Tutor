package com.admin4tutor.bot.dto;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import lombok.Getter;
import lombok.Setter;

public class Student extends User {
    
    @Getter
    private final Map<DayOfWeek, String> schedule = new ConcurrentHashMap<>();
    
    @Getter @Setter
    private Long tutorId;

    public Student(long chatId, long telegramId) {
        super(chatId, telegramId);
    }

    @Override
    public String toString(){
        String result = "🔽 Расписание учебных занятий 🔽\n";
        for(var day: schedule.keySet())
            result += day.getValue() + ": " + schedule.get(day) + "\n";
        return super.toString() + "\n" + result;
    }

}
