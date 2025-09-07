package com.simo.learnspringboot.courseracapstoneprojspring.service;

import com.simo.learnspringboot.courseracapstoneprojspring.model.Doctor;
import com.simo.learnspringboot.courseracapstoneprojspring.model.Patient;
import com.simo.learnspringboot.courseracapstoneprojspring.repository.DoctorRepository;
import com.simo.learnspringboot.courseracapstoneprojspring.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminService {

    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;

    @Autowired
    public AdminService(DoctorRepository doctorRepository, PatientRepository patientRepository) {
        this.doctorRepository = doctorRepository;
        this.patientRepository = patientRepository;
    }

    public List<Doctor> findAllDoctors() {
        return doctorRepository.findAll();
    }

    public List<Patient> findAllPatients() {
        return patientRepository.findAll();
    }
}
