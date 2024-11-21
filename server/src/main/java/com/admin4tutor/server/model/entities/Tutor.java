package com.admin4tutor.server.model.entities;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

import com.admin4tutor.server.model.Language;

import jakarta.persistence.*;
import lombok.Data;

@Data
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

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "VARCHAR(15)", nullable = false)
    private Language language;

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
}
