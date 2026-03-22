package dev.aj.design_patterns.behavioural.strategy;

import dev.aj.design_patterns.behavioural.strategy.sort_interface.CustomSort;
import dev.aj.design_patterns.behavioural.strategy.sort_interface.SortOrder;
import dev.aj.design_patterns.behavioural.strategy.sort_interface.SortStrategy;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;

import java.util.List;

@RequiredArgsConstructor
public class CourseSorter implements SortStrategy {

    private final SortStrategy sortStrategy;

    @Override
    public void sort(List<@NonNull Course> courses, @NonNull SortOrder sortOrder) {
        sortStrategy.sort(courses, sortOrder);
    }

    @Override
    public void customSort(List<Course> courses, List<CustomSort> customSorts) {
        sortStrategy.customSort(courses, customSorts);
    }
}
