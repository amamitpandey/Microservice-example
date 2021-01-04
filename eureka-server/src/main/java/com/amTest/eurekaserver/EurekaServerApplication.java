package com.amTest.eurekaserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

// create a eureka server app to monitor all service are up and redirect to another service if load balance needed
// only eureka cong server enable
// test on localhost:8761
@SpringBootApplication
// mandatory to declare as server app
@EnableEurekaServer
public class EurekaServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(EurekaServerApplication.class, args);
	}

}
