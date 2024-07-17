package com.example.doctorappointment.controller;

import com.example.doctorappointment.model.Patient;
import com.example.doctorappointment.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/patient")
public class PatientController {

    @Autowired
    private PatientService patientService;

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