/**
 * 
 */
package com.blogspot.aruncyberspace.attendance_management_system_student;

/**
 * @author Arun
 *
 */
import java.util.Collection;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

@Root
public class Professor {
	@Element
	private String EmailId;
	@ElementList
	private Collection<Course> courses;

	public String getEmailId() {
		return EmailId;
	}

	public void setEmailId(String emailId) {
		EmailId = emailId;
	}

	public Collection<Course> getCourses() {
		return courses;
	}

	public void setCourses(Collection<Course> courses) {
		this.courses = courses;
	}

}
