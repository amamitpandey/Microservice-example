package com.amTest.demo;


import org.apache.catalina.mbeans.UserMBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("user")
public class userDetails {

    @Autowired
    Environment environment;

    @GetMapping("/details")
    private userBean getUser(){
        userBean userBeanLoc = new userBean();
        userBeanLoc.setMsg("user details msg");
        userBeanLoc.setPort(Integer.parseInt(environment.getProperty("local.server.port")));
        return userBeanLoc;
    }
}
