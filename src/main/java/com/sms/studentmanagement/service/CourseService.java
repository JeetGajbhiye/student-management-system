package com.sms.studentmanagement.service;

import com.sms.studentmanagement.dto.CourseDto;
import com.sms.studentmanagement.dto.PagedResponse;

import java.util.List;

public interface CourseService {
    CourseDto.Response createCourse(CourseDto.Request request);
    CourseDto.Response getCourseById(Long id);
    CourseDto.Response updateCourse(Long id, CourseDto.Request request);
    void deleteCourse(Long id);
    PagedResponse<CourseDto.Response> getAllCourses(int page, int size, String sortBy, String sortDir);
    PagedResponse<CourseDto.Response> searchCourses(String keyword, int page, int size);
    List<CourseDto.Response> getCoursesByDepartment(Long departmentId);
}
