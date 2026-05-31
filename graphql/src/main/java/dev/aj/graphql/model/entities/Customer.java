package dev.aj.graphql.model.entities;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor(staticName = "create")
@JsonPropertyOrder({"id", "firstName", "lastName", "email", "age"})
public class Customer {

    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private Integer age;

}
