package com.school.service;

import com.school.controller.RegistrationController;
import com.school.exception.RollBackException;
import com.school.model.CourseRegisterationRequest;
import com.school.model.CourseRegisterationResult;
import com.school.repository.CourseRegisterationRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Repository
public class CourseRegisterService {

    @PersistenceContext
    private EntityManager entityManager;

    private static final Logger logger = LogManager.getLogger(CourseRegisterService.class);

    @Autowired
    private CourseRegisterationRepository courseRegisterationRepository;

    public CourseRegisterService(EntityManager entityManager, CourseRegisterationRepository courseRegisterationRepository) {
        this.entityManager = entityManager;
        this.courseRegisterationRepository = courseRegisterationRepository;
    }

    // Using spring transactions either all queries update should be successful or it should not.
    // So raising a custom exception for rollback.
    @Transactional(rollbackForClassName="RollBackException")
    public int registerStudentForCourse(CourseRegisterationRequest courseRegisterationRequest) throws RollBackException {
        int courseregistrationResult=0;
        int studentCourseCountResult=0;
        int courseStudentCountResult=0;
           studentCourseCountResult= increaseStudentCourseCountResult(courseRegisterationRequest);
           logger.info("registerStudentForCourse studentCourseCountResult {}",studentCourseCountResult);
            if(studentCourseCountResult==1){
                courseStudentCountResult = increaseCourseStudentCountResult(courseRegisterationRequest);
                logger.info("registerStudentForCourse courseStudentCountResult {} ",courseStudentCountResult);
                if(courseStudentCountResult==1){
                   Collection<CourseRegisterationResult> courseRegisteration= courseRegisterationRepository.getRegisteredCourse(courseRegisterationRequest.getCourseId(),courseRegisterationRequest.getStudentId());
                    if(courseRegisteration.size()>0){
                        List<CourseRegisterationResult> courseRegisterationList = new ArrayList(courseRegisteration);
                        if(courseRegisterationList.get(0).isDeregistered()==true){
                            courseregistrationResult= registerOldCourseResult(courseRegisterationRequest);
                            if(courseregistrationResult==1){
                                return courseregistrationResult;
                            } else{
                                throw new RollBackException("Rollback registerStudentForCourse");
                            }
                        } else{
                            throw new RollBackException("Rollback registerStudentForCourse");
                        }
                    } else{
                        courseregistrationResult= registerNewCourseResult(courseRegisterationRequest);
                        logger.info("registerStudentForCourse courseregistrationResult {} " , courseregistrationResult);
                        if(courseregistrationResult==1){
                            return courseregistrationResult;
                        }
                        else{
                            throw new RollBackException("Rollback registerStudentForCourse");
                        }
                    }
                } else{
                    throw new RollBackException("Rollback registerStudentForCourse");
                }
            } else{
                throw new RollBackException("Rollback registerStudentForCourse");
            }
    }

    private int registerNewCourseResult(CourseRegisterationRequest courseRegisterationRequest) {
        return entityManager.createNativeQuery("insert into school.courseregistration (course_id,student_id, deregistered) values (?,?, false)")
                .setParameter(1, courseRegisterationRequest.getCourseId())
                .setParameter(2, courseRegisterationRequest.getStudentId())
                .executeUpdate();
    }

    private int registerOldCourseResult(CourseRegisterationRequest courseRegisterationRequest) {
        return entityManager.createNativeQuery("update school.courseregistration cr set cr.deregistered=false where cr.course_id=?1 and cr.student_id=?2")
                .setParameter(1, courseRegisterationRequest.getCourseId())
                .setParameter(2, courseRegisterationRequest.getStudentId())
                .executeUpdate();
    }

    private int increaseCourseStudentCountResult(CourseRegisterationRequest courseRegisterationRequest) {
        int courseStudentCountResult;
        courseStudentCountResult = entityManager.createNativeQuery("update school.course c set c.count_of_students=c.count_of_students+1 where c.count_of_students<50 and c.id=? and c.deleted=false")
                .setParameter(1, courseRegisterationRequest.getCourseId())
                .executeUpdate();
        return courseStudentCountResult;
    }

    private int increaseStudentCourseCountResult(CourseRegisterationRequest courseRegisterationRequest) {
        return entityManager.createNativeQuery("update school.student s set s.count_of_courses=s.count_of_courses+1 where s.count_of_courses<5 and s.id=? and s.deleted=false")
                .setParameter(1, courseRegisterationRequest.getStudentId())
                .executeUpdate();
    }

    @Transactional(rollbackForClassName="RollBackException")
    public int deRegisterStudentForCourse(CourseRegisterationRequest courseRegisterationRequest) throws RollBackException {
        int courseregistrationResult=0;
        int studentCourseCountResult=0;
        int courseStudentCountResult=0;
        studentCourseCountResult= decreaseStudentCourseCountResult(courseRegisterationRequest);
        logger.info("deRegisterStudentForCourse studentCourseCountResult {} " , studentCourseCountResult);
        if(studentCourseCountResult==1){
            courseStudentCountResult= decreaseCourseStudentCountResult(courseRegisterationRequest);
            logger.info("deRegisterStudentForCourse courseStudentCountResult {} ",courseStudentCountResult);
            if(courseStudentCountResult==1){
                courseregistrationResult= deregisterCourseRegistrationResult(courseRegisterationRequest);
                logger.info("deRegisterStudentForCourse courseregistrationResult {} ",courseregistrationResult);
                if(courseregistrationResult==1){
                    return courseregistrationResult;
                }
                else{
                    throw new RollBackException("Rollback registerStudentForCourse");
                }
            }
            else{
                throw new RollBackException("Rollback registerStudentForCourse");
            }
        }
        else{
            throw new RollBackException("Rollback registerStudentForCourse");
        }

    }

    private int deregisterCourseRegistrationResult(CourseRegisterationRequest courseRegisterationRequest) {
        return entityManager.createNativeQuery("update school.courseregistration cr set cr.deregistered=true where cr.course_id=?1 and cr.student_id=?2 and cr.deregistered=false")
                .setParameter(1, courseRegisterationRequest.getCourseId())
                .setParameter(2, courseRegisterationRequest.getStudentId())
                .executeUpdate();
    }

    private int decreaseCourseStudentCountResult(CourseRegisterationRequest courseRegisterationRequest) {
        return entityManager.createNativeQuery("update school.course c set c.count_of_students=c.count_of_students-1 where c.count_of_students>0 and c.id=? and c.deleted=false")
                .setParameter(1, courseRegisterationRequest.getCourseId())
                .executeUpdate();
    }

    private int decreaseStudentCourseCountResult(CourseRegisterationRequest courseRegisterationRequest) {
        return entityManager.createNativeQuery("update school.student s set s.count_of_courses=s.count_of_courses-1 where s.count_of_courses>0 and s.id=? and s.deleted=false")
                .setParameter(1, courseRegisterationRequest.getStudentId())
                .executeUpdate();
    }

}