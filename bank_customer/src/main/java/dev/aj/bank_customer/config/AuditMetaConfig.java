package dev.aj.bank_customer.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.auditing.DateTimeProvider;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.Optional;

@Configuration
@RequiredArgsConstructor
@EnableJpaAuditing(auditorAwareRef = "auditorProvider", dateTimeProviderRef = "dateTimeProvider")
public class AuditMetaConfig {

        @Bean
        public AuditorAware<String> auditorProvider() {
            return () -> {
//                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//                if (authentication != null && authentication.getPrincipal() instanceof SecurityUser user) {
//                    return Optional.of("%s %s".formatted(user.getUsername(), user.getRoles()));
//                }
                return Optional.of("AJ");
            };
        }

        @Bean
        public DateTimeProvider dateTimeProvider() {
            return () -> Optional.of(java.time.ZonedDateTime.now());
        }

}
