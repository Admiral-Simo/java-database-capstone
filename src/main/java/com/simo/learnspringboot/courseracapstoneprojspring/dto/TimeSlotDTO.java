package com.simo.learnspringboot.courseracapstoneprojspring.dto;

import java.time.LocalTime;

public record TimeSlotDTO(
        LocalTime startTime,
        LocalTime endTime
) {
}
