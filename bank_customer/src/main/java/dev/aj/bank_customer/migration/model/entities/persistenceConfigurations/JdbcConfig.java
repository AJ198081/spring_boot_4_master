package dev.aj.bank_customer.migration.model.entities.persistenceConfigurations;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.data.jdbc.core.convert.JdbcCustomConversions;
import org.springframework.data.jdbc.core.mapping.JdbcValue;
import org.springframework.data.jdbc.repository.config.AbstractJdbcConfiguration;

import java.sql.JDBCType;
import java.sql.Timestamp;
import java.time.ZonedDateTime;
import java.time.temporal.TemporalAccessor;
import java.util.List;

@Configuration
public class JdbcConfig extends AbstractJdbcConfiguration {

    @Override
    public @NonNull JdbcCustomConversions jdbcCustomConversions() {
        return new JdbcCustomConversions(List.of(ZonedDateTimeToTimestampConverter.INSTANCE, TimestampTZToZonedDateTimeConverter.INSTANCE));
    }

    @WritingConverter
    public enum ZonedDateTimeToTimestampConverter implements Converter<ZonedDateTime, JdbcValue> {
        INSTANCE;

        @Override
        public JdbcValue convert(@Nullable ZonedDateTime source) {

            if (source == null) {
                return null;
            }

            return JdbcValue.of(new Timestamp(source.toInstant().toEpochMilli()), JDBCType.TIMESTAMP);
        }
    }

    @ReadingConverter
    public enum TimestampTZToZonedDateTimeConverter implements Converter<JdbcValue, ZonedDateTime> {
        INSTANCE;
        @Override
        public ZonedDateTime convert(@NonNull JdbcValue source) {

            if (source.getValue() != null) {
                return ZonedDateTime.from((TemporalAccessor) source.getValue());
            }

            return null;

        }
    }
}
