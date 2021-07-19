package com.school.repository;

import com.school.exception.RollBackException;
import com.school.model.Student;
import java.util.List;
import java.util.Optional;

public interface StudentServiceImpl {
    void create(Student Student);
    Student read(Integer studentId);
    int update(Student student);
    int delete(Student student) throws RollBackException;
    int saveAll(List<Student> studentList);
    List getStudent(int course_id);

}
