package com.example.doctorappointment.controller;

import com.example.doctorappointment.model.Patient;
import com.example.doctorappointment.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/patient")
public class PatientController {

    @Autowired
    private PatientService patientService;

    @GetMapping
    public String listPatients(Model model) {
        List<Patient> patients = patientService.getAllPatients();
        model.addAttribute("patients", patients);
        return "patient-list";  // Thymeleaf template to display patients
    }

    @GetMapping("/{id}")
    public String getPatientById(@PathVariable("id") Integer id, Model model) {
        Optional<Patient> patient = patientService.getPatientById(id);
        model.addAttribute("patient", patient);
        return "patient-details";  // Thymeleaf template to display patient details
    }
    
    @GetMapping("/pt")
    public List<Patient> getAllPatients() {
        return patientService.getAllPatients();
    }
    @PostMapping("/addpt")
    public Patient createPatient(@RequestBody Patient patient) {
        return patientService.createPatient(patient);
    }

    @PutMapping("/editpt/{id}")
    public Patient updatePatient(@PathVariable Integer id, @RequestBody Patient patient) {
        patient.setId(id);
        return patientService.updatePatient(patient);
    }

    @DeleteMapping("/delpt/{id}")
    public void deletePatient(@PathVariable Integer id) {
        patientService.deletePatient(id);
    }
}