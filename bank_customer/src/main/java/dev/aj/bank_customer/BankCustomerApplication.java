package dev.aj.bank_customer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.modulith.Modulithic;
import org.springframework.resilience.annotation.EnableResilientMethods;

@SpringBootApplication
@EnableResilientMethods
@Modulithic(
        sharedModules = {
                "model",
                "config"
        }
)
public class BankCustomerApplication {

    public static void main(String[] args) {
        SpringApplication.run(BankCustomerApplication.class, args);
    }

}
