package com.simo.learnspringboot.courseracapstoneprojspring.service;

import com.simo.learnspringboot.courseracapstoneprojspring.model.mongo.Prescription;
import com.simo.learnspringboot.courseracapstoneprojspring.repository.PrescriptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class PrescriptionService {

    private final PrescriptionRepository prescriptionRepository;

    @Autowired
    public PrescriptionService(PrescriptionRepository prescriptionRepository) {
        this.prescriptionRepository = prescriptionRepository;
    }

    public List<Prescription> findPrescriptionsByPatientId(UUID patientId) {
        return prescriptionRepository.findByPatientId(patientId);
    }

    public List<Prescription> findPrescriptionsByDoctorId(UUID doctorId) {
        return prescriptionRepository.findByDoctorId(doctorId);
    }

    public Prescription createPrescription(Prescription prescription) {
        // In a real application, you'd add validation and other business logic here
        return prescriptionRepository.save(prescription);
    }
}
