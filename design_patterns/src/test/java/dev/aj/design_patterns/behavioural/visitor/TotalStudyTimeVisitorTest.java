package dev.aj.design_patterns.behavioural.visitor;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TotalStudyTimeVisitorTest {

    public static final int HALF_AN_HOUR = 30;
    static Course course;

    @BeforeAll
    static void beforeAll() {

        course = Course.builder()
                .name("Java Design Patterns")
                .build();

        course.addContent(new VideoLesson("Visitor pattern overview", "http://dev.aj/visitor_patter", HALF_AN_HOUR));
        course.addContent(new TextLesson("Visitor pattern implementation", "http://dev.aj/visitor_implementation", HALF_AN_HOUR));
        course.addContent(new Quiz("Quiz on visitor pattern", 10, HALF_AN_HOUR));
        course.addContent(new Assignment("Assignment on a visitor pattern", HALF_AN_HOUR));
    }

    @Test
    void testTotalStudyTimeVisitor() {
        TotalStudyTimeVisitor totalStudyTimeVisitor = new TotalStudyTimeVisitor();
        course.accept(totalStudyTimeVisitor);
        assertEquals(HALF_AN_HOUR * 4, totalStudyTimeVisitor.getTotalStudyTime());
    }

}