/**
 * 
 */
package com.blogspot.aruncyberspace.webservices;

import java.io.StringWriter;
import java.io.Writer;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

/**
 * @author Arun
 *
 */
@Path("AMSService")
public class AMSWebServiceImpl {
	/*
	 * @Path("/FeetToInch/{f}")
	 * 
	 * @GET
	 * 
	 * @Produces(MediaType.TEXT_XML) public String
	 * convertFeetToInch(@PathParam("f") int f) { int inch = 0; int feet = f;
	 * inch = 12 * feet;
	 * 
	 * return "<FeetToInchService>" + "<Feet>" + feet + "</Feet>" + "<Inch>" +
	 * inch + "</Inch>" + "</FeetToInchService>"; }
	 */
	public String getUserXml(User User) {
		String xml = "";
		Writer writer = new StringWriter();
		Serializer serializer = new Persister();
		try {
			serializer.write(User, writer);
			xml = writer.toString();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return xml;
	}

	public User deserializeUserXML(String userxml) {
		// String parseXML = new String(userxml, "UTF-16");
		User user = new User();
		Writer writer = new StringWriter();
		Serializer serializer = new Persister();
		try {
			user = serializer.read(User.class, userxml);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return user;
	}

	public User processUserResultSet(ResultSet result) {
		User user = null;
		if (result != null) {
			try {
				if (result.next()) {
					user = new User();
					try {
						String userId = result.getString("UserId");
						user.setUserId(userId);
					} catch (Exception ex) {

					}

					String firstName = result.getString("FirstName");
					if (firstName != "")
						user.setFirstName(firstName);
					String lastName = result.getString("LastName");
					if (lastName != "")
						user.setLastName(lastName);
					String mavEmail = result.getString("MavEmail");
					if (mavEmail != "")
						user.setMavEmail(mavEmail);
					String androidId = result.getString("DeviceId");
					if (androidId != "")
						user.setAndroidDeviceId(androidId);

				}

			} catch (Exception e) {
				e.printStackTrace();
			}

		}

		return user;
	}

	public String generateRegisterQuery(User user) {
		String query = "insert into login(FirstName,LastName,MavEmail,Password,DeviceId) values('"
				+ user.getFirstName()
				+ "','"
				+ user.getLastName()
				+ "','"
				+ user.getMavEmail()
				+ "','"
				+ user.getPassword()
				+ "','"
				+ user.getAndroidDeviceId() + "')";
		System.out.println(query);
		return query;
	}

	@Path("/Register")
	@POST
	@Produces(MediaType.APPLICATION_XML)
	public String register(String request) {
		System.out.println("register: requestReceived:" + request);
		String response = "";
		try {
			User user = deserializeUserXML(request);
			String query = generateRegisterQuery(user);

			MySQLHelper helper = new MySQLHelper();
			helper.executeQuery(query);
			helper.disposeConnection();
			response = "SUCCESS";

		} catch (Exception ex) {
			response = "FAILURE";
		}

		System.out.println(response);

		return response;
	}

	@Path("/Authenticate/{username}/{password}")
	@GET
	@Produces(MediaType.TEXT_XML)
	public String Authenticate(@PathParam("username") String username,
			@PathParam("password") String password) {
		/*
		 * String result = ""; result = "<User>" + "<FirstName>Arun</FirstName>"
		 * + "<LastName>Gopinathan</LastName>" + "<MavEmail>a</MavEmail>" +
		 * "<AndroidDeviceId />" +
		 * "<Courses><Course><courseId>CSE5324</courseId><courseName>Software Engineering 1</courseName><courseStartTime>12:30 AM</courseStartTime><courseEndTime>11:50 PM</courseEndTime></Course></Courses>"
		 * + "</User>";
		 * 
		 * return result;
		 */
		String result = "";
		try {

			MySQLHelper helper = new MySQLHelper();
			String query = "select * from login where MavEmail='" + username
					+ "' and Password='" + password + "'";
			ResultSet resultset = helper.executeQueryAndGetResultSet(query);

			User user = processUserResultSet(resultset);

			String coursesquery = "SELECT c.CourseId, CourseName, CourseStartTime, CourseEndTime from courses c inner join enrolled e on e.courseId = c.courseId inner join login l on l.userId = e.StudentId where l.userId =(select UserId from login where MavEmail='"
					+ user.getMavEmail() + "') ";
			System.out.println(coursesquery);

			Collection<Course> courses = new ArrayList<Course>();

			ResultSet rs = helper.executeQueryAndGetResultSet(coursesquery);
			while (rs.next()) {
				String courseId = rs.getString("CourseId");
				String courseName = rs.getString("CourseName");
				String courseStartTime = rs.getString("CourseStartTime");
				String courseEndTime = rs.getString("CourseEndTime");

				Course course = new Course();
				course.setCourseId(courseId);
				course.setCourseName(courseName);
				course.setCourseStartTime(courseStartTime);
				course.setCourseEndTime(courseEndTime);

				courses.add(course);

			}
			user.setCourses(courses);

			// dispose connection
			helper.disposeConnection();
			if (user != null)
				System.out.println(user.toString());

			result = getUserXml(user);
			java.util.Date date = new java.util.Date();
			System.out.println(new Timestamp(date.getTime()));
			System.out.println("Authenticated:" + result);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return result;

	}

	@Path("/Add")
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String AddAttendance(@QueryParam("request") String request) {
		System.out.println("inside");
		return request;
	}

	@Path("/ManualAttendance")
	@GET
	@Produces(MediaType.TEXT_XML)
	public String manualAttendance(String request) {
		String response = "";

		return response;
	}

	@Path("/AddManualAttendance")
	@POST
	@Produces(MediaType.TEXT_XML)
	public String addManualAttendance(String request) {
		String response = "";
		AddManualAttendanceRequest addManualAttendance = new AddManualAttendanceRequest();
		Serializer serializer = new Persister();
		Writer writer = new StringWriter();
		try {
			addManualAttendance = serializer.read(
					AddManualAttendanceRequest.class, request);

			MySQLHelper helper = new MySQLHelper();
			for (ManualStudent s : addManualAttendance.getStudents()) {

				String query;
				if (s.getTimeStamp() == null) {
					query = "insert into attendance(StudentId,CourseId,TimeStamp) values('"
							+ s.getStudentId()
							+ "','"
							+ s.getCourseNumber()
							+ "',CURRENT_TIMESTAMP)";
				} else {

					query = "insert into attendance(StudentId,CourseId,TimeStamp) values('"
							+ s.getStudentId()
							+ "','"
							+ s.getCourseNumber()
							+ "','" + s.getTimeStamp() + "')";

				}
				System.out.println(query);
				helper.executeQuery(query);

			}
			response = "SUCCESS";
		} catch (Exception ex) {
			ex.printStackTrace();
			response = "FAILURE";
		}

		return response;
	}

	@Path("/GetManualStudentsInCourse/{courseId}/{professorEmail}")
	@GET
	@Produces(MediaType.TEXT_XML)
	public String getManualStudentsInCourse(
			@PathParam("courseId") String courseId,
			@PathParam("professorEmail") String professorEmail) {
		System.out.println(courseId);
		System.out.println(professorEmail);
		String response = "";
		String todayDate = "";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date();
		todayDate = sdf.format(date);

		String query = "select c.CourseId,userId,FirstName,LastName from enrolled e inner join "
				+ "courses c on c.courseId = e.courseId "
				+ "inner join login l on l.userid = e.StudentId "
				+

				"where professorId =(select UserId from login where MavEmail='"
				+ professorEmail
				+ "') "
				+ "and "
				+ "UserId not in (select StudentId from attendance where CourseId = '"
				+ courseId + "' and DATE(TimeStamp)='" + todayDate + "')";

		System.out.println(query);
		GetManualStudentsInCourseResponse responseobj = new GetManualStudentsInCourseResponse();
		// responseobj.setCourseId(courseId);
		Collection<ManualStudent> Students = new ArrayList<ManualStudent>();

		try {
			MySQLHelper helper = new MySQLHelper();
			ResultSet rs = helper.executeQueryAndGetResultSet(query);
			while (rs.next()) {
				ManualStudent s = new ManualStudent();
				String cId = rs.getString("CourseId");
				String Sid = rs.getString("UserId");
				String sFN = rs.getString("FirstName");
				String sLN = rs.getString("LastName");
				s.setCourseNumber(cId);
				s.setStudentId(Sid);
				s.setFirstName(sFN);
				s.setLastName(sLN);
				Students.add(s);
			}
			//
			responseobj.setStudents(Students);
			Serializer serializer = new Persister();
			Writer writer = new StringWriter();

			serializer.write(responseobj, writer);
			response = writer.toString();
			System.out.println(response);

		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return response;

	}

	@Path("/GetProfessorTeaches")
	@POST
	@Produces(MediaType.TEXT_XML)
	public String getProfessorTeaches(String request) {
		String response = "";
		System.out.println(request);
		Serializer serializer = new Persister();
		Writer writer = new StringWriter();
		User user = new User();
		try {
			user = serializer.read(User.class, request);

			String getProfessorteachesquery = "select c.courseId,c.CourseName from teaches t inner join courses c "
					+ "on c.courseId = t.courseId "
					+ "inner join login l on t.ProfessorId = l.UserId "
					+ "where t.ProfessorId = (select UserId from login where MavEmail = '"
					+ user.getMavEmail() + "')";

			System.out.println(getProfessorteachesquery);
			MySQLHelper helper = new MySQLHelper();
			ResultSet rs = helper
					.executeQueryAndGetResultSet(getProfessorteachesquery);

			Professor professor = new Professor();
			professor.setEmailId(user.getMavEmail());
			Collection<Course> courses = new ArrayList<Course>();

			while (rs.next()) {
				Course course = new Course();
				String CourseId = rs.getString("CourseId");
				String CourseName = rs.getString("CourseName");

				course.setCourseId(CourseId);
				course.setCourseName(CourseName);
				courses.add(course);
			}
			helper.disposeConnection();

			professor.setCourses(courses);

			serializer.write(professor, writer);
			response = writer.toString();
			System.out.println(response);

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return response;
	}

	@Path("/GetDailyReport/{courseId}/{professorId}")
	@GET
	@Produces(MediaType.APPLICATION_XML)
	public String getDailyReport(@PathParam("courseId") String courseId,
			@PathParam("professorId") String professorId) {
		String response = "";

		// get the total students
		String query = "SELECT count(*) as TotalStudents FROM enrolled where professorId='"
				+ professorId + "' and " + "CourseId='" + courseId + "'";
		MySQLHelper helper = new MySQLHelper();
		ResultSet rs = helper.executeQueryAndGetResultSet(query);
		ReportResponse rr = new ReportResponse();
		DailyReport dailyReport = new DailyReport();
		try {
			if (rs.next()) {
				String totalStudents = Integer.toString(rs
						.getInt("TotalStudents"));
				dailyReport.setTotalNumberOfStudents(totalStudents);
			}

			query = " SELECT count(*) as AttendanceToday FROM attendance where courseId='"
					+ courseId + "' and " + " DATE(TimeStamp)= CURRENT_DATE";

			// get total attendance today
			rs = helper.executeQueryAndGetResultSet(query);
			if (rs.next()) {
				String totalAttendanceToday = Integer.toString(rs
						.getInt("AttendanceToday"));
				dailyReport.setTotalAttendanceToday(totalAttendanceToday);
			}

			double percentage = (Double.parseDouble(dailyReport
					.getTotalAttendanceToday())
					/ Double.parseDouble(dailyReport.getTotalNumberOfStudents()))*100.0;

			
			dailyReport.setAttendancePercentage(String.format("%.2f",percentage) + "%");

			rr.setDailyReport(dailyReport);

			Serializer serializer = new Persister();
			Writer writer = new StringWriter();
			
			serializer.write(rr,writer);
			response = writer.toString();

		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return response;
	}

	/*
	 * SELECT count(*) FROM enrolled where professorId='2' and
	 * CourseId='CSE5324'
	 * 
	 * SELECT count(*) FROM attendance where courseId='CSE5324' and
	 * DATE(TimeStamp)= CURRENT_DATE
	 */
}
