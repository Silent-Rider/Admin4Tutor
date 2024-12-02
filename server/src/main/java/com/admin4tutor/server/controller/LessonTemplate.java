package com.admin4tutor.server.controller;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import lombok.Getter;
import lombok.Setter;

public class LessonTemplate {

    static final Map<Long, List<LessonTemplate>> SCHEDULES = new ConcurrentHashMap<>();
    
    @Getter @Setter
    private LocalTime startTime;

    @Getter @Setter
    private DayOfWeek dayOfWeek;
}
