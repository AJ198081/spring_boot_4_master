package dev.aj.stream_gatherers.integrators;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.stream.Gatherer;
import java.util.stream.IntStream;

@SpringBootTest(classes = FilterIntegrator.class)
class FilterIntegratorTest {

    @Autowired
    private FilterIntegrator filterIntegrator;

    @Test
    void integrationTest() {

        IntStream.rangeClosed(1, 10)
                .boxed()
                .gather(Gatherer.of(filterIntegrator))
                .forEach(element -> System.out.printf("Terminal operation on %d%n", element));

    }


}