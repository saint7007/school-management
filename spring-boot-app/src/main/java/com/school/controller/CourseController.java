package com.school.controller;

import com.school.model.Course;
import com.school.service.CourseRegisterService;
import com.school.service.CourseService;
import com.school.service.StudentService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController()
@RequestMapping(path = "/course")
public class CourseController {
    private static final Logger logger = LogManager.getLogger(CourseController.class);

    @Autowired
    CourseService courseService;

    @PostMapping("/insertOne")
    @ResponseBody
    @ResponseStatus(code = HttpStatus.CREATED)
    public Course insertCourse(@Valid @RequestBody Course course) {
        logger.info("insertOne Course obj {} "+course.toString());
        try {
            courseService.create(course);
            return course;
        }catch (Exception e){
            logger.info("Exception {}"+e);
           return new Course();
        }
    }

    @PostMapping("/insertMany")
    @ResponseBody
    @ResponseStatus(code = HttpStatus.CREATED)
    public int insertCourses(@Valid @RequestBody List<Course> courses) {
        int result = 0;
        logger.info("insertMany Course list size {} "+courses.size());
        try {
            result = courseService.saveAll(courses);
        } catch (DataAccessException e) {
            logger.info("Exception {}"+e);
        }
        return result;
    }

    @GetMapping("/readOne")
    @ResponseBody
    @ResponseStatus(code = HttpStatus.ACCEPTED)
    public Course getCourse(@RequestParam(name = "id",required = true) Integer courseId) {
        logger.info("readOne courseId  {} "+courseId);
        try {
            return courseService.read(courseId.longValue());
        }catch (Exception e){
            logger.info("Exception {}"+e);
            return new Course();
        }
    }

    @PutMapping("/updateOne")
    @ResponseBody
    @ResponseStatus(code = HttpStatus.ACCEPTED)
    public int updateCourse(@Valid @RequestBody Course course) {
        logger.info("updateOne Course obj {} "+course.toString());
        try{
            return courseService.update(course);
        } catch (Exception e){
            logger.info("Exception {}"+e);
            return 0;
        }
    }

    @DeleteMapping("/deleteOne")
    @ResponseBody
    @ResponseStatus(code = HttpStatus.ACCEPTED)
    public int deleteCourse(@Valid @RequestBody Course course) {
        logger.info("deleteOne Course obj {} "+course.toString());
        int result = 0;
        try {
            result = courseService.delete(course);
        } catch (Exception e) {
        }
        return result;
    }

}
