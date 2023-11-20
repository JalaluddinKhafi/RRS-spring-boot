package com.khafi.rrs.rrs_springboot.RrsSpringBootApplication;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.khafi.rrs.rrs_springboot.RrsSpringBootApplication")
public class RrsSpringBootApplication {
    public static void main(String[] args) {
        SpringApplication.run(RrsSpringBootApplication.class, args);
    }

}
