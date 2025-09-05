package com.simo.learnspringboot.courseracapstoneprojspring.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@Table(name = "appointments")
public class Appointment {
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private LocalDateTime appointmentDateTime;
    private String reason;

    @ManyToOne
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @ManyToOne
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor;
}
