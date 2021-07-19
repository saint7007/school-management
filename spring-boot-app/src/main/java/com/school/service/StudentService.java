package com.school.service;

import com.school.exception.RollBackException;
import com.school.model.Course;
import com.school.model.CourseRegisterationResult;
import com.school.model.Student;
import com.school.repository.CourseRegisterationRepository;
import com.school.repository.StudentRepository;
import com.school.repository.StudentServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class StudentService implements StudentServiceImpl {
    @Autowired
    StudentRepository studentRepository;
    @Autowired
    private CourseRegisterationRepository courseRegisterationRepository;
    @PersistenceContext
    private EntityManager entityManager;
    @Override
    public void create(Student student) {
        studentRepository.save(student);
    }

    @Override
    public Student read(Integer studentId) {
      return  studentRepository.findById(studentId.longValue()).get();
    }

    @Override
    public int update(Student student) {
        return entityManager.createNativeQuery("update student s set s.name = ?1 where s.id = ?2")
                .setParameter(1, student.getName())
                .setParameter(2, student.getId())
                .executeUpdate();
    }
    /*
    Its a soft delete, this function does the following
    a. delete flag =true
    b. reduces the count of students in course
    c.  de-registers the student from course
     */
    @Override
    @org.springframework.transaction.annotation.Transactional(rollbackForClassName="RollBackException")
    public int delete(Student student) throws RollBackException {
        int deletedResult= entityManager.createNativeQuery("update student s set s.deleted = true , s.count_of_courses=0 where s.id = ?1")
                .setParameter(1, student.getId())
                .executeUpdate();
        System.out.println("deletedResult "+deletedResult);
        if (deletedResult==1){
            Collection<CourseRegisterationResult> courseRegisteration= courseRegisterationRepository.getRegistertionByStudentId(student.getId().intValue());
            if(courseRegisteration.size()>0){
                int reduceCountOfCourseResult= entityManager.createNativeQuery("update school.course c set c.count_of_students=c.count_of_students-1 where c.count_of_students>0 and c.id in (select course_id from courseregistration cr where cr.student_id=?1)")
                        .setParameter(1, student.getId())
                        .executeUpdate();
                System.out.println("reduceCountOfCourseResult "+reduceCountOfCourseResult);
                if (reduceCountOfCourseResult>0){
                    int deregisterStudentsByCourse= entityManager.createNativeQuery("update school.courseregistration cr set cr.deregistered=true where cr.student_id=?1")
                            .setParameter(1, student.getId())
                            .executeUpdate();
                    System.out.println("deregisterStudentsByCourse "+deregisterStudentsByCourse);
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

    @Override
    public int saveAll(List<Student> studentList) {
       return studentRepository.saveAll(studentList).size();
    }


    @Override
    public List getStudent(int course_id) {

        return null;
    }
    @Transactional
    public int insertWithQuery(Student student, Course course) {
       return entityManager.createQuery("INSERT INTO course_registered (student_id, course_id) VALUES (?,?)")
                .setParameter(1, student.getId())
                .setParameter(2, course.getId())
                .executeUpdate();
    }
}
