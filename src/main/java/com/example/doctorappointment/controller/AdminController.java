package com.example.doctorappointment.controller;

import com.example.doctorappointment.model.Appointment;
import com.example.doctorappointment.model.Doctor;
import com.example.doctorappointment.model.Patient;
import com.example.doctorappointment.service.AppointmentService;
import com.example.doctorappointment.service.DoctorService;
import com.example.doctorappointment.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

	@Autowired
	private AppointmentService appointmentService;

	@Autowired
	private DoctorService doctorService;

	@Autowired
	private PatientService patientService;

	  // For Appointments
	  
//	  @GetMapping("/appointments") 
//	  public String getAllAppointments(Model model) {
//	  List<Appointment> appointments = appointmentService.getAllAppointments();
//	  model.addAttribute("appointments", appointments); 
//	  return "appointments";
//	  }
	  
//	  @PostMapping("/appointments") 
//	  public String createAppointment(@ModelAttribute
//	  Appointment appointment ) { // System.out.println("in Appoinment creat");
//	  System.out.println(appointment.getPatient());
//	  System.out.println(appointment.getDoctor()); 
//	  // appointmentService.createAppointment(patientID, doctorID); 
//	  // appointmentService.createAppointment(appointment);
//	  System.out.println(appointment); // return "redirect:/admin/appointments";
//	  return "appointmentsDtl"; }
	  
//	  @PostMapping("/appointments/{id}") 
//	  public String
//	  updateAppointment(@PathVariable Integer id, @ModelAttribute Appointment
//	  appointment, @RequestParam("_method") String method) { if ("put".equalsIgnoreCase(method))
//	  { appointment.setId(id); appointmentService.updateAppointment(appointment); }
//	  return "redirect:/admin/appointments"; }
	  
//	  @PostMapping("/appointments/{id}/delete") public String
//	  deleteAppointment(@PathVariable Integer id, @RequestParam("_method") String
//	  method) { if ("delete".equalsIgnoreCase(method)) {
//	  appointmentService.deleteAppointment(id); } return
//	  "redirect:/admin/appointments"; }
	 

	// For Doctors
	@GetMapping("/doctors")
	public String getAllDoctors(Model model) {
		List<Doctor> doctors = doctorService.getAllDoctors();
		model.addAttribute("doctors", doctors);
		return "doctors"; // return html doctor file
	}

	@PostMapping("/doctors")
	public String createDoctor(@ModelAttribute Doctor doctor) {
		doctorService.createDoctor(doctor);
		return "redirect:/admin/doctors";
	}

	@PostMapping("/doctors/{id}")
	public String updateDoctor(@PathVariable Integer id, @ModelAttribute Doctor doctor,
			@RequestParam("_method") String method) {
		if ("put".equalsIgnoreCase(method)) {
			doctor.setId(id);
			doctorService.updateDoctor(doctor);
		}
		return "redirect:/admin/doctors";
	}

	@PostMapping("/doctors/{id}/delete")
	public String deleteDoctor(@PathVariable Integer id, @RequestParam("_method") String method) {
		if ("delete".equalsIgnoreCase(method)) {
			doctorService.deleteDoctor(id);
		}
		return "redirect:/admin/doctors";
	}

	// For Patients
	@GetMapping("/patients")
	public String getAllPatients(Model model) {
		List<Patient> patients = patientService.getAllPatients();
		model.addAttribute("patients", patients);
		return "patients"; // return html patient file
	}

	@PostMapping("/patients")
	public String createPatient(@ModelAttribute Patient patient) {
		patientService.createPatient(patient);
		return "redirect:/admin/patients";
	}

	@PostMapping("/patients/{id}")
	public String updatePatient(@PathVariable Integer id, @ModelAttribute Patient patient) {
		patient.setId(id);
		patientService.updatePatient(patient);
		return "redirect:/admin/patients";
	}

	@PostMapping("/patients/{id}/delete")
	public String deletePatient(@PathVariable Integer id) {
		patientService.deletePatient(id);
		return "redirect:/admin/patients";
	}

}
