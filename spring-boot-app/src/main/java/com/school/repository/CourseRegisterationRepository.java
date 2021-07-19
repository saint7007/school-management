package com.school.repository;

import com.school.model.CourseRegisteration;
import com.school.model.CourseRegisterationResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Collection;

@Repository
public interface CourseRegisterationRepository extends JpaRepository<CourseRegisteration, Integer> {


    @Query(nativeQuery = true, value = "SELECT * FROM courseregistration as cr\n" +
            "    where cr.course_id =:course_id and cr.student_id = :student_id ")
    public Collection<CourseRegisterationResult> getRegisteredCourse(@Param("course_id") Integer course_id, @Param("student_id") Integer student_id);

    @Query(nativeQuery = true, value = "SELECT * FROM courseregistration as cr\n" +
            "    where cr.course_id =:course_id ")
    public Collection<CourseRegisterationResult> getRegistertionByCourseId(@Param("course_id") Integer course_id);

    @Query(nativeQuery = true, value = "SELECT * FROM courseregistration as cr\n" +
            "    where cr.course_id =:student_id ")
    public Collection<CourseRegisterationResult> getRegistertionByStudentId(@Param("student_id") Integer student_id);
}
