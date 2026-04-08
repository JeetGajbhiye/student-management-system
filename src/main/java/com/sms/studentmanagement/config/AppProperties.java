package com.sms.studentmanagement.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

/**
 * Type-safe configuration properties bound to the {@code app.*} prefix.
 * Registered via @EnableConfigurationProperties in StudentManagementApplication.
 */
@ConfigurationProperties(prefix = "app")
@Validated
@Getter
@Setter
public class AppProperties {

    private final Jwt jwt = new Jwt();
    private final Mail mail = new Mail();
    private final Cors cors = new Cors();

    @Getter
    @Setter
    public static class Jwt {
        @NotBlank
        private String secret = "404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970";
        @Positive
        private long expirationMs = 86_400_000L;
        @Positive
        private long refreshExpirationMs = 604_800_000L;
    }

    @Getter
    @Setter
    public static class Mail {
        private String from = "noreply@sms.com";
        private boolean enabled = false;
    }

    @Getter
    @Setter
    public static class Cors {
        private String allowedOrigins = "*";
        private String allowedMethods = "GET,POST,PUT,PATCH,DELETE,OPTIONS";
    }
}
