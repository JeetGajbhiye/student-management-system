package com.sms.studentmanagement.util;

import com.sms.studentmanagement.config.UserDetailsImpl;
import com.sms.studentmanagement.constants.AppConstants;
import lombok.experimental.UtilityClass;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Helper methods for Spring Security context access.
 */
@UtilityClass
public class SecurityUtils {

    public static Optional<UserDetailsImpl> getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) return Optional.empty();
        if (auth.getPrincipal() instanceof UserDetailsImpl ud) return Optional.of(ud);
        return Optional.empty();
    }

    public static Optional<String> getCurrentUsername() {
        return getCurrentUser().map(UserDetailsImpl::getUsername);
    }

    public static Optional<Long> getCurrentUserId() {
        return getCurrentUser().map(UserDetailsImpl::getId);
    }

    public static Set<String> getCurrentUserRoles() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) return Set.of();
        return auth.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());
    }

    public static boolean isAdmin() {
        return getCurrentUserRoles().contains(AppConstants.ROLE_ADMIN);
    }

    public static boolean isTeacher() {
        return getCurrentUserRoles().contains(AppConstants.ROLE_TEACHER);
    }
}
