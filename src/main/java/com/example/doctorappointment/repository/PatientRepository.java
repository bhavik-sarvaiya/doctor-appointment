package com.example.doctorappointment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.doctorappointment.model.Patient;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Integer> {
}
