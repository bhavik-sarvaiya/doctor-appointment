package com.example.doctorappointment.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.doctorappointment.model.Admin;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Integer> {

	Admin findByNameAndPassword(String name, String password);

	Admin findByName(String name);
	
	Optional<Admin> findById(Integer id);

}
