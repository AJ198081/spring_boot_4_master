package dev.aj.shipping_service;

import org.springframework.boot.SpringApplication;

class LocalShippingServiceApplication {

    static void main(String[] args) {
        SpringApplication.from(ShippingServiceApplication::main)
                .withAdditionalProfiles("test","local")
                .run(args);
    }

}
