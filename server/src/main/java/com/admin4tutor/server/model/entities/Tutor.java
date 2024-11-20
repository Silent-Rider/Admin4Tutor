package com.admin4tutor.server.model.entities;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.admin4tutor.server.model.Language;

import jakarta.persistence.*;

@Entity
@Table(name = "tutors")
public class Tutor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "telegram_id", unique = true, nullable = false)
    private Long telegramId;

    @Column(name = "chat_id", unique = true, nullable = false)
    private Long chatId;

    @Column(nullable = false)
    private String name;

    @Column(name = "date_of_birth", nullable = false)
    private Date dateOfBirth;

    @ElementCollection
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "tutor_languages", joinColumns = @JoinColumn(name = "tutor_id"))
    @Column(name = "language", columnDefinition = "VARCHAR(15)", nullable = false)
    private Set <Language> languages = new HashSet<>();

    @Column(nullable = false)
    private Integer price;

    @Column(unique = true)
    private String email;

    @Column(name = "phone_number", unique = true)
    private String phoneNumber;

    @Column(name = "created_at", nullable = false)
    private Timestamp createdAt = Timestamp.valueOf(LocalDateTime.now());

    @Column(columnDefinition = "TEXT", nullable = false)
    private String biography;

    @OneToMany(mappedBy = "tutor", cascade = CascadeType.ALL)
    private List <Lesson> lessons;

    @OneToMany(mappedBy = "tutor", cascade = CascadeType.ALL)
    private List <Availability> availabilities;

    @OneToMany(mappedBy="tutor", cascade = CascadeType.ALL)
    private List<Student> students;

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public Long getTelegramId() {
        return telegramId;
    }
    public void setTelegramId(Long telegramId) {
        this.telegramId = telegramId;
    }
    public Long getChatId() {
        return chatId;
    }
    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public Date getDateOfBirth() {
        return dateOfBirth;
    }
    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }
    public Set<Language> getLanguages() {
        return languages;
    }
    public void setLanguages(Set<Language> languages) {
        this.languages = languages;
    }
    public Integer getPrice() {
        return price;
    }
    public void setPrice(Integer price) {
        this.price = price;
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
    public Timestamp getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
    public String getBiography() {
        return biography;
    }
    public void setBiography(String biography) {
        this.biography = biography;
    }
    public List<Lesson> getLessons() {
        return lessons;
    }
    public void setLessons(List<Lesson> lessons) {
        this.lessons = lessons;
    }
    public List<Availability> getAvailabilities() {
        return availabilities;
    }
    public void setAvailabilities(List<Availability> availabilities) {
        this.availabilities = availabilities;
    }
    public List<Student> getStudents() {
        return students;
    }
    public void setStudents(List<Student> students) {
        this.students = students;
    }
}
