package dev.aj.design_patterns.behavioural.visitor;

import lombok.Getter;

@Getter
public class ContentCountVisitor implements ContentVisitor {

    private int videoCount = 0;
    private int textCount = 0;
    private int quizCount = 0;
    private int assignmentCount = 0;

    @Override
    public void visit(VideoLesson videoLesson) {
        videoCount++;
    }

    @Override
    public void visit(TextLesson textLesson) {
        textCount++;
    }

    @Override
    public void visit(Quiz quiz) {
        quizCount++;
    }

    @Override
    public void visit(Assignment assignment) {
        assignmentCount++;
    }

    public int getTotalContentCount() {
        return videoCount + textCount + quizCount + assignmentCount;
    }

}
