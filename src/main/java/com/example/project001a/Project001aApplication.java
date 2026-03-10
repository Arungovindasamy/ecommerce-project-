package com.example.project001a;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.example.project001a.repository")  // ✅ CRITICAL
public class Project001aApplication {
	public static void main(String[] args) {
		SpringApplication.run(Project001aApplication.class, args);
	}
}
