/**
 * 
 */
package com.blogspot.aruncyberspace.webservices;

import java.util.Collection;

import org.simpleframework.xml.*;
/**
 * @author Arun
 *
 */
@Root
public class AddAttenance {
	@ElementList
	private Collection<Student> students;

	private Collection<Student> getStudents() {
		return students;
	}

	private void setStudents(Collection<Student> students) {
		this.students = students;
	}

}
