package dev.aj.design_patterns.behavioural.concurrency.structured;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.util.concurrent.Callable;

@Slf4j
@RequiredArgsConstructor
public class LongRunningTask implements Callable<TaskResponse> {

    private final String taskName;
    private final int taskDurationSeconds;
    private final String output;
    private final int failTaskAfterSeconds;

    @Override
    public TaskResponse call() {

        logTaskInfo("Starting");

        TaskResponse taskResponse;

        long startTime = System.currentTimeMillis();
        long endTime;

        int numSeconds = 0;

        try {

            while (numSeconds++ < this.taskDurationSeconds) {


                logTaskInfo("Running for %d seconds".formatted(numSeconds - 1));

                Thread.sleep(Duration.ofSeconds(1));

                if (numSeconds >= this.failTaskAfterSeconds) {
                    throwInterruptedException();
                }
            }

        } catch (InterruptedException e) {
            log.error("Thread Interrupted - {}", e.getMessage());
        } finally {
            logTaskInfo("Completed");
            endTime = System.currentTimeMillis();
            taskResponse = new TaskResponse(this.taskName, this.output, endTime - startTime);
        }

        return taskResponse;
    }

    private void throwInterruptedException() throws InterruptedException {
            throw new InterruptedException("Thread was unexpectedly interrupted.");
    }

    public void logTaskInfo(String message) {
        log.info("{} : Task {}", message, this.taskName);
    }
}
