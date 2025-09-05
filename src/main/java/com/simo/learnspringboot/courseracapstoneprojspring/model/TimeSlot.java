package com.simo.learnspringboot.courseracapstoneprojspring.model;

// In a new file: TimeSlot.java
import jakarta.persistence.Embeddable;
import java.time.DayOfWeek;
import java.time.LocalTime;

@Embeddable // Marks this as a component that can be embedded in other entities
public class TimeSlot {
    private DayOfWeek dayOfWeek; // e.g., MONDAY
    private LocalTime startTime; // e.g., 09:00
    private LocalTime endTime;   // e.g., 17:00

    // Constructors, getters, setters, equals, and hashCode
}