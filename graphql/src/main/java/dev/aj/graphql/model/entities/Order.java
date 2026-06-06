package dev.aj.graphql.model.entities;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor(staticName = "create")
@JsonPropertyOrder({"id", "description"})
public class Order {

    private Long id;
    private List<OrderItem> orderItems;
    private Status orderStatus;
    private Instant orderTimestamp;

    public enum Status {
        NEW,
        IN_PROGRESS,
        CANCELLED,
        REJECTED,
        SHIPPED,
        COMPLETED
    }

}

