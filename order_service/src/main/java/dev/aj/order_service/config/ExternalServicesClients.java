package dev.aj.order_service.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;

@Configuration
@EnableConfigurationProperties(value = ExternalServiceProperties.class)
@RequiredArgsConstructor
public class ExternalServicesClients {

    private final LoggingInterceptor loggingInterceptor = new LoggingInterceptor();

    private final RestClient.Builder restClientBuilder;

    @Bean
    public RestClient billingClient(ExternalServiceProperties services) {
        return createRestClient(services.billing());
    }

    @Bean
    public RestClient customerClient(ExternalServiceProperties services) {
        return createRestClient(services.customer());
    }

    @Bean
    public RestClient paymentClient(ExternalServiceProperties services) {

        return createRestClient(services.payment());
    }

    @Bean
    public RestClient productClient(ExternalServiceProperties services) {

        return createRestClient(services.product());
    }

    @Bean
    public RestClient shippingClient(ExternalServiceProperties services) {

        return createRestClient(services.shipping());
    }

    @Bean
    public RestClient couponClient(ExternalServiceProperties services) {

        return createRestClient(services.coupon());
    }


    private RestClient createRestClient(String endpointUrl) {

        return restClientBuilder
                .baseUrl(endpointUrl)
                .defaultHeaders(httpHeaders -> httpHeaders.addAll(createDefaultHeaders()))
                .requestInterceptor(loggingInterceptor)
                .build();
    }

    private HttpHeaders createDefaultHeaders() {

        HttpHeaders defaultHeaders = new HttpHeaders();
        defaultHeaders.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        defaultHeaders.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);

        return defaultHeaders;
    }
}
