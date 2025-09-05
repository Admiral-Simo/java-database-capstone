package com.simo.learnspringboot.courseracapstoneprojspring.model.mongo;

import jakarta.persistence.Id;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Data // @Data is safe for non-relational models like this one.
@Document(collection = "prescriptions")
public class Prescription {
    @Id
    private String id;

    // --- Relational Links ---
    @NotNull
    private UUID patientId;
    @NotNull
    private UUID doctorId;

    // --- Denormalized Data for Read Performance ---
    @NotNull
    private String patientFullName;
    @NotNull
    private String doctorFullName;
    @NotNull
    private String doctorSpecialization;

    // --- Prescription Details ---
    @NotNull
    private LocalDate datePrescribed;

    private String notes;

    @NotEmpty
    private List<Medication> medications;

    @Data
    public static class Medication {
        private String name;
        private String dosage;
        private String frequency;
    }
}