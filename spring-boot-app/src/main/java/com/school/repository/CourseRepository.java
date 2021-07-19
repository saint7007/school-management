package com.school.repository;

import com.school.model.Course;
import com.school.model.CourseRegisteration;
import com.school.model.StudentCourse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
    @Query(nativeQuery = true,value = "SELECT student_id as studentid , s2.name as studentname, course_id as courseid, c2.name as coursename FROM courseregistration as cr\n" +
            "    INNER JOIN student s2  ON cr.student_id = s2.id\n" +
            "    INNER JOIN course  c2   ON cr.course_id = c2.id where course_id= :course_id and c2.deleted=false and s2.deleted=false")
    public Collection<StudentCourse> filterCoursesbyCourse(@Param("course_id") Integer course_id);

    @Query(nativeQuery = true,value = "SELECT c2.id as courseId , c2.name as courseName FROM courseregistration as cr\n" +
            "    right JOIN course c2  ON cr.course_id = c2.id where cr.student_id is NULL and c2.deleted=false or cr.deregistered=true")
    public Collection<StudentCourse> courseWithNoStudent();

    @Query(nativeQuery = true,value = "SELECT * FROM courseregistration as cr\n" +
            "    where cr.course_id =:course_id and cr.student_id = :student_id ")
    public CourseRegisteration getRegisteredCourse(@Param("course_id") Integer course_id,@Param("course_id") Integer student_id);
}
