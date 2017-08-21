//package io.sv.tools.utils;
//
//import java.io.FileInputStream;
//import java.sql.SQLException;
//import java.util.Properties;
//
//import framework.DataBase.DBManager;
//
//public class GlobalObjects {
//	private static Properties properties = new Properties();
//	//private static DBManager db;
//	static {
//		try {
//			FileInputStream in = new FileInputStream("config/Properties");
//			properties.load(in);
//			in.close();
//
//
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//
//	static{
//	//	db = new DBManager(properties.getProperty("db"));  
//	}
//
//	public static String getProperty(String key) {
//		return properties.getProperty(key);
//	}
//
//
////	public static DBManager getSingletonDBObject(){
////		return db;
////	}
//
//	public static void closeDBConnection(){
//		db.closeConnection();	
//	}
//
//	public static void main(String[] args) {
//
//		String logonId = "vhasfcchanj";
//		String sql = "SELECT count(transmission_id) FROM view_recent_transmissions "+
//				  "WHERE customer_id = (select customer_id from customer_account where user_record_id = "+  
//						"(select user_record_id from user_record where logon_user_name = '"+ logonId + "')) "+
//				  "AND STATUS_CD NOT IN (67 , 1461)" ;
//		
//		//DBManager db = new GlobalObjects().getSingletonDBObject();
//		try {
//			System.out.println(db.runQuery(sql)[0][0]);
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//	}
//
//}
