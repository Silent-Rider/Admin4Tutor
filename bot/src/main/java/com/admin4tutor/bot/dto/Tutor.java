package com.admin4tutor.bot.dto;

import java.util.HashMap;
import java.util.Map;

public class Tutor extends User {
    
    private String biography;
    private Integer price;
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
    
    public Map<DayOfWeek, String> getAvailability() {
        return availability;
    }
    
    public String getBiography() {
        return biography;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
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
