package com.sms.studentmanagement.audit;

import com.sms.studentmanagement.config.UserDetailsImpl;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Provides the current authenticated username for JPA auditing
 * ({@code @CreatedBy} / {@code @LastModifiedBy}).
 */
@Component("auditorAware")
public class AuditAwareImpl implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()
                || authentication.getPrincipal().equals("anonymousUser")) {
            return Optional.of("system");
        }
        if (authentication.getPrincipal() instanceof UserDetailsImpl userDetails) {
            return Optional.of(userDetails.getUsername());
        }
        return Optional.of(authentication.getName());
    }
}
