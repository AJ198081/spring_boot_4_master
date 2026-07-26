package dev.aj.commons.rest.client;

import dev.aj.commons.exceptions.NonRetryableException;
import dev.aj.commons.exceptions.RetryableException;
import dev.aj.commons.rest.utils.LoggingInterceptor;
import jakarta.annotation.Nullable;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.util.function.Supplier;

@Slf4j
@SuppressWarnings("unused")
public abstract class AbstractServiceClient {

    protected abstract String getServiceName();
    private final HttpHeaders requestHeaders = this.createDefaultHeaders();

    protected <T extends RestClient.ResponseSpec> T executeRequest(Supplier<T> responseSpecSupplier) {
        try {
            return responseSpecSupplier.get();
        } catch (ResourceAccessException e) {
            log.error("{} is unavailable. Retrying...", getServiceName());
            throw new RetryableException(e.getMessage());
        } catch (RestClientException e) {
            log.error("Invalid interaction with {}, due to {}", getServiceName(), e.getMessage());
            if (e.getCause() instanceof HttpMessageNotReadableException) {
                throw new NonRetryableException(e.getMessage(), e.getCause());
            }
            throw new RuntimeException(e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

//  Do you want to add additional headers to the request? Call additionalHeaders() before creating the rest client.
    protected RestClient createRestClient(String endpointUrl) {

        return RestClient.builder()
                .baseUrl(endpointUrl)
                .defaultHeaders(httpHeaders -> httpHeaders.addAll(requestHeaders))
                .requestInterceptor(new LoggingInterceptor())
                .build();
    }

//  Call this method to add additional headers to the request, prior to the rest client being created
    protected void additionalHeaders(@Nullable HttpHeaders headers) {
        if (headers != null) {
            this.requestHeaders.addAll(headers);
        }
    }

    private HttpHeaders createDefaultHeaders() {
        HttpHeaders defaultHeaders = new HttpHeaders();
        defaultHeaders.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        defaultHeaders.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
        return defaultHeaders;
    }
}
