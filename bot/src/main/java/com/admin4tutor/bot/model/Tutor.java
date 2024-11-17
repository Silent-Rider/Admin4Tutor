package com.admin4tutor.bot.model;

import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;

public class Tutor extends User {
    
    private String biography;
    private final Map<DayOfWeek, String> availability = new ConcurrentHashMap<>(7){{
        put(DayOfWeek.MONDAY, "busy");
        put(DayOfWeek.TUESDAY, "busy");
        put(DayOfWeek.WEDNESDAY, "busy");
        put(DayOfWeek.THURSDAY, "busy");
        put(DayOfWeek.FRIDAY, "busy");
        put(DayOfWeek.SATURDAY, "busy");
        put(DayOfWeek.SUNDAY, "busy");
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
