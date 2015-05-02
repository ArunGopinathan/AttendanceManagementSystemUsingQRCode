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
