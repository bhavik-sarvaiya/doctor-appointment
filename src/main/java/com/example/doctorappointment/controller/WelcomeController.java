package com.example.doctorappointment.controller;

import java.security.Principal;
import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.doctorappointment.model.Appointment;
import com.example.doctorappointment.model.Patient;
import com.example.doctorappointment.service.AppointmentService;

import jakarta.servlet.http.HttpSession;

@Controller
public class WelcomeController {
	
	@Autowired
	private AppointmentService appointmentService;

    @GetMapping({"/welcome", "/"})
    public String welcome(Model model, HttpSession session) {
    	String username = (String) session.getAttribute("username");
		model.addAttribute("username", username);
        return "welcome"; // This corresponds to welcome.html in src/main/resources/templates
    }
    
    @GetMapping("/p_welcome")
    public String p_welcome(Model model, HttpSession session) {
    	String username = (String) session.getAttribute("username");
    	Integer userId = (Integer) session.getAttribute("userId");
		model.addAttribute("username", username);
		model.addAttribute("userId", userId);

        return "p_welcome"; // This corresponds to welcome.html in src/main/resources/templates
    }
    
    
    @GetMapping("/about")
    public String about(Model model, HttpSession session) {
    	String username = (String) session.getAttribute("username");
		model.addAttribute("username", username);
        return "about"; // This corresponds to about.html in src/main/resources/templates
    }
    
    @GetMapping("/contact")
    public String contact(Model model, HttpSession session) {
    	String username = (String) session.getAttribute("username");
		model.addAttribute("username", username);
        return "contact"; // This corresponds to contact.html in src/main/resources/templates
    }
    
    @GetMapping("/logout")
    public String logout(Model model, HttpSession session) {
    	
    	session.invalidate();
    	return "welcome"; // This corresponds to contact.html in src/main/resources/templates
    }
}
