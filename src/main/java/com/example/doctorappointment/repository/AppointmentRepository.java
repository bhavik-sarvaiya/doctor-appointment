package com.example.doctorappointment.repository;


import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.example.doctorappointment.model.Appointment;
import com.example.doctorappointment.model.Doctor;
import com.example.doctorappointment.model.Patient;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Integer> {

	List<Appointment> findByPatient(Patient patient);

	List<Appointment> findByDoctor(Doctor doctor);
	
	Page<Appointment> findByPatient(Patient patient, Pageable pageable);
	
	Page<Appointment> findByDoctor(Doctor doctor, Pageable pageable);
	
//	@Modifying
//	@Query("UPDATE Appointment a SET a.doctor = :doctor WHERE a.id = :id")
//	void updateDoctor(@Param("id") Integer id, @Param("doctor") Doctor doctor);
	
	@Query("SELECT a FROM Appointment a WHERE a.patient.id = :patientId ORDER BY a.appointmentDate DESC")
	Page<Appointment> findByPatientId(@Param("patientId") Integer patientId, Pageable pageable);
	


	
	
}
