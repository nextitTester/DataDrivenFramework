package dataprovider;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Iterator;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.testng.annotations.DataProvider;

import base.TestBase;

public class DataProviderClass extends TestBase{
	
	@DataProvider(name="dataprovider")
	public Object[][] customerData(Method m) throws IOException  {
		excelFileSetup();
		
		int noOfSheets=workbook.getNumberOfSheets();
		for(int i=0;i<noOfSheets;i++) {
			if(workbook.getSheetAt(i).getSheetName().equals(m.getName())) {
				sheet=workbook.getSheet(m.getName());
			}
		}
		
		int rowNum = 0;
		int cellNum = 0;
		boolean k = false;
		Iterator<Row> rows = sheet.rowIterator();
		while (rows.hasNext()) {
			Row row = rows.next();
			Iterator<Cell> cells = row.cellIterator();
			while (cells.hasNext()) {
				Cell cell = cells.next();
				if (cell.getStringCellValue().contains("First Name") || cell.getStringCellValue().contains("Customer")) {
					rowNum = row.getRowNum();
					cellNum = cell.getColumnIndex();
					k = true;
					break;
				}
			}
			if (k == true) {
				break;
			}
		}
		Object[][] data = null;
		if(m.getName().contains("add")) {
			 data= new Object[sheet.getLastRowNum() - rowNum][sheet.getRow(rowNum).getLastCellNum()];
		}
		else if(m.getName().contains("open")) {
			data= new Object[sheet.getLastRowNum() - rowNum][sheet.getRow(rowNum).getLastCellNum()];
		}		
		for (int i = rowNum + 1; i < sheet.getLastRowNum() - rowNum + 1; i++) {
			int j = cellNum;
			while (j < sheet.getRow(i).getLastCellNum()) {
				if (sheet.getRow(i).getCell(j).getColumnIndex() < sheet.getRow(i).getLastCellNum()) {
					try {
						data[i - 1][j] = sheet.getRow(i).getCell(j).getStringCellValue();
					} catch (Exception e) {
						data[i - 1][j] = sheet.getRow(i).getCell(j).getNumericCellValue();
					}
				}
				j++;
			}
		}
		return data;

	}

}
