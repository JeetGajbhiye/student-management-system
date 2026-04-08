package com.sms.studentmanagement.repository;

import com.sms.studentmanagement.entity.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    Optional<Student> findByEmail(String email);
    Optional<Student> findByStudentId(String studentId);
    boolean existsByEmail(String email);
    boolean existsByStudentId(String studentId);

    List<Student> findByDepartmentId(Long departmentId);

    @Query("SELECT s FROM Student s WHERE " +
           "LOWER(s.firstName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(s.lastName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(s.email) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(s.studentId) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<Student> searchStudents(@Param("keyword") String keyword, Pageable pageable);

    @Query("SELECT s FROM Student s WHERE s.department.id = :deptId")
    Page<Student> findByDepartmentId(@Param("deptId") Long deptId, Pageable pageable);

    @Query("SELECT COUNT(s) FROM Student s WHERE s.department.id = :deptId")
    long countByDepartmentId(@Param("deptId") Long deptId);

    @Query("SELECT s.department.name, COUNT(s) FROM Student s GROUP BY s.department.name")
    List<Object[]> countStudentsByDepartment();

    @Query("SELECT MAX(s.studentId) FROM Student s WHERE s.studentId LIKE :prefix%")
    Optional<String> findMaxStudentIdByPrefix(@Param("prefix") String prefix);
}
