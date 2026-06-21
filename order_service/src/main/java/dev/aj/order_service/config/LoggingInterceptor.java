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

        if (body.length > 0) {
            log.info("""
                            \nRequesting : {}  to {},
                            Headers: {},
                            Body: {}
                            """,
                    request.getMethod(),
                    request.getURI(),
                    request.getHeaders(),
                    new String(body));
        } else {
            log.info("""
                            \nRequesting : {}  to {},
                            Headers: {}
                            """,
                    request.getMethod(),
                    request.getURI(),
                    request.getHeaders());
        }

        ClientHttpResponse clientHttpResponse = execution.execute(request, body);

        log.info("""
                \nResponse received from {},
                Status code: {},
                Headers: {},
                Body: {}
                """,
                request.getURI(),
                clientHttpResponse.getStatusCode(), clientHttpResponse.getHeaders(),
                clientHttpResponse.getBody());

        return clientHttpResponse;
    }
}
