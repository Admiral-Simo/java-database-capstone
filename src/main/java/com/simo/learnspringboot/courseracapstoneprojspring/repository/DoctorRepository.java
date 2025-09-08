package com.simo.learnspringboot.courseracapstoneprojspring.repository;

import com.simo.learnspringboot.courseracapstoneprojspring.model.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, UUID> {
    Optional<Doctor> findByUsername(String username);
}
