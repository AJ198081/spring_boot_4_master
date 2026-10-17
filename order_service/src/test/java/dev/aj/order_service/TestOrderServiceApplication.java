package dev.aj.order_service;

import org.springframework.boot.SpringApplication;

class TestOrderServiceApplication {

    static void main(String[] args) {
        SpringApplication.from(OrderServiceApplication::main)
                .withAdditionalProfiles("test","local")
                .run(args);
    }

}
