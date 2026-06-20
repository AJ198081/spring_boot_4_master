package dev.aj.order_service;

import net.datafaker.Faker;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.resilience.annotation.EnableResilientMethods;

import java.util.Locale;

@SpringBootApplication
@EnableResilientMethods
public class OrderServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrderServiceApplication.class, args);
    }

    @Bean
    public Faker faker() {
        return new Faker(Locale.of("en", "IN"));
    }
}
