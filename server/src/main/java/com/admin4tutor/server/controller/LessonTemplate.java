package com.admin4tutor.server.controller;

import java.time.LocalTime;

import lombok.Data;
import java.time.DayOfWeek;

@Data
public class LessonTemplate {

    private LocalTime startTime;

    private DayOfWeek dayOfWeek;
}
