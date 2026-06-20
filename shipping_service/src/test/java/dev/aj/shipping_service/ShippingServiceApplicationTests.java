package dev.aj.shipping_service;

import org.springframework.boot.SpringApplication;

class ShippingServiceApplicationTests {

    static void main(String[] args) {
        SpringApplication.from(ShippingServiceApplication::main)
                .withAdditionalProfiles("test","local")
                .run(args);
    }

}
