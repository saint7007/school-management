package com.school;

import com.school.model.StudentCoursePojoTest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
public class RegisterReportControllerTest extends  AbstractTest{
    @Override
    @Before
    public void setUp() {
        super.setUp();
    }
    @Test
    public void getStudentByCourseId() throws Exception {
        String uri = "/reports/getStudentByCourseId?id=3";
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri)
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
        String content = mvcResult.getResponse().getContentAsString();
        StudentCoursePojoTest[] productlist = super.mapFromJson(content, StudentCoursePojoTest[].class);
        for (StudentCoursePojoTest ac:
         productlist) {
            System.out.println("getStudentByCourseId "+ac.toString());

        }
        assertTrue(productlist.length >= 0);
    }
    @Test
    public void getStudentWithNoCourse() throws Exception {
        String uri = "/reports/getStudentByCourseId";
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri)
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
        String content = mvcResult.getResponse().getContentAsString();
        StudentCoursePojoTest[] productlist = super.mapFromJson(content, StudentCoursePojoTest[].class);
        for (StudentCoursePojoTest ac:
                productlist) {
            System.out.println("getStudentWithNoCourse "+ac.toString());

        }
        assertTrue(productlist.length >= 0);
    }
}
