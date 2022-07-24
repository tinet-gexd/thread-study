package com.gxd.hystrix;

import com.netflix.hystrix.contrib.metrics.eventstream.HystrixMetricsStreamServlet;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.hystrix.HystrixStreamEndpoint;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

import javax.servlet.ServletRegistration;

@SpringBootApplication
@EnableCircuitBreaker
@EnableFeignClients("com.gxd.hystrix")
public class HystrixStudyApplication {

    public static void main(String[] args) {
        SpringApplication.run(HystrixStudyApplication.class, args);
    }



    @Bean
    public ServletRegistrationBean servletRegistrationBean(){
        HystrixMetricsStreamServlet hystrixMetricsStreamServlet = new HystrixMetricsStreamServlet();
        ServletRegistrationBean hy = new ServletRegistrationBean(hystrixMetricsStreamServlet);
        hy.setLoadOnStartup(1);
        hy.addUrlMappings("/hystrix.stream");
        hy.setName("HystrixStudyApplication");
        return hy ;
    }
}
