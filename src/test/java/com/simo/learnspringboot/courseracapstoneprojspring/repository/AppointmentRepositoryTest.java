package com.simo.learnspringboot.courseracapstoneprojspring.repository;

import com.simo.learnspringboot.courseracapstoneprojspring.dto.DoctorPatientCountDTO;
import com.simo.learnspringboot.courseracapstoneprojspring.model.Appointment;
import com.simo.learnspringboot.courseracapstoneprojspring.model.AppointmentStatus;
import com.simo.learnspringboot.courseracapstoneprojspring.model.Doctor;
import com.simo.learnspringboot.courseracapstoneprojspring.model.Patient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest // Focuses on JPA components, making tests faster
@Testcontainers // Enables JUnit 5 support for Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) // Disable default in-memory H2 database
class AppointmentRepositoryTest {

    @Container
    // Create a PostgreSQL container that will be started before tests and shut down after
    static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine");

    @DynamicPropertySource
    // Dynamically set the database properties for the test application context
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        // Explicitly set the driver class name to prevent H2 from being used
        registry.add("spring.datasource.driver-class-name", () -> "org.postgresql.Driver");
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "create-drop");
        // Add this property to ensure Hibernate quotes all table and column names,
        // which resolves potential naming conflicts with PostgreSQL.
        registry.add("spring.jpa.properties.hibernate.globally_quoted_identifiers", () -> "true");
        // Explicitly set the PostgreSQL dialect to ensure correct SQL generation for schema creation.
        registry.add("spring.jpa.database-platform", () -> "org.hibernate.dialect.PostgreSQLDialect");
    }

    @Autowired
    private AppointmentRepository appointmentRepository;
    @Autowired
    private DoctorRepository doctorRepository;
    @Autowired
    private PatientRepository patientRepository;

    private Doctor drSmith;
    private Doctor drJones;

    @BeforeEach
    void setUp() {
        // Clean up database tables before each test to ensure isolation
        appointmentRepository.deleteAll();
        doctorRepository.deleteAll();
        patientRepository.deleteAll();

        // --- Setup Test Data ---

        // Create Doctors with complete information
        Doctor smith = new Doctor("dr.smith", "pass", "John", "Cardiology");
        smith.setLastName("Smith");
        drSmith = doctorRepository.save(smith);

        Doctor jones = new Doctor("dr.jones", "pass", "Emily", "Neurology");
        jones.setLastName("Jones");
        drJones = doctorRepository.save(jones);

        // Create Patients with usernames that satisfy the validation constraints (min 3 chars)
        Patient patient1 = patientRepository.save(new Patient("patient1", "pass", "Patient", "One"));
        Patient patient2 = patientRepository.save(new Patient("patient2", "pass", "Patient", "Two"));
        Patient patient3 = patientRepository.save(new Patient("patient3", "pass", "Patient", "Three"));

        // Define a target month for appointments that is always in the future to satisfy the @Future constraint.
        LocalDateTime targetMonthStart = YearMonth.from(LocalDateTime.now().plusMonths(1)).atDay(1).atStartOfDay();

        // --- Schedule Appointments ---

        // Dr. Smith will be the top doctor with 3 unique patients this month.
        createAppointment(drSmith, patient1, targetMonthStart.plusDays(1));
        createAppointment(drSmith, patient2, targetMonthStart.plusDays(2));
        createAppointment(drSmith, patient3, targetMonthStart.plusDays(3));

        // Dr. Jones will be second, with 4 appointments but only 2 unique patients.
        createAppointment(drJones, patient1, targetMonthStart.plusDays(5));
        createAppointment(drJones, patient1, targetMonthStart.plusDays(6)); // Same patient again
        createAppointment(drJones, patient2, targetMonthStart.plusDays(7));
        createAppointment(drJones, patient2, targetMonthStart.plusDays(8)); // Same patient again

        // Create an appointment outside the target month to ensure it's ignored by the query.
        createAppointment(drSmith, patient1, targetMonthStart.plusMonths(2));
    }

    @Test
    @DisplayName("Should find top doctors ranked by unique patient count for a given month")
    void findTopDoctorsByPatientCount_shouldReturnCorrectlyRankedDoctors() {
        // Arrange: Define the date range for the query to match the future test data.
        YearMonth targetYearMonth = YearMonth.from(LocalDateTime.now().plusMonths(1));
        LocalDateTime startDate = targetYearMonth.atDay(1).atStartOfDay();
        LocalDateTime endDate = targetYearMonth.atEndOfMonth().atTime(23, 59, 59);

        // Act: Call the repository method
        List<DoctorPatientCountDTO> topDoctors = appointmentRepository.findTopDoctorsByPatientCount(startDate, endDate);

        // Assert: Verify the results
        assertThat(topDoctors).isNotNull();
        assertThat(topDoctors).hasSize(2); // Only drSmith and drJones should be in the list

        // 1. Verify the top doctor is Dr. Smith with 3 unique patients
        DoctorPatientCountDTO topDoctor = topDoctors.get(0);
        assertThat(topDoctor.doctorId()).isEqualTo(drSmith.getId());
        assertThat(topDoctor.doctorFirstName()).isEqualTo("John");
        assertThat(topDoctor.doctorLastName()).isEqualTo("Smith");
        assertThat(topDoctor.patientCount()).isEqualTo(3);

        // 2. Verify the second doctor is Dr. Jones with 2 unique patients
        DoctorPatientCountDTO secondDoctor = topDoctors.get(1);
        assertThat(secondDoctor.doctorId()).isEqualTo(drJones.getId());
        assertThat(secondDoctor.doctorFirstName()).isEqualTo("Emily");
        assertThat(secondDoctor.doctorLastName()).isEqualTo("Jones");
        assertThat(secondDoctor.patientCount()).isEqualTo(2);
    }

    // Helper method to quickly create and save appointments
    private void createAppointment(Doctor doctor, Patient patient, LocalDateTime dateTime) {
        Appointment appointment = new Appointment();
        appointment.setDoctor(doctor);
        appointment.setPatient(patient);
        appointment.setAppointmentDateTime(dateTime);
        appointment.setStatus(AppointmentStatus.SCHEDULED);
        appointment.setReason("Check-up");
        appointmentRepository.save(appointment);
    }
}

