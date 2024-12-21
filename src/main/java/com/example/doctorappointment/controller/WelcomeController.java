package com.example.doctorappointment.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.HttpSession;

@Controller
public class WelcomeController {

    @GetMapping("/welcome")
    public String welcome(Model model, HttpSession session) {
    	String username = (String) session.getAttribute("username");
		model.addAttribute("username", username);
        return "welcome"; // This corresponds to welcome.html in src/main/resources/templates
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
