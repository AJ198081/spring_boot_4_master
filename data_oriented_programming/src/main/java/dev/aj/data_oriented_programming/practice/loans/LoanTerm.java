package dev.aj.data_oriented_programming.practice.loans;

import lombok.Getter;

import java.time.Duration;
import java.util.Arrays;

public record LoanTerm(int amount, DaysDuration days) {
}

@Getter
enum DaysDuration {
    DAYS_THIRTY(30),
    DAYS_SIXTY(60),
    DAYS_NINETY(90),
    DAYS_ONE_EIGHTY(180),
    DAYS_THREE_HUNDRED_AND_SIXTY_FIVE(365);

    private final int days;

    DaysDuration(int days) {
        this.days = days;
    }

    public Duration toDuration() {

        if (Arrays.stream(DaysDuration.values())
                .noneMatch(duration -> duration.days == this.days)) {
            throw new IllegalArgumentException("Invalid days duration, only standard set of days are allowed");
        }

        return Duration.ofDays(days);
    }
}

