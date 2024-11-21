package com.admin4tutor.bot.dto;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Student extends User {
    
    private final Map<DayOfWeek, String> schedule = new ConcurrentHashMap<>();
    private Long tutorId;

    public Student(long chatId, long telegramId) {
        super(chatId, telegramId);
    }
    
    public Map<DayOfWeek, String> getSchedule() {
        return schedule;
    }

    public Long getTutorId() {
        return tutorId;
    }
    
    public void setTutorId(Long tutorId) {
        this.tutorId = tutorId;
    }

    @Override
    public String toString(){
        String result = "🔽 Расписание учебных занятий 🔽\n";
        for(var day: schedule.keySet())
            result += day.getValue() + ": " + schedule.get(day) + "\n";
        return super.toString() + "\n" + result;
    }

}
