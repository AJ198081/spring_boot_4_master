package dev.aj.accounts;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.modulith.Modulithic;
import org.springframework.resilience.annotation.EnableResilientMethods;

@EnableResilientMethods
@SpringBootApplication
@Modulithic(
        sharedModules = {
                "common"
        },
        useFullyQualifiedModuleNames = false
)
@OpenAPIDefinition(
        info = @Info(
                title = "Accounts API",
                version = "1.0",
                description = "API for managing customer's bank accounts"
        ),
        servers = {
                @io.swagger.v3.oas.annotations.servers.Server(url = "/")
        },
        security = {
                @io.swagger.v3.oas.annotations.security.SecurityRequirement(
                        name = "bearerAuth"
                )
        }
)
public class AccountsApplication {

    public static void main(String[] args) {
        SpringApplication.run(AccountsApplication.class, args);
    }

}
