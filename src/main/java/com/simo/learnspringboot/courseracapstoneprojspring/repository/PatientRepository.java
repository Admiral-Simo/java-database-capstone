package com.simo.learnspringboot.courseracapstoneprojspring.repository;

import com.simo.learnspringboot.courseracapstoneprojspring.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PatientRepository extends JpaRepository<Patient, UUID> {
    Optional<Patient> findById(UUID uuid);
    Optional<Patient> findByUsername(String username);
}
