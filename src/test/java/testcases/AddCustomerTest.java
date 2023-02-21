package testcases;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Iterator;

import org.apache.commons.io.FileUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.SkipException;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

import base.TestBase;
import dataprovider.DataProviderClass;

public class AddCustomerTest extends TestBase {

	
	
	@Test (dataProviderClass = DataProviderClass.class,dataProvider = "dataprovider")
	public void addCustomer(String firstname, String lastname, Double postcode,String alert, String runmodeData) throws WebDriverException, IOException{
		String runmodeTest=runmodeTest("addCustomer");
		if(runmodeTest.equals("no")) {
			((ExtentTest) context.getAttribute("ExtentTest")).log(LogStatus.SKIP, "Test skipped");
			throw new SkipException("Skipped");
		}
		else if(runmodeData.equals("no")) {
			((ExtentTest) context.getAttribute("ExtentTest")).log(LogStatus.SKIP, "Test skipped for dataset");
			throw new SkipException("Skipped");
		}
		
		int noOfSheets=workbook.getNumberOfSheets();
		for(int i=0;i<noOfSheets;i++) {
			if(workbook.getSheetAt(i).getSheetName().equals("addCustomer")) {
				sheet=workbook.getSheet("addCustomer");
			}
		}

		try {
			
			click("Bank_Manager_Login_Button");
			
		} catch (Exception e) {
			
			
		}
		
		for (int i = 0; i < driver
				.findElements(By.cssSelector(objectRepositoryProperties.getProperty("bankManagerPageButtons")))
				.size(); i++) {
			if (driver.findElements(By.cssSelector(objectRepositoryProperties.getProperty("bankManagerPageButtons")))
					.get(i).getAttribute("innerHTML").contains("Add Customer")) {
				String info="Add_Customer_Button";
				driver.findElements(By.cssSelector(objectRepositoryProperties.getProperty("bankManagerPageButtons")))
						.get(i).click();
				((ExtentTest) context.getAttribute("ExtentTest")).log(LogStatus.INFO, "Click on "+info);
				Reporter.log("Click on "+info);
				break;
			}
		}
		//verifyEquals("abc", "xyz");
		type("firstname", firstname);
		type("lastname", lastname);
		

		type("postcode", Double.toString(postcode).replaceAll(".0", ""));
		click("Add_Customer_Button");
		String s = driver.switchTo().alert().getText();
		if (s.contains(alert)) {
			Assert.assertTrue(true);
			Reporter.log("test passed");
		} else {
			Assert.assertTrue(false);
			Reporter.log("test failed");
		}
		driver.switchTo().alert().accept();
		
		//Assert.fail("failed to add customer");
	}
	
	@Test
	public void verifyAddcustomerButton() {
		
	}
	
	@Test
	public void verifyfields() {
		
	}


}
