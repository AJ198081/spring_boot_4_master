package dev.aj.design_patterns.behavioural.interpreter;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class JoinExpression implements Expression {

    private String channelName;

    @Override
    public void interpret(ChatContext context) {
        System.out.printf("%s joined the channel %s%n", context.getCurrentUser(), channelName);
    }
}
