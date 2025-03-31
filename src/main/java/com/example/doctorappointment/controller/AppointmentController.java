package com.example.doctorappointment.controller;

import com.example.doctorappointment.model.Appointment;
import com.example.doctorappointment.model.Doctor;
import com.example.doctorappointment.model.Patient;
import com.example.doctorappointment.repository.AppointmentRepository;
import com.example.doctorappointment.service.AppointmentService;
import com.example.doctorappointment.service.DoctorService;
import com.example.doctorappointment.service.PatientService;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.data.domain.Pageable;

import java.net.http.HttpClient.Redirect;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
	
	@GetMapping("/appointments")
	public String showAppointments(Model model, HttpSession session, @RequestParam(defaultValue = "0") int page, Patient patient) {
		System.out.println("in get appoinments");
		String username = (String) session.getAttribute("username");
		System.out.println("username :" +username);
		Integer userId = (Integer) session.getAttribute("userId");
		System.out.println("userId : "+userId);
	    Optional<Appointment> appointments = appointmentService.getAppointmentById(userId);
	    Pageable pageable = PageRequest.of(page, 5, Sort.by(Sort.Order.desc("appointmentDate")));
        Page<Appointment> appointmentsPage = appointmentService.getAppointmentsByPatientnadPage(userId, pageable);
	    List<Doctor> doctors = doctorService.getAllDoctors();
	    
	    model.addAttribute("doctors", doctors);
        model.addAttribute("appointments", appointmentsPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", appointmentsPage.getTotalPages());
        model.addAttribute("totalAppointments", appointmentsPage.getTotalElements());
        
        Patient patient2 = patientService.findByName(username);
        session.setAttribute("username", patient2.getName());
        session.setAttribute("userDId", patient2.getId());

		model.addAttribute("username", patient2.getName());
		model.addAttribute("userId", patient2.getId());
	    
        return "plogin"; // Make sure this is the correct template name
	}


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
		model.addAttribute("doctors", doctors);
		System.out.println(model.addAttribute("doctors", doctorService.getAllDoctors()));

//		System.out.println(model.addAttribute("patients", patientService.getAllPatients()));
		return "appointments";
	}

	@GetMapping("/{page}")
	public String listAppointments(Model model, @PathVariable("page") int page, HttpSession session, Patient patient,
			Doctor doctor) {
		System.out.println("Loading createAppointmentForm in appointmentsDtl");
		String username1 = (String) session.getAttribute("username");
		System.out.println(username1);
		List<Doctor> doctors = doctorService.getAllDoctors();
		System.out.println("doctors :" + doctors);

		
		if (Objects.nonNull(patientService.findByName(username1))) {
			model.addAttribute("doctors", doctors);
			System.out.println("list app in patient");
			Pageable pageable = (Pageable) PageRequest.of(page, 5, Sort.by(Sort.Order.desc("appointmentDate")));
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
			return "appointmentTable"; // Return the table fragment
		} else if (Objects.nonNull(doctorService.findByName(username1))) {
			model.addAttribute("doctors", doctors);
			System.out.println("list app in doctor");
			Pageable pageable = (Pageable) PageRequest.of(page, 5, Sort.by(Sort.Order.desc("appointmentDate")));
			Integer userId = (Integer) session.getAttribute("userDId");
			Page<Appointment> appointmentsPage = appointmentService.getAppointmentsByDoctoradPage(userId, pageable);
			model.addAttribute("appointments", appointmentsPage.getContent()); // Current page data
			System.out.println("appointments" + appointmentsPage.getContent());
			model.addAttribute("currentPage", page); // Current page number
			System.out.println("currentPage : " + page);
			model.addAttribute("totalPages", appointmentsPage.getTotalPages()); // Total pages
			System.out.println("totalPages : " + appointmentsPage.getTotalPages());
			model.addAttribute("totalAppointments", appointmentsPage.getTotalElements()); // Total appointments
			System.out.println("totalAppointments : " + appointmentsPage.getTotalElements());
			return "appointmentsDtl"; // Return the table fragment
		} else {
			return "welcome";
		}
	}

	@PostMapping("/create")
	public String createAppointment(@RequestParam Integer patientId, @RequestParam Integer doctorId, Patient patient,
			@RequestParam String appointmentSlot, @RequestParam LocalDate appointmentDate, HttpSession session,
			@RequestParam(defaultValue = "0") int page, Model model) {
		appointmentService.createAppointment(patientId, doctorId, appointmentDate, appointmentSlot);
		Integer userId = (Integer) session.getAttribute("userId");
		String userName = (String) session.getAttribute("username");
		System.out.println(userName);
		model.addAttribute("username", userName);
		Pageable pageable = (Pageable) PageRequest.of(page, 5, Sort.by(Sort.Order.desc("appointmentDate")));
		Page<Appointment> appointmentsPage = appointmentService.getAppointmentsByPatientnadPage(userId, pageable);
		model.addAttribute("appointments", appointmentsPage.getContent()); // Current page data
		System.out.println("appointments" + appointmentsPage.getContent());
		model.addAttribute("currentPage", page); // Current page number
		System.out.println("currentPage : " + page);
		model.addAttribute("totalPages", appointmentsPage.getTotalPages()); // Total pages
		System.out.println("totalPages : " + appointmentsPage.getTotalPages());
		model.addAttribute("totalAppointments", appointmentsPage.getTotalElements()); // Total appointments
		System.out.println("totalAppointments : " + appointmentsPage.getTotalElements());

//		return "redirect:/appointment/" + page;
		return "plogin";
	}

	// Get all appointments
	@GetMapping("/ap")
	public List<Appointment> getAllAppointments() {
		return appointmentService.getAllAppointments();
	}

