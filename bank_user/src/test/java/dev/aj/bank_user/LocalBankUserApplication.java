package dev.aj.bank_user;

import org.springframework.boot.SpringApplication;

class LocalBankUserApplication {

    static void main(String... args) {
        SpringApplication.from(BankUserApplication::main)
                .withAdditionalProfiles("test")
                .run(args);
    }
}
