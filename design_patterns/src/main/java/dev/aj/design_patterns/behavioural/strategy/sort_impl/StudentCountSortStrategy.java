package dev.aj.design_patterns.behavioural.strategy.sort_impl;

import dev.aj.design_patterns.behavioural.strategy.Course;
import dev.aj.design_patterns.behavioural.strategy.sort_interface.SortOrder;
import dev.aj.design_patterns.behavioural.strategy.sort_interface.SortStrategy;

import java.util.Comparator;
import java.util.List;

public class StudentCountSortStrategy implements SortStrategy {

    @Override
    public void sort(List<Course> courses, SortOrder sortOrder) {

        Comparator<Course> comparator = Comparator.comparing(Course::getStudentCount);

        if (sortOrder == SortOrder.DESC) {
            comparator = comparator.reversed();
        }

        courses.sort(comparator);
    }

}
