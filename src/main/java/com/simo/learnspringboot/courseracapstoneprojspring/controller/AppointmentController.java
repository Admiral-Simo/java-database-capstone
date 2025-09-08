package com.simo.learnspringboot.courseracapstoneprojspring.controller;

import com.simo.learnspringboot.courseracapstoneprojspring.model.AppointmentStatus;
import com.simo.learnspringboot.courseracapstoneprojspring.service.AppointmentService;
import com.simo.learnspringboot.courseracapstoneprojspring.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Controller
@RequestMapping("/appointments")
public class AppointmentController {

    private final AppointmentService appointmentService;
    private final TokenService tokenService;

    @Autowired
    public AppointmentController(AppointmentService appointmentService, TokenService tokenService) {
        this.appointmentService = appointmentService;
        this.tokenService = tokenService;
    }

    @PostMapping("/{appointmentId}/update-status")
    public ResponseEntity<String> updateAppointmentStatus(
            @PathVariable UUID appointmentId,
            @RequestParam("status") AppointmentStatus status,
            @RequestHeader("Authorization") String token) {

        String authToken = token.substring(7); // Remove "Bearer " prefix
        if (!tokenService.validateToken(authToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or expired token.");
        }

        try {
            appointmentService.updateAppointmentStatus(appointmentId, status);
            return ResponseEntity.ok("Appointment status updated successfully.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred.");
        }
    }
}
