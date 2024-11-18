package com.admin4tutor.bot.model;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;

public abstract class User {
    
    private Long chatId;
    private Long telegramId;
    private String name;
    private Language language;
    private String email;
    private String phoneNumber;
    private String dateOfBirth;
    
    public Long getChatId() {
        return chatId;
    }
    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }
    public Long getTelegramId() {
        return telegramId;
    }
    public void setTelegramId(Long telegramId) {
        this.telegramId = telegramId;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public Language getLanguage() {
        return language;
    }
    public void setLanguage(Language language) {
        this.language = language;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getPhoneNumber() {
        return phoneNumber;
    }
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    public String getDateOfBirth() {
        return dateOfBirth;
    }
    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public int getAge(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        LocalDate date = LocalDate.parse(dateOfBirth, formatter);
        return Period.between(date, LocalDate.now()).getYears();
    }

    @Override
    public String toString(){
        return name + "\nВозраст: " + getAge() + "\nЯзык: " + language.getValue() + 
        (email == null ? "" : "\nПочта: " + email) + 
        (phoneNumber == null ? "" : "\nНомер телефона: " + phoneNumber);
    }
}
