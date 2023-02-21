package testcases;

import java.io.IOException;

import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.SkipException;
import org.testng.annotations.Test;

import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

import base.TestBase;
import dataprovider.DataProviderClass;

public class OpenAccountTest extends TestBase{
	
	
	@Test(dataProviderClass = DataProviderClass.class,dataProvider = "dataprovider")
	public void openAccount(String customer,String currency,String runmodeData) throws IOException {
		String runmodeTest=runmodeTest("openAccount");
		if(runmodeTest.equals("no")) {
			((ExtentTest) context.getAttribute("ExtentTest")).log(LogStatus.SKIP, "Test skipped");
			throw new SkipException("Skipped");
		}
		else if(runmodeData.equals("no")) {
			((ExtentTest) context.getAttribute("ExtentTest")).log(LogStatus.SKIP, "Test skipped");
			throw new SkipException("Test skipped for dataset");
		}
		
		try {
			click("Bank_Manager_Login_Button");
		} catch (Exception e) {
		}
		for (int i = 0; i < driver
				.findElements(By.cssSelector(objectRepositoryProperties.getProperty("bankManagerPageButtons")))
				.size(); i++) {
			if (driver.findElements(By.cssSelector(objectRepositoryProperties.getProperty("bankManagerPageButtons")))
					.get(i).getAttribute("innerHTML").contains("Open Account")) {
				String info="Open_Account_Button";				
				driver.findElements(By.cssSelector(objectRepositoryProperties.getProperty("bankManagerPageButtons")))
						.get(i).click();
				((ExtentTest) context.getAttribute("ExtentTest")).log(LogStatus.INFO, "Click on "+info);
				Reporter.log("Click on "+info);
				break;
			}
		}
		Select select=new Select(driver.findElement(By.cssSelector(objectRepositoryProperties.getProperty("Currency_Dropdown"))));
		select.selectByVisibleText(currency);
		click("Process_Button");
		Assert.assertEquals(select.getFirstSelectedOption().getText(), currency);
		//Assert.fail("failed to open account");
	}

}
