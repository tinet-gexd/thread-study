package com.gxd.hystrix;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;

@SpringBootApplication
@EnableCircuitBreaker
public class HystrixStudyApplication {

    public static void main(String[] args) {
        SpringApplication.run(HystrixStudyApplication.class, args);
    }

}
