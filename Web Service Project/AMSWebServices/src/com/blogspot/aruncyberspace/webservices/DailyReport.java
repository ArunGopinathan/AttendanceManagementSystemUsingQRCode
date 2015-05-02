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
public class DailyReport {
	
	@Element
	private String TotalNumberOfStudents;
	@Element
	private String TotalAttendanceToday;
	@Element
	private String AttendancePercentage;
	public String getTotalNumberOfStudents() {
		return TotalNumberOfStudents;
	}
	public void setTotalNumberOfStudents(String totalNumberOfStudents) {
		TotalNumberOfStudents = totalNumberOfStudents;
	}
	public String getTotalAttendanceToday() {
		return TotalAttendanceToday;
	}
	public void setTotalAttendanceToday(String totalAttendanceToday) {
		TotalAttendanceToday = totalAttendanceToday;
	}
	public String getAttendancePercentage() {
		return AttendancePercentage;
	}
	public void setAttendancePercentage(String attendancePercentage) {
		AttendancePercentage = attendancePercentage;
	}

}
