package com.simo.learnspringboot.courseracapstoneprojspring.service;

import com.simo.learnspringboot.courseracapstoneprojspring.dto.TimeSlotDTO;
import com.simo.learnspringboot.courseracapstoneprojspring.model.Appointment;
import com.simo.learnspringboot.courseracapstoneprojspring.model.AppointmentStatus;
import com.simo.learnspringboot.courseracapstoneprojspring.model.Doctor;
import com.simo.learnspringboot.courseracapstoneprojspring.model.TimeSlot;
import com.simo.learnspringboot.courseracapstoneprojspring.repository.AppointmentRepository;
import com.simo.learnspringboot.courseracapstoneprojspring.repository.DoctorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class DoctorService {

    private final AppointmentRepository appointmentRepository;
    private final DoctorRepository doctorRepository;


    @Autowired
    public DoctorService(AppointmentRepository appointmentRepository, DoctorRepository doctorRepository) {
        this.appointmentRepository = appointmentRepository;
        this.doctorRepository = doctorRepository;
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

    /**
     * Calculates the available appointment slots for a doctor on a specific date.
     *
     * @param doctorId The ID of the doctor.
     * @param date The date to check for availability.
     * @return A list of available time slots.
     */
    public List<TimeSlotDTO> getAvailableSlots(UUID doctorId, LocalDate date) {
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new IllegalArgumentException("Doctor not found with ID: " + doctorId));

        DayOfWeek dayOfWeek = date.getDayOfWeek();

        // Find the doctor's general availability for that day of the week
        Optional<TimeSlot> workingHours = doctor.getAvailability().stream()
                .filter(slot -> slot.getDayOfWeek() == dayOfWeek)
                .findFirst();

        if (workingHours.isEmpty()) {
            return new ArrayList<>(); // Doctor does not work on this day
        }

        // Get appointments already booked for that day
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.atTime(LocalTime.MAX);
        List<Appointment> bookedAppointments = appointmentRepository.findByDoctorIdAndAppointmentDateTimeBetween(doctorId, startOfDay, endOfDay);
        List<LocalTime> bookedTimes = bookedAppointments.stream()
                .map(appointment -> appointment.getAppointmentDateTime().toLocalTime())
                .collect(Collectors.toList());

        // Generate potential slots and filter out booked ones
        List<TimeSlotDTO> availableSlots = new ArrayList<>();
        TimeSlot daySlot = workingHours.get();
        LocalTime currentTime = daySlot.getStartTime();
        int slotDuration = 30; // Assuming 30-minute slots

        while (currentTime.isBefore(daySlot.getEndTime())) {
            if (!bookedTimes.contains(currentTime)) {
                availableSlots.add(new TimeSlotDTO(currentTime, currentTime.plusMinutes(slotDuration)));
            }
            currentTime = currentTime.plusMinutes(slotDuration);
        }

        return availableSlots;
    }
}
