package dev.aj.design_patterns.behavioural.strategy.sort_impl;

import dev.aj.design_patterns.behavioural.strategy.Course;
import dev.aj.design_patterns.behavioural.strategy.sort_interface.CustomSort;
import dev.aj.design_patterns.behavioural.strategy.sort_interface.SortOrder;
import dev.aj.design_patterns.behavioural.strategy.sort_interface.SortStrategy;
import lombok.extern.slf4j.Slf4j;

import java.util.Comparator;
import java.util.List;

@Slf4j
public class CustomSortStrategy implements SortStrategy {

    @Override
    public void sort(List<Course> courses, SortOrder sortOrder) {
      log.error("Custom sort strategy is not implemented");
    }

    @Override
    public void customSort(List<Course> courses, List<CustomSort> customSorts) {

        Comparator<Course> comparator = null;

        for (CustomSort customSort : customSorts) {

            Comparator<Course> currentComparator = switch (customSort.getCourseFieldName()) {
                case NAME -> Comparator.comparing(Course::getName);
                case STUDENT_COUNT -> Comparator.comparing(Course::getStudentCount);
                case RATING -> Comparator.comparing(Course::getRating);
            };

            if (customSort.getSortOrder() == SortOrder.DESC) {
                currentComparator = currentComparator.reversed();
            }
            if (comparator == null) {
                comparator = currentComparator;
            } else {
                comparator = comparator.thenComparing(currentComparator);
            }
        }

        if (comparator != null) {
            courses.sort(comparator);
        } else {
            log.error("No valid custom sort criteria provided");
        }

    }
}
