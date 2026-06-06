package dev.aj.graphql;

import net.datafaker.Faker;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import tools.jackson.databind.SerializationFeature;
import tools.jackson.databind.json.JsonMapper;

import java.util.Locale;

@SpringBootApplication
public class GraphqlApplication {

    public static void main(String[] args) {

        SpringApplication.run(GraphqlApplication.class, args);
    }

    @Bean
    public Faker faker() {
        return new Faker(Locale.of("EN", "IN"));
    }

    @Bean
    public JsonMapper jsonMapper() {
        return JsonMapper.builderWithJackson2Defaults()
                .enable(SerializationFeature.INDENT_OUTPUT)
                .build();
    }

}
