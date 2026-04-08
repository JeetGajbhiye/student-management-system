package com.sms.studentmanagement.repository;

import com.sms.studentmanagement.entity.Enrollment;
import com.sms.studentmanagement.entity.Enrollment.EnrollmentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {

    List<Enrollment> findByStudentId(Long studentId);
    List<Enrollment> findByCourseId(Long courseId);
    Optional<Enrollment> findByStudentIdAndCourseId(Long studentId, Long courseId);
    boolean existsByStudentIdAndCourseId(Long studentId, Long courseId);

    Page<Enrollment> findByStudentId(Long studentId, Pageable pageable);
    Page<Enrollment> findByCourseId(Long courseId, Pageable pageable);
    Page<Enrollment> findByStatus(EnrollmentStatus status, Pageable pageable);

    @Query("SELECT COUNT(e) FROM Enrollment e WHERE e.status = :status")
    long countByStatus(@Param("status") EnrollmentStatus status);

    @Query("SELECT e.status, COUNT(e) FROM Enrollment e GROUP BY e.status")
    List<Object[]> countEnrollmentsByStatus();

    @Query("SELECT e FROM Enrollment e JOIN FETCH e.student JOIN FETCH e.course WHERE e.student.id = :studentId")
    List<Enrollment> findByStudentIdWithDetails(@Param("studentId") Long studentId);
}
