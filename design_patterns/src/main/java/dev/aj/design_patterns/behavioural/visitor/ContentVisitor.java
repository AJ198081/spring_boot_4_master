package dev.aj.design_patterns.behavioural.visitor;

public interface ContentVisitor {
    void visit(VideoLesson videoLesson);
    void visit(TextLesson textLesson);
    void visit(Quiz quiz);
    void visit(Assignment assignment);
}
