package com.school.controller;

import com.school.exception.RollBackException;
//import com.school.model.CourseRegistered;
import com.school.model.CourseRegisterationRequest;
//import com.school.model.CourseRegistration;
//import com.school.repository.CourseRegisteredRepository;
import com.school.service.CourseRegisterService;
//import com.school.repository.CourseRegisteredRepository;
import com.school.repository.CourseRepository;
import com.school.repository.StudentRepository;
import com.school.service.CourseService;
import com.school.service.StudentService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController()
@RequestMapping(path = "/register")
public class RegistrationController {

    @Autowired
    CourseRegisterService courseRegisteredRepository;

    private static final Logger logger = LogManager.getLogger(RegistrationController.class);

    @PostMapping("/registerCourse")
    @ResponseBody
    @ResponseStatus(code = HttpStatus.CREATED)
    public int registerCourses(@Valid @RequestBody CourseRegisterationRequest courseRegisterationRequest) {
        logger.info("registerCourses {}" +courseRegisterationRequest.toString());
        int result=0;
        try {
            result= courseRegisteredRepository.registerStudentForCourse(courseRegisterationRequest);
        }
        catch (DataAccessException | RollBackException e){
            logger.info("Exception {}"+e);
        }
        return result;
    }

    @PostMapping("/deregisterCourse")
    @ResponseBody
    @ResponseStatus(code = HttpStatus.CREATED)
    public int deregisterCourses(@Valid @RequestBody CourseRegisterationRequest courseRegisterationRequest) {
        logger.info("deregisterCourse {}" +courseRegisterationRequest.toString());
        int result=0;
        try {
            result= courseRegisteredRepository.deRegisterStudentForCourse(courseRegisterationRequest);
        }
        catch (Exception e){
            logger.info("Exception {}"+e);
        }
        return result;
    }


}
