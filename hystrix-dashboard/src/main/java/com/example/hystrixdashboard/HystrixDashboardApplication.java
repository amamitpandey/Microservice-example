package com.example.hystrixdashboard;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;
// only for monitoring performance fo api
// two mvn included : eureka discovery client and hystrix dashboard
// for open this on chrome : http://localhost:9192/hystrix/


@SpringBootApplication
// allowing hystrix dashboard
@EnableHystrixDashboard
public class HystrixDashboardApplication {

	public static void main(String[] args) {
		SpringApplication.run(HystrixDashboardApplication.class, args);
	}

}
