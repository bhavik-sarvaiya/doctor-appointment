package com.example.doctorappointment.controller;

import com.example.doctorappointment.model.Appointment;
import com.example.doctorappointment.model.Doctor;
import com.example.doctorappointment.model.Patient;
import com.example.doctorappointment.service.AppointmentService;
import com.example.doctorappointment.service.DoctorService;
import com.example.doctorappointment.service.PatientService;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Controller
@RequestMapping("/appointment")
public class AppointmentController {

	@Autowired
	private AppointmentService appointmentService;
	@Autowired
	private PatientService patientService;
	@Autowired
	private DoctorService doctorService;
	
//	@GetMapping("/appointments/{userId}")
//	public String showAppointments(Model model, HttpSession session, @RequestParam(defaultValue = "0") int page, Patient patient) {
//		System.out.println("in get appoinments");
//		String username = (String) session.getAttribute("username");
//		System.out.println("username :" +username);
//		Integer userId = (Integer) session.getAttribute("userId");
//		System.out.println("userId : "+userId);
//
//	    Pageable pageable = PageRequest.of(page, 5, Sort.by(Sort.Order.desc("appointmentDate")));
//        Page<Appointment> appointmentsPage = appointmentService.getAppointmentsByPatientnadPage(userId, pageable);
//	    List<Doctor> doctors = doctorService.getAllDoctors();
//	    
//	    model.addAttribute("doctors", doctors);
//        model.addAttribute("appointments", appointmentsPage.getContent());
//        model.addAttribute("currentPage", page);
//        model.addAttribute("totalPages", appointmentsPage.getTotalPages());
//        model.addAttribute("totalAppointments", appointmentsPage.getTotalElements());
//        
//        Patient patient2 = patientService.findByName(username);
//        session.setAttribute("username", patient2.getName());
//        session.setAttribute("userId", patient2.getId());
//
//		model.addAttribute("username", patient2.getName());
//		model.addAttribute("userId", patient2.getId());
//	    
//       return "plogin"; // Make sure this is the correct template name
//	}
	
	@GetMapping("/appointments/{userId}")
	public String showAppointments(Model model, HttpSession session,
	                                @RequestParam(defaultValue = "0") int page) {
	    
	    System.out.println("in get appointments");

	    String username = (String) session.getAttribute("username");
	    System.out.println("username : " + username);
	    
	    Integer userId = (Integer) session.getAttribute("userId");
	    System.out.println("userId : " + userId);

	    // Check if patient exists
	    Patient patient = patientService.findByName(username);
	    
	    if (patient != null) {
	        System.out.println("Patient Login");

	        Pageable pageable = PageRequest.of(page, 5, Sort.by(Sort.Order.desc("appointmentDate")));
	        Page<Appointment> appointmentsPage = appointmentService.getAppointmentsByPatientnadPage(userId, pageable);
	        List<Doctor> doctors = doctorService.getAllDoctors();

	        session.setAttribute("username", patient.getName());
	        session.setAttribute("userId", patient.getId());

	        model.addAttribute("username", patient.getName());
	        model.addAttribute("userId", patient.getId());
	        model.addAttribute("doctors", doctors);
	        model.addAttribute("appointments", appointmentsPage.getContent());
	        model.addAttribute("currentPage", page);
	        model.addAttribute("totalPages", appointmentsPage.getTotalPages());
	        model.addAttribute("totalAppointments", appointmentsPage.getTotalElements());

	        return "plogin";   // Patient Login Page
	    }

	    // If patient not found, Check Doctor
	    Doctor doctor = doctorService.findByName(username);

	    if (doctor != null) {
	        System.out.println("Doctor Login");

	        Pageable pageable = PageRequest.of(page, 5, Sort.by(Sort.Order.desc("appointmentDate")));
	        Page<Appointment> appointmentsPage = appointmentService.getAppointmentsByDoctoradPage(userId, pageable);
//	        List<Doctor> doctors = doctorService.getAllDoctors();

	        session.setAttribute("username", doctor.getName());
	        session.setAttribute("userId", doctor.getId());

	        model.addAttribute("username", doctor.getName());
	        model.addAttribute("userId", doctor.getId());
//	        model.addAttribute("doctors", doctors);
	        model.addAttribute("appointments", appointmentsPage.getContent());
	        model.addAttribute("currentPage", page);
	        model.addAttribute("totalPages", appointmentsPage.getTotalPages());
	        model.addAttribute("totalAppointments", appointmentsPage.getTotalElements());
	        
	        return "dlogin";  // Doctor Login Page
	    }

	    // If neither patient nor doctor found => Admin or Error
	    System.out.println("User not found");
	    return "redirect:/welcome"; 
	}



