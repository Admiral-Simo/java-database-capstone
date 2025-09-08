package com.simo.learnspringboot.courseracapstoneprojspring.service;

import com.simo.learnspringboot.courseracapstoneprojspring.model.Appointment;
import com.simo.learnspringboot.courseracapstoneprojspring.model.AppointmentStatus;
import com.simo.learnspringboot.courseracapstoneprojspring.repository.AppointmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@Service
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;

    @Autowired
    public AppointmentService(AppointmentRepository appointmentRepository) {
        this.appointmentRepository = appointmentRepository;
    }

    public List<Appointment> findAppointmentsByDoctorId(UUID doctorId) {
        return appointmentRepository.findByDoctorId(doctorId);
    }

    @Transactional
    public void updateAppointmentStatus(UUID appointmentId, AppointmentStatus newStatus) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid appointment ID:" + appointmentId));
        appointment.setStatus(newStatus);
        appointmentRepository.save(appointment);
    }

    public List<Appointment> findAppointmentsForDoctorOnDate(UUID doctorId, LocalDate date) {
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.atTime(LocalTime.MAX);
        return appointmentRepository.findByDoctorIdAndAppointmentDateTimeBetween(doctorId, startOfDay, endOfDay);
    }
}
