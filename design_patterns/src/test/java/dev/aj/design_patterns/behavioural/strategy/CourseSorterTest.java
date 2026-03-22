package dev.aj.design_patterns.behavioural.strategy;

import dev.aj.design_patterns.behavioural.strategy.sort_impl.CustomSortStrategy;
import dev.aj.design_patterns.behavioural.strategy.sort_impl.NameSortStrategy;
import dev.aj.design_patterns.behavioural.strategy.sort_impl.RatingSortStrategy;
import dev.aj.design_patterns.behavioural.strategy.sort_impl.StudentCountSortStrategy;
import dev.aj.design_patterns.behavioural.strategy.sort_interface.CustomSort;
import dev.aj.design_patterns.behavioural.strategy.sort_interface.SortOrder;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CourseSorterTest {

    public static final String JAVA = "Java";
    public static final String PYTHON = "Python";

    private static List<Course> courses;

    @BeforeAll
    static void beforeAll() {
        Course java = Course.builder()
                .name(JAVA)
                .studentCount(100)
                .rating(4.9)
                .build();

        Course python = Course.builder()
                .name(PYTHON)
                .studentCount(50)
                .rating(4.8)
                .build();

        courses = Arrays.asList(java, python);
    }

    @Nested
    class testNamedSortStrategy {

        CourseSorter courseSorter = new CourseSorter(new NameSortStrategy());

        @Test
        void sortCoursesByNameAscending() {
            courseSorter.sort(courses, SortOrder.ASC);

            assertEquals(2, courses.size());

            assertEquals(JAVA, courses.getFirst().getName());
            assertEquals(PYTHON, courses.getLast().getName());
        }

        @Test
        void sortCoursesByNameDescending() {
            courseSorter.sort(courses, SortOrder.DESC);
            assertEquals(2, courses.size());
            assertEquals(PYTHON, courses.getFirst().getName());
            assertEquals(JAVA, courses.getLast().getName());
        }
    }

    @Nested
    class testRatingSortStrategy {
        CourseSorter courseSorter = new CourseSorter(new RatingSortStrategy());

        @Test
        void sortCoursesByRatingAscending() {
            courseSorter.sort(courses, SortOrder.ASC);

            assertEquals(2, courses.size());
            assertEquals(PYTHON, courses.getFirst().getName());
            assertEquals(JAVA, courses.getLast().getName());
        }

        @Test
        void sortCoursesByRatingDescending() {
            courseSorter.sort(courses, SortOrder.DESC);

            assertEquals(2, courses.size());
            assertEquals(JAVA, courses.getFirst().getName());
            assertEquals(PYTHON, courses.getLast().getName());
        }
    }

    @Nested
    class testStudentCountSortStrategy {

        CourseSorter courseSorter = new CourseSorter(new StudentCountSortStrategy());

        @Test
        void testSortCoursesByStudentCountAscending() {
            courseSorter.sort(courses, SortOrder.ASC);

            assertEquals(2, courses.size());
            assertEquals(PYTHON, courses.getFirst().getName());
            assertEquals(JAVA, courses.getLast().getName());
        }

        @Test
        void testSortCoursesByStudentCountDescending() {
            courseSorter.sort(courses, SortOrder.DESC);

            assertEquals(2, courses.size());
            assertEquals(JAVA, courses.getFirst().getName());
            assertEquals(PYTHON, courses.getLast().getName());
        }
    }

    @Nested
    class testCustomSort {
        CourseSorter courseSorter = new CourseSorter(new CustomSortStrategy());

        @Test
        void testCustomSortMultipleCriteria() {

            List<CustomSort> customSorts = List.of(
                    CustomSort.builder().courseFieldName(CourseField.NAME).sortOrder(SortOrder.ASC).build(),
                    CustomSort.builder().courseFieldName(CourseField.RATING).sortOrder(SortOrder.ASC).build()
            );

            courseSorter.customSort(courses, customSorts);

            assertEquals(2, courses.size());
            assertEquals(JAVA, courses.getFirst().getName());
            assertEquals(PYTHON, courses.getLast().getName());

            // Test Descending
            customSorts = List.of(
                    CustomSort.builder().courseFieldName(CourseField.STUDENT_COUNT).sortOrder(SortOrder.DESC).build(),
                    CustomSort.builder().courseFieldName(CourseField.RATING).sortOrder(SortOrder.DESC).build()
            );

            courseSorter.customSort(courses, customSorts);
            assertEquals(JAVA, courses.getFirst().getName());
            assertEquals(PYTHON, courses.getLast().getName());
        }
    }

}
