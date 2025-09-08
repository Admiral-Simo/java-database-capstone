# Schema Architecture

![Alt text](doc_images/architecture.png "Optional title text")

# MySQL Database Schema Design

This document outlines the relational database schema for the medical appointment application, designed for a MySQL environment. The schema is derived directly from the Java Persistence API (JPA) entities defined within the Spring Boot application.

It includes tables for managing users (admins, doctors, patients), appointments, and doctor availability. Note that the `Prescription` data, as defined in the application, is designed for a NoSQL database (MongoDB) and is therefore not part of this relational schema.

---

### Entity-Relationship Diagram (ERD)

```mermaid
erDiagram
    admins {
        BINARY(16) id PK
        VARCHAR(50) username "UNIQUE, NOT NULL"
        VARCHAR(255) password "NOT NULL"
        VARCHAR(255) role "NOT NULL"
        VARCHAR(255) full_name "NOT NULL"
    }

    patients {
        BINARY(16) id PK
        VARCHAR(50) username "UNIQUE, NOT NULL"
        VARCHAR(255) password "NOT NULL"
        VARCHAR(255) role "NOT NULL"
        VARCHAR(255) first_name "NOT NULL"
        VARCHAR(255) last_name "NOT NULL"
        DATE date_of_birth
    }

    doctors {
        BINARY(16) id PK
        VARCHAR(50) username "UNIQUE, NOT NULL"
        VARCHAR(255) password "NOT NULL"
        VARCHAR(255) role "NOT NULL"
        VARCHAR(255) first_name "NOT NULL"
        VARCHAR(255) last_name
        VARCHAR(255) specialization "NOT NULL"
    }

    appointments {
        BINARY(16) id PK
        DATETIME appointment_date_time "NOT NULL"
        VARCHAR(255) reason
        VARCHAR(255) status "NOT NULL"
        BINARY(16) patient_id FK "NOT NULL"
        BINARY(16) doctor_id FK "NOT NULL"
    }

    doctor_availability {
        BINARY(16) doctor_id FK
        VARCHAR(255) day_of_week
        TIME start_time
        TIME end_time
    }

    patients ||--o{ appointments : "schedules"
    doctors ||--o{ appointments : "has"
    doctors ||--|{ doctor_availability : "defines"
