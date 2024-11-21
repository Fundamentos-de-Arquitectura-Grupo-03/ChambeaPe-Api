package com.digitaldark.ChambeaPe_Api;

import com.digitaldark.ChambeaPe_Api.security.util.Utilities;
import com.digitaldark.ChambeaPe_Api.user_security.model.enums.ERole;
import com.digitaldark.ChambeaPe_Api.user_security.repository.IRoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ChambeaPeApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(ChambeaPeApiApplication.class, args);
	}

	@Bean
	CommandLineRunner initDatabase(IRoleRepository roleRepository) {
		return args -> {
			Utilities.insertRoleIfNotFound(roleRepository, ERole.ROLE_EMPLOYER);
			Utilities.insertRoleIfNotFound(roleRepository, ERole.ROLE_WORKER);
		};
	}
}