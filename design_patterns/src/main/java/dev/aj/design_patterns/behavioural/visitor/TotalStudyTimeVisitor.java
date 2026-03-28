package dev.aj.design_patterns.behavioural.visitor;

import lombok.Getter;

@Getter
public class TotalStudyTimeVisitor implements ContentVisitor{
    private int totalStudyTime = 0;

    @Override
    public void visit(VideoLesson videoLesson) {
        totalStudyTime += videoLesson.getDurationMinutes();
    }

    @Override
    public void visit(TextLesson textLesson) {
        totalStudyTime += textLesson.getEstimatedMinutesToComplete();
    }

    @Override
    public void visit(Quiz quiz) {
        totalStudyTime += quiz.getEstimatedMinutesToComplete();
    }

    @Override
    public void visit(Assignment assignment) {
        totalStudyTime += assignment.getEstimatedMinutesToComplete();
    }
}
