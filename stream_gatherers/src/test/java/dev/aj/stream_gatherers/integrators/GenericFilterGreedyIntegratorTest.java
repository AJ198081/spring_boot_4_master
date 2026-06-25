package dev.aj.stream_gatherers.integrators;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.TestPropertySource;

import java.util.stream.Gatherer;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@ExtendWith(MockitoExtension.class)
@TestPropertySource(properties = {
        "logging.level.dev.aj.stream_gatherers.integrators.GenericFilterGreedyIntegrator=none",
        "logging.level.dev.aj.stream_gatherers.integrators.GenericFilterIntegrator=none"
})
class GenericFilterGreedyIntegratorTest {

    private Stream<Integer> integerStream;
    Logger logger = LoggerFactory.getLogger(GenericFilterGreedyIntegratorTest.class);
    private GenericFilterGreedyIntegrator<Integer> filteringIntegrator;

    @BeforeEach
    void setUp() {

        integerStream = IntStream.rangeClosed(1, 1_000_000)
                .parallel()
                .boxed();

        filteringIntegrator = new GenericFilterGreedyIntegrator<>(i -> i % 3 == 0);
    }

    @Test
    void testSequentialGatherer() {
        Statistics gathererStats = new Statistics("Sequential Gatherer");
        long processedCount = integerStream
                .gather(Gatherer.ofSequential(filteringIntegrator))
                .count();
        gathererStats = gathererStats.withEndTime(new TimeStamp(System.currentTimeMillis()));
        gathererStats.printTimeTaken();
    }

    @Test
    void testParallelGatherer() {
        Statistics gathererStats = new Statistics("Parallel Gatherer");
        long processedCount = integerStream
                .gather(Gatherer.of(filteringIntegrator))
                .count();
        gathererStats = gathererStats.registerEndTime();
        gathererStats.printTimeTaken();
    }


    record Statistics(String process, TimeStamp startTime, TimeStamp endTime) {

        static Logger logger = LoggerFactory.getLogger(Statistics.class);

        public Statistics(String process) {
            this(process, new TimeStamp(System.currentTimeMillis()), new TimeStamp(System.currentTimeMillis()));
        }

        public Statistics withEndTime(TimeStamp endTime) {
            return new Statistics(this.process(), this.startTime(), endTime);
        }

        public Statistics registerEndTime() {
            return this.withEndTime(new TimeStamp(System.currentTimeMillis()));
        }

        public Long subtract(TimeStamp other) {
            return this.endTime().timeStamp - other.timeStamp;
        }

        public Long calculateTimeTaken() {
            return this.endTime().timeStamp - this.startTime().timeStamp;
        }

        public void printStats() {
            logger.info("Statistics: {}, total time taken {}", this, this.calculateTimeTaken());
        }

        public void printTimeTaken() {
            logger.info("{} took : {}ms", this.process, this.calculateTimeTaken());
        }
    }

    record TimeStamp(Long timeStamp) {
    }



}