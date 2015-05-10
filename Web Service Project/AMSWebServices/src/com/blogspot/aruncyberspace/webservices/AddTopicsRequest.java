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
public class AddTopicsRequest {
	@Element
	private String ProfessorId;
	@Element
	private String CourseId;
	@Element
	private String Topics;
	@Element
	private String date;
	public String getProfessorId() {
		return ProfessorId;
	}
	public void setProfessorId(String professorId) {
		ProfessorId = professorId;
	}
	public String getCourseId() {
		return CourseId;
	}
	public void setCourseId(String courseId) {
		CourseId = courseId;
	}
	public String getTopics() {
		return Topics;
	}
	public void setTopics(String topics) {
		Topics = topics;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}

}
