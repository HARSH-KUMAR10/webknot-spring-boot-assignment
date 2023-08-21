package com.webknot.assignment;

import com.webknot.assignment.model.RoleEnum;
import com.webknot.assignment.model.UserRole;
import com.webknot.assignment.repository.RoleRepository;
import com.webknot.assignment.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AssignmentApplication implements CommandLineRunner {

	@Autowired
	RoleRepository roleRepository;

	public static void main(String[] args) {
		SpringApplication.run(AssignmentApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		roleRepository.save(new UserRole(1, RoleEnum.ROLE_USER));
		roleRepository.save(new UserRole(2, RoleEnum.ROLE_MODERATOR));
		roleRepository.save(new UserRole(3, RoleEnum.ROLE_ADMIN));
		for(UserRole role: roleRepository.findAll()){
			System.out.println(role.getName());
		}
	}
}
