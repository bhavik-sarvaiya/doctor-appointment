package com.example.doctorappointment.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.doctorappointment.model.Admin;
import com.example.doctorappointment.model.Appointment;
import com.example.doctorappointment.model.Doctor;
import com.example.doctorappointment.model.Patient;
import com.example.doctorappointment.service.AdminService;
import com.example.doctorappointment.service.AppointmentService;
import com.example.doctorappointment.service.DoctorService;
import com.example.doctorappointment.service.PatientService;

import jakarta.servlet.http.HttpSession;

@Controller
public class Login {

	@Autowired
	private DoctorService doctorService;

	@Autowired
	private PatientService patientService;

	@Autowired
	private AppointmentService appointmentService;

	@Autowired
	private AdminService adminService;

	@PostMapping("/login")
	public String login(@RequestParam(defaultValue = "0") int page, @ModelAttribute Doctor doctor,
			@ModelAttribute Patient patient, @ModelAttribute Admin admin, Model model, HttpSession session) {

		// Check if an admin is logged in
		Integer adminId = (Integer) session.getAttribute("adminId");
		if (adminId != null) {
			Optional<Admin> adminOptional = adminService.getAdminById(adminId);
			if (adminOptional.isPresent()) {
				return loadAdminDashboard(adminOptional.get(), model, session);
			}
		}

		// Check if doctor is logged in
		Integer userDId = (Integer) session.getAttribute("userDId");
		if (userDId != null) {
			Optional<Doctor> doctorOptional = doctorService.getDoctorById(userDId);
			if (doctorOptional.isPresent()) {
				return loadDoctorDashboard(doctorOptional.get(), page, model, session);
			}
		}

		// Check if patient is logged in
		Integer userId = (Integer) session.getAttribute("userId");
		if (userId != null) {
			Optional<Patient> patientOptional = patientService.getPatientById(userId);
			if (patientOptional.isPresent()) {
				return loadPatientDashboard(patientOptional.get(), page, model, session);
			}
		}

		// Authenticate admin
		Admin authenticatedAdmin = adminService.findByNameAndPassword(admin.getName(), admin.getPassword());
		if (authenticatedAdmin != null) {
			session.setAttribute("adminUsername", authenticatedAdmin.getName());
			session.setAttribute("adminId", authenticatedAdmin.getId());
			return loadAdminDashboard(authenticatedAdmin, model, session);
		}

		// Authenticate doctor
		Doctor authenticatedDoctor = doctorService.findByNameAndPassword(doctor.getName(), doctor.getPassword());
		if (authenticatedDoctor != null) {
			session.setAttribute("username", authenticatedDoctor.getName());
			session.setAttribute("userDId", authenticatedDoctor.getId());
			return loadDoctorDashboard(authenticatedDoctor, page, model, session);
		}

		// Authenticate patient
		Patient authenticatedPatient = patientService.findByNameAndPassword(patient.getName(), patient.getPassword());
		if (authenticatedPatient != null) {
			session.setAttribute("username", authenticatedPatient.getName());
			session.setAttribute("userId", authenticatedPatient.getId());
			return loadPatientDashboard(authenticatedPatient, page, model, session);
		}

		// Authentication failed
		model.addAttribute("message", "Register first or check user and password");
		return "welcome";
	}

	private String loadAdminDashboard(Admin admin, Model model, HttpSession session) {
//		model.addAttribute("admin", admin);
		 
		List<Patient> patients = patientService.getAllPatients();	
		
		model.addAttribute("admin", admin);
		model.addAttribute("patients", patients);
		return "admin";
	}

	private String loadDoctorDashboard(Doctor doctor, int page, Model model, HttpSession session) {
		Pageable pageable = PageRequest.of(page, 5, Sort.by(Sort.Order.desc("appointmentDate")));
		Page<Appointment> appointmentsPage = appointmentService.getAppointmentsByDoctoradPage(doctor.getId(), pageable);

		model.addAttribute("doctor", doctor);
		model.addAttribute("appointments", appointmentsPage.getContent());
		model.addAttribute("currentPage", page);
		model.addAttribute("totalPages", appointmentsPage.getTotalPages());
		model.addAttribute("totalAppointments", appointmentsPage.getTotalElements());

		return "dlogin";
	}

	private String loadPatientDashboard(Patient patient, int page, Model model, HttpSession session) {
		Pageable pageable = PageRequest.of(page, 5, Sort.by(Sort.Order.desc("appointmentDate")));
		Page<Appointment> appointmentsPage = appointmentService.getAppointmentsByPatientnadPage(patient.getId(),
				pageable);

		model.addAttribute("doctors", doctorService.getAllDoctors());
		model.addAttribute("appointments", appointmentsPage.getContent());
		model.addAttribute("currentPage", page);
		model.addAttribute("totalPages", appointmentsPage.getTotalPages());
		model.addAttribute("totalAppointments", appointmentsPage.getTotalElements());

		return "plogin";
	}
}
