package com.travel.fare;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main entry point for the Fare Calculation Microservice.
 * Calculates cab fare based on distance and cab type.
 * Runs on port 8082.
 */
@SpringBootApplication
public class FareServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(FareServiceApplication.class, args);
    }
}
