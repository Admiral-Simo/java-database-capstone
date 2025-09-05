package com.simo.learnspringboot.courseracapstoneprojspring.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@MappedSuperclass // Specifies this is a base class and its fields should be mapped to subclasses.
public abstract class BaseUser {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @NotNull
    @Size(min = 3, max = 50)
    @Column(unique = true, nullable = false)
    private String username;

    @NotNull
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY) // Good for APIs, prevents password from being sent in JSON responses.
    @Column(nullable = false)
    private String password;

    @NotNull
    @Column(nullable = false)
    private String role;

    protected BaseUser(String username, String password, String role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }


    // Use the ID for equals and hashCode to avoid issues with lazy loading.
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BaseUser baseUser = (BaseUser) o;
        return id != null && id.equals(baseUser.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}