package dev.aj.bank_customer.migration.model.entities.persistenceConfigurations;

import org.jspecify.annotations.NonNull;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.data.jdbc.repository.config.AbstractJdbcConfiguration;

import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Configuration
public class JdbcConfig extends AbstractJdbcConfiguration {

    @WritingConverter
    public enum ZonedDateTimeToOffsetDateTimeConverter implements Converter<ZonedDateTime, OffsetDateTime> {
        INSTANCE;

        @Override
        public OffsetDateTime convert(@NonNull ZonedDateTime source) {
            return source.toOffsetDateTime();
        }
    }

    @WritingConverter
    public enum ZonedDateTimeToTimestampConverter implements Converter<ZonedDateTime, Timestamp> {
        INSTANCE;

        @Override
        public Timestamp convert(@NonNull ZonedDateTime source) {
            return Timestamp.from(source.toInstant());
        }
    }

    @WritingConverter
    public enum OffsetDateTimeWritingConverter implements Converter<OffsetDateTime, OffsetDateTime> {
        INSTANCE;

        @Override
        public OffsetDateTime convert(@NonNull OffsetDateTime source) {
            return source;
        }
    }

    @ReadingConverter
    public enum OffsetDateTimeToZonedDateTimeConverter implements Converter<OffsetDateTime, ZonedDateTime> {
        INSTANCE;

        @Override
        public ZonedDateTime convert(@NonNull OffsetDateTime source) {
            return source.atZoneSameInstant(ZoneId.systemDefault());
        }
    }

/*    @Override
    protected @NonNull List<?> userConverters() {

        return Arrays.asList(
//                ZonedDateTimeToOffsetDateTimeConverter.INSTANCE,
                ZonedDateTimeToTimestampConverter.INSTANCE,
                OffsetDateTimeWritingConverter.INSTANCE,
                OffsetDateTimeToZonedDateTimeConverter.INSTANCE
        );
    }*/
}
