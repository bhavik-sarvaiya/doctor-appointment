package com.example.doctorappointment.controller;

import java.util.List;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.doctorappointment.model.Appointment;
import com.example.doctorappointment.model.Doctor;
import com.example.doctorappointment.model.Patient;
import com.example.doctorappointment.service.AppointmentService;
import com.example.doctorappointment.service.DoctorService;
import com.example.doctorappointment.service.PatientService;

import jakarta.servlet.http.HttpSession;

@Controller
public class Login {

	@Autowired
	Doctor doctor;
	@Autowired
	DoctorService doctorService;
	@Autowired
	Patient patient;
	@Autowired
	PatientService patientService;
	@Autowired
	AppointmentService appointmentService;

	@PostMapping("/login")
	public String login(@ModelAttribute Doctor doctor, @ModelAttribute Patient patient,
			@ModelAttribute Appointment appointment, Model model, HttpSession session) {

//		Doctor duser = doctorService.findByNameAndPassword(doctor.getName(), doctor.getPassword());
//		Patient puser = patientService.findByNameAndPassword(patient.getName(), patient.getPassword());

		if (Objects.nonNull(doctorService.findByNameAndPassword(doctor.getName(), doctor.getPassword()))) {

			Doctor dId = doctorService.findByNameAndPassword(doctor.getName(), doctor.getPassword());
			session.setAttribute("username", doctor.getName());
			session.setAttribute("userDId", dId.getId());
			System.out.println("DId :" + dId.getId());
			List<Appointment> appointments = appointmentService.getAppointmentsByDoctor(dId.getId());
			model.addAttribute("appointments", appointments);
			model.addAttribute("username", doctor.getName());
			model.addAttribute("userDId", dId.getId());

			return "dlogin";

		} else if (Objects.nonNull(patientService.findByNameAndPassword(patient.getName(), patient.getPassword()))) {

			Patient pId = patientService.findByNameAndPassword(patient.getName(), patient.getPassword());
			session.setAttribute("username", patient.getName());
			session.setAttribute("userId", pId.getId());
			System.out.println("PId :" + pId.getId());
			System.out.println("PName :" + patient.getName());
			model.addAttribute("username", patient.getName());
			model.addAttribute("userId", pId.getId());

			return "welcome";
		} else {

			model.addAttribute("message", "Register first or check user and password");
			return "welcome";
		}
//		model.addAttribute("message", "Register first");
//		return "welcome";
	}
}
