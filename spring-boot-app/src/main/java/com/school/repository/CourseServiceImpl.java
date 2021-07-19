package com.school.repository;

import com.school.exception.RollBackException;
import com.school.model.Course;
import java.util.List;
import java.util.Optional;

public interface CourseServiceImpl {

    void create(Course course);
    Course read(Long course);
    int update(Course course);
    int delete(Course course) throws RollBackException;
    int saveAll(List<Course> courseList);

}
