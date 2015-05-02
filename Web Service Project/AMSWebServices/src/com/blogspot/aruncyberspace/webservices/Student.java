/**
 * 
 */
package com.blogspot.aruncyberspace.webservices;
import org.simpleframework.xml.*;
/**
 * @author Arun
 *
 */
@Root
public class Student {
	@Element
	private String StudentId;
	@Element
	private String CourseNumber;
	@Element
	private String AndroidDeviceId;
	@Element
	private String TimeStamp;
	
	private String getStudentId() {
		return StudentId;
	}
	private void setStudentId(String studentId) {
		this.StudentId = studentId;
	}
	private String getCourseNumber() {
		return CourseNumber;
	}
	private void setCourseNumber(String courseNumber) {
		this.CourseNumber = courseNumber;
	}
	private String getAndroidDeviceId() {
		return AndroidDeviceId;
	}
	private void setAndroidDeviceId(String androidDeviceId) {
		this.AndroidDeviceId = androidDeviceId;
	}
	private String getTimeStamp() {
		return TimeStamp;
	}
	private void setTimeStamp(String timeStamp) {
		this.TimeStamp = timeStamp;
	}

}
