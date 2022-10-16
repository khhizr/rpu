package com.bestdeals.requestprocessor.config;

import com.bestdeals.commons.services.HelloWorldService;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BestdealsRequestProcessorConfiguration {

    @Bean
    public HelloWorldService helloWorldService() {
        return new HelloWorldService();
    }
}
