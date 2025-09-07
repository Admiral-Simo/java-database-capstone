package com.simo.learnspringboot.courseracapstoneprojspring.service;

import com.simo.learnspringboot.courseracapstoneprojspring.model.Appointment;
import com.simo.learnspringboot.courseracapstoneprojspring.model.AppointmentStatus;
import com.simo.learnspringboot.courseracapstoneprojspring.repository.AppointmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class DoctorService {

    private final AppointmentRepository appointmentRepository;

    @Autowired
    public DoctorService(AppointmentRepository appointmentRepository) {
        this.appointmentRepository = appointmentRepository;
    }

    public List<Appointment> findAppointmentsByDoctorId(UUID doctorId) {
        // Use the more efficient repository method
        return appointmentRepository.findByDoctorId(doctorId);
    }

    @Transactional // Ensures the operation is atomic
    public void updateAppointmentStatus(UUID appointmentId, AppointmentStatus newStatus) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid appointment ID:" + appointmentId));
        appointment.setStatus(newStatus);
        appointmentRepository.save(appointment);
    }
}

