package dev.aj.stream_gatherers.stateful;

public record Window(int size) {
    public Window {
        if (size <= 0) {
            throw new IllegalArgumentException("Window size must be a positive number.");
        }
    }
}
