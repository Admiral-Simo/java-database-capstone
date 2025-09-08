# Enhanced Application User Stories

This document outlines the user stories for the three main roles in the application: Admin, Patient, and Doctor. Each story includes a priority, estimated story points, and detailed acceptance criteria to ensure clarity for development.

## Admin User Stories

---

#### ADM-001: Admin Authentication

- **Priority:** High
- **Story Points:** 5
- **User Story:** As an Admin, I want to securely log in to the system, so that I can access the administrative dashboard and manage the application.
- **Acceptance Criteria:**
    - **Given** I am an admin on the main login page,
    - **When** I enter my correct admin username and password and click "Login",
    - **Then** I am successfully authenticated and redirected to the admin dashboard.
    - ---
    - **Given** I am an admin on the main login page,
    - **When** I enter an incorrect username or password,
    - **Then** I see an "Invalid credentials" error message and remain on the login page.

#### ADM-002: Manage User Accounts

- **Priority:** High
- **Story Points:** 8
- **User Story:** As an Admin, I want to view a list of all patient and doctor accounts, so that I can monitor and manage the user base.
- **Acceptance Criteria:**
    - **Given** I am logged in as an Admin and on the dashboard,
    - **When** I click on the "Manage Users" section,
    - **Then** I see a paginated table displaying all registered patients and doctors with key information (e.g., Name, Role, Registration Date).
    - ---
    - **Given** I am viewing the user list,
    - **When** I use the search bar to type a user's name,
    - **Then** the list is filtered in real-time to show matching results.

#### ADM-003: Create New Patient Account

- **Priority:** High
- **Story Points:** 3
- **User Story:** As an Admin, I want to manually create a new patient account, so that I can register a patient who is unable to do it themselves.
- **Acceptance Criteria:**
    - **Given** I am on the "Manage Users" page,
    - **When** I click "Add New Patient" and fill in all required fields (First Name, Last Name, DOB, etc.) with valid data,
    - **Then** a new patient account is created, and I am redirected back to the user list where the new patient appears.

#### ADM-004: Update Patient Information

- **Priority:** Medium
- **Story Points:** 3
- **User Story:** As an Admin, I want to update a patient's profile information, so that I can correct errors or update their details on their behalf.
- **Acceptance Criteria:**
    - **Given** I am on the "Manage Users" page,
    - **When** I select a patient and click "Edit", I am taken to their profile form.
    - **And When** I change their contact information and click "Save",
    - **Then** the changes are saved, and I see a "Profile updated successfully" confirmation message.

#### ADM-005: Bulk Patient Creation

- **Priority:** Medium
- **Story Points:** 8
- **User Story:** As an Admin, I want to upload a CSV file to create multiple patient accounts at once, so that I can efficiently onboard groups of new patients.
- **Acceptance Criteria:**
    - **Given** I am on the "Manage Users" page,
    - **When** I upload a correctly formatted CSV file with patient data,
    - **Then** all patients in the file are created in the system, and I see a success summary (e.g., "15 of 15 patients created successfully").
    - ---
    - **Given** I am on the "Manage Users" page,
    - **When** I upload a CSV file with formatting errors or missing required data,
    - **Then** the file is rejected, and an error message is displayed detailing the issues (e.g., "Error on row 5: Missing last name").

## Patient User Stories

---

#### PAT-001: Patient Authentication

- **Priority:** High
- **Story Points:** 5
- **User Story:** As a Patient, I want to securely log in to my account, so that I can access my personal health information and manage my appointments.
- **Acceptance Criteria:**
    - **Given** I am a registered patient on the login page,
    - **When** I enter my correct username and password,
    - **Then** I am logged in and redirected to my personal dashboard.

#### PAT-002: View Profile Data

- **Priority:** High
- **Story Points:** 2
- **User Story:** As a Patient, I want to view my personal profile data, so that I can verify my information is correct.
- **Acceptance Criteria:**
    - **Given** I am logged in,
    - **When** I navigate to my "Profile" page,
    - **Then** I can see all my personal details (Name, Date of Birth, Contact Info, etc.).

