package dev.aj.design_patterns.behavioural.visitor;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class ContentCountVisitorTest {

    public static final int HALF_AN_HOUR = 30;
    public static final String JAVA_DESIGN_PATTERNS = "Java Design Patterns";
    static Course course;

    @BeforeAll
    static void beforeAll() {

        course = Course.builder()
                .name(JAVA_DESIGN_PATTERNS)
                .build();

        course.addContent(new VideoLesson("Visitor pattern overview", "http://dev.aj/visitor_patter", HALF_AN_HOUR));
        course.addContent(new TextLesson("Visitor pattern implementation", "http://dev.aj/visitor_implementation", HALF_AN_HOUR));
        course.addContent(new Quiz("Quiz on visitor pattern", 10, HALF_AN_HOUR));
        course.addContent(new Assignment("Assignment on visitor pattern", HALF_AN_HOUR));
    }

    @Test
    public void testContentCountVisitor() {
        ContentCountVisitor contentCounter = new ContentCountVisitor();
        course.accept(contentCounter);

        Assertions.assertThat(contentCounter.getTotalContentCount())
                .isEqualTo(4);
    }

}