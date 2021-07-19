package com.school;

import com.school.model.Course;
import com.school.model.CoursePojoTest;
import com.school.service.CourseService;
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

public class CourseControllerTest extends  AbstractTest{
    @Autowired
    CourseService courseService;
    @Override
    @Before
    public void setUp() {
        super.setUp();
    }
    @Test
    public void insertOneCourse() throws Exception {
        String uri = "/course/insertOne";
        CoursePojoTest course = new CoursePojoTest();
        UUID uuid = UUID. randomUUID();
        course.setName("Course"+uuid.toString());
        String inputJson = super.mapToJson(course);
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(inputJson)).andReturn();

        int status = mvcResult.getResponse().getStatus();


        assertEquals(201, status);
        String content = mvcResult.getResponse().getContentAsString();
        CoursePojoTest courseResult = super.mapFromJson(content, CoursePojoTest.class);
        assertNotNull(courseResult.getCreatedAt());
    }
    @Test
    public void insertManyCourse() throws Exception {
        String uri = "/course/insertMany";
        CoursePojoTest course1 = new CoursePojoTest();
        CoursePojoTest course2 = new CoursePojoTest();
        UUID uuid = UUID. randomUUID();
        UUID uuid2 = UUID. randomUUID();
        course1.setName("Course"+uuid);
        course2.setName("Course"+uuid2);
        List<CoursePojoTest> courseList= Arrays.asList(course1,course2);
        String inputJson = super.mapToJson(courseList);
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
        String uri = "/course/updateOne";
        CoursePojoTest course = new CoursePojoTest();
        UUID uuid = UUID. randomUUID();
        course.setId(1L);
        course.setName("Course"+uuid);
        String inputJson = super.mapToJson(course);
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
        String uri = "/course/deleteOne";
        Course course = new Course();
        CoursePojoTest coursePojoTest = new CoursePojoTest();
        UUID uuid = UUID. randomUUID();
//      course.setId(1000L);
        course.setName("Course"+uuid);
        courseService.create(course);
        coursePojoTest.setId(course.getId());
        coursePojoTest.setName("Course"+uuid);
        String inputJson = super.mapToJson(coursePojoTest);

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.delete(uri)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(inputJson)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(202, status);
        String content = mvcResult.getResponse().getContentAsString();
        assertEquals(content, "1");
    }
}
