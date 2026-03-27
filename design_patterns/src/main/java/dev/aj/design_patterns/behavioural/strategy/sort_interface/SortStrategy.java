package dev.aj.design_patterns.behavioural.strategy.sort_interface;

import dev.aj.design_patterns.behavioural.strategy.Course;

import java.util.List;

public interface SortStrategy {
    void sort(List<Course> courses, SortOrder sortOrder);

    default void customSort(List<Course> courses, List<CustomSort> customSorts) {
        throw new UnsupportedOperationException("Custom sort is not implemented for this strategy");
    }
}
