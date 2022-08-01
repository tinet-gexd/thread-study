package com.gxd.hystrix;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;

@SpringBootApplication
@EnableHystrixDashboard
public class HystrixDashBoard {

    /**
     * todo 3
     * @param args
     */
    public static void main(String[] args) {
        SpringApplication.run(HystrixDashBoard.class,args);
    }

}
