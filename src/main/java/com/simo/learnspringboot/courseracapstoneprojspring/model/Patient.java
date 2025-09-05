package com.simo.learnspringboot.courseracapstoneprojspring.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

@Entity
@Data
@Table(name = "patients")
public class Patient {
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;

    @OneToMany(mappedBy = "patient")
    private Set<Appointment> appointments;
}
