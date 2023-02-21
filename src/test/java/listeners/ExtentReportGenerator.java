package listeners;

import com.relevantcodes.extentreports.ExtentReports;

public class ExtentReportGenerator {
	
	
	
	public static ExtentReports reportInitializer() {
		ExtentReports extentReports=new ExtentReports("test-output\\extentreport.html");
		extentReports.addSystemInfo("Tester", "QA Team");
		extentReports.assignProject("Project");
		return extentReports;
	}

}
