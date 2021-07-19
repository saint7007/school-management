package com.school.controller;

import com.school.model.Course;
import com.school.model.Student;
import com.school.service.StudentService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.List;

@RestController()
@RequestMapping(path = "/student")
public class StudentController {
    @Autowired
    StudentService studentService;
    private static final Logger logger = LogManager.getLogger(StudentController.class);

    @PostMapping("/insertOne")
    @ResponseBody
    @ResponseStatus(code = HttpStatus.CREATED)
    public Student insertStudent(@Valid @RequestBody Student student) {
        logger.info("insertStudent {}",student.toString());
        try {
            studentService.create(student);
            return student;
        }catch (Exception e){
            logger.info("Exception {}"+e);
            return new Student();
        }
    }

    @PostMapping("/insertMany")
    @ResponseBody
    @ResponseStatus(code = HttpStatus.CREATED)
    public int insertStudents(@Valid @RequestBody List<Student> students) {
        logger.info("insertStudents size {}",students.size());
        try {
            return studentService.saveAll(students);

        }catch (Exception e){
            logger.info("Exception {}"+e);
            return 0;
        }
    }

    @GetMapping("/readOne")
    @ResponseBody
    @ResponseStatus(code = HttpStatus.ACCEPTED)
    public Student getStudent(@RequestParam(name = "id",required = false) Integer studentId) {
        logger.info("getStudent {}",studentId);
        try {
            return studentService.read(studentId);
        } catch (Exception e){
            logger.info("Exception {}"+e);
            return new Student();
        }
    }

    @PutMapping("/updateOne")
    @ResponseBody
    @ResponseStatus(code = HttpStatus.ACCEPTED)
    public int updateStudent(@Valid @RequestBody Student student) {
        logger.info("updateStudent {}",student.toString());
        try {
            return studentService.update(student);
        }catch (Exception e){
            logger.info("Exception {}"+e);
            return 0;
        }

    }

    @DeleteMapping("/deleteOne")
    @ResponseBody
    @ResponseStatus(code = HttpStatus.ACCEPTED)
    public int deleteStudent(@Valid @RequestBody Student student) {
        int result = 0;
        try {
            result = studentService.delete(student);
        } catch (Exception e) {
            logger.info("Exception {}"+e);
        }
        return result;
    }

}
