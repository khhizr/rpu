package com.bestdeals.eureka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class BestdealsEurekaApplication {
    public static void main(String[] args) {
        SpringApplication.run(BestdealsEurekaApplication.class, args);
    }
}
