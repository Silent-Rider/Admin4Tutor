package com.apps.admin4tutor.model.entities;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.*;

@Entity
@Table(name = "tutors")
public class Tutor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "telegram_id", unique = true, nullable = false)
    private Long telegramId;

    @Column(nullable = false)
    private String name;

    @Column(unique = true)
    private String email;

    @Column(name = "phone_number", unique = true)
    private String phoneNumber;

    @Column(name = "created_at", nullable = false)
    private Timestamp createdAt = Timestamp.valueOf(LocalDateTime.now());

    @OneToMany(mappedBy = "tutor", cascade = CascadeType.ALL)
    private List <Lesson> lessons;

    @OneToMany(mappedBy = "tutor", cascade = CascadeType.ALL)
    private List <Availability> availabilities;
}
