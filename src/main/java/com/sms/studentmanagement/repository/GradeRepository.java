package com.sms.studentmanagement.repository;

import com.sms.studentmanagement.entity.Grade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GradeRepository extends JpaRepository<Grade, Long> {

    Optional<Grade> findByEnrollmentId(Long enrollmentId);
    boolean existsByEnrollmentId(Long enrollmentId);

    @Query("SELECT g FROM Grade g JOIN g.enrollment e WHERE e.student.id = :studentId")
    List<Grade> findByStudentId(@Param("studentId") Long studentId);

    @Query("SELECT g FROM Grade g JOIN g.enrollment e WHERE e.course.id = :courseId")
    List<Grade> findByCourseId(@Param("courseId") Long courseId);

    @Query("SELECT AVG(g.marksObtained / g.maxMarks * 100) FROM Grade g " +
           "JOIN g.enrollment e JOIN e.student s WHERE s.department.id = :deptId")
    Optional<Double> findAveragePercentageByDepartment(@Param("deptId") Long deptId);

    @Query("SELECT s.department.name, AVG(g.marksObtained / g.maxMarks * 100) " +
           "FROM Grade g JOIN g.enrollment e JOIN e.student s GROUP BY s.department.name")
    List<Object[]> findAverageGradeByDepartment();
}
