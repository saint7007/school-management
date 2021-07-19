package com.school.model;

public class CourseRegisterationRequest {
    int studentId;
    int courseId;


    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    @Override
    public String toString() {
        return "CourseRegisterationRequest{" +
                "studentId=" + studentId +
                ", courseId=" + courseId +
                '}';
    }
}
