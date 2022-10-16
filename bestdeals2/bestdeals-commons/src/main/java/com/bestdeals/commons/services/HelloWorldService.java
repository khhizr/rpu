package com.bestdeals.commons.services;

public class HelloWorldService {
    public String getWelcomeNote() {
        return "Hello World Welcome to Bestdeals";
    }

    public String getWelcomeNote(String serviceName) {
        return serviceName + ": Hello World Welcome to Bestdeals microservice";
    }
}