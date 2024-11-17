package com.admin4tutor.bot.model;

import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;

public class Tutor extends User {
    
    private String biography;
    private final Map<DayOfWeek, String> availability = new ConcurrentHashMap<>(7){{
        put(DayOfWeek.MONDAY, null);
        put(DayOfWeek.TUESDAY, null);
        put(DayOfWeek.WEDNESDAY, null);
        put(DayOfWeek.THURSDAY, null);
        put(DayOfWeek.FRIDAY, null);
        put(DayOfWeek.SATURDAY, null);
        put(DayOfWeek.SUNDAY, null);
    }};
    
    public Map<DayOfWeek, String> getAvailability() {
        return availability;
    }
    
    public String getBiography() {
        return biography;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }
}
