package com.sms.studentmanagement.event;

import com.sms.studentmanagement.entity.Grade;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * Domain event fired when a grade is assigned or updated.
 */
@Getter
public class GradeAssignedEvent extends ApplicationEvent {

    private final Grade grade;

    public GradeAssignedEvent(Object source, Grade grade) {
        super(source);
        this.grade = grade;
    }
}
