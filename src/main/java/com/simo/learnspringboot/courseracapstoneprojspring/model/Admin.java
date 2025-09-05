package com.simo.learnspringboot.courseracapstoneprojspring.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Entity
@Data
@Table(name = "admins")
public class Admin {
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String username;
    private String password;
    private String fullName;
}
