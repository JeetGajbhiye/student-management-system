package com.sms.studentmanagement.scheduler;

import com.sms.studentmanagement.entity.Enrollment;
import com.sms.studentmanagement.entity.Enrollment.EnrollmentStatus;
import com.sms.studentmanagement.repository.EnrollmentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class EnrollmentCleanupScheduler {

    private final EnrollmentRepository enrollmentRepository;

    @Scheduled(cron = "0 0 0 * * ?")
    @Transactional
    public void dropStalePendingEnrollments() {
        LocalDate cutoff = LocalDate.now().minusDays(7);
        List<Enrollment> stale = enrollmentRepository.findAll().stream()
                .filter(e -> e.getStatus() == EnrollmentStatus.PENDING
                        && e.getEnrollmentDate().isBefore(cutoff))
                .toList();

        stale.forEach(e -> {
            e.setStatus(EnrollmentStatus.DROPPED);
            log.info("[SCHEDULER] Dropped stale enrollment id={} student={}", e.getId(),
                    e.getStudent().getStudentId());
        });

        if (!stale.isEmpty()) {
            enrollmentRepository.saveAll(stale);
            log.info("[SCHEDULER] Dropped {} stale pending enrollments", stale.size());
        }
    }

    @Scheduled(cron = "0 0 6 ? * SUN")
    public void weeklyEnrollmentSummary() {
        long total  = enrollmentRepository.count();
        long active = enrollmentRepository.countByStatus(EnrollmentStatus.ACTIVE);
        log.info("[SCHEDULER] Weekly summary – total={}, active={}", total, active);
    }
}
