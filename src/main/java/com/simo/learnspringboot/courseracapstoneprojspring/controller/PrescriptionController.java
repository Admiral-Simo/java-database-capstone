package com.simo.learnspringboot.courseracapstoneprojspring.controller;

import com.simo.learnspringboot.courseracapstoneprojspring.model.mongo.Prescription;
import com.simo.learnspringboot.courseracapstoneprojspring.service.PrescriptionService;
import com.simo.learnspringboot.courseracapstoneprojspring.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/prescriptions")
public class PrescriptionController {

    private final PrescriptionService prescriptionService;
    private final TokenService tokenService;

    @Autowired
    public PrescriptionController(PrescriptionService prescriptionService, TokenService tokenService) {
        this.prescriptionService = prescriptionService;
        this.tokenService = tokenService;
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<?> getPrescriptionsByPatientId(
            @PathVariable UUID patientId,
            @RequestHeader("Authorization") String token) {

        if (!isTokenValid(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or expired token.");
        }

        List<Prescription> prescriptions = prescriptionService.findPrescriptionsByPatientId(patientId);
        return ResponseEntity.ok(prescriptions);
    }

    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<?> getPrescriptionsByDoctorId(
            @PathVariable UUID doctorId,
            @RequestHeader("Authorization") String token) {

        if (!isTokenValid(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or expired token.");
        }

        List<Prescription> prescriptions = prescriptionService.findPrescriptionsByDoctorId(doctorId);
        return ResponseEntity.ok(prescriptions);
    }

    @PostMapping("/")
    public ResponseEntity<?> createPrescription(
            @RequestBody Prescription prescription,
            @RequestHeader("Authorization") String token) {

        if (!isTokenValid(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or expired token.");
        }

        try {
            Prescription createdPrescription = prescriptionService.createPrescription(prescription);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdPrescription);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error creating prescription: " + e.getMessage());
        }
    }

    private boolean isTokenValid(String token) {
        if (token == null || !token.startsWith("Bearer ")) {
            return false;
        }
        String authToken = token.substring(7);
        return tokenService.validateToken(authToken);
    }
}
