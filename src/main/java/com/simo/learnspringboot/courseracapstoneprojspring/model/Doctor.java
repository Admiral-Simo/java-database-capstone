package com.simo.learnspringboot.courseracapstoneprojspring.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Set;
import java.util.UUID;

@Entity
@Data
@Table(name = "doctors")
public class Doctor {
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String firstName;
    private String lastName;
    private String specialization;

    @OneToMany(mappedBy = "doctor")
    private Set<Appointment> appointments;
}
