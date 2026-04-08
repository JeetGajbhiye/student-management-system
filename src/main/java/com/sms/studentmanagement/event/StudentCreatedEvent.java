package com.sms.studentmanagement.event;

import com.sms.studentmanagement.entity.Student;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * Domain event fired when a new student record is created.
 */
@Getter
public class StudentCreatedEvent extends ApplicationEvent {

    private final Student student;

    public StudentCreatedEvent(Object source, Student student) {
        super(source);
        this.student = student;
    }
}
