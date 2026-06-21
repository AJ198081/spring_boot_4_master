package dev.aj.coupon_service;

import org.springframework.boot.SpringApplication;

class CouponServiceApplicationTests {

    static void main(String[] args) {

        SpringApplication.from(CouponServiceApplication::main)
                .withAdditionalProfiles("test","local")
                .run(args);
    }

}
