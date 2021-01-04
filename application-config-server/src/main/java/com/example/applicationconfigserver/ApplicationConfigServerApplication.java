package com.example.applicationconfigserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;
@EnableConfigServer
@SpringBootApplication
public class ApplicationConfigServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApplicationConfigServerApplication.class, args);
	}

}
