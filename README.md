## Latest micro service code in 2.3.7 version
Code reference:
 
https://www.youtube.com/watch?v=BnknNTN8icw&ab_channel=DailyCodeBuffer  

https://github.com/shabbirdwd53/Springboot-Microservice/blob/main/user-service/pom.xml

## Creating a git-repo project
Create .properties file app name wise
Like: user-details.properties
Here will keep all config and controller form outside

# in user-department.properties
// keeping all config here for future purpose
// spring.application.name = user-department // only name should be mention // in bootstrap other config are optional
// name is selector of profile
// we verify this config is linked or by changing port no
server.port = 8000
spring.zipkin.base-url= http://127.0.0.1:9411/

some-var: 'somethings'

# in user-details.properties
server.port = 8080
spring.zipkin.base-url= http://127.0.0.1:9411/

some-var: 'somethings'

// in git folder commit and save



## Create a cloud config project
Only connect to git config to app via bootstrap.properties 

# Add in pom.xml
<dependency>
	<groupId>org.springframework.cloud</groupId>
	<artifactId>spring-cloud-config-server</artifactId>
</dependency>
<dependency>
	<groupId>org.springframework.cloud</groupId>
	<artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
</dependency>

# in application.properties

spring.application.name=application-config-server
server.port=8888
spring.cloud.config.server.git.uri = file:///Users/amit/Desktop/SBProj/Am Microservic/git-repo

- to test it's working or not. check on chrome by profile wise:
- http://localhost:8888/user-details/default

# in application.java
Enable server
@EnableConfigServer

## creating user-details project
Only return single rest service, called by user-department service

# In pom.xml use dependency
spring-boot-starter-web
spring-cloud-starter-netflix-eureka-client
spring-cloud-starter-config
spring-cloud-starter-sleuth // for tracing log by id
spring-cloud-starter-zipkin. // for tracing log by id

# in bootstrap.properties for connect git config

spring.application.name= user-details

// add new file bootstrap.properties
// link to spring cloud server
spring.cloud.config.uri = http://localhost:8888

# in userDetails.java

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

# in userBean.java
public class userBean {

    String msg;
    int port;


    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }


    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}

# for test : http://localhost:8080/user/details

## create a second micro services : user-department

# In pom.xml use dependency
spring-boot-starter-web
spring-cloud-starter-netflix-eureka-client
spring-cloud-starter-config
spring-cloud-starter-sleuth // for tracing log by id
spring-cloud-starter-zipkin. // for tracing log by id


# in bootstrap.properties for connect git config

spring.application.name= user-department
#server.port= 8002


# add new file bootstrap.properties
# link to spring cloud server
spring.cloud.config.uri = http://localhost:8888

# in UserDepartmentApplication.java
 
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

# in userDepartment.java

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

# in userBean.java

public class userBean {
    String msg;
    int port;


    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }


    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}

## create a eureka server app to monitor all service are up and redirect to another service if load balance needed
// test on localhost:8761


# In pom.xml use dependency
spring-cloud-starter-netflix-eureka-server

# in application.properties

spring.application.name= netflix-eureka-naming-server
server.port= 8761
# eureka server don't register itself
eureka.client.register-with-eureka: false
eureka.client.fetch-registry: false

# in java file
@EnableEurekaServer


## create api gateway to redirect all api thought it, easy to monitor by hystrix gateway and error handler

# In pom.xml use dependency
spring-boot-starter-actuator
spring-cloud-starter-gateway
spring-cloud-starter-netflix-eureka-client
spring-cloud-starter-netflix-hystrix


# in application.properties
spring.application.name=api-gateway
server.port=9191

# in application.yml

spring:
  cloud:
    gateway:
      routes:
        - id: user-details
          uri: lb://user-details
          predicates:
            - Path=/user/**
          filters:
            - name: CircuitBreaker
              args:
                name: user-details
                fallbackuri: farword:/user-api-error
        - id: user-department
          uri: lb://user-department
          predicates:
            - Path=/department/**
          filters:
            - name: CircuitBreaker
              args:
                name: user-department
                fallbackuri: farword:/user-department-api-error

hystrix:
  command:
    fallbackcmd:
      execution:
        isolation:
          thread:
            timeoutInMilliseconds: 4000

management:
  endpoints:
    web:
      exposure:
        include: hystrix.stream

# in application.java
// redirect all api thought it, easy to monitor by hystrix gateway
// used circuit breaker to handle error
@SpringBootApplication
@EnableHystrix


# in FallbackHandler.java

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

## To show performance of all api, monitor api gate create a app
Hystrix gateway,Hystrix is a api monitoring tools

# in pom.xml
spring-cloud-starter-netflix-eureka-client
spring-cloud-starter-netflix-hystrix-dashboard

# in application.yml

spring:
  application:
    name: hystrix-dashboard
server:
  port: 9192

hystrix:
  dashboard:
    proxy-stream-allow-list: "*"

# in application.java

@EnableHystrixDashboard

// test part
		Apigatway port no with this url

		http://localhost:9191/actuator/hystrix.stream

		In another tab

		http://localhost:9192/hystrix/

http://localhost:9191/actuator/hystrix.stream

And test



## To test in zipkin server

download jar file from and use java for run
https://zipkin.io/pages/quickstart

To run code
java -jar zipkin.jar

Set up in app by adding mvn in app user-details and user-department

<dependency>
	<groupId>org.springframework.cloud</groupId>
	<artifactId>spring-cloud-starter-config</artifactId>
</dependency>
<dependency>
	<groupId>org.springframework.cloud</groupId>
	<artifactId>spring-cloud-starter-sleuth</artifactId>
</dependency>

In properties file

spring.zipkin.base-url= http://127.0.0.1:9411/

# For test : For test on browser
http://localhost:9411/



http://localhost:9411/zipkin/traces/72e0cf9e88aaccb6
