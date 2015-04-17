package com.blogspot.aruncyberspace.attendance_management_system_student;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * Created by Arun on 4/10/2015.
 */
@Root
public class Course {

    @Element
    private String courseId;
    @Element
    private String courseName;
    @Element
    private String courseStartTime;
    @Element
    private String courseEndTime;

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getCourseStartTime() {
        return courseStartTime;
    }

    public void setCourseStartTime(String courseStartTime) {
        this.courseStartTime = courseStartTime;
    }

    public String getCourseEndTime() {
        return courseEndTime;
    }

    public void setCourseEndTime(String courseEndTime) {
        this.courseEndTime = courseEndTime;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }
}
