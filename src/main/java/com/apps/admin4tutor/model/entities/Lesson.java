package com.apps.admin4tutor.model.entities;

import java.sql.Date;
import java.sql.Time;

import jakarta.persistence.*;

@Entity
@Table(name = "schedule")
public class Lesson {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @ManyToOne
    @JoinColumn(name = "tutor_id", nullable = false)
    private Tutor tutor;

    @Column(name = "lesson_date", nullable = false)
    private Date lessonDate;

    @Column(name = "start_time", nullable = false)
    private Time startTime;

    @Column(name = "end_time", nullable = false)
    private Time endTime;

    private Status status = Status.SCHEDULED;

    private enum Status{
        SCHEDULED, 
        COMPLETED, 
        CANCELLED
    } 
}
