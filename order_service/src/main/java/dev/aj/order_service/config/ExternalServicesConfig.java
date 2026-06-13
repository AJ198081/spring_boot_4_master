package dev.aj.order_service.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;

@Configuration
@ConfigurationProperties(prefix = "external.services.url")
public class ExternalServicesConfig {
    private String billing;
    private String customer;
    private String payment;
    private String product;
    private String shipping;

    @Bean
    public RestClient billingClient() {
        return createRestClient(billing);
    }

    @Bean
    public RestClient customerClient() {
        return createRestClient(customer);
    }

    @Bean
    public RestClient paymentClient() {
        return createRestClient(payment);
    }

    @Bean
    public RestClient productClient() {
        return createRestClient(product);
    }

    @Bean
    public RestClient shippingClient() {
        return createRestClient(shipping);
    }


    private RestClient createRestClient(String endpointUrl) {
        return RestClient.builder()
                .baseUrl(endpointUrl)
                .defaultHeaders(httpHeaders -> httpHeaders.addAll(createDefaultHeaders()))
                .build();
    }

    private HttpHeaders createDefaultHeaders() {
        HttpHeaders defaultHeaders = new HttpHeaders();
        defaultHeaders.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        defaultHeaders.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
        return defaultHeaders;
    }
}
