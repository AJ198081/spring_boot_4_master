package dev.aj.bank_customer;

import org.springframework.boot.SpringApplication;

class LocalBankCustomerApplication {

    static void main(String[] args) {
        SpringApplication.from(BankCustomerApplication::main)
                .withAdditionalProfiles("test", "hibernate", "observability")
                .run(args);
    }

}
