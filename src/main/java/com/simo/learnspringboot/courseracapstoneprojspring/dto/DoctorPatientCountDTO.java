package com.simo.learnspringboot.courseracapstoneprojspring.dto;

import java.util.UUID;

// Using a Java Record is a concise way to create a DTO
public record DoctorPatientCountDTO(
        UUID doctorId,
        String doctorFirstName,
        String doctorLastName,
        long patientCount
) {}