package com.taskhive.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
//@EnableJpaRepositories(basePackages = {"com.taskhive.backend.repository"})
//@EntityScan(basePackages = {"com.taskhive.backend.model"})

//not using the above annotations as they are added to the ApplicationContext while testing
public class BackendApplication {
    public static void main(String[] args) {
        SpringApplication.run(BackendApplication.class, args);
    }
}
