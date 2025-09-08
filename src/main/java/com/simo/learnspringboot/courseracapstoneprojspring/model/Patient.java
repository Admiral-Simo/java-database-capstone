package com.simo.learnspringboot.courseracapstoneprojspring.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;
import java.util.Set;

@Entity
@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
@Table(name = "patients", uniqueConstraints = {
        @UniqueConstraint(columnNames = "email"),
        @UniqueConstraint(columnNames = "phoneNumber")
})
public class Patient extends BaseUser {
    @NotNull
    private String firstName;

    @NotNull
    private String lastName;

    @Past
    private LocalDate dateOfBirth;

    @Email
    @NotNull
    @Column(nullable = false, unique = true)
    private String email;

    @Size(min = 10, max = 15)
    @NotNull
    @Column(nullable = false, unique = true)
    private String phoneNumber;


    @OneToMany(mappedBy = "patient", cascade =  CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private Set<Appointment> appointments;

    public Patient(String username, String password, String firstName, String lastName, String email, String phoneNumber) {
        super(username, password, "ROLE_PATIENT"); // Automatically set the role
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }
}
