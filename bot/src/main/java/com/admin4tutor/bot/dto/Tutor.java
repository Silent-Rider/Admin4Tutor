package com.admin4tutor.bot.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import lombok.Getter;
import lombok.Setter;

public class Tutor extends User {
    
    @Getter @Setter
    private String biography;
    
    @Getter @Setter
    private Integer price;
    
    @Getter
    private final Map<DayOfWeek, List<String>> availability;

    {
        List<String> busy = new ArrayList<>();
        availability = new ConcurrentHashMap<>(7){{
            put(DayOfWeek.MONDAY, busy);
            put(DayOfWeek.TUESDAY, busy);
            put(DayOfWeek.WEDNESDAY, busy);
            put(DayOfWeek.THURSDAY, busy);
            put(DayOfWeek.FRIDAY, busy);
            put(DayOfWeek.SATURDAY, busy);
            put(DayOfWeek.SUNDAY, busy);
        }};
    }

    public Tutor(long chatId, long telegramId) {
        super(chatId, telegramId);
    }

    private String getPriceString(){
        String priceString = String.valueOf(price);
        if(priceString.length() > 1 && priceString.substring(priceString.length()-2, priceString.length()).matches("1[1-9]"))
            return priceString + " рублей";
        else if(priceString.substring(priceString.length()-1, priceString.length()).matches("1"))
            return priceString + " рубль";
        else if(priceString.substring(priceString.length()-1, priceString.length()).matches("[2-4]"))
            return priceString + " рубля";
        return priceString + " рублей";
    }

    @Override
    public String toString(){
        return super.toString() + "\nЦена за занятие: " + getPriceString() + "\n🌠 О себе 🌠\n" + biography;
    }
}
