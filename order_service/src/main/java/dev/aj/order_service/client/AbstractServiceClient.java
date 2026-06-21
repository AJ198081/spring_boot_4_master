package dev.aj.order_service.client;

import dev.aj.order_service.model.exception.ApplicationException;
import dev.aj.order_service.model.exception.DomainError;
import dev.aj.order_service.model.exception.RetryableException;
import dev.aj.order_service.model.exception.SystemError;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientException;

import java.util.function.Supplier;

@Slf4j
public abstract class AbstractServiceClient {

    public abstract String getServiceName();

    protected <T> T executeRequest(Supplier<T> supplier) {
        try {
            return supplier.get();
        } catch (ResourceAccessException e) {
                log.error("{} is unavailable. Retrying...", getServiceName());
                throw new RetryableException(new SystemError.ExternalServerError(getServiceName(), e.getMessage()));
        } catch (RestClientException e) {
            log.error("Invalid interaction with {}, due to {}", getServiceName(), e.getMessage());
            if (e.getCause() instanceof HttpMessageNotReadableException) {
                throw new ApplicationException(new DomainError.InvalidResponseBody("Invalid response body from %s".formatted(getServiceName())));
            } else {
                throw new RuntimeException(e.getMessage());
            }
        }
        catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
