package com.admin4tutor.bot.dto;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;

public class Student extends User {
    
    @JsonIgnore
    @Getter
    private final Map<DayOfWeek, String> schedule = new ConcurrentHashMap<>();
    
    @JsonIgnore
    @Getter @Setter
    private Long tutorId;

    public Student(long telegramId) {
        super(telegramId);
    }

    @Override
    public String toString(){
        String result = "🔽 Расписание учебных занятий 🔽\n";
        for(var day: schedule.keySet())
            result += day.getValue() + ": " + schedule.get(day) + "\n";
        return super.toString() + "\n" + result;
    }

}
