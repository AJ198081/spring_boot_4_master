package dev.aj.order_service.client;

import dev.aj.order_service.model.exception.RetryableException;
import dev.aj.order_service.model.exception.SystemError;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.client.ResourceAccessException;

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
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
