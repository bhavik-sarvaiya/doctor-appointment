package com.example.doctorappointment.controller;

import com.example.doctorappointment.model.Doctor;
import com.example.doctorappointment.service.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/doctor")
public class DoctorController {

    @Autowired
    private DoctorService doctorService;
    
    @GetMapping
    public String listDoctors(Model model) {
        List<Doctor> doctors = doctorService.getAllDoctors();
        model.addAttribute("doctors", doctors);
        return "doctor-list";  // Thymeleaf template to display doctors
    }

    @GetMapping("/{id}")
    public String getDoctorById(@PathVariable("id") Integer id, Model model) {
        Optional<Doctor> doctor = doctorService.getDoctorById(id);
        model.addAttribute("doctor", doctor);
        return "doctor-details";  // Thymeleaf template to display doctor details
    }

    @PostMapping("/adddr")
    public Doctor createDoctor(@RequestBody Doctor doctor) {
        return doctorService.createDoctor(doctor);
    }

    @GetMapping("/dr")
    public List<Doctor> getAllDoctors() {
        return doctorService.getAllDoctors();
    }

    @PutMapping("/editdr/{id}")
    public Doctor updateDoctor(@PathVariable Integer id, @RequestBody Doctor doctor) {
        doctor.setId(id);
        return doctorService.updateDoctor(doctor);
    }

    @DeleteMapping("/deldr/{id}")
    public void deleteDoctor(@PathVariable Integer id) {
        doctorService.deleteDoctor(id);
    }
}
