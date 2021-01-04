package com.amTest.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

// simple app
// return a string value to call rest api  : http://localhost:8000/department/details
// calling user-details to get info using rest-template : http://localhost:8000/department/getDepartment
// connect to cloud config to use git configuration
// connected to eureka discovery client for load balancing
// connected to saluth and zipkin for trace management
@SpringBootApplication
//@EnableDiscoveryClient
public class UserDepartmentApplication {

	public static void main(String[] args) {
		SpringApplication.run(UserDepartmentApplication.class, args);
	}

	// mandatory to declare here
	@Bean
	@LoadBalanced
	public RestTemplate restTemplate(){
		return new RestTemplate();
	}

}
