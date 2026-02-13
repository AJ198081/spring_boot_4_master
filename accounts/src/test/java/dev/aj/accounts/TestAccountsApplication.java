package dev.aj.accounts;

import org.springframework.boot.SpringApplication;

public class TestAccountsApplication {

    public static void main(String[] args) {
        SpringApplication.from(AccountsApplication::main)
                .with(TestcontainersConfiguration.class)
                .withAdditionalProfiles( "test")
                .run(args);
    }

}
