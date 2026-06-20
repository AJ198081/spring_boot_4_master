package dev.aj.product_service;

import org.springframework.boot.SpringApplication;

class ProductServiceApplicationTests {

    static void main(String[] args) {
        SpringApplication.from(ProductServiceApplication::main)
                .withAdditionalProfiles("test","local")
                .run(args);
    }

}
