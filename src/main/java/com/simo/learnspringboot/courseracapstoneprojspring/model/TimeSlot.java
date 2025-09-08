package com.simo.learnspringboot.courseracapstoneprojspring.model;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.DayOfWeek;
import java.time.LocalTime;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
public class TimeSlot {
    private DayOfWeek dayOfWeek; // e.g., MONDAY
    private LocalTime startTime; // e.g., 09:00
    private LocalTime endTime;   // e.g., 17:00

}
