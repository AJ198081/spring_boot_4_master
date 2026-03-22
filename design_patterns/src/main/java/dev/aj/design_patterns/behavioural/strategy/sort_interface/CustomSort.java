package dev.aj.design_patterns.behavioural.strategy.sort_interface;

import dev.aj.design_patterns.behavioural.strategy.CourseField;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class CustomSort {

    public CourseField courseFieldName;
    public SortOrder sortOrder;

}
