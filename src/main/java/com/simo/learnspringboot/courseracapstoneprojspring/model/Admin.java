package com.simo.learnspringboot.courseracapstoneprojspring.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
@Table(name = "admins")
public class Admin extends BaseUser {
    @NotNull(message = "Full name cannot be null")
    private String fullName;

    public Admin(String username, String password, String fullName) {
        super(username, password, "ROLE_ADMIN"); // Automatically set the role
        this.fullName = fullName;
    }
}
