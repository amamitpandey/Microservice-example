package com.amTest.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

// simple app
// return a string value to call rest api
// connect to cloud config to use git configuration
// connected to eureka discovery client for load balancing
// connected to sleuth and zipkin for trace management
// for test : http://localhost:8080/user/details
@SpringBootApplication
public class UserDetailsApplication {

	public static void main(String[] args) {
		SpringApplication.run(UserDetailsApplication.class, args);
	}

}
