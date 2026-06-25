package dev.aj.stream_gatherers.stateful;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.stream.IntStream;
import java.util.stream.Stream;

class LimitIntegratorTest {

    Stream<Integer> stream;

    @BeforeEach
    void setUp() {
        stream = IntStream.rangeClosed(1, 10)
                .boxed();
    }

    @Test
    void limitIntegratorTest() {

        stream.gather(StatefulIntegrator.limit(5))
                .forEach(System.out::println);

    }

    @Test
    void distinctIntegratorTest() {

        Stream.concat(stream, IntStream.rangeClosed(1, 10).boxed())
                .gather(StatefulIntegrator.distinct())
                .forEach(System.out::println);
    }
}