package com.example.doctorappointment.controller;

import com.example.doctorappointment.model.Appointment;
import com.example.doctorappointment.model.Doctor;
import com.example.doctorappointment.model.Patient;
import com.example.doctorappointment.service.AppointmentService;
import com.example.doctorappointment.service.DoctorService;
import com.example.doctorappointment.service.PatientService;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/appointment")
public class AppointmentController {

	@Autowired
	private AppointmentService appointmentService;
	@Autowired
	private PatientService patientService;
	@Autowired
	private DoctorService doctorService;

	@GetMapping("/create")
	public String createAppointmentForm(Model model, HttpSession session, @ModelAttribute Patient patient) {
//		System.out.println("Loading createAppointmentForm");
		// Ensure no null values
		String username = (String) session.getAttribute("username");
		 Integer userId = (Integer) session.getAttribute("userId");
		
		System.out.println(session.getAttribute("username"));
		System.out.println(session.getAttribute("userId"));
		List<Patient> patients = patientService.getAllPatients();
		List<Doctor> doctors = doctorService.getAllDoctors();

		if (patients == null) {
			patients = new ArrayList<>();
		}

		if (doctors == null) {
			doctors = new ArrayList<>();
		}

		model.addAttribute("username", username);
		model.addAttribute("userId", userId);
		model.addAttribute("doctors", doctorService.getAllDoctors());

//		System.out.println(model.addAttribute("patients", patientService.getAllPatients()));
		return "appointments";
	}

	@GetMapping("/{patientId}")
	public String listAppointments(Model model, @PathVariable Integer patientId, HttpSession session) {
		System.out.println("Loading createAppointmentForm in appointmentsDtl");
		System.out.println(patientId);
		List<Appointment> appointments = appointmentService.getAppointmentsByPatient(patientId);
		String username = (String) session.getAttribute("username");
		model.addAttribute("username", username);
		model.addAttribute("appointments", appointments);
		return "appointmentsDtl"; // Thymeleaf template to display appointments
	}

	@PostMapping("/create")
	public String createAppointment(@RequestParam Integer patientId, @RequestParam Integer doctorId,
			@RequestParam String appointmentTime, HttpSession session) {
		appointmentService.createAppointment(patientId, doctorId, appointmentTime);
		Integer userId = (Integer) session.getAttribute("userId");
	
		return "redirect:/appointment/" + userId;
	}

	// Create a new appointment
//    @PostMapping("/addap")
//    public Appointment createAppointment(@RequestBody Appointment appointment) {
//        return appointmentService.createAppointment(appointment);
//    }

	// Get all appointments
	@GetMapping("/ap")
	public List<Appointment> getAllAppointments() {
		return appointmentService.getAllAppointments();
	}

	// Update appointment
	@PutMapping("/updateap/{id}")
	public Appointment updateAppointment(@PathVariable Integer id, @RequestBody Appointment appointment) {
		appointment.setId(id);
		return appointmentService.updateAppointment(appointment);
	}

	// Delete appointment
	@DeleteMapping("/deleteap/{id}")
	public void deleteAppointment(@PathVariable Integer id) {
		appointmentService.deleteAppointment(id);
	}
}
