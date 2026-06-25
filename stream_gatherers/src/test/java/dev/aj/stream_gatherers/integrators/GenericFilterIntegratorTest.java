package dev.aj.stream_gatherers.integrators;

import org.junit.jupiter.api.Test;

import java.util.stream.Gatherer;
import java.util.stream.IntStream;

class GenericFilterIntegratorTest {

    @Test
    void genericFilterTest() {
        IntStream.rangeClosed(1, 10)
                .boxed()
                .gather(Gatherer.of(new GenericFilterIntegrator<>(i -> i % 3 == 0)))
                .forEach(element -> System.out.printf("Terminal operation on %d%n", element));
    }

}