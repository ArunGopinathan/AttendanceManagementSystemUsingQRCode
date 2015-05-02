/**
 *
 */
package com.blogspot.aruncyberspace.attendance_management_system_professor;

/**
 * @author Arun
 *
 */

import org.simpleframework.xml.*;

import java.util.Collection;

@Root
public class User {

    @Element(required = false)
    private String UserId;
    @Element
    private String FirstName;
    @Element
    private String LastName;
    @Element
    private String MavEmail;
    @Element(required = false)
    private String Password;
    @Element(required = false)
    private String AndroidDeviceId;
    @ElementList(required=false)
    private Collection<Course> Courses;

    public String getFirstName() {
        return FirstName;
    }

    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }

    public String getMavEmail() {
        return MavEmail;
    }

    public void setMavEmail(String mavEmail) {
        MavEmail = mavEmail;
    }

    public String getAndroidDeviceId() {
        return AndroidDeviceId;
    }

    public void setAndroidDeviceId(String androidDeviceId) {
        AndroidDeviceId = androidDeviceId;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        this.Password = password;
    }

    public Collection<Course> getCourses() {
        return Courses;
    }

    public void setCourses(Collection<Course> courses) {
        Courses = courses;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }
}
