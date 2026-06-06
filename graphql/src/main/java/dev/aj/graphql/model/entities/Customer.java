package dev.aj.graphql.model.entities;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

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
    private List<Order> orders;

    public Customer toNewCustomer() {
        return new Customer(this.getId(), this.getFirstName(), this.getLastName(), this.getEmail(), this.getAge(), new ArrayList<>());
    }
}
