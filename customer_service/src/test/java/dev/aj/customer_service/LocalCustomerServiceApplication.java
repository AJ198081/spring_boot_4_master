package dev.aj.customer_service;

import org.springframework.boot.SpringApplication;

class LocalCustomerServiceApplication {

    static void main(String[] args) {
        SpringApplication.from(CustomerServiceApplication::main)
                .withAdditionalProfiles("test","local")
                .run(args);
    }

}
