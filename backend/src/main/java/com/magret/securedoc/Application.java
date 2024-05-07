package com.magret.securedoc;

import com.magret.securedoc.domain.RequestContext;
import com.magret.securedoc.entity.RoleEntity;
import com.magret.securedoc.enumeration.Authority;
import com.magret.securedoc.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableJpaAuditing
@EnableAsync
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Bean
	CommandLineRunner commandLineRunner(RoleRepository roleRepository){
		return args -> {
			/*RequestContext.setUserId(0L);
			var userRole = new RoleEntity();
			userRole.setName(Authority.USER.name());
			userRole.setAuthorities(Authority.USER);

			var adminRole = new RoleEntity();
			adminRole.setName(Authority.ADMIN.name());
			adminRole.setAuthorities(Authority.ADMIN);

			roleRepository.save(adminRole);
			roleRepository.save(userRole);

			RequestContext.start();*/
		};
	}

}
