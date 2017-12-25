package com.DP.sit;

	import java.io.FileInputStream;
	import java.io.IOException;

	import org.apache.poi.xssf.usermodel.XSSFCell;
	import org.apache.poi.xssf.usermodel.XSSFRow;
	import org.apache.poi.xssf.usermodel.XSSFSheet;
	import org.apache.poi.xssf.usermodel.XSSFWorkbook;
	import org.testng.annotations.Test;

	import com.DP.utilities.getRxFillHistoryPageObj;



	public class doGetRxFillHistory {

		public static 	FileInputStream myExcelFile = null;
		public static   FileInputStream fs;
		public static 	XSSFWorkbook  wb;
		public static   XSSFSheet  sh;
		public static 	XSSFRow row;
		public static 	XSSFCell column ;
		public static   String Edata;
		public static 	String OCID;
		public static	String CPID;
		public static	String Requester;
		public static	String fillId;
		public static	String storeNumber;
		
	 //Function to fetch request , save in XML format, Modify , last fill date, Save and copy the same to Excel 
		public static  String fetchCellData(){
			try{
				XSSFCell data;
				XSSFCell data_1;
				XSSFCell data_2;
				XSSFCell data_3;
				XSSFCell data_4;
				
				myExcelFile = new FileInputStream("data//doGetFillDetails.xlsx");
				wb = new XSSFWorkbook(myExcelFile);
	  			sh = wb.getSheet("Sheet1");
	  			int rowcount = sh.getLastRowNum();
	  			
	  			for(int i=1;i<rowcount+1;i++)
	  			{
	  				data = sh.getRow(i).getCell(0);
	  				CPID = String.format("%s", data);
	  				CPID = CPID.trim();
	  				data_1 = sh.getRow(i).getCell(1);
	  				OCID = String.format("%s", data_1);
	  				OCID = OCID.trim();
	  				data_2 = sh.getRow(i).getCell(2);
	  				Requester = String.format("%s", data_2);
	  				Requester = Requester.trim();
	  				data_3 = sh.getRow(i).getCell(3);
	  				fillId = String.format("%s", data_3);
	  				fillId = fillId.trim();
	  				data_4 = sh.getRow(i).getCell(4);
	  				storeNumber = String.format("%s", data_4);
	  				storeNumber = storeNumber.trim();
	  				String[] dataFromExcel={CPID,OCID,Requester,fillId,storeNumber};
	  				getRxFillHistoryPageObj.Request_xmlparse_Modify(dataFromExcel);
	  				getRxFillHistoryPageObj.sendSoapRequest("http://centraldbmiddleware.qa.pharmacyservices.rsso.qa.cloud.wal-mart.com/CentralDBWebServices/centralDBWebServices","doGetRXFillHistory_ModifiedRequest.xml");
	  				getRxFillHistoryPageObj.Response_xmlparse_Validate(dataFromExcel);			
	  			}	
			}
	  		catch(Exception e){
			e.printStackTrace();
	  		}
	  		return null;
		}
		

		@Test
		public static void executeTest() throws IOException{

			fetchCellData();
			
		}
			
	}








