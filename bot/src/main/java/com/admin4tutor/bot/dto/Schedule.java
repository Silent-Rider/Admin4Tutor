package com.admin4tutor.bot.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Schedule {

    private String startTime;

    private DayOfWeek dayOfWeek;

    private String studentName;
}
