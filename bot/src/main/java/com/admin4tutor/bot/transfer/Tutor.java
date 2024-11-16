package com.admin4tutor.bot.transfer;

import java.util.HashMap;
import java.util.Map;

public class Tutor extends User {
    
    private final Map<DayOfWeek, String> availability = new HashMap<>(7){{
        put(DayOfWeek.MONDAY, null);
        put(DayOfWeek.TUESDAY, null);
        put(DayOfWeek.WEDNESDAY, null);
        put(DayOfWeek.THURSDAY, null);
        put(DayOfWeek.FRIDAY, null);
        put(DayOfWeek.SATURDAY, null);
        put(DayOfWeek.SUNDAY, null);
    }};
    private String biography;
}
