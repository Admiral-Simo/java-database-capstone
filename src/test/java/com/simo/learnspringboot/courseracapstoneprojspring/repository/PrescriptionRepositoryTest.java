package com.simo.learnspringboot.courseracapstoneprojspring.repository;

import com.simo.learnspringboot.courseracapstoneprojspring.model.Doctor;
import com.simo.learnspringboot.courseracapstoneprojspring.model.Patient;
import com.simo.learnspringboot.courseracapstoneprojspring.model.mongo.Prescription;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

// Add webEnvironment = SpringBootTest.WebEnvironment.NONE to prevent loading the web context
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@Testcontainers // Enable Testcontainers support
public class PrescriptionRepositoryTest {
    @Autowired
    private PrescriptionRepository prescriptionRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    // This will start a MongoDB container for the test
    @Container
    static MongoDBContainer mongoDBContainer = new MongoDBContainer(DockerImageName.parse("mongo:latest"));

    // This method dynamically sets the MongoDB URI for the test application context
    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
    }

    private Patient patient;
    private Doctor doctor;

    @BeforeEach
    void setUp() {
        // Clear repositories to ensure test isolation
        prescriptionRepository.deleteAll();
        patientRepository.deleteAll();
        doctorRepository.deleteAll();

        // Setup test data
        patient = new Patient("johndoe", "password123", "John", "Doe");
        patient.setDateOfBirth(LocalDate.of(2006, 1, 15));
        // ADD THESE TWO LINES
        patient = patientRepository.save(patient);

        doctor = new Doctor("janesmith", "password1234", "Jane", "Cardiology");
        // ADD THESE TWO LINES
        doctor = doctorRepository.save(doctor);
    }
    @Test
    void findByPatientId_shouldReturnPrescriptions() {
        // Arrange: Create medication and prescription
        Prescription.Medication medication = new Prescription.Medication();
        medication.setName("Aspirin");
        medication.setDosage("81mg");
        medication.setFrequency("Once a day");

        Prescription prescription = new Prescription();
        prescription.setPatientId(patient.getId());
        prescription.setDoctorId(doctor.getId());
        prescription.setDatePrescribed(LocalDate.now());
        prescription.setMedications(Collections.singletonList(medication));

        prescriptionRepository.save(prescription);

        // Act: Find prescriptions by patient ID
        List<Prescription> foundPrescription = prescriptionRepository.findByPatientId(patient.getId());

        // Assert: Verify the prescription was found and contains the correct data
        assertThat(foundPrescription).hasSize(1);
        assertThat(foundPrescription.get(0).getPatientId()).isEqualTo(patient.getId());
        assertThat(foundPrescription.get(0).getMedications()).hasSize(1);
        assertThat(foundPrescription.get(0).getMedications().get(0).getName()).isEqualTo("Aspirin");
    }
}

