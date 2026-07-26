package dev.aj.commons.rest.utils;

import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NullMarked;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.StopWatch;

import java.io.IOException;
import java.net.URI;
import java.util.concurrent.TimeUnit;

@Slf4j
@NullMarked
public class LoggingInterceptor implements ClientHttpRequestInterceptor {

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {

        URI targetUri = request.getURI();

        if (body.length > 0) {
            log.debug("""
                            \nRequesting : {}
                            to {},
                            Headers: {},
                            Body: {}
                            """,
                    request.getMethod(),
                    targetUri,
                    request.getHeaders(),
                    new String(body));
        } else {
            log.debug("""
                            \nRequesting : {}  to {},
                            Headers: {}
                            """,
                    request.getMethod(),
                    targetUri,
                    request.getHeaders());
        }

        StopWatch stopWatch = new StopWatch(targetUri.toString());
        stopWatch.start(request.getMethod().toString());

        ClientHttpResponse clientHttpResponse = execution.execute(request, body);

        stopWatch.stop();

        log.info("""
                        \nStatus code: {},
                        {}
                        """,
                clientHttpResponse.getStatusCode(),
                stopWatch.prettyPrint(TimeUnit.MILLISECONDS)
        );

        return clientHttpResponse;
    }
}
