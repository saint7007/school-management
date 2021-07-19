package com.school.component;

import com.school.model.Course;
import com.school.model.Student;
import com.school.service.CourseService;
import com.school.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
class DemoCommandLineRunner implements CommandLineRunner {

	@Autowired
	private StudentService studentService;

	@Autowired
	private CourseService courseService;
	@Override
	public void run(String... args) throws Exception {
		try {
			List<Student> studentList = new ArrayList<>();
			for (int i = 0; i <= 56; i++) {
				Student student = new Student();
				student.setName("Student " + i);
				studentList.add(student);
			}
			studentService.saveAll(studentList);

			List<Course> coursesList = new ArrayList<>();
//		List<String> courseName=  Arrays.asList("Erlang", "Golang", "Java","Scala","Kotlin","Machine Learning");
			List<String> courseName = Arrays.asList("Course 1", "Course 2", "Course 3", "Course 4", "Course 5", "Course 6");
			for (int i = 0; i <= courseName.size() - 1; i++) {
				Course course = new Course();
				course.setName(courseName.get(i));
				coursesList.add(course);
			}
			courseService.saveAll(coursesList);

		}
		catch (Exception exception){
			System.out.println("It seems data already exist.");
		}
	}
}