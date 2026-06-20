package dev.aj.order_service.config;

import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NullMarked;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;

@Slf4j
@NullMarked
public class LoggingInterceptor implements ClientHttpRequestInterceptor {

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {

        log.info("\nRequest: {}", request.getMethod() + " " + request.getURI());
        log.info("Headers: {}", request.getHeaders());

        if (body.length > 0) {
            log.info("Body: {}\n", new String(body));
        }

        return execution.execute(request, body);
    }
}
