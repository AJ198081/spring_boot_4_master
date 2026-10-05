package dev.aj.bank_customer.model.entities.sequence_generators;

import org.hibernate.MappingException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.generator.GeneratorCreationContext;
import org.hibernate.id.enhanced.SequenceStyleGenerator;
import org.springframework.stereotype.Component;

import java.util.Properties;

@Component
public class CustomerSequenceGenerator extends SequenceStyleGenerator {

    public static final String SEQUENCE_NAME = "customer_sequence";

    @Override
    public Object generate(SharedSessionContractImplementor session, Object object) {

        return String.format("%010d", Long.parseLong(super.generate(session, object).toString()));
    }

    @Override
    public void configure(GeneratorCreationContext creationContext, Properties parameters) throws MappingException {
        super.configure(creationContext, parameters);
    }
}
