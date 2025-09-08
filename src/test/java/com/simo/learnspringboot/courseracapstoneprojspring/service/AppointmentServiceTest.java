package com.simo.learnspringboot.courseracapstoneprojspring.service;

import com.simo.learnspringboot.courseracapstoneprojspring.model.Appointment;
import com.simo.learnspringboot.courseracapstoneprojspring.model.AppointmentStatus;
import com.simo.learnspringboot.courseracapstoneprojspring.repository.AppointmentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AppointmentServiceTest {
    @Mock
    private AppointmentRepository appointmentRepository;

    @InjectMocks
    private AppointmentService appointmentService;

    @Test
    public void findAppointmentsByDoctorId_ShouldReturnAppointments() {
        // Arrange
        UUID doctorId = UUID.randomUUID();
        Appointment appointment = new Appointment();
        List<Appointment> expectedAppointments = Collections.singletonList(appointment);

        when(appointmentRepository.findByDoctorId(doctorId)).thenReturn(expectedAppointments);

        // Act
        List<Appointment> actualAppointments = appointmentService.findAppointmentsByDoctorId(doctorId);

        // Assert
        assertThat(actualAppointments).isEqualTo(expectedAppointments);
        verify(appointmentRepository).findByDoctorId(doctorId);
    }

    @Test
    public void findAppointmentsByPatientId_ShouldReturnAppointments() {
        // Arrange
        UUID patientId = UUID.randomUUID();
        Appointment appointment = new Appointment();
        List<Appointment> expectedAppointments = Collections.singletonList(appointment);

        when(appointmentRepository.findByPatientId(patientId)).thenReturn(expectedAppointments);

        // Act
        List<Appointment> actualAppointments = appointmentService.findAppointmentsByPatientId(patientId);

        // Assert
        assertThat(actualAppointments).isEqualTo(expectedAppointments);
        verify(appointmentRepository).findByPatientId(patientId);
    }

    @Test
    public void updateAppointmentStatus_ShouldUpdateStatus() {
        // Arrange
        UUID appointmentId = UUID.randomUUID();
        AppointmentStatus currentStatus = AppointmentStatus.SCHEDULED;
        AppointmentStatus desiredStatus = AppointmentStatus.COMPLETED;

        Appointment appointment = new Appointment();
        appointment.setId(appointmentId);
        appointment.setReason("Some reason");
        appointment.setStatus(currentStatus);
        appointment.setAppointmentDateTime(LocalDateTime.now().plusDays(1));

        when(appointmentRepository.findById(appointmentId)).thenReturn(Optional.of(appointment));

        // Act
        appointmentService.updateAppointmentStatus(appointmentId, desiredStatus);

        ArgumentCaptor<Appointment> appointmentCaptor = ArgumentCaptor.forClass(Appointment.class);
        verify(appointmentRepository).save(appointmentCaptor.capture());
        Appointment savedAppointment = appointmentCaptor.getValue();

        // Assert
        assertThat(savedAppointment.getStatus()).isEqualTo(desiredStatus);

        verify(appointmentRepository).findById(appointmentId);
    }

    @Test
    public void updateAppointmentStatus_ShouldThrowException_WhenAppointmentNotFound() {
        // Arrange
        UUID appointmentId = UUID.randomUUID();
        when(appointmentRepository.findById(appointmentId)).thenReturn(Optional.empty());

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            appointmentService.updateAppointmentStatus(appointmentId, AppointmentStatus.COMPLETED);
        });

        // Assert
        assertThat(thrown).isNotNull();
        assertThat(thrown.getMessage()).isEqualTo("Invalid appointment ID:" + appointmentId);
        verify(appointmentRepository).findById(appointmentId);
        verify(appointmentRepository, never()).save(any(Appointment.class));

    }

    @Test
    public void findDoctorAppointmentOnDate_ShouldGetAppointmentBasedOnDate() {
        // Arrange
        UUID doctorId = UUID.randomUUID();
        LocalDate date = LocalDate.now();
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.atTime(LocalTime.MAX);

        Appointment appointment = new Appointment();
        List<Appointment> expectedAppointments = Collections.singletonList(appointment);

        when(appointmentRepository.findByDoctorIdAndAppointmentDateTimeBetween(doctorId, startOfDay, endOfDay))
                .thenReturn(expectedAppointments);

        // Act
        List<Appointment> actualAppointments = appointmentService.findAppointmentsForDoctorOnDate(doctorId, date);

        // Assert
        assertThat(actualAppointments).isEqualTo(expectedAppointments);
        verify(appointmentRepository).findByDoctorIdAndAppointmentDateTimeBetween(doctorId, startOfDay, endOfDay);
    }
}
