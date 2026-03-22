package dev.aj.design_patterns.behavioural.strategy.sort_interface;

import dev.aj.design_patterns.behavioural.strategy.Course;

import java.util.Comparator;
import java.util.List;

public interface SortStrategy {
    void sort(List<Course> courses, SortOrder sortOrder);

    default void customSort(List<Course> courses, List<CustomSort> customSorts) {
        if (customSorts == null || customSorts.isEmpty()) {
            return;
        }

        Comparator<Course> comparator = null;

        for (CustomSort customSort : customSorts) {
            Comparator<Course> currentComparator = switch (customSort.getCourseFieldName()) {
                case NAME -> Comparator.comparing(Course::getName);
                case RATING -> Comparator.comparing(Course::getRating);
                case STUDENT_COUNT -> Comparator.comparing(Course::getStudentCount);
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
        }
    }
}
