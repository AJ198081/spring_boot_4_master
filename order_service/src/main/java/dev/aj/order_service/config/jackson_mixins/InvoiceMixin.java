package dev.aj.order_service.config.jackson_mixins;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import dev.aj.order_service.model.invoice.Invoice;
import org.springframework.boot.jackson.JacksonMixin;

//@JsonTypeInfo(use = JsonTypeInfo.Id.DEDUCTION, property = "type", include = JsonTypeInfo.As.PROPERTY)
@JsonTypeInfo(use = JsonTypeInfo.Id.DEDUCTION)
@JsonSubTypes({
        @JsonSubTypes.Type(value = Invoice.Paid.class),
        @JsonSubTypes.Type(value = Invoice.Unpaid.class)
})
@JacksonMixin(Invoice.class)
public class InvoiceMixin {
}