//	// Update appointment
//	@PutMapping("/updateap/{id}")
//	public String updateAppointment(@PathVariable Integer id,
//			@RequestParam("doctorId") Integer doctorId, 
//	        @RequestParam("appointmentSlot") String appointmentSlot,
//	        @RequestParam("appointmentDate") LocalDate appointmentDate,
//	        Model model,
//			@RequestParam(defaultValue = "0") int page, 
//			HttpSession session) {
////		appointment.setId(id);
////		return appointmentService.updateAppointment(appointment);
//		System.out.println("In update start");
//		Optional<Appointment> existingAppointmentOpt = appointmentService.getAppointmentById(id);
//
//		if (existingAppointmentOpt.isPresent()) {
//			Appointment existingAppointment = existingAppointmentOpt.get();
//			
//			Integer userId = (Integer) session.getAttribute("userId");
//			System.out.println("userId : " + userId);
//			String userName = (String) session.getAttribute("username");
//			System.out.println("username : " + userName);
//			Pageable pageable = (Pageable) PageRequest.of(page, 5, Sort.by(Sort.Order.desc("appointmentDate")));
//			Page<Appointment> appointmentsPage = appointmentService.getAppointmentsByPatientnadPage(userId, pageable);
//			model.addAttribute("appointments", appointmentsPage.getContent()); // Current page data
//			System.out.println("appointments" + appointmentsPage.getContent());
//			model.addAttribute("currentPage", page); // Current page number
//			System.out.println("currentPage : " + page);
//			model.addAttribute("totalPages", appointmentsPage.getTotalPages()); // Total pages
//			System.out.println("totalPages : " + appointmentsPage.getTotalPages());
//			model.addAttribute("totalAppointments", appointmentsPage.getTotalElements()); // Total appointments
//			System.out.println("totalAppointments : " + appointmentsPage.getTotalElements());
//
//			// Update the necessary fields
//			existingAppointment.setPatient(existingAppointment.getPatient());
//
//	        // Retrieve doctor details
//			 Optional<Doctor> optionalDoctor = doctorService.getDoctorById(doctorId);
//		        if (optionalDoctor.isPresent()) {
//		            existingAppointment.setDoctor(optionalDoctor.get());
//		        } else if (existingAppointment.getDoctor() != null) {
//		            // Retain existing doctor if no new doctor is selected
//		            System.out.println("Retaining existing doctor: " + existingAppointment.getDoctor().getName());
//		        } else {
//		            throw new RuntimeException("Doctor not found: " + doctorId);
//		        }
//		        
////			existingAppointment.setDoctor(updatedAppointment.getDoctor());
//			existingAppointment.setAppointmentSlot(appointmentSlot);
//			existingAppointment.setAppointmentDate(appointmentDate);
//			
//			System.out.println("Updating appointment:");
//	        System.out.println("Doctor: " + (existingAppointment.getDoctor() != null ? existingAppointment.getDoctor().getName() : "No Doctor Assigned"));
//	        System.out.println("Slot: " + appointmentSlot);
//	        System.out.println("Date: " + appointmentDate);
//
//			// Save the updated appointment
//			appointmentService.updateAppointment(existingAppointment);
//			
//			System.out.println("go to plogin");
//			
//			// Refresh appointment list
//		    Integer userId1 = (Integer) session.getAttribute("userId");
//		    Pageable pageable1 = PageRequest.of(0, 5, Sort.by(Sort.Order.desc("appointmentDate")));
//		    Page<Appointment> appointmentsPage1 = appointmentService.getAppointmentsByPatientnadPage(userId1, pageable1);
//		    model.addAttribute("appointments", appointmentsPage1.getContent());
////			Patient pId = patientService.findByNameAndPassword(patient.getName(), patient.getPassword());
//			// Re-fetch doctors list
//			List<Doctor> doctors = doctorService.getAllDoctors();
//			System.out.println("doctors :" + doctors);
//			model.addAttribute("doctors",doctors);
//			
//			// Update session attributes
//			session.setAttribute("username", existingAppointment.getPatient().getName());
//			session.setAttribute("userId", existingAppointment.getId());
//			System.out.println("PId :" + existingAppointment.getId());
//			System.out.println("PName :" + existingAppointment.getPatient().getName());
//			
//			// Add attributes for reloading page
//			model.addAttribute("username", existingAppointment.getPatient().getName());
//			model.addAttribute("userId", existingAppointment.getId());
//			
//			return "plogin";
//		} else {
//			return ("Appointment not found");
//		}
//	}
	
	@PutMapping("/updateap/{id}")
	public String updateAppointment(
	        @PathVariable Integer id,
	        @RequestParam("doctorId") Integer doctorId,
	        @RequestParam("appointmentSlot") String appointmentSlot,
	        @RequestParam("appointmentDate") LocalDate appointmentDate,
	        @RequestParam(defaultValue = "0") int page,
	        Model model, HttpSession session) {

	    System.out.println("Updating appointment with ID: " + id);
	    
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
//	    Pageable pageable = PageRequest.of(page, 5, Sort.by(Sort.Order.desc("appointmentDate")));
//	    Page<Appointment> appointmentsPage = appointmentService.getAppointmentsByPatientnadPage(userId, pageable);
//	    model.addAttribute("appointments", appointmentsPage.getContent());
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
		session.setAttribute("userId", existingAppointment.getId());
		System.out.println("PId :" + existingAppointment.getId());
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
		
		Pageable pageable = (Pageable) PageRequest.of(page, 5, Sort.by(Sort.Order.desc("appointmentDate")));
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
		
//		return "plogin";
		return "redirect:/appointment/appointments";
	}
}