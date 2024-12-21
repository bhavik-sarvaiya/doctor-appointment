package com.example.doctorappointment.service;

import com.example.doctorappointment.model.Appointment;
import com.example.doctorappointment.model.Doctor;
import com.example.doctorappointment.model.Patient;
import com.example.doctorappointment.repository.AppointmentRepository;
import com.example.doctorappointment.repository.DoctorRepository;
import com.example.doctorappointment.repository.PatientRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AppointmentService {

    @Autowired
    private AppointmentRepository appointmentRepository;
    @Autowired
    private PatientRepository patientRepository;
    @Autowired
    private DoctorRepository doctorRepository;
    
    
    public List<Appointment> getAppointmentsByPatient(Integer patientId) {
        Patient patient = patientRepository.findById(patientId).orElse(null);
        return appointmentRepository.findByPatient(patient);
    }

    public List<Appointment> getAppointmentsByDoctor(Integer doctorId) {
        Doctor doctor = doctorRepository.findById(doctorId).orElse(null);
        return appointmentRepository.findByDoctor(doctor);
    }

    public Appointment createAppointment(Integer patientId, Integer doctorId, String appointmentTime) {
        Patient patient = patientRepository.findById(patientId).orElse(null);
        Doctor doctor = doctorRepository.findById(doctorId).orElse(null);
        
        Appointment appointment = new Appointment();
        appointment.setPatient(patient);
        appointment.setDoctor(doctor);
        appointment.setAppointmentTime(appointmentTime);

        return appointmentRepository.save(appointment);
    }

//    public Appointment createAppointment(Appointment appointment) {
//        return appointmentRepository.save(appointment);
//    }

    public List<Appointment> getAllAppointments() {
        return appointmentRepository.findAll();
    }

    //optional is a container object..it used to contain not null objects..by optional check the value present or not..if present than give the value by id nd not present than handle the NullPointerException..
    public Optional<Appointment> getAppointmentById(Integer id) {
        return appointmentRepository.findById(id);
    }

    public Appointment updateAppointment(Appointment appointment) {
        return appointmentRepository.save(appointment);
    }

    public void deleteAppointment(Integer id) {
        appointmentRepository.deleteById(id);
    }


}
