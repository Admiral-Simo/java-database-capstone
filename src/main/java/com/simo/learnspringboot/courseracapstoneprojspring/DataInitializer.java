package com.simo.learnspringboot.courseracapstoneprojspring;

import com.simo.learnspringboot.courseracapstoneprojspring.model.*;
import com.simo.learnspringboot.courseracapstoneprojspring.repository.AppointmentRepository;
import com.simo.learnspringboot.courseracapstoneprojspring.repository.DoctorRepository;
import com.simo.learnspringboot.courseracapstoneprojspring.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;
    private final AppointmentRepository appointmentRepository;

    @Autowired
    public DataInitializer(DoctorRepository doctorRepository, PatientRepository patientRepository, AppointmentRepository appointmentRepository) {
        this.doctorRepository = doctorRepository;
        this.patientRepository = patientRepository;
        this.appointmentRepository = appointmentRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        // Check if data already exists to avoid duplication on every restart
        if (doctorRepository.count() == 0 && patientRepository.count() == 0) {
            System.out.println("No data found. Initializing mock data...");

            // Create Doctors
            Doctor drHouse = new Doctor("ghouse", "password123", "Gregory", "Diagnostician");
            drHouse.setLastName("House");
            Doctor drGrey = new Doctor("mgrey", "password123", "Meredith", "General Surgery");
            drGrey.setLastName("Grey");
            Doctor drStrange = new Doctor("sstrange", "password123", "Stephen", "Neurosurgery");
            drStrange.setLastName("Strange");

            doctorRepository.saveAll(List.of(drHouse, drGrey, drStrange));

            // Create Patients
            Patient patient1 = new Patient("jdoe", "pass", "John", "Doe", "email@gmail.com", "+1234567890");
            patient1.setDateOfBirth(LocalDate.of(1985, 5, 20));
            Patient patient2 = new Patient("jsmith", "pass", "Jane", "Smith", "jsmith@gmail.com", "+1987654321");
            patient2.setDateOfBirth(LocalDate.of(1992, 8, 15));
            Patient patient3 = new Patient("bwilly", "pass", "Bruce", "Willy", "bwilly@gmail.com", "+1122334455");
            patient3.setDateOfBirth(LocalDate.of(1978, 3, 10));

            patientRepository.saveAll(List.of(patient1, patient2, patient3));

            // Create Appointments
            Appointment app1 = new Appointment();
            app1.setDoctor(drHouse);
            app1.setPatient(patient1);
            app1.setAppointmentDateTime(LocalDateTime.now().plusDays(3).withHour(10).withMinute(30));
            app1.setReason("Severe headache and vision problems.");
            app1.setStatus(AppointmentStatus.SCHEDULED);

            Appointment app2 = new Appointment();
            app2.setDoctor(drHouse);
            app2.setPatient(patient2);
            app2.setAppointmentDateTime(LocalDateTime.now().plusDays(4).withHour(11).withMinute(0));
            app2.setReason("Follow-up on previous diagnosis.");
            app2.setStatus(AppointmentStatus.SCHEDULED);

            Appointment app3 = new Appointment();
            app3.setDoctor(drGrey);
            app3.setPatient(patient3);
            app3.setAppointmentDateTime(LocalDateTime.now().plusDays(5).withHour(9).withMinute(0));
            app3.setReason("Consultation for abdominal pain.");
            app3.setStatus(AppointmentStatus.COMPLETED);

            Appointment app4 = new Appointment();
            app4.setDoctor(drStrange);
            app4.setPatient(patient1);
            app4.setAppointmentDateTime(LocalDateTime.now().plusDays(1).withHour(14).withMinute(0));
            app4.setReason("Consultation for nerve pain.");
            app4.setStatus(AppointmentStatus.SCHEDULED);

            appointmentRepository.saveAll(List.of(app1, app2, app3, app4));

            System.out.println("Mock data initialization complete.");
        } else {
            System.out.println("Database already contains data. Skipping mock data initialization.");
        }
    }
}
