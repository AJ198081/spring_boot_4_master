package dev.aj.accounts;

import org.springframework.boot.SpringApplication;

public class TestAccountsApplication {

    static void main(String[] args) {
        SpringApplication.from(AccountsApplication::main)
                .withAdditionalProfiles( "test")
                .run(args);
    }

}
