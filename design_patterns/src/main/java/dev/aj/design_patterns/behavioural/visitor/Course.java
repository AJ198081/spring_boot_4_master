package dev.aj.design_patterns.behavioural.visitor;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class Course {

    private String name;

    @Builder.Default
    private List<CourseContent> contents = new ArrayList<>();

    public void addContent(CourseContent content) {
        contents.add(content);
    }

    public void accept(ContentVisitor visitor) {
        contents.forEach(content -> content.accept(visitor));
    }
}
