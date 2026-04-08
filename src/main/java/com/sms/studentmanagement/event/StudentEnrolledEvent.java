package com.sms.studentmanagement.event;

import com.sms.studentmanagement.entity.Enrollment;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * Domain event fired whenever a student is successfully enrolled in a course.
 * Listeners (e.g., email notification) run asynchronously.
 */
@Getter
public class StudentEnrolledEvent extends ApplicationEvent {

    private final Enrollment enrollment;

    public StudentEnrolledEvent(Object source, Enrollment enrollment) {
        super(source);
        this.enrollment = enrollment;
    }
}
