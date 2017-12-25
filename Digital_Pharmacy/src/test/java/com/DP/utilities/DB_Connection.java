package com.DP.utilities;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Properties;


public class DB_Connection {

	public static String dbURL = "jdbc:db2://DSNTDRDA:446/DSNT";

	public static String dbDriver = "com.ibm.db2.jcc.DB2Driver";

	public static String dbUser = "REMOTPF";

	public static String dbPassword = "walmar16";
    
	public static Connection connection=null;
    
	public static Statement stmt;
    
	public static ResultSet rs;
    
	public static String [] simpleArray;

    
    public static String[] getData(String Query) throws Exception 
    {
       try 
       {
    	ArrayList<String> list=new ArrayList<String>();
		String row="";
		String result = null;
		connection = null;
		if (connection==null)
			{
				list.clear();
				Class.forName(dbDriver);
				Properties props = new Properties();
				props.put("user", dbUser);
				props.put("password", dbPassword);
				connection = DriverManager.getConnection(dbURL, props);
				stmt = connection.createStatement();
				rs = stmt.executeQuery(Query);
				
				ResultSetMetaData metadata = rs.getMetaData();
				
			    int columnCount = metadata.getColumnCount(); 
			    
				while(rs.next())
						
				{
										 row = "";
				        for (int i = 1; i <= columnCount; i++) {
				        //row += rs.getString(i) + ", ";    
				          list.add(rs.getString(i));
					
				}
				
			/*	simpleArray=list.toArray(new String[list.size()]);
				for(int i=0;i<simpleArray.length;i++)
				{
					System.out.println(simpleArray[i]);
				}
				*/
			}
				simpleArray=list.toArray(new String[list.size()]);
				for(int i=0;i<simpleArray.length;i++)
				{
					//System.out.println(simpleArray[i]);
				}

			}
		rs.close();
		stmt.close();
		connection.close();
		connection.close();
		
       }catch(Exception e){
    	   e.printStackTrace();
       }
	return simpleArray;
        
   }
    



}
