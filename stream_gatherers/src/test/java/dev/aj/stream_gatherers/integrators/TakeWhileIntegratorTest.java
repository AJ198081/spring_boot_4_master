package dev.aj.stream_gatherers.integrators;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.stream.Gatherer;
import java.util.stream.IntStream;

class TakeWhileIntegratorTest {

    private IntStream intStream;


    @BeforeEach
    void setUp() {
        intStream = IntStream.rangeClosed(1, 10);
    }

    @Test
    void integrate() {

        intStream
                .boxed()
                .gather(Gatherer.of(new TakeWhileIntegrator(i -> i <= 5)))
                .forEach(System.out::println);

    }

}