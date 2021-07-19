package com.school;

import com.school.model.Course;
import com.school.model.CoursePojoTest;
import com.school.model.Student;
import com.school.model.StudentPojoTest;
import com.school.service.CourseService;
import com.school.service.StudentService;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.*;

public class StudentControllerTest extends  AbstractTest{
    @Autowired
    StudentService studentService;
    @Override
    @Before
    public void setUp() {
        super.setUp();
    }
    @Test
    public void insertOneCourse() throws Exception {
        String uri = "/student/insertOne";
        StudentPojoTest studentPojoTest = new StudentPojoTest();
        UUID uuid = UUID. randomUUID();
        studentPojoTest.setName("Student"+uuid.toString());
        String inputJson = super.mapToJson(studentPojoTest);
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(inputJson)).andReturn();

        int status = mvcResult.getResponse().getStatus();


        assertEquals(201, status);
        String content = mvcResult.getResponse().getContentAsString();
        StudentPojoTest studentPojoTestResult = super.mapFromJson(content, StudentPojoTest.class);
        assertNotNull(studentPojoTestResult.getCreatedAt());
    }
    @Test
    public void insertManyCourse() throws Exception {
        String uri = "/student/insertMany";
        StudentPojoTest student1 = new StudentPojoTest();
        StudentPojoTest student2 = new StudentPojoTest();
        UUID uuid = UUID. randomUUID();
        UUID uuid2 = UUID. randomUUID();
        student1.setName("Student"+uuid);
        student2.setName("Student"+uuid2);
        List<StudentPojoTest> studentPojoTestList= Arrays.asList(student1,student2);
        String inputJson = super.mapToJson(studentPojoTestList);
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(inputJson)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(201, status);
        String content = mvcResult.getResponse().getContentAsString();
        assertEquals(content, "2");
    }

    @Test
    public void updateOneCourse() throws Exception {
        String uri = "/student/updateOne";
        StudentPojoTest studentPojoTest = new StudentPojoTest();
        UUID uuid = UUID. randomUUID();
        studentPojoTest.setId(1L);
        studentPojoTest.setName("Student"+uuid);
        String inputJson = super.mapToJson(studentPojoTest);
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.put(uri)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(inputJson)).andReturn();

        int status = mvcResult.getResponse().getStatus();


        assertEquals(202, status);
        String content = mvcResult.getResponse().getContentAsString();
        assertEquals(content, "1");
    }

    @Test
    public void deleteOneCourse() throws Exception {
        String uri = "/student/deleteOne";
        Student student = new Student();
        StudentPojoTest studentPojoTest = new StudentPojoTest();
        UUID uuid = UUID. randomUUID();
//      course.setId(1000L);
        student.setName("Student"+uuid);
        studentService.create(student);
        studentPojoTest.setId(student.getId());
        studentPojoTest.setName("Student"+uuid);
        String inputJson = super.mapToJson(studentPojoTest);

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.delete(uri)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(inputJson)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(202, status);
        String content = mvcResult.getResponse().getContentAsString();
        assertEquals(content, "1");
    }
}
