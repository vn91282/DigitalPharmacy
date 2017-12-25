package com.DP.utilities;

/* Call SOAP URL and send the Request XML and Get Response XML back */
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


 
public class getFillDetlsPageObj {
	
	static String requester;
	static String centralPatientId;
    static String deliveryMethod;
    static String dispensedDrugName;
    static String fillCost;
    static String fillDate;
    static String fillStatus;
    static String fillStoreNumber;
    static String prescriberName;
    static String rxNumber;
    static String cardSuffixNumber;
    static String onlineCustomerId;
    static String statusCode;
    static String statusDescription;
	
	public static List<String> Service_values = new ArrayList<String>();
	
	
/**
 * Fix for Exception in thread "main" javax.net.ssl.SSLHandshakeException:
 * sun.security.validator.ValidatorException: PKIX path building failed:
 * sun.security.provider.certpath.SunCertPathBuilderException: unable to find
 * valid certification path to requested target
 */
	public static void ssl() throws Exception{
		try {
			  TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
	          public java.security.cert.X509Certificate[] getAcceptedIssuers() { return null; }
	          public void checkClientTrusted(X509Certificate[] certs, String authType) { }
	          public void checkServerTrusted(X509Certificate[] certs, String authType) { }

	      } };

	      SSLContext sc = SSLContext.getInstance("SSL");
	      sc.init(null, trustAllCerts, new java.security.SecureRandom());
	      HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

	      // Create all-trusting host name verifier
	      HostnameVerifier allHostsValid = new HostnameVerifier() {
	          public boolean verify(String hostname, SSLSession session) { return true; }
	      };
	      // Install the all-trusting host verifier
	      HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
	      /* End of the fix*/
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

/*** Function to copy string ***/
   
    public static void copy(InputStream in, OutputStream out)
            throws IOException {
 
        synchronized (in) {
            synchronized (out) {
                byte[] buffer = new byte[256];
                while (true) {
                    int bytesRead = in.read(buffer);
                    if (bytesRead == -1)
                        break;
                    out.write(buffer, 0, bytesRead);
                }
            }
        }
    }
    
   /*** Function to run the service and copy response to an XML file ***/ 
 public static  void sendSoapRequest(String SOAPUrl, String Request) throws Exception {
	 System.out.println("\n-------------Run Soap service---------------");
    	ssl();
        System.out.println("Service Url :"+SOAPUrl+"");
        
    	// Create the connection with http
        URL url = new URL(SOAPUrl);
        URLConnection connection = url.openConnection();
        HttpURLConnection httpConn = (HttpURLConnection) connection;
        FileInputStream fin = new FileInputStream(Request);
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        copy(fin, bout);
        fin.close();
        byte[] b = bout.toByteArray();
        String s=new String(b);
        String NDC2 = s;
        b=NDC2.getBytes();
        
        
        // Set the appropriate HTTP parameters.
        httpConn.setRequestProperty("Content-Length", String.valueOf(b.length));
        httpConn.setRequestProperty("Content-Type", "text/xml; charset=utf-8");
        httpConn.setRequestProperty("SOAPAction", "");
        httpConn.setRequestMethod("POST");
        httpConn.setDoOutput(true);
 
        // send the XML that was read in to b.
        OutputStream out = httpConn.getOutputStream();
        out.write(b);
        out.close();
     
        // Read the response.
        httpConn.connect();
        System.out.println("http connection status :"+ httpConn.getResponseMessage());
        InputStreamReader isr = new InputStreamReader(httpConn.getInputStream());
        BufferedReader in = new BufferedReader(isr);
        String inputLine;
      
        while ((inputLine = in.readLine()) != null) {
    	   PrintWriter out1 = new PrintWriter(new FileWriter("rsp.xml"));
    	   //output to the file a line
    	   out1.println(inputLine);
    	   out1.close();
    	 }
        in.close();
     }   
 
 	/*** Function to validate the response with Excel data ***/	
 
