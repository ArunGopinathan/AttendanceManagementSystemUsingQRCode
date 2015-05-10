/**
 * 
 */
package com.blogspot.aruncyberspace.attendance_management_system_professor;

/**
 * @author Arun
 *
 */
import org.simpleframework.xml.*;
@Root
public class ReportResponse {

	@Element
	private DailyReport dailyReport;

	public DailyReport getDailyReport() {
		return dailyReport;
	}

	public void setDailyReport(DailyReport dailyReport) {
		this.dailyReport = dailyReport;
	}
}
