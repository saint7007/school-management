package com.school.model;

import javax.persistence.*;

@Entity
@Table(name = "courseregistration")
public class CourseRegisteration {

    @EmbeddedId
    private CourseRegistrationKey id;

    @ManyToOne
    @MapsId("studentId")
    @JoinColumn(name = "student_id")
    private Student student;

    @ManyToOne
    @MapsId("courseId")
    @JoinColumn(name = "course_id")
    private Course course;

    @Column(name = "deregistered")
    private boolean deregistered= false;


    public CourseRegisteration() {
    }

    public CourseRegistrationKey getId() {
        return id;
    }

    public void setId(CourseRegistrationKey id) {
        this.id = id;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }


    public boolean isDeregistered() {
        return deregistered;
    }

    public void setDeregistered(boolean deregistered) {
        this.deregistered = deregistered;
    }

    @Override
    public String toString() {
        return "CourseRegisteration{" +
                "id=" + id +
                ", student=" + student.toString() +
                ", course=" + course.toString() +
                ", deregistered=" + deregistered +
                '}';
    }

}