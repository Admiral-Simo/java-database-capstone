package com.simo.learnspringboot.courseracapstoneprojspring.controller;

import com.simo.learnspringboot.courseracapstoneprojspring.dto.TimeSlotDTO;
import com.simo.learnspringboot.courseracapstoneprojspring.model.Appointment;
import com.simo.learnspringboot.courseracapstoneprojspring.model.AppointmentStatus;
import com.simo.learnspringboot.courseracapstoneprojspring.model.Doctor;
import com.simo.learnspringboot.courseracapstoneprojspring.repository.DoctorRepository;
import com.simo.learnspringboot.courseracapstoneprojspring.service.DoctorService;
import com.simo.learnspringboot.courseracapstoneprojspring.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;


import java.text.DateFormat;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Controller
@RequestMapping("/doctor")
public class DoctorController {

    private final DoctorService doctorService;
    private final DoctorRepository doctorRepository; // For fetching doctor details
    private final TokenService tokenService; // For fetching doctor details

    public DoctorController(
            DoctorService doctorService,
            DoctorRepository doctorRepository,
            TokenService tokenService) {
        this.doctorService = doctorService;
        this.doctorRepository = doctorRepository;
        this.tokenService = tokenService;
    }

    @GetMapping("/dashboard")
    public String showDoctorDashboard(@RequestParam("id") UUID doctorId, Model model) {
        Optional<Doctor> doctorOpt = doctorRepository.findById(doctorId);
        if (doctorOpt.isEmpty()) {
            // Handle case where doctor is not found
            return "error"; // You should create an error.html page
        }

        Doctor doctor = doctorOpt.get();
        List<Appointment> appointments = doctorService.findAppointmentsByDoctorId(doctorId);

        // Calculate statistics
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

    @PostMapping("/appointments/update")
    public RedirectView updateAppointmentStatus(@RequestParam("appointmentId") UUID appointmentId,
                                                @RequestParam("status") AppointmentStatus status,
                                                @RequestParam("doctorId") UUID doctorId) {
        doctorService.updateAppointmentStatus(appointmentId, status);
        // Redirect back to the doctor's dashboard to see the change
        return new RedirectView("/doctor/dashboard?id=" + doctorId);
    }

    @GetMapping("/{doctorId}/availability")
    @ResponseBody
    public ResponseEntity<?> getDoctorAvailability(
            @PathVariable UUID doctorId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestHeader("Authorization") String token
    ) {
        String authToken = token.substring(7);
        if (!tokenService.validateToken(authToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        }

        try {
            // 3. Call Service to get availability
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

