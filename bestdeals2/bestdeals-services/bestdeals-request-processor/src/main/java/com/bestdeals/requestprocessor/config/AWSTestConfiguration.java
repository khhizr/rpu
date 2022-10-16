package com.bestdeals.requestprocessor.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.testcontainers.containers.localstack.LocalStackContainer;
import org.testcontainers.containers.localstack.LocalStackContainer.Service;
import com.bestdeals.commons.testing.AbstractBaseTest;

import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClient;

@Configuration
@Profile("test")
public class AWSTestConfiguration {
    @Bean
    public AmazonSNS snsClient() {
        LocalStackContainer localStackContainer = AbstractBaseTest.getLocalStackContainer();
        return AmazonSNSClient.builder()
                .withEndpointConfiguration(localStackContainer.getEndpointConfiguration(Service.SNS))
                .withCredentials(localStackContainer.getDefaultCredentialsProvider()).build();
    }

}