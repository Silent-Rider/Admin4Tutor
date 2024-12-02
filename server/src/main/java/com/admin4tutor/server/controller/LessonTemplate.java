package com.admin4tutor.server.controller;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

import lombok.Data;

@Data
public class LessonTemplate {

    static Map<Long, List<LessonTemplate>> schedules;

    private LocalTime startTime;

    private DayOfWeek dayOfWeek;
}
