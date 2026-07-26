package dev.aj.bank_user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.modulith.Modulithic;

@SpringBootApplication
@Modulithic(
        sharedModules = {
                "model",
                "config"
        }
)

public class BankUserApplication {

    public static void main(String[] args) {

        SpringApplication.run(BankUserApplication.class, args);
    }

}
