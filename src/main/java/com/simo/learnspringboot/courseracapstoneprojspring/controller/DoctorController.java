package com.simo.learnspringboot.courseracapstoneprojspring.controller;

import com.simo.learnspringboot.courseracapstoneprojspring.dto.LoginRequestDTO;
import com.simo.learnspringboot.courseracapstoneprojspring.dto.TimeSlotDTO;
import com.simo.learnspringboot.courseracapstoneprojspring.model.Appointment;
import com.simo.learnspringboot.courseracapstoneprojspring.model.AppointmentStatus;
import com.simo.learnspringboot.courseracapstoneprojspring.model.Doctor;
import com.simo.learnspringboot.courseracapstoneprojspring.repository.DoctorRepository;
import com.simo.learnspringboot.courseracapstoneprojspring.service.AppointmentService;
import com.simo.learnspringboot.courseracapstoneprojspring.service.DoctorService;
import com.simo.learnspringboot.courseracapstoneprojspring.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Controller
@RequestMapping("/doctor")
public class DoctorController {

    private final DoctorService doctorService;
    private final AppointmentService appointmentService;
    private final DoctorRepository doctorRepository;
    private final TokenService tokenService;

    @Autowired
    public DoctorController(DoctorService doctorService, AppointmentService appointmentService, DoctorRepository doctorRepository, TokenService tokenService) {
        this.doctorService = doctorService;
        this.appointmentService = appointmentService;
        this.doctorRepository = doctorRepository;
        this.tokenService = tokenService;
    }

    @PostMapping("/login")
    @ResponseBody
    public ResponseEntity<?> login(@RequestBody LoginRequestDTO loginRequest) {
        try {
            doctorService.validateLogin(loginRequest.username(), loginRequest.password());
            String token = tokenService.generateToken(loginRequest.username());
            return ResponseEntity.ok(Map.of("token", token));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

    @GetMapping("/dashboard")
    public String showDoctorDashboard(@RequestParam("id") UUID doctorId, Model model) {
        Optional<Doctor> doctorOpt = doctorRepository.findById(doctorId);
        if (doctorOpt.isEmpty()) {
            return "error";
        }

        Doctor doctor = doctorOpt.get();
        List<Appointment> appointments = appointmentService.findAppointmentsByDoctorId(doctorId);

        long upcomingCount = appointments.stream()
                .filter(a -> a.getStatus() == AppointmentStatus.SCHEDULED)
                .count();
        long completedCount = appointments.stream()
                .filter(a -> a.getStatus() == AppointmentStatus.COMPLETED)
                .count();
        long uniquePatientCount = appointments.stream()
                .map(a -> a.getPatient().getId())
                .distinct()
                .count();

        model.addAttribute("doctor", doctor);
        model.addAttribute("appointments", appointments);
        model.addAttribute("upcomingCount", upcomingCount);
        model.addAttribute("completedCount", completedCount);
        model.addAttribute("uniquePatientCount", uniquePatientCount);


        return "doctorDashboard";
    }

    @GetMapping("/{doctorId}/availability")
    @ResponseBody
    public ResponseEntity<?> getDoctorAvailability(
            @PathVariable UUID doctorId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestHeader("Authorization") String token) {

        String authToken = token.substring(7);
        if (!tokenService.validateToken(authToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or expired token.");
        }

        try {
            List<TimeSlotDTO> availableSlots = doctorService.getAvailableSlots(doctorId, date);
            if (availableSlots.isEmpty()) {
                return ResponseEntity.ok("No available slots for this day.");
            }
            return ResponseEntity.ok(availableSlots);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred.");
        }
    }
}

