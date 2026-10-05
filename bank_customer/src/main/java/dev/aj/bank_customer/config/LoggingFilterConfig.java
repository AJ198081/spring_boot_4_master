package dev.aj.bank_customer.config;

import jakarta.persistence.EntityManagerFactory;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.StopWatch;
import org.hibernate.SessionFactory;
import org.hibernate.stat.Statistics;
import org.jspecify.annotations.NullMarked;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.UUID;

import static dev.aj.commons.constants.ApplicationConstants.METHOD;
import static dev.aj.commons.constants.ApplicationConstants.PATH;
import static dev.aj.commons.constants.ApplicationConstants.STATUS;
import static dev.aj.commons.constants.ApplicationConstants.TIME_TAKEN;
import static dev.aj.commons.constants.ApplicationConstants.X_REQUEST_ID;

@NullMarked
@Slf4j
@Component
@RequiredArgsConstructor
public class LoggingFilterConfig extends OncePerRequestFilter {

    private final EntityManagerFactory entityManagerFactory;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        StopWatch stopWatch = StopWatch.createStarted();

        String correlationId = request.getHeader(X_REQUEST_ID);

        if (correlationId == null || correlationId.isBlank()) {
            correlationId = UUID.randomUUID().toString();
        }

        MDC.put(X_REQUEST_ID, correlationId);
        MDC.put(METHOD, request.getMethod());
        MDC.put(PATH, request.getRequestURI());

        response.setHeader(X_REQUEST_ID, correlationId);

        log.info("{} request received with correlationId: {} to path: {}",
                MDC.get(METHOD), MDC.get(X_REQUEST_ID), MDC.get(PATH));

        try {
            filterChain.doFilter(request, response);
        } finally {
            stopWatch.stop();

            MDC.put(STATUS, Integer.toString(response.getStatus()));
            MDC.put(TIME_TAKEN, Long.toString(stopWatch.getTime()));

            NumberFormat numberFormat = NumberFormat.getInstance();
            numberFormat.setRoundingMode(RoundingMode.HALF_UP);
            numberFormat.setMaximumFractionDigits(2);

            log.info("Request completed correlationId: {}, at path: {}, with status: {}, and took {} ms",
                    MDC.get(X_REQUEST_ID), MDC.get(PATH), MDC.get(STATUS), numberFormat.format(Long.valueOf(MDC.get(TIME_TAKEN))));

            Statistics statistics = entityManagerFactory.unwrap(SessionFactory.class).getStatistics();

            // Can enable statistics via properties file too
            statistics.setStatisticsEnabled(true);

            log.info("Second level cache regions: {}", String.join(",", statistics.getSecondLevelCacheRegionNames()));

            statistics.logSummary();

            MDC.clear();
        }


    }
}
