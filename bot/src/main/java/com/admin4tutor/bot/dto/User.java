package com.admin4tutor.bot.dto;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;

import lombok.Data;

@Data
public abstract class User {

    private Long telegramId;
    private String name;
    private Language language;
    private String email;
    private String phoneNumber;
    private String dateOfBirth;
    
    protected User(long telegramId){
        this.telegramId = telegramId;
    }

    private String getAge(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        LocalDate date = LocalDate.parse(dateOfBirth, formatter);
        String age = String.valueOf(Period.between(date, LocalDate.now()).getYears());
        if(age.length() > 1 && age.substring(age.length()-2, age.length()).matches("1[1-9]"))
            return age + " лет";
        else if(age.substring(age.length()-1, age.length()).matches("1"))
            return age + " год";
        else if(age.substring(age.length()-1, age.length()).matches("[2-4]"))
            return age + " года";
        return age + " лет";
    }

    @Override
    public String toString(){
        return name + "\nВозраст: " + getAge() + "\n" + (this instanceof Tutor ? "Преподаю: " : "Изучаю: ") + 
        language.getValue() + (email == null ? "" : "\nПочта: " + email) + 
        (phoneNumber == null ? "" : "\nНомер телефона: " + phoneNumber);
    }
}
