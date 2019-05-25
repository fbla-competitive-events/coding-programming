package fep.control;

import java.sql.*;

public class SQLiteConnection {
	/**
	 * @author Aatif Jiwani
	 * @version 3.0
	 * 
	 * This is a connection class. This class finds the .sqlite database within the directory 
	 * and then returns a complete connection
	 */

	/**
	 * !IMPORTANT!
	 * All Try and Catches are meant to notify the user of their mistake if their input
	 * is not proper. 
	 */
	
	/**
	 * 
	 * @return
	 * 		A connection to the database. 
	 */
	public static Connection Connector(){
		try{
			Class.forName("org.sqlite.JDBC");
			Connection conn = DriverManager.getConnection("jdbc:sqlite:FEPState.sqlite");
			return conn;
		} catch (Exception e){
			return null;
		}
		
	}
}