 	public static List<String> Response_xmlparse_Validate(String[] ExcelData){
 		
 		String result = null;
 		Service_values.clear();
 		try {	
 			System.out.println("\n-------------Get Response data---------------\n");
    	 String[] RXFillDBData =DB_Connection.getData("SELECT * FROM rx_fill WHERE rx_fill_id =  " +ExcelData[3]);
        File inputFile = new File("rsp.xml");
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(inputFile);
		doc.getDocumentElement().normalize();
		System.out.println("Root element :" + doc.getDocumentElement().getNodeName());

		NodeList nList1 = doc.getElementsByTagName("doGetFillDetailsReturn");
		System.out.println("----------------------------");
		int count1 = nList1.getLength();
		for (int temp1 = 0; temp1 < count1; temp1++) 
		{
	
			//Get the staff element by tag name directly
			Node nodes = doc.getElementsByTagName("doGetFillDetailsReturn").item(temp1);
		//loop the staff child node
			NodeList list1 = nodes.getChildNodes();
		

		for (int i = 0; i != list1.getLength(); ++i)
			{
				Node child = list1.item(i);
				if (child.getNodeName().equals("centralPatientId")) 
					{
						
					centralPatientId = 	child.getFirstChild().getTextContent();
					centralPatientId = centralPatientId.trim();
					System.out.println("centralPatientId in Response : "+centralPatientId);
					}
				
				if (child.getNodeName().equals("fillDate")) 
				{
					
					fillDate = 	child.getFirstChild().getTextContent();
					fillDate = fillDate.trim();
					System.out.println("fillDate in Response: "+fillDate);
				}
				
				if (child.getNodeName().equals("fillStoreNumber")) 
				{
					
					fillStoreNumber = 	child.getFirstChild().getTextContent();
					fillStoreNumber = fillStoreNumber.trim();
					System.out.println("fillStoreNumber in Response: "+fillStoreNumber);
				}
				if (child.getNodeName().equals("onlineCustomerId")) 
				{
					
					onlineCustomerId = 	child.getFirstChild().getTextContent();
					onlineCustomerId = onlineCustomerId.trim();
					System.out.println("onlineCustomerId in Response: "+onlineCustomerId);
				}
				if (child.getNodeName().equals("requester")) 
				{
					
					requester = 	child.getFirstChild().getTextContent();
					requester = requester.trim();
					System.out.println("requester in Response: "+requester);
				}
				if (child.getNodeName().equals("rxNumber")) 
				{
					
					rxNumber = 	child.getFirstChild().getTextContent();
					rxNumber = rxNumber.trim();
					System.out.println("rxNumber in Response: "+rxNumber);
				}	
			}
		
			
 				NodeList nList4 = doc.getElementsByTagName("status");
 				
 				int count4 = nList4.getLength();
 				
 				for (int temp4 = 0; temp4 < count4; temp4++) 
 				{
 			
 					//Get the staff element by tag name directly
 					Node nodes4 = doc.getElementsByTagName("status").item(temp4);
					//loop the staff child node
					NodeList list4 = nodes4.getChildNodes();

				for (int j = 0; j != list4.getLength(); ++j)
					{
						Node subchild = list4.item(j);
						if (subchild.getNodeName().equals("statusCode")) 
							{
								
							statusCode = 	subchild.getFirstChild().getTextContent();
							statusCode = statusCode.trim();
							System.out.println("statusCode in Response: "+statusCode);
							}
						if (subchild.getNodeName().equals("statusDescription")) 
						{
							
							statusDescription = subchild.getFirstChild().getTextContent();
							statusDescription = statusDescription.trim();
						System.out.println("statusDescription in Response: " +statusDescription);
						}
					}
				} 	
 				System.out.println("\n-------------Response Validation---------------\n");
 				
				if (requester.equals(ExcelData[2]) && centralPatientId.equals(ExcelData[0]) && onlineCustomerId.equals(ExcelData[1]) && fillStoreNumber.equals(ExcelData[4])){
					System.out.println("\n---------------------- Excel data validation -------------\n");
					System.out.println("PASS: Excel data validation is successfull");
					result="PASS";
					Service_values.add(result);
				}else
				{
					System.out.println("\n---------------------- Excel data validation -------------\n");
					System.out.println("FAIL: Excel data validation is unsuccessfull");
					result="FAIL";
					Service_values.add(result);
				}
		        
		        String MFillDate = Modify_Date(fillDate);
		        
		        String[] RXDBData =DB_Connection.getData("SELECT * FROM rx WHERE rx_nbr= " +rxNumber+" and store_nbr = " +ExcelData[4]);
		        
		      if((rxNumber.equals(RXFillDBData[2].trim())) && (MFillDate.equals(RXFillDBData[4].trim()))){
		        	System.out.println("\n---------------------- DB data validation -------------\n");
		        	System.out.println("rxNumber in DB: "+RXFillDBData[2].trim());
					System.out.println("FillDate in DB: "+RXFillDBData[4].trim());
		        	System.out.println("\nPASS: DB data validation is successfull");
					result="PASS";
					Service_values.add(result);
		        }else
				{
		        	System.out.println("\n---------------------- DB data validation -------------\n");
		        	System.out.println("FAIL: DB data validation is unsuccessfull");
					result="FAIL";
					Service_values.add(result);
					
				}
		        
		        if(statusCode.equals("0") && statusDescription.equals("Good process") ){
		        	System.out.println("\n---------------------- Status validation -------------\n");
		        	System.out.println("PASS: Status validation is successfull");
					result="PASS";
					Service_values.add(result);
		        }else
				{
		        	System.out.println("\n---------------------- Status validation -------------\n");
		        	System.out.println("FAIL: Status validation is unsuccessfull");
		        	result="FAIL";
					Service_values.add(result);
				}			
			}
									
      if(Service_values.isEmpty())  {
    	  System.out.println("No alert message");
    	  Service_values.add("No alert message");
    	  Service_values.add("FAIL");
    	  
      }
   } catch (Exception e) {
        e.printStackTrace();
     }
	return Service_values ;
	
}

