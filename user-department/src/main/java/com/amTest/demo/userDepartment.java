package com.amTest.demo;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("department")
public class userDepartment {

    @Autowired
    Environment environment;

    private Logger logger = LoggerFactory.getLogger(this.getClass());


    @GetMapping("/details")
    private String getUser() {
        return "Department  port " + environment.getProperty("local.server.port");
    }

    @Autowired
    private RestTemplate restTemplateLoc;

    @GetMapping("/getDepartment")
    private userBean getDepartment() {
        logger.info("call msg: getDepartment" );
        userBean restTemplate = restTemplateLoc.getForObject("http://user-details/user/details", userBean.class);
        logger.info("call msg: " + restTemplate);
        return restTemplate;
    }

}