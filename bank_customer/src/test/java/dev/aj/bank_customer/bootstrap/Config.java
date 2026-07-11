package dev.aj.bank_customer.bootstrap;

import net.datafaker.Faker;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.context.annotation.Bean;

import java.util.Locale;

@TestComponent
public class Config {

    @Bean
    public Faker faker() {
        return new Faker(Locale.of("en", "IN"));
    }

}
