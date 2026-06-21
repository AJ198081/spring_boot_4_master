package dev.aj.order_service.client;

import dev.aj.order_service.model.exception.ApplicationException;
import dev.aj.order_service.model.exception.RetryableException;
import dev.aj.order_service.model.exception.SystemError;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.resilience.annotation.Retryable;
import org.springframework.web.client.HttpStatusCodeException;

import java.util.function.Supplier;

@Slf4j
public abstract class AbstractServiceClient {

    private final Environment environment;

    protected AbstractServiceClient(Environment environment) {
        this.environment = environment;
    }

    protected String getServiceName() {
        return environment.getProperty("spring.application.name");
    }

    @Retryable(
            maxDelay = 15000,
            value = {RetryableException.class},
            delay = 500L,
            multiplier = 2,
            jitter = 20,
            maxRetries = 5
    )
    protected <T> T executeRequest(Supplier<T> supplier) {
        try {
            return supplier.get();
        } catch (HttpStatusCodeException e) {
            if (e.getStatusCode().equals(HttpStatus.SERVICE_UNAVAILABLE)) {
                log.error("Service is unavailable for {}. Retrying...", getServiceName());
                throw new RetryableException(new SystemError.ExternalServerError(getServiceName(), e.getMessage()));
            }
            throw new ApplicationException(new SystemError.ExternalServerError(getServiceName(), e.getMessage()));
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
