package com.simo.learnspringboot.courseracapstoneprojspring.repository;

import com.simo.learnspringboot.courseracapstoneprojspring.dto.DoctorPatientCountDTO;
import com.simo.learnspringboot.courseracapstoneprojspring.model.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, UUID> {

    /**
     * Finds the doctor(s) with the most unique patients for a given date range.
     * This query groups appointments by doctor, counts the distinct patients,
     * and orders the results to find the doctor with the highest count.
     */
    @Query("""
        SELECT new com.simo.learnspringboot.courseracapstoneprojspring.dto.DoctorPatientCountDTO(
            a.doctor.id,
            a.doctor.firstName,
            a.doctor.lastName,
            COUNT(DISTINCT a.patient.id) AS patientCount
        )
        FROM Appointment a
        WHERE a.appointmentDateTime BETWEEN :startDate AND :endDate
        GROUP BY a.doctor.id, a.doctor.firstName, a.doctor.lastName
        ORDER BY COUNT(DISTINCT a.patient.id) DESC
    """)
    List<DoctorPatientCountDTO> findTopDoctorsByPatientCount(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );
}