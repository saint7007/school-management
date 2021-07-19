package com.school.repository;

import com.school.model.Student;
import com.school.model.StudentCourse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Collection;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {


    @Query(nativeQuery = true,value = "SELECT student_id as studentId , s2.name as studentName, course_id as courseId, c2.name as courseName FROM courseregistration as cr\n" +
            "    INNER JOIN student s2  ON cr.student_id = s2.id\n" +
            "    INNER JOIN course  c2   ON cr.course_id = c2.id where student_id= :student_id and c2.deleted=false and s2.deleted=false and cr.deregistered=false")
    public Collection<StudentCourse> filterCoursesbyStudent(@Param("student_id") int student_id);



    @Query(nativeQuery = true,value = "SELECT s2.id as studentId , s2.name as studentName, course_id as courseId FROM courseregistration as cr\n" +
            "    right JOIN student s2  ON cr.student_id = s2.id where cr.course_id is NULL and s2.deleted=false or cr.deregistered=true")
    public Collection<StudentCourse> studentwithNoCourse();




}