	@GetMapping("/create")
	public String createAppointmentForm(Model model, HttpSession session, @ModelAttribute Patient patient) {
		System.out.println("Loading get createAppointmentForm");
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
		model.addAttribute("doctors", doctors);
		
		return "appointments";
	}

	@GetMapping("/{page}")
	public String listAppointments(Model model, 
			@PathVariable("page") int page, 
			HttpSession session) {
		System.out.println("Loading createAppointmentForm in appointmentsDtl");

		
		String username1 = (String) session.getAttribute("username");
		System.out.println(username1);
		List<Doctor> doctors = doctorService.getAllDoctors();
		System.out.println("doctors :" + doctors);
		
		if (Objects.nonNull(patientService.findByName(username1))) {
			model.addAttribute("doctors", doctors);
			System.out.println("list app in patient");
			
			Pageable pageable = PageRequest.of(page, 5, Sort.by(Sort.Order.desc("appointmentDate")));
			Integer userId = (Integer) session.getAttribute("userId");
			Page<Appointment> appointmentsPage = appointmentService.getAppointmentsByPatientnadPage(userId, pageable);
			model.addAttribute("appointments", appointmentsPage.getContent()); // Current page data
			System.out.println("appointments" + appointmentsPage.getContent());
			model.addAttribute("currentPage", page); // Current page number
			System.out.println("currentPage : " + page);
			model.addAttribute("totalPages", appointmentsPage.getTotalPages()); // Total pages
			System.out.println("totalPages : " + appointmentsPage.getTotalPages());
			model.addAttribute("totalAppointments", appointmentsPage.getTotalElements()); // Total appointments
			System.out.println("totalAppointments : " + appointmentsPage.getTotalElements());
			return "plogin :: #table-container"; // Return the table fragment
		} else if (Objects.nonNull(doctorService.findByName(username1))) {
			model.addAttribute("doctors", doctors);
			System.out.println("list app in doctor");
			Pageable pageable = PageRequest.of(page, 5, Sort.by(Sort.Order.desc("appointmentDate")));
			Integer userId = (Integer) session.getAttribute("userId");
			Page<Appointment> appointmentsPage = appointmentService.getAppointmentsByDoctoradPage(userId, pageable);
			model.addAttribute("appointments", appointmentsPage.getContent()); // Current page data
			System.out.println("appointments" + appointmentsPage.getContent());
			model.addAttribute("currentPage", page); // Current page number
			System.out.println("currentPage : " + page);
			model.addAttribute("totalPages", appointmentsPage.getTotalPages()); // Total pages
			System.out.println("totalPages : " + appointmentsPage.getTotalPages());
			model.addAttribute("totalAppointments", appointmentsPage.getTotalElements()); // Total appointments
			System.out.println("totalAppointments : " + appointmentsPage.getTotalElements());

			return "dlogin :: #table-container";
		} else {
			return "welcome";
		}
	}

	@PostMapping("/create")
	public String createAppointment(@RequestParam Integer patientId, @RequestParam Integer doctorId,
			@RequestParam String appointmentSlot, @RequestParam LocalDate appointmentDate, HttpSession session,
			@RequestParam(defaultValue = "0") int page, Model model) {
		System.out.println("Loading post createAppointmentForm");
		appointmentService.createAppointment(patientId, doctorId, appointmentDate, appointmentSlot);
		Integer userId = (Integer) session.getAttribute("userId");
		String userName = (String) session.getAttribute("username");
		System.out.println(userName);
		model.addAttribute("username", userName);
		Pageable pageable = PageRequest.of(page, 5, Sort.by(Sort.Order.desc("appointmentDate")));
		Page<Appointment> appointmentsPage = appointmentService.getAppointmentsByPatientnadPage(userId, pageable);
		model.addAttribute("appointments", appointmentsPage.getContent()); // Current page data
		System.out.println("appointments" + appointmentsPage.getContent());
		model.addAttribute("currentPage", page); // Current page number
		System.out.println("currentPage : " + page);
		model.addAttribute("totalPages", appointmentsPage.getTotalPages()); // Total pages
		System.out.println("totalPages : " + appointmentsPage.getTotalPages());
		model.addAttribute("totalAppointments", appointmentsPage.getTotalElements()); // Total appointments
		System.out.println("totalAppointments : " + appointmentsPage.getTotalElements());
		List<Doctor> doctors = doctorService.getAllDoctors();
		model.addAttribute("doctors", doctors); // Total appointments

		return "plogin";
	}

	// Get all appointments
	@GetMapping("/ap")
	public List<Appointment> getAllAppointments() {
		return appointmentService.getAllAppointments();
	}
	
