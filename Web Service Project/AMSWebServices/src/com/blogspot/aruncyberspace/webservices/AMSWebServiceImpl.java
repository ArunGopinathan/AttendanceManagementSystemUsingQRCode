/**
 * 
 */
package com.blogspot.aruncyberspace.webservices;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * @author Arun
 *
 */
@Path("AMSService")
public class AMSWebServiceImpl {
	/*@Path("/FeetToInch/{f}")
	@GET
	@Produces(MediaType.TEXT_XML)
	public String convertFeetToInch(@PathParam("f") int f) {
		int inch = 0;
		int feet = f;
		inch = 12 * feet;

		return "<FeetToInchService>" + "<Feet>" + feet + "</Feet>" + "<Inch>"
				+ inch + "</Inch>" + "</FeetToInchService>";
	}*/

	@Path("/Authenticate/{username}/{password}")
	@GET
	@Produces(MediaType.TEXT_XML)
	public String Authenticate(@PathParam("username") String username,
			@PathParam("password") String password) {
		String result = "";
		result = "<User>" + "<FirstName>Arun</FirstName>"
				+ "<LastName>Gopinathan</LastName>" + "<MavEmail>a</MavEmail>"
				+ "<AndroidDeviceId />" + 
				"<Courses><Course><courseId>CSE5324</courseId><courseName>Software Engineering 1</courseName><courseStartTime>12:30 AM</courseStartTime><courseEndTime>11:50 PM</courseEndTime></Course></Courses>"+
				"</User>";

		return result;

	}
}
