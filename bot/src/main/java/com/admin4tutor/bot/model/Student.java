package com.admin4tutor.bot.model;

import java.util.HashMap;
import java.util.Map;

public class Student extends User {
    
    private final Map<DayOfWeek, String> schedule = new HashMap<>(2);

    public Map<DayOfWeek, String> getSchedule() {
        return schedule;
    }

}
