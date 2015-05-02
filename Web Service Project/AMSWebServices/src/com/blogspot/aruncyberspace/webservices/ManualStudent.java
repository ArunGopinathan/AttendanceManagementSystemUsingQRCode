/**
 * 
 */
package com.blogspot.aruncyberspace.webservices;

/**
 * @author Arun
 *
 */
import org.simpleframework.xml.*;

@Root
public class ManualStudent {

	@Element
	private String StudentId;
	@Element(required=false)
	private String FirstName;
	@Element(required=false)
	private String LastName;
	@Element(required = false)
	private String CourseId;
	@Element(required = false)
	private String TimeStamp;

	public String getStudentId() {
		return StudentId;
	}

	public void setStudentId(String studentId) {
		StudentId = studentId;
	}

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

	public String getCourseNumber() {
		return CourseId;
	}

	public void setCourseNumber(String courseNumber) {
		CourseId = courseNumber;
	}

	public String getTimeStamp() {
		return TimeStamp;
	}

	public void setTimeStamp(String timeStamp) {
		TimeStamp = timeStamp;
	}

}
