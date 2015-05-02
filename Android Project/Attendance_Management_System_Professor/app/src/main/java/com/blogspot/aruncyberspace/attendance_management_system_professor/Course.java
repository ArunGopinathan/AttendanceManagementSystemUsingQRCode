/**
 * 
 */
package com.blogspot.aruncyberspace.attendance_management_system_professor;

/**
 * @author Arun
 *
 */
import java.util.Collection;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

@Root
public class Course {

	@Element
	private String CourseId;
	@Element
	private String CourseName;
	@Element(required = false)
	private String CourseStartTime;
	@Element(required = false)
	private String CourseEndTime;

	/*
	 * @ElementList private Collection<ManualStudent> Students;
	 */
	public String getCourseId() {
		return CourseId;
	}

	public void setCourseId(String courseId) {
		CourseId = courseId;
	}

	public String getCourseName() {
		return CourseName;
	}

	public void setCourseName(String courseName) {
		CourseName = courseName;
	}

	public String getCourseStartTime() {
		return CourseStartTime;
	}

	public void setCourseStartTime(String courseStartTime) {
		CourseStartTime = courseStartTime;
	}

	public String getCourseEndTime() {
		return CourseEndTime;
	}

	public void setCourseEndTime(String courseEndTime) {
		CourseEndTime = courseEndTime;
	}

	/*
	 * 
	 * 
	 * public Collection<ManualStudent> getStudents() { return Students; }
	 * 
	 * public void setStudents(Collection<ManualStudent> students) { Students =
	 * students; }
	 */

}
