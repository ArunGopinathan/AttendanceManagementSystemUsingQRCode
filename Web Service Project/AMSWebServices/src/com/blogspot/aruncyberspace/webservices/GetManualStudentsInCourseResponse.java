/**
 * 
 */
package com.blogspot.aruncyberspace.webservices;

/**
 * @author Arun
 *
 */
import java.util.Collection;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

@Root
public class GetManualStudentsInCourseResponse {

	@Element
	// private String CourseId;
	@ElementList
	private Collection<ManualStudent> Students;

	/*
	 * public String getCourseId() { return CourseId; }
	 * 
	 * public void setCourseId(String courseId) { CourseId = courseId; }
	 */

	public Collection<ManualStudent> getStudents() {
		return Students;
	}

	public void setStudents(Collection<ManualStudent> students) {
		Students = students;
	}

}
