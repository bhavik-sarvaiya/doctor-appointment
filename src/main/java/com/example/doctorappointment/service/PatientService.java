package com.example.doctorappointment.service;

import com.example.doctorappointment.model.Patient;
import com.example.doctorappointment.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PatientService {

    @Autowired
    private PatientRepository patientRepository;

    public Patient createPatient(Patient patient) {
        return patientRepository.save(patient);
    }

    public List<Patient> getAllPatients() {
        return patientRepository.findAll();
    }

    public Optional<Patient> getPatientById(Integer id) {
        return patientRepository.findById(id);
    }
   
    public Patient updatePatient(Patient patient) {
        return patientRepository.save(patient);
    }

    public void deletePatient(Integer id) {
        patientRepository.deleteById(id);
    }

	public Patient findByNameAndPassword(String name, String password) {
		return patientRepository.findByNameAndPassword(name,password);
	}
	
	public Patient findByName(String name) {
		return patientRepository.findByName(name);
	}

		
}
