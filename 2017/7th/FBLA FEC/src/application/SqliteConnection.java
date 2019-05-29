package application;
import java.sql.*;

//Establishes the connection with the database
public class SqliteConnection {

		public static Connection Connector(){
			try{
				Class.forName("org.sqlite.JDBC");
				Connection conn = DriverManager.getConnection("jdbc:sqlite:Infinity_DB.db");
				return conn;
			} 
			catch (Exception e){
				System.out.println(e);
				return null;
			}
		}
}