#### PAT-003: Edit Profile Data

- **Priority:** Medium
- **Story Points:** 3
- **User Story:** As a Patient, I want to be able to edit my profile data, so that I can keep my personal information up-to-date.
- **Acceptance Criteria:**
    - **Given** I am on my "Profile" page,
    - **When** I click the "Edit" button, update my phone number, and click "Save",
    - **Then** the new information is saved, and I see a confirmation message.

#### PAT-004: Book an Appointment

- **Priority:** High
- **Story Points:** 8
- **User Story:** As a Patient, I want to book an available appointment with a doctor, so that I can receive medical consultation.
- **Acceptance Criteria:**
    - **Given** I am logged in and on the "Book Appointment" page,
    - **When** I select a specialty (e.g., Cardiology),
    - **Then** I see a list of doctors for that specialty and their available time slots.
    - ---
    - **Given** I have selected a doctor and an available time slot,
    - **When** I confirm my booking,
    - **Then** the appointment is scheduled, and it appears in my "My Appointments" list.

#### PAT-005: View My Appointments

- **Priority:** High
- **Story Points:** 3
- **User Story:** As a Patient, I want to see a list of my upcoming and past appointments, so that I can keep track of my medical history and schedule.
- **Acceptance Criteria:**
    - **Given** I am logged in,
    - **When** I navigate to the "My Appointments" page,
    - **Then** I see two lists: one for upcoming appointments and one for past appointments, showing the doctor, date, and time for each.

## Doctor User Stories

---

#### DOC-001: Doctor Authentication

- **Priority:** High
- **Story Points:** 5
- **User Story:** As a Doctor, I want to securely log in to the system, so that I can access my professional dashboard.
- **Acceptance Criteria:**
    - **Given** I am a registered doctor on the login page,
    - **When** I enter my correct username and password,
    - **Then** I am logged in and redirected to my doctor dashboard.

#### DOC-002: View Profile Data

- **Priority:** High
- **Story Points:** 2
- **User Story:** As a Doctor, I want to view my professional profile data, so that I can ensure my specialty and qualifications are listed correctly.
- **Acceptance Criteria:**
    - **Given** I am logged in,
    - **When** I navigate to my "Profile" page,
    - **Then** I see all my professional details (Name, Specialization, etc.).

#### DOC-003: View My Appointments

- **Priority:** High
- **Story Points:** 3
- **User Story:** As a Doctor, I want to see a schedule of my appointments for the day, so that I can prepare for my consultations.
- **Acceptance Criteria:**
    - **Given** I am logged in and on my dashboard,
    - **When** I view the "Today's Schedule" section,
    - **Then** I see a chronological list of all my scheduled appointments for the current day, including patient names and appointment times.

#### DOC-004: Manage Appointment Status

- **Priority:** High
- **Story Points:** 5
- **User Story:** As a Doctor, I want to update the status of an appointment (e.g., to "Completed" or "Cancelled"), so that the system reflects the actual outcome.
- **Acceptance Criteria:**
    - **Given** I am viewing my list of appointments,
    - **When** I change the status of a `SCHEDULED` appointment to `COMPLETED` and save,
    - **Then** the appointment status is updated, and it moves from the "Upcoming" to the "Past" appointments list.
    - ---
    - **Given** I am viewing my list of appointments,
    - **When** I change the status of a `SCHEDULED` appointment to `CANCELLED` and save,
    - **Then** the appointment status is updated and clearly marked as cancelled.

#### DOC-005: View Patient Information for an Appointment

- **Priority:** High
- **Story Points:** 3
- **User Story:** As a Doctor, I want to view a patient's basic information before an appointment, so that I can be prepared for the consultation.
- **Acceptance Criteria:**
    - **Given** I am viewing my appointment schedule,
    - **When** I click on a specific appointment,
    - **Then** I can see relevant patient details, such as their name, age, and the reason for the visit.
