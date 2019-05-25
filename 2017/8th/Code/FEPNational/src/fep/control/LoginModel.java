package fep.control;
import java.sql.*;

public class LoginModel {
	/**
	 * @author Aatif Jiwani
	 * @version 3.0
	 * 
	 * This is a connection handler class. 
	 * All this class does is establish a connection between the program and the database. 
	 * If a controller needs connection, they simply create an instance of this class. 
	 */

	/**
	 * !IMPORTANT!
	 * All Try and Catches are meant to notify the user of their mistake if their input
	 * is not proper. 
	 */
	
	Connection connection;
	
	//Establishes connection
	public LoginModel(){
		connection = SQLiteConnection.Connector();
		if (connection == null) System.exit(1);
	}
	
	//Checks whether connection is sucessful or not. 
	public boolean isConnect(){
		try {
			return !connection.isClosed();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			return false;
		}
	}
}
