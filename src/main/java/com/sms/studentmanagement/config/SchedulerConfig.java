package com.sms.studentmanagement.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Enables Spring's scheduled task execution.
 * Actual scheduled tasks live in the {@code scheduler} package.
 */
@Configuration
@EnableScheduling
public class SchedulerConfig {
}
