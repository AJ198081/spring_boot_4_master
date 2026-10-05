package dev.aj.bank_customer;

import dev.aj.bank_customer.repositories.impl.NaturalIDRepositoryImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
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
@EnableJpaRepositories(repositoryBaseClass = NaturalIDRepositoryImpl.class)
public class BankCustomerApplication {

    public static void main(String[] args) {
        SpringApplication.run(BankCustomerApplication.class, args);
    }

}
