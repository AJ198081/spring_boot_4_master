package dev.aj.order_service.client;

import dev.aj.order_service.model.exception.ApplicationException;
import dev.aj.order_service.model.exception.RetryableException;
import dev.aj.order_service.model.exception.SystemError;
import org.springframework.http.HttpStatus;
import org.springframework.resilience.annotation.Retryable;
import org.springframework.web.client.HttpStatusCodeException;

import java.util.function.Supplier;

public abstract class AbstractServiceClient {

    protected abstract String getServiceName();

    @Retryable(
            maxDelay = 10000,
            value = {RetryableException.class},
            delay = 1000L,
            multiplier = 2,
            jitter = 20,
            maxRetries = 4
    )
    protected <T> T executeRequest(Supplier<T> supplier) {
        try {
            return supplier.get();
        } catch (HttpStatusCodeException e) {
            if (e.getStatusCode().equals(HttpStatus.SERVICE_UNAVAILABLE)) {
                throw new RetryableException(new SystemError.ExternalServerError(getServiceName(), e.getMessage()));
            }
            throw new ApplicationException(new SystemError.ExternalServerError(getServiceName(), e.getMessage()));
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
