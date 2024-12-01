package com.admin4tutor.bot.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Availability {

    private DayOfWeek dayOfWeek;

    private String startTime;

    private String endTime;
}
