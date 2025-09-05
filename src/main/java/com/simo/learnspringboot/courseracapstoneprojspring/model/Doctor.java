package com.simo.learnspringboot.courseracapstoneprojspring.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Set;

@Entity
@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
@Table(name = "doctors")
public class Doctor extends BaseUser {
    @NotNull
    @Size(min = 3, max = 10)
    private String firstName;

    @Size(min = 3, max = 10)
    private String lastName;

    @NotNull
    private String specialization;

    @OneToMany(mappedBy = "doctor", cascade =  CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private Set<Appointment> appointments;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "doctor_availability", joinColumns = @JoinColumn(name = "doctor_id"))
    private Set<TimeSlot> availability;

    public Doctor(String username, String password, String firstName, String specialization) {
        super(username, password, "ROLE_DOCTOR"); // Automatically set the role
        this.firstName = firstName;
        this.specialization = specialization;
    }
}