	/** Function to change the Fill date format**/
 	public static String Modify_Date(String response_FillDate){
 		//12072017
 		//2017-12-07
 		//response_FillDate = "12072017";
 		String Rday= response_FillDate.substring(0,2);
 		 String Rmonth= response_FillDate.substring(2,4);
 		 String Ryear= response_FillDate.substring(4,8);
 		 String RDate= (Ryear+"-"+Rday+"-"+Rmonth);
 		//System.out.println(RDate);
 		 return RDate;
 	}
 	
 	/** Function to Modify the input data in Request XML**/
 	public static void Request_xmlparse_Modify(String[] dataFromExcel ){
 		
 	   try {	
 		  System.out.println("\n---------------Request Modification-------------\n");
 	      File inputFile = new File("data//doGetFillDetails_Request.xml");
 	      DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
 			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
 			Document doc = dBuilder.parse(inputFile);
 			doc.getDocumentElement().normalize();
 			System.out.println("Root element :" + doc.getDocumentElement().getNodeName());

 			NodeList nList = doc.getElementsByTagName("fillInfoRequest");
 			
 			int count = nList.getLength();
 			for (int temp = 0; temp < count; temp++) 
 			{
 		
 				//Get the staff element by tag name directly
 				Node nodes = doc.getElementsByTagName("fillInfoRequest").item(temp);
 				//loop the staff child node
 				NodeList list = nodes.getChildNodes();

 			for (int i = 0; i != list.getLength(); ++i)
 				{
 					Node child = list.item(i);
 					
 					if (child.getNodeName().equals("onlineCustomerId")) 
 						{
 							child.getFirstChild().setNodeValue(dataFromExcel[1]);
 							String onlineCustomerId = 	child.getFirstChild().getTextContent();
 							System.out.println("onlineCustomerId in Request : "+onlineCustomerId);
 						}
 					if (child.getNodeName().equals("centralPatientId")) 
 					{
 						child.getFirstChild().setNodeValue(dataFromExcel[0]);
 						String centralPatientId = 	child.getFirstChild().getTextContent();
 						System.out.println("centralPatientId in Request : "+centralPatientId);
 					}
 					if (child.getNodeName().equals("requester")) 
 					{
 						child.getFirstChild().setNodeValue(dataFromExcel[2]);
 						String requester = 	child.getFirstChild().getTextContent();
 						System.out.println("requester in Request : "+requester);
 					}
 					if (child.getNodeName().equals("fillId")) 
 					{
 						child.getFirstChild().setNodeValue(dataFromExcel[3]);
 						String fillId = 	child.getFirstChild().getTextContent();
 						System.out.println("fillId in Request : "+fillId);
 					}
 					if (child.getNodeName().equals("storeNumber")) 
 					{
 						child.getFirstChild().setNodeValue(dataFromExcel[4]);
 						String storeNumber = 	child.getFirstChild().getTextContent();
 						System.out.println("storeNumber in Request : "+storeNumber);
 					}
 						
 				}
 			}
 			TransformerFactory transformerFactory = TransformerFactory.newInstance();
 			Transformer transformer = transformerFactory.newTransformer();
 			DOMSource source = new DOMSource(doc);
 			StreamResult result = new StreamResult("doGetFillDetails_ModifiedRequest.xml");
 			transformer.transform(source, result);
 	 } catch (Exception e) {
 	      e.printStackTrace();
 	   }
 		
}
}