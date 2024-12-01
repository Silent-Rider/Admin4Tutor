package com.admin4tutor.bot.dto;

import java.util.HashMap;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

public class Tutor extends User {
    
    @Getter @Setter
    private String biography;
    
    @Getter @Setter
    private Integer price;
    
    @Getter
    private final Map<DayOfWeek, String> availability = new HashMap<>(7){{
        put(DayOfWeek.MONDAY, null);
        put(DayOfWeek.TUESDAY, null);
        put(DayOfWeek.WEDNESDAY, null);
        put(DayOfWeek.THURSDAY, null);
        put(DayOfWeek.FRIDAY, null);
        put(DayOfWeek.SATURDAY, null);
        put(DayOfWeek.SUNDAY, null);
    }};

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
