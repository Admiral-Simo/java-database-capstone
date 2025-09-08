package com.simo.learnspringboot.courseracapstoneprojspring.repository;

import com.simo.learnspringboot.courseracapstoneprojspring.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PatientRepository extends JpaRepository<Patient, UUID> {
    Optional<Patient> findByUsername(String username);

    /**
     * Finds a patient by their email address.
     * @param email The email to search for.
     * @return An Optional containing the patient if found.
     */
    Optional<Patient> findByEmail(String email);

    /**
     * Finds a patient by their phone number.
     * @param phoneNumber The phone number to search for.
     * @return An Optional containing the patient if found.
     */
    Optional<Patient> findByPhoneNumber(String phoneNumber);

    /**
     * Finds a patient by their email address and phone number.
     * @param email The email to search for.
     * @param phoneNumber The phone number to search for.
     * @return An Optional containing the patient if found.
     */
    Optional<Patient> findByEmailAndPhoneNumber(String email, String phoneNumber);
}
