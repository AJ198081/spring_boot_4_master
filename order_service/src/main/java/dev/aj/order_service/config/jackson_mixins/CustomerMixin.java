package dev.aj.order_service.config.jackson_mixins;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.springframework.boot.jackson.JacksonMixin;

//@JsonTypeInfo(use = JsonTypeInfo.Id.DEDUCTION, property = "type", include = JsonTypeInfo.As.PROPERTY)
@JsonTypeInfo(use = JsonTypeInfo.Id.DEDUCTION)
@JsonSubTypes({
        @JsonSubTypes.Type(value = dev.aj.order_service.model.customer.Customer.RetailCustomer.class),
        @JsonSubTypes.Type(value = dev.aj.order_service.model.customer.Customer.WholesaleCustomer.class)
})
@JacksonMixin(dev.aj.order_service.model.customer.Customer.class)
public class CustomerMixin {
}
