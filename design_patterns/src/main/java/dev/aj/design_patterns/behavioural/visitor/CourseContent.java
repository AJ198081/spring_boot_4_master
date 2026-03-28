package dev.aj.design_patterns.behavioural.visitor;

public interface CourseContent {

    String getTitle();

    void accept(ContentVisitor visitor);

}
