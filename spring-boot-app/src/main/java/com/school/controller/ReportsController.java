package com.school.controller;

import com.school.model.StudentCourse;
import com.school.repository.CourseRepository;
import com.school.repository.StudentRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

@RestController()
@RequestMapping(path = "/reports")
public class ReportsController {
    @Autowired
    StudentRepository studentRepository;

    @Autowired
    CourseRepository courseRepository;

    private static final Logger logger = LogManager.getLogger(ReportsController.class);

    @GetMapping("/getStudentByCourseId")
    @ResponseBody
    @ResponseStatus(code = HttpStatus.FOUND)
    public ResponseEntity<Collection<StudentCourse>> getRegisteredCourse(@RequestParam(name = "id",required = false) Integer courseId) {
        logger.info("getRegisteredCourse {} ",courseId);
        try {
            if (courseId == null) {
                return ResponseEntity.ok(studentRepository.studentwithNoCourse());
            } else {
                return ResponseEntity.ok(courseRepository.filterCoursesbyCourse(courseId));
            }
        }catch (Exception e){
            logger.info("Exception {}"+e);
            return ResponseEntity.ok(Collections.EMPTY_LIST);
        }
    }
    @GetMapping("/getCoursesByStudentId")
    @ResponseBody
    @ResponseStatus(code = HttpStatus.FOUND)
    public ResponseEntity<Collection<StudentCourse>> getRegisteredStudent(@RequestParam(name = "id",required = false) Integer studentId) {
        logger.info("getRegisteredStudent {} ",studentId);
        try {
            if (studentId == null) {
                return ResponseEntity.ok(courseRepository.courseWithNoStudent());
            } else {
                return ResponseEntity.ok(studentRepository.filterCoursesbyStudent(studentId));
            }
        }catch (Exception e){
            logger.info("Exception {}"+e);
            return ResponseEntity.ok(Collections.EMPTY_LIST);
        }
    }

}
