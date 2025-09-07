package com.simo.learnspringboot.courseracapstoneprojspring.controller;

import com.simo.learnspringboot.courseracapstoneprojspring.model.Doctor;
import com.simo.learnspringboot.courseracapstoneprojspring.repository.DoctorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Optional;
import java.util.UUID;

@Controller
public class HomeController {

    private final DoctorRepository doctorRepository;

    @Autowired
    public HomeController(DoctorRepository doctorRepository) {
        this.doctorRepository = doctorRepository;
    }

    @GetMapping("/")
    public String home(Model model) {
        // Find the first doctor to use for the demo link
        Optional<Doctor> firstDoctor = doctorRepository.findAll().stream().findFirst();

        // If a doctor exists, add their ID to the model
        if (firstDoctor.isPresent()) {
            model.addAttribute("demoDoctorId", firstDoctor.get().getId());
        } else {
            // Provide a fallback or handle the case where no doctors exist
            model.addAttribute("demoDoctorId", null);
        }

        return "index";
    }
}

