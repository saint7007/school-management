package com.school.service;

import com.school.exception.RollBackException;
import com.school.model.Course;
import com.school.model.CourseRegisterationResult;
import com.school.repository.CourseRegisterationRepository;
import com.school.repository.CourseRepository;
import com.school.repository.CourseServiceImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Transactional
public class CourseService implements CourseServiceImpl {
    @Autowired
    CourseRepository courseRepository;
    @Autowired
    private CourseRegisterationRepository courseRegisterationRepository;
    @PersistenceContext
    private EntityManager entityManager;
    private static final Logger logger = LogManager.getLogger(CourseRegisterService.class);

    @Override
    public void create(Course course) {

        courseRepository.save(course);
    }

    @Override
    public Course read(Long courseId) {
        Optional<Course> courseOptional=courseRepository.findById(courseId.longValue());
        return courseOptional.get();
    }

    @Override
    public int update(Course course) {
        return entityManager.createNativeQuery("update course c set c.name = ?1 where c.id = ?2")
                .setParameter(1, course.getName())
                .setParameter(2, course.getId())
                .executeUpdate();
    }
    /*
        Its a soft delete, this function does the following
        a. delete flag =true
        b. reduces the count of courses in student
        c. de-registers the course from student
    */
    @Override
    @org.springframework.transaction.annotation.Transactional(rollbackForClassName="RollBackException")
    public int delete(Course course) throws RollBackException {
        int deletedResult= softDeleteCourse(course);
       logger.info("CourseService delete deletedResult {}",deletedResult);
        if (deletedResult==1){
            Collection<CourseRegisterationResult> courseRegisteration= courseRegisterationRepository.getRegistertionByCourseId(course.getId().intValue());
            if(courseRegisteration.size()>0){
                int reduceCountOfCourseResult= reduceCountOfCourseResult(course);
                logger.info("reduceCountOfCourseResult {}",reduceCountOfCourseResult);
                if (reduceCountOfCourseResult>0){
                    int deregisterStudentsByCourse= deregisterStudentsByCourse(course);
                    logger.info("deregisterStudentsByCourse {}",deregisterStudentsByCourse);
                    if(deregisterStudentsByCourse>0){
                        return deregisterStudentsByCourse;
                    }else{
                        throw new RollBackException("Rollback registerStudentForCourse");
                    }
                }else{
                    throw new RollBackException("Rollback registerStudentForCourse");
                }
            }else{
                return deletedResult;
            }

        }else{
            throw new RollBackException("Rollback registerStudentForCourse");
        }

    }

    private int softDeleteCourse(Course course) {
        return entityManager.createNativeQuery("update course c set c.deleted = true , c.count_of_students=0 where c.id = ?1 and c.deleted=false")
                .setParameter(1, course.getId())
                .executeUpdate();
    }

    private int deregisterStudentsByCourse(Course course) {
        return entityManager.createNativeQuery("update school.courseregistration cr set cr.deregistered=true where cr.course_id=?1")
                .setParameter(1, course.getId())
                .executeUpdate();
    }

    private int reduceCountOfCourseResult(Course course) {
        return entityManager.createNativeQuery("update school.student s set s.count_of_courses=s.count_of_courses-1 where s.count_of_courses>0 and s.id in (select student_id from courseregistration cr where cr.course_id=?1)")
                .setParameter(1, course.getId())
                .executeUpdate();
    }

    @Override
    public int saveAll(List<Course> courseList) {
        return courseRepository.saveAll(courseList).size();
    }

    public static void processRequest(Course value, Function<Course, Course> func) {
        System.out.println(func.apply(value));
    }

}
