package com.gxd.hystrix;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableCircuitBreaker
@EnableFeignClients("com.gxd.hystrix")
public class HystrixStudyApplication {

    public static void main(String[] args) {
        SpringApplication.run(HystrixStudyApplication.class, args);
    }

}
