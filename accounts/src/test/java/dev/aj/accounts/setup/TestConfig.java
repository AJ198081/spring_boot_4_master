package dev.aj.accounts.setup;

import net.datafaker.Faker;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClient;

import java.util.Locale;

@TestConfiguration(proxyBeanMethods = false)
public class TestConfig {

    public RestClient restClient(String baseUrl) {
        return RestClient.builder()
                .baseUrl(baseUrl)
                .defaultStatusHandler(HttpStatusCode::isError, (request, response) -> {
                    throw new HttpStatusCodeException(response.getStatusCode(), "Error calling %s".formatted(request.getURI())) {};
                })
                .build();
    }

    @Bean
    public Faker faker() {
        return new Faker(Locale.of("en", "IN"));
    }

}
