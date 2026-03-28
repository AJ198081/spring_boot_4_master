package dev.aj.design_patterns.behavioural.visitor;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class VideoLesson implements CourseContent {

    private String title;
    private String videoUrl;
    private int durationMinutes;

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public void accept(ContentVisitor visitor) {
        visitor.visit(this);
    }
}
