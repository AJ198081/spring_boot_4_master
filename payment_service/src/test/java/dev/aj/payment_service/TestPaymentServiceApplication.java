package dev.aj.payment_service;

import org.springframework.boot.SpringApplication;

class TestPaymentServiceApplication {

    static void main(String[] args) {
        SpringApplication.from(PaymentServiceApplication::main)
                .withAdditionalProfiles("test", "local")
                .run(args);

    }
}
