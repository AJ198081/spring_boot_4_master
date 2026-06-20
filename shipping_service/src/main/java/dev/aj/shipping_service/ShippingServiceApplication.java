package dev.aj.shipping_service;

import net.datafaker.Faker;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Locale;

@SpringBootApplication
public class ShippingServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShippingServiceApplication.class, args);
    }


    @Bean
    public Faker faker() {
        return new Faker(Locale.of("en", "IN"));
    }
}
