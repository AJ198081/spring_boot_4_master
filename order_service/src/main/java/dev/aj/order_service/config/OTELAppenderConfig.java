package dev.aj.order_service.config;

import io.opentelemetry.api.OpenTelemetry;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OTELAppenderConfig implements InitializingBean {

    private final OpenTelemetry openTelemetry;

    @Override
    public void afterPropertiesSet() throws Exception {
    }
}
