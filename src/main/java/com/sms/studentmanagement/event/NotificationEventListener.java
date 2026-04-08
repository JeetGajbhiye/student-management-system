package com.sms.studentmanagement.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * Async listener for application domain events.
 * In production wire in a real EmailService or push-notification gateway.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class NotificationEventListener {

    @Async("taskExecutor")
    @EventListener
    public void onStudentCreated(StudentCreatedEvent event) {
        log.info("[EVENT] StudentCreated – studentId={}, email={}",
                event.getStudent().getStudentId(),
                event.getStudent().getEmail());
        // TODO: send welcome e-mail via MailService
    }

    @Async("taskExecutor")
    @EventListener
    public void onStudentEnrolled(StudentEnrolledEvent event) {
        String studentName = event.getEnrollment().getStudent().getFirstName()
                + " " + event.getEnrollment().getStudent().getLastName();
        String courseCode = event.getEnrollment().getCourse().getCode();
        log.info("[EVENT] StudentEnrolled – student='{}', course='{}'", studentName, courseCode);
        // TODO: send enrollment confirmation e-mail
    }

    @Async("taskExecutor")
    @EventListener
    public void onGradeAssigned(GradeAssignedEvent event) {
        log.info("[EVENT] GradeAssigned – enrollmentId={}, grade='{}', percentage={}%",
                event.getGrade().getEnrollment().getId(),
                event.getGrade().getGradeLetter(),
                event.getGrade().getPercentage());
        // TODO: notify student of grade
    }
}