	@PutMapping("/updateap/{id}")
	public String updateAppointment(
	        @PathVariable Integer id,
	        @RequestParam("doctorId") Integer doctorId,
	        @RequestParam("appointmentSlot") String appointmentSlot,
	        @RequestParam("appointmentDate") LocalDate appointmentDate,
	        @RequestParam(defaultValue = "0") int page,
	        Model model, HttpSession session) {

	    System.out.println("Updating appointment with ID: " + id);
	    System.out.println("Doctor ID: " + doctorId);
	    System.out.println("Slot: " + appointmentSlot);
	    System.out.println("Date: " + appointmentDate);
	    
	    // 🔹 Print all appointments before update
	    List<Appointment> allAppointmentsBefore = appointmentService.getAllAppointments();
	    for (Appointment app : allAppointmentsBefore) {
	        System.out.println("Before Update -> ID: " + app.getId() + ", Doctor: " + 
	                          (app.getDoctor() != null ? app.getDoctor().getName() : "Not Assigned"));
	    }
	    // Get the existing appointment
	    Optional<Appointment> existingAppointmentOpt = appointmentService.getAppointmentById(id);
	    if (existingAppointmentOpt.isEmpty()) {
	        return "error";  
	    }
	    Appointment existingAppointment = existingAppointmentOpt.get();
	    // Preserve patient reference
	    Patient patient = existingAppointment.getPatient();
	    if (patient == null) {
	        return "error"; 
	    }

	    // Find and assign the correct doctor
	    Optional<Doctor> optionalDoctor = doctorService.getDoctorById(doctorId);
	    if (optionalDoctor.isPresent()) {
	        existingAppointment.setDoctor(optionalDoctor.get());
	    } else {
	        return "error"; 
	    }

	    // Update other appointment details
	    existingAppointment.setAppointmentSlot(appointmentSlot);
	    existingAppointment.setAppointmentDate(appointmentDate);

	    // 🔥 IMPORTANT: Save only the updated appointment, not all appointments
	    appointmentService.updateAppointment(existingAppointment);

	    System.out.println("Appointment updated successfully!");

	    // 🔹 Print all appointments after update
	    List<Appointment> allAppointmentsAfter = appointmentService.getAllAppointments();
	    for (Appointment app : allAppointmentsAfter) {
	        System.out.println("After Update -> ID: " + app.getId() + ", Doctor: " + 
	                          (app.getDoctor() != null ? app.getDoctor().getName() : "Not Assigned"));
	    }

	    Integer userId = (Integer) session.getAttribute("userId");
	    if (userId == null) {
	        userId = existingAppointment.getPatient().getId(); // Ensure correct patient ID
	        session.setAttribute("userId", userId);
	        
	    }
	    System.out.println("userId : "+ userId);
		Pageable pageable = PageRequest.of(page, 5, Sort.by(Sort.Order.desc("appointmentDate")));
		System.out.println("pageable : " +pageable);
		Page<Appointment> appointmentsPage = appointmentService.getAppointmentsByPatientnadPage(userId, pageable);
		System.out.println("appointmentsPage : " +appointmentsPage);
		model.addAttribute("appointments", appointmentsPage.getContent()); // Current page data
		System.out.println("appointments" + appointmentsPage.getContent());
		model.addAttribute("currentPage", page); // Current page number
		System.out.println("currentPage : " + page);
		model.addAttribute("totalPages", appointmentsPage.getTotalPages()); // Total pages
		System.out.println("totalPages : " + appointmentsPage.getTotalPages());
		model.addAttribute("totalAppointments", appointmentsPage.getTotalElements()); // Total appointments
		System.out.println("totalAppointments : " + appointmentsPage.getTotalElements());

	    // Add doctors list to model
	    List<Doctor> doctors = doctorService.getAllDoctors();
	    model.addAttribute("doctors", doctors);
		// Update session attributes
		session.setAttribute("username", existingAppointment.getPatient().getName());
		session.setAttribute("userId", existingAppointment.getPatient().getId());
		System.out.println("PId :" + existingAppointment.getPatient().getId());
		System.out.println("PName :" + existingAppointment.getPatient().getName());
		
		// Add attributes for reloading page
		model.addAttribute("username", existingAppointment.getPatient().getName());
		model.addAttribute("userId", existingAppointment.getId());
		
		return "plogin";
	}


