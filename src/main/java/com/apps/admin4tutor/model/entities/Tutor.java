package com.apps.admin4tutor.model.entities;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.apps.admin4tutor.model.Language;

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

    @ElementCollection
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "tutor_languages", joinColumns = @JoinColumn(name = "tutor_id"))
    @Column(name = "language", columnDefinition = "VARCHAR(15)", nullable = false)
    private Set <Language> languages = new HashSet<>();

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
}
