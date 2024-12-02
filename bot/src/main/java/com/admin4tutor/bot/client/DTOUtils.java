package com.admin4tutor.bot.client;

import java.util.ArrayList;
import java.util.List;

import com.admin4tutor.bot.dto.Availability;
import com.admin4tutor.bot.dto.Lesson;
import com.admin4tutor.bot.dto.Student;
import com.admin4tutor.bot.dto.Tutor;

public class DTOUtils {

    static List<Availability> getAvailabilities(Tutor tutor){
        List<Availability> availabilities = new ArrayList<>();
        for(var dayOfWeek: tutor.getAvailability().keySet()){
            List<String> intervals = tutor.getAvailability().get(dayOfWeek);
            if(intervals.isEmpty()) continue;
            for(var interval: intervals){
                String[] times = interval.split("-");
                Availability availability = Availability.builder().startTime(times[0])
                .endTime(times[1]).dayOfWeek(dayOfWeek).build();
                availabilities.add(availability);
            }
        }
        return availabilities;
    }

    static List<Lesson> getLessons(Student student){
        List<Lesson> lessons = new ArrayList<>();
        for(var dayOfWeek: student.getSchedule().keySet()){
            Lesson lesson = Lesson.builder()
            .startTime(student.getSchedule().get(dayOfWeek))
            .dayOfWeek(dayOfWeek)
            .build();
            lessons.add(lesson);
        }
        return lessons;
    }
}
