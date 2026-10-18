package dev.aj.billing_service;

import org.springframework.boot.SpringApplication;

class LocalBillingServiceApplication {

    static void main(String[] args) {
        SpringApplication.from(BillingServiceApplication::main)
                .withAdditionalProfiles("test","local")
                .run(args);
    }

}
