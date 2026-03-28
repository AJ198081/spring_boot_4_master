package dev.aj.design_patterns.behavioural.visitor;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class TextLesson implements CourseContent{

    private String title;
    private String content;
    private int estimatedMinutesToComplete;

    @Override
    public void accept(ContentVisitor visitor) {
            visitor.visit(this);
    }

    @Override
    public String getTitle() {
        return title;
    }
}
