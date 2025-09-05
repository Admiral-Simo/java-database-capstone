package com.simo.learnspringboot.courseracapstoneprojspring.model.mongo;

import jakarta.persistence.Id;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Data
@Document(collection = "prescriptions")
public class Prescription {
    @Id
    private String id;

    private UUID patientId;
    private UUID doctorId;
    private LocalDate datePrescribed;
    private List<Medication> medications;

    @Data
    public static class Medication {
        private String name;
        private String dosage;
        private String frequency;
    }
}