	// Delete appointment
	@DeleteMapping("/deleteap/{id}")
	public String deleteAppointment(@PathVariable Integer id, 
			@RequestParam(defaultValue = "0") int page, 
			HttpSession session, 
			Model model,  
			RedirectAttributes redirectAttributes) {
		
		System.out.println("Deleting appointment with ID: " + id);
		
		Pageable pageable = PageRequest.of(page, 5, Sort.by(Sort.Order.desc("appointmentDate")));
		Integer userId = (Integer) session.getAttribute("userId");
		System.out.println("userId : " + userId);
		String userName = (String) session.getAttribute("username");
		System.out.println("username : " + userName);
		Page<Appointment> appointmentsPage = appointmentService.getAppointmentsByDoctoradPage(userId, pageable);
		model.addAttribute("appointments", appointmentsPage.getContent()); // Current page data
		System.out.println("appointments" + appointmentsPage.getContent());
		model.addAttribute("currentPage", page); // Current page number
		System.out.println("currentPage : " + page);
		model.addAttribute("totalPages", appointmentsPage.getTotalPages()); // Total pages
		System.out.println("totalPages : " + appointmentsPage.getTotalPages());
		model.addAttribute("totalAppointments", appointmentsPage.getTotalElements()); // Total appointments
		System.out.println("totalAppointments : " + appointmentsPage.getTotalElements());
		
		appointmentService.deleteAppointment(id);
		redirectAttributes.addFlashAttribute("message", "Appointment deleted successfully!");
		System.out.println("in controller del");
		
		  // Determine user type based on username
	    if (patientService.findByName(userName) != null) {
	        return "plogin";
	    } else if (doctorService.findByName(userName) != null) {
	        return "dlogin";
	    } else {
	        return "redirect:/"; // fallback in case user is not found
	    }
	}
	
	@GetMapping("/dateFilter")
	public String getAppointmentsByDateAndSlot(
	        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
	        @RequestParam(required = false) String slot,
	        @RequestParam(defaultValue = "0") int page,
	        HttpSession session,
	        Model model) {

	    Integer userId = (Integer) session.getAttribute("userId");
	    Pageable pageable = PageRequest.of(page, 5, Sort.by(Sort.Order.desc("appointmentDate")));
	    Page<Appointment> appointmentsPage;

	    if (date != null && slot != null && !slot.isEmpty()) {
	        appointmentsPage = appointmentService.findByDoctorIdAndDateAndSlot(userId, date, slot, pageable);
	    } else if (date != null) {
	        appointmentsPage = appointmentService.getAppointmentsByDate(userId, date, pageable);
	    } else if (slot != null && !slot.isEmpty()) {
	        appointmentsPage = appointmentService.getAppointmentsBySlot(userId, slot, pageable);
	    } else {
	        appointmentsPage = appointmentService.getAppointmentsByDoctoradPage(userId, pageable);
	    }
	    
	    model.addAttribute("appointments", appointmentsPage.getContent());
	    model.addAttribute("currentPage", page);
	    model.addAttribute("totalPages", appointmentsPage.getTotalPages());
	    model.addAttribute("totalAppointments", appointmentsPage.getTotalElements());
	    model.addAttribute("filterDate", date);
	    model.addAttribute("filterSlot", slot);

	    return "dlogin";
//	    return "appointment/dateFilter";
	}
	
	@GetMapping("/dateFilter/{page}")
	public String listAppointmentsAfterFilter(Model model, 
			@PathVariable("page") int page, 
			HttpSession session,  
			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
	        @RequestParam(value = "slot", required = false) String slot) {
		System.out.println("Loading createAppointmentForm in appointmentsDtl");

		System.out.println("date filter:" + date);
		System.out.println("slot filter:" + slot);
		String username1 = (String) session.getAttribute("username");
		System.out.println(username1);
		List<Doctor> doctors = doctorService.getAllDoctors();
		System.out.println("doctors :" + doctors);
		
			model.addAttribute("doctors", doctors);
			System.out.println("list app in doctor");
			Pageable pageable = PageRequest.of(page, 5, Sort.by(Sort.Order.desc("appointmentDate")));
			Integer userId = (Integer) session.getAttribute("userId");
			Page<Appointment> appointmentsPage = appointmentService.getAppointmentsByDoctoradPage(userId, pageable);
			model.addAttribute("appointments", appointmentsPage.getContent()); // Current page data
			System.out.println("appointments" + appointmentsPage.getContent());
			model.addAttribute("currentPage", page); // Current page number
			System.out.println("currentPage : " + page);
			model.addAttribute("totalPages", appointmentsPage.getTotalPages()); // Total pages
			System.out.println("totalPages : " + appointmentsPage.getTotalPages());
			model.addAttribute("totalAppointments", appointmentsPage.getTotalElements()); // Total appointments
			System.out.println("totalAppointments : " + appointmentsPage.getTotalElements());

			return "dlogin :: #table-container";
		}

}


