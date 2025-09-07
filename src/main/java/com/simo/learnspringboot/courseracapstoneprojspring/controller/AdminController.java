package com.simo.learnspringboot.courseracapstoneprojspring.controller;

import com.simo.learnspringboot.courseracapstoneprojspring.model.Doctor;
import com.simo.learnspringboot.courseracapstoneprojspring.model.Patient;
import com.simo.learnspringboot.courseracapstoneprojspring.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final AdminService adminService;

    @Autowired
    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/dashboard")
    public String showAdminDashboard(Model model) {
        List<Doctor> doctors = adminService.findAllDoctors();
        List<Patient> patients = adminService.findAllPatients();

        model.addAttribute("doctors", doctors);
        model.addAttribute("patients", patients);
        model.addAttribute("doctorCount", doctors.size());
        model.addAttribute("patientCount", patients.size());

        return "adminDashboard";
    }
}
