package dev.aj.billing_service;

import org.springframework.boot.SpringApplication;

class BillingServiceApplicationTests {

    static void main(String[] args) {
        SpringApplication.from(BillingServiceApplication::main)
                .withAdditionalProfiles("test","local")
                .run(args);
    }

}
