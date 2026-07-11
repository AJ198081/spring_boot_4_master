package dev.aj.bank_customer;

import org.springframework.boot.SpringApplication;

class CustomerAppTests {

    static void main(String[] args) {
        SpringApplication.from(BankCustomerApplication::main)
                .withAdditionalProfiles("test")
                .run(args);
    }

}
