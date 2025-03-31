package com.example.doctorappointment.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.doctorappointment.model.Admin;
import com.example.doctorappointment.model.Patient;
import com.example.doctorappointment.repository.AdminRepository;

@Service
public class AdminService {
	
	@Autowired
	AdminRepository adminRepository;

	public boolean validateAdmin(String adminUsername, String adminPassword) {
		// TODO Auto-generated method stub
		return false;
	}

	public Admin findByNameAndPassword(String name, String password) {
		return adminRepository.findByNameAndPassword(name,password);
	}

	public Optional<Admin> getAdminById(Integer adminId) {
		// TODO Auto-generated method stub
		return adminRepository.findById(adminId);
	}

	 
}
