package listeners;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.annotations.BeforeMethod;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;
import com.relevantcodes.extentreports.ExtentTest;

import base.TestBase;

public class CustomListeners extends TestBase implements ITestListener{

	
	
	@Override
	public void onTestFailure(ITestResult result) {
		String path=null;
			try {
				//FileUtils.copyFile(((TakesScreenshot)result.getTestContext().getAttribute("WebDriver")).getScreenshotAs(OutputType.FILE),new File(path));
				path=screenshot(result.getMethod().getMethodName(),(WebDriver)result.getTestContext().getAttribute("WebDriver"));
			
			} catch (WebDriverException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		Reporter.log("<a href="+path+">Screenshot</a>");
		Reporter.log("<a href="+path+"><img src="+path+" height=100 width=100></img></a>");
		extentTest.log(LogStatus.FAIL, result.getTestClass().getRealClass().getName()+" "+result.getMethod().getMethodName()+result.getThrowable().toString());
		extentTest.log(LogStatus.FAIL,extentTest.addScreenCapture(path));
	}
	
	@Override
	public void onTestSuccess(ITestResult result) {
		extentTest.log(LogStatus.PASS, result.getName()+" Passed");
	}
	

	@Override
	public void onTestStart(ITestResult result) {
		extentTest=extentReport.startTest(result.getTestClass().getRealClass().getName());
		result.getTestContext().setAttribute("ExtentTest", extentTest);
		result.getTestContext().setAttribute("Method", result.getMethod().getMethodName());
	}
	
	@Override
	public void onFinish(ITestContext context) {
		extentReport.endTest(extentTest);
		extentReport.flush();
	}
	
}
