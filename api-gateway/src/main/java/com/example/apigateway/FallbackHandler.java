package com.example.apigateway;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

// used circuit breaker to handle error if service is down or else
// to test down the server and try : http://localhost:9191/user/details
@RestController
public class FallbackHandler {

    @GetMapping("user-api-error")
    public  String userApiError(){
        return "User api taking longer time than expected time";
    }

    @GetMapping("user-department-api-error")
    public  String userDepartmentApiError(){
        return "userDepartmentApiError api taking longer time than expected time";
    }
}
