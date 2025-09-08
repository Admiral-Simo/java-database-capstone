package com.simo.learnspringboot.courseracapstoneprojspring.service;

import com.simo.learnspringboot.courseracapstoneprojspring.dto.TimeSlotDTO;
import com.simo.learnspringboot.courseracapstoneprojspring.model.Appointment;
import com.simo.learnspringboot.courseracapstoneprojspring.model.Doctor;
import com.simo.learnspringboot.courseracapstoneprojspring.model.TimeSlot;
import com.simo.learnspringboot.courseracapstoneprojspring.repository.DoctorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class DoctorService {

    private final DoctorRepository doctorRepository;
    private final AppointmentService appointmentService;

    @Autowired
    public DoctorService(DoctorRepository doctorRepository, AppointmentService appointmentService) {
        this.doctorRepository = doctorRepository;
        this.appointmentService = appointmentService;
    }

    public void validateLogin(String username, String password) {
        Doctor doctor = doctorRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Invalid username or password."));

        // In a real application, passwords should be hashed using a strong algorithm like BCrypt.
        // For this project, we'll use a direct comparison.
        if (!doctor.getPassword().equals(password)) {
            throw new IllegalArgumentException("Invalid username or password.");
        }
    }

    public List<TimeSlotDTO> getAvailableSlots(UUID doctorId, LocalDate date) {
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new IllegalArgumentException("Doctor not found with ID: " + doctorId));

        DayOfWeek dayOfWeek = date.getDayOfWeek();

        Optional<TimeSlot> workingHours = doctor.getAvailability().stream()
                .filter(slot -> slot.getDayOfWeek() == dayOfWeek)
                .findFirst();

        if (workingHours.isEmpty()) {
            return new ArrayList<>();
        }

        List<Appointment> bookedAppointments = appointmentService.findAppointmentsForDoctorOnDate(doctorId, date);
        List<LocalTime> bookedTimes = bookedAppointments.stream()
                .map(appointment -> appointment.getAppointmentDateTime().toLocalTime())
                .collect(Collectors.toList());

        List<TimeSlotDTO> availableSlots = new ArrayList<>();
        TimeSlot daySlot = workingHours.get();
        LocalTime currentTime = daySlot.getStartTime();
        int slotDuration = 30;

        while (currentTime.isBefore(daySlot.getEndTime())) {
            if (!bookedTimes.contains(currentTime)) {
                availableSlots.add(new TimeSlotDTO(currentTime, currentTime.plusMinutes(slotDuration)));
            }
            currentTime = currentTime.plusMinutes(slotDuration);
        }

        return availableSlots;
    }
}

