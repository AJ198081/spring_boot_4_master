package dev.aj.stream_gatherers.integrators;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.stream.Gatherer;
import java.util.stream.IntStream;

@SpringJUnitConfig(classes = LimitIntegrator.class)
@TestPropertySource(
        locations = "classpath:application-test.properties"
)
class LimitIntegratorTest {

    @Autowired
    private LimitIntegrator limitIntegrator;

    @Test
    void test() {
        IntStream.rangeClosed(1, 5)
                .boxed()
                .gather(Gatherer.of(limitIntegrator))
                .forEach(element -> System.out.printf("Terminal operation on %d%n", element));
    }

}