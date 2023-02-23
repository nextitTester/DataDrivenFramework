package base;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Category;
import org.apache.log4j.Logger;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.Reporter;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

import listeners.ExtentReportGenerator;

public class TestBase {
	
	public WebDriver driver;
	Properties configProperties=new Properties();
	public Properties objectRepositoryProperties=new Properties();
	FileInputStream fileInputStream;
	File file;
	public XSSFWorkbook workbook;
	public  XSSFSheet sheet;
	public WebDriverWait wait;
	public Date date;
	public ExtentTest extentTest;
	public ExtentReports extentReport=ExtentReportGenerator.reportInitializer();
	public ITestContext context;
	SoftAssert softAssert=new SoftAssert();
	public String dateString;
	
	@BeforeTest
	@Parameters( value = {"browser"})
	public void setUp(@Optional("edge") String browser) throws IOException {
		if(driver==null) {
			file=new File(System.getProperty("user.dir")+"\\src\\test\\resources\\properties\\Config.properties");
			fileInputStream=new FileInputStream(file);
			configProperties.load(fileInputStream);
			
			file=new File(System.getProperty("user.dir")+"\\src\\test\\resources\\properties\\ObjectRepository.properties");
			fileInputStream=new FileInputStream(file);
			 objectRepositoryProperties.load(fileInputStream);
			 
			 objectRepositoryProperties.getProperty("Bank_Manager_Login_Button");
			 
			 
			 Properties prop=new Properties();
			 file=new File(System.getProperty("user.dir")+"\\src\\test\\resources\\properties\\NewProp.properties");
			fileInputStream=new FileInputStream(file);
			
			prop.load(fileInputStream);
			
			prop.getProperty("bankManagerPageButtons");
				
				
			 
			 
			 
	
			
			
			
			if(browser==null) {
				System.setProperty("webdriver.edge.driver", System.getProperty("user.dir")+"\\src\\test\\resources\\executables\\msedgedriver.exe");
				driver=new EdgeDriver();
			}
			
			else if(browser.equals("chrome")) {
				System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir")+"\\src\\test\\resources\\executables\\chromedriver.exe");
				driver=new ChromeDriver();
			}
			
			else if(browser.equals("edge")) {
				System.setProperty("webdriver.edge.driver", System.getProperty("user.dir")+"\\src\\test\\resources\\executables\\msedgedriver.exe");
				driver=new EdgeDriver();
				//((RemoteWebDriver) driver).setLogLevel(Level.INFO);
			} 
			//loggerApplication.info("browser opened");
			
			driver.manage().window().maximize();
			driver.get(configProperties.getProperty("url"));
			//loggerApplication.info("url opened");
			driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(Long.parseLong((String) configProperties.get("implicitWait"))));
			wait=new WebDriverWait(driver,Duration.ofSeconds(5));
		}
		
	}
	
	@BeforeTest(dependsOnMethods = "setUp")
	public void excelFileSetup() throws IOException {
		
		
		System.out.println(System.getenv("Browser"));
		file=new File(System.getProperty("user.dir")+"\\src\\test\\resources\\excel\\testdata.xlsx");
		fileInputStream=new FileInputStream(file);
		workbook=new XSSFWorkbook(fileInputStream);
	}
	
	@BeforeTest(dependsOnMethods = "excelFileSetup")
	public void context(ITestContext context) {
		context.setAttribute("WebDriver", driver);
		this.context=context;
	}
	
	@AfterTest
	public void tearDown() {
		if(driver!=null) {
			driver.quit();
		}
		//loggerApplication.info("test completed");
	}
	
	
	
	
	public void click(String locatorName) {
		
		
		driver.findElement(By.cssSelector(objectRepositoryProperties.getProperty(locatorName))).click();
		
		//driver.findElement(By.cssSelector("button[ng-click='manager()']")).click();
		
		
		((ExtentTest) context.getAttribute("ExtentTest")).log(LogStatus.INFO, "Click on "+locatorName);
		Reporter.log("Click on "+locatorName);
	}
	
	public void  type(String locatorName,String value) {
		
		driver.findElement(By.cssSelector(objectRepositoryProperties.getProperty(locatorName))).sendKeys(value);
		((ExtentTest) context.getAttribute("ExtentTest")).log(LogStatus.INFO, "Enter "+"< "+value+" >"+" in "+locatorName);
		Reporter.log("Enter "+"< "+value+" >"+" in "+locatorName);
	}
	
	public String screenshot(String method,WebDriver driver) throws WebDriverException, IOException {
		date=new Date();
		dateString=date.toString().replace(" ", "_").replace(":", "_");
		String path=System.getProperty("user.dir")+"\\src\\test\\resources\\logs\\"+method+"\\"+dateString+".jpg";
		FileUtils.copyFile(((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE),new File(path));
		return path;
	}
	
	public void verifyEquals(String actual,String expected) throws WebDriverException, IOException{
		
		try {
			softAssert.assertEquals(actual, expected);
			softAssert.assertAll();
		} catch (Throwable t) {
			String path=screenshot((String) context.getAttribute("Method"),driver);
			((ExtentTest) context.getAttribute("ExtentTest")).log(LogStatus.FAIL, ((ExtentTest) context.getAttribute("ExtentTest")).addScreenCapture(path));
		}		
	}
	
	public String runmodeTest(String method) throws IOException {
		excelFileSetup();
		String value=null;
		int noOfSheets=workbook.getNumberOfSheets();
		for(int i=0;i<noOfSheets;i++) {
			if(workbook.getSheetAt(i).getSheetName().equals("runmode")) {
				sheet=workbook.getSheet("runmode");
			}
		}
		
		int count =sheet.getLastRowNum()-sheet.getFirstRowNum();
		for(int i=1;i<=count;i++) {
			if(sheet.getRow(i).getCell(0).getStringCellValue().equals(method)){
				value=sheet.getRow(i).getCell(1).getStringCellValue();
				break;
			}
		}
		return value;
	}

}
