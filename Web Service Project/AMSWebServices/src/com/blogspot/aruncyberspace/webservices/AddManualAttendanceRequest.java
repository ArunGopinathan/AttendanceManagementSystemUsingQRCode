/**
 * 
 */
package com.blogspot.aruncyberspace.webservices;

/**
 * @author Arun
 *
 */
import java.util.Collection;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

@Root
public class AddManualAttendanceRequest {

	@ElementList
	private Collection<ManualStudent> Students;

	public Collection<ManualStudent> getStudents() {
		return Students;
	}

	public void setStudents(Collection<ManualStudent> students) {
		Students = students;
	}
	

}
