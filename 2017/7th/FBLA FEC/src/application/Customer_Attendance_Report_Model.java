package application;

//imported required packages for this class
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Customer_Attendance_Report_Model {
	
	Connection connection;
    private Statement statement;
    private ResultSet resultSet;
    
	//Method to calculate the number of AM Customer Attendances on a given Sunday
    public int calcNumAMSunday(String date){
		
		String sqlQuery = "select * FROM Customers_Attendance";
		connection = SqliteConnection.Connector();
		int numAM_Sunday = 0; 	
	    try {
			
	    	statement = connection.createStatement();
			 	resultSet = statement.executeQuery(sqlQuery);
			 	
			 	//Checks to see if table entry is sunday, am, and the proper date to increment the numAM_Sunday variable
			 	while(resultSet.next()){
			 		if(resultSet.getString("AMPM").equals("AM") && resultSet.getString("Day_of_Week").equals("SUNDAY") && resultSet.getString("Date").equals(date)){			 			
			 			numAM_Sunday++;
			 		}
			 	}
			 	
			 	statement.close();
			 	resultSet.close();
			 	connection.close();
			 	
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();		
		}
	    return numAM_Sunday;	
	}
	
    //method to calculate the number of AM Customer Attendances on a given Monday
	public int calcNumAMMonday(String date){
		
		String sqlQuery = "select * FROM Customers_Attendance";
		connection = SqliteConnection.Connector();
		int numAM_Monday = 0; 	
	    try {
			
	    	statement = connection.createStatement();
			 	resultSet = statement.executeQuery(sqlQuery);
			 	
			 	//Checks to see whether the day of week is monday and sets corresponding values
			 	while(resultSet.next()){
			 		if(resultSet.getString("AMPM").equals("AM") && resultSet.getString("Day_of_Week").equals("MONDAY")&& resultSet.getString("Date").equals(date)){			 			
			 			numAM_Monday++;
			 		}
			 	}
			 	
			 	statement.close();
			 	resultSet.close();
			 	connection.close();
			 	
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();		
		}
	    return numAM_Monday;	
	}
	
	//method to calculate the number of AM Customer Attendances on a given Tuesday
	public int calcNumAMTuesday(String date){
		
		String sqlQuery = "select * FROM Customers_Attendance";
		connection = SqliteConnection.Connector();
		int numAM_Tuesday = 0; 	
	    try {
			
	    	statement = connection.createStatement();
			 	resultSet = statement.executeQuery(sqlQuery);
			 	
			 	//Checks to see whether the day of week is tuesday and sets corresponding values
			 	while(resultSet.next()){
			 		if(resultSet.getString("AMPM").equals("AM") && resultSet.getString("Day_of_Week").equals("TUESDAY")&& resultSet.getString("Date").equals(date)){			 			
			 			numAM_Tuesday++;
			 		}
			 	}
			 	
			 	statement.close();
			 	resultSet.close();
			 	connection.close();
			 	
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();		
		}
	    return numAM_Tuesday;	
	}
	
	//method to calculate the number of AM Customer Attendances on a given Wednesday
	public int calcNumAMWednesday(String date){
		
		String sqlQuery = "select * FROM Customers_Attendance";
		connection = SqliteConnection.Connector();
		int numAM_Wednesday = 0; 	
	    try {
			
	    	statement = connection.createStatement();
			 	resultSet = statement.executeQuery(sqlQuery);
			 	
			 	//Checks to see whether the day of week is wednesday and sets corresponding values
			 	while(resultSet.next()){
			 		if(resultSet.getString("AMPM").equals("AM") && resultSet.getString("Day_of_Week").equals("WEDNESDAY")&& resultSet.getString("Date").equals(date)){			 			
			 			numAM_Wednesday++;
			 		}
			 	}
			 	
			 	statement.close();
			 	resultSet.close();
			 	connection.close();
			 	
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();		
		}
	    return numAM_Wednesday;	
	}
	
	//method to calculate the number of AM Customer Attendances on a given Thursday
	public int calcNumAMThursday(String date){
		
		String sqlQuery = "select * FROM Customers_Attendance";
		connection = SqliteConnection.Connector();
		int numAM_Thursday = 0; 	
	    try {
			
	    	statement = connection.createStatement();
			 	resultSet = statement.executeQuery(sqlQuery);
			 	
			 	//Checks to see whether the day of week is thursday and sets corresponding values
			 	while(resultSet.next()){
			 		if(resultSet.getString("AMPM").equals("AM") && resultSet.getString("Day_of_Week").equals("THURSDAY")&& resultSet.getString("Date").equals(date)){			 			
			 			numAM_Thursday++;
			 		}
			 	}
			 	
			 	statement.close();
			 	resultSet.close();
			 	connection.close();
			 	
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();		
		}
	    return numAM_Thursday;	
	}
	
	//method to calculate the number of AM Customer Attendances on a given Friday
	public int calcNumAMFriday(String date){
		
		String sqlQuery = "select * FROM Customers_Attendance";
		connection = SqliteConnection.Connector();
		int numAM_Friday = 0; 	
	    try {
			
	    	statement = connection.createStatement();
			 	resultSet = statement.executeQuery(sqlQuery);
			 	
			 	//Checks to see whether the day of week is friday and sets corresponding values
			 	while(resultSet.next()){
			 		if(resultSet.getString("AMPM").equals("AM") && resultSet.getString("Day_of_Week").equals("FRIDAY")&& resultSet.getString("Date").equals(date)){			 			
			 			numAM_Friday++;
			 		}
			 	}
			 	
			 	statement.close();
			 	resultSet.close();
			 	connection.close();
			 	
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();		
		}
	    return numAM_Friday;	
	}
	
	//method to calculate the number of AM Customer Attendances on a given Saturday
	public int calcNumAMSaturday(String date){
		
		String sqlQuery = "select * FROM Customers_Attendance";
		connection = SqliteConnection.Connector();
		int numAM_Saturday = 0; 	
	    try {
			
	    	statement = connection.createStatement();
			 	resultSet = statement.executeQuery(sqlQuery);
			 	
			 	//Checks to see whether the day of week is saturday and sets corresponding values
			 	while(resultSet.next()){
			 		if(resultSet.getString("AMPM").equals("AM") && resultSet.getString("Day_of_Week").equals("SATURDAY")&& resultSet.getString("Date").equals(date)){			 			
			 			numAM_Saturday++;
			 		}
			 	}
			 	
			 	statement.close();
			 	resultSet.close();
			 	connection.close();
			 	
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();		
		}
	    return numAM_Saturday;	
	}
	
	//method to calculate the number of PM Customer Attendances on a given Sunday
	public int calcNumPMSunday(String date){
		
		String sqlQuery = "select * FROM Customers_Attendance";
		connection = SqliteConnection.Connector();
		int numPM_Sunday = 0; 	
	    try {
			
	    	statement = connection.createStatement();
			 	resultSet = statement.executeQuery(sqlQuery);
			 	
			 	
			 	//Checks to see whether the day of week is sunday and sets corresponding values
			 	while(resultSet.next()){
			 		if(resultSet.getString("AMPM").equals("PM") && resultSet.getString("Day_of_Week").equals("SUNDAY")&& resultSet.getString("Date").equals(date)){			 			
			 			numPM_Sunday++;
			 		}
			 	}
			 	
			 	statement.close();
			 	resultSet.close();
			 	connection.close();
			 	
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();		
		}
	    return numPM_Sunday;	
	}
	
	//method to calculate the number of PM Customer Attendances on a given Monday
	public int calcNumPMMonday(String date){
		
		String sqlQuery = "select * FROM Customers_Attendance";
		connection = SqliteConnection.Connector();
		int numPM_Monday = 0; 	
	    try {
			
	    	statement = connection.createStatement();
			 	resultSet = statement.executeQuery(sqlQuery);
			 	
			 	//Checks to see whether the day of week is monday and sets corresponding values
			 	while(resultSet.next()){
			 		if(resultSet.getString("AMPM").equals("PM") && resultSet.getString("Day_of_Week").equals("MONDAY")&& resultSet.getString("Date").equals(date)){			 			
			 			numPM_Monday++;
			 		}
			 	}
			 	
			 	statement.close();
			 	resultSet.close();
			 	connection.close();
			 	
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();		
		}
	    return numPM_Monday;	
	}
	
	//method to calculate the number of PM Customer Attendances on a given Tuesday
	public int calcNumPMTuesday(String date){
		
		String sqlQuery = "select * FROM Customers_Attendance";
		connection = SqliteConnection.Connector();
		int numPM_Tuesday = 0; 	
	    try {
			
	    	statement = connection.createStatement();
			 	resultSet = statement.executeQuery(sqlQuery);
			 	
			 	//Checks to see whether the day of week is tuesday and sets corresponding values
			 	while(resultSet.next()){
			 		if(resultSet.getString("AMPM").equals("PM") && resultSet.getString("Day_of_Week").equals("TUESDAY")&& resultSet.getString("Date").equals(date)){			 			
			 			numPM_Tuesday++;
			 		}
			 	}
			 	
			 	statement.close();
			 	resultSet.close();
			 	connection.close();
			 	
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();		
		}
	    return numPM_Tuesday;	
	}
	
	//method to calculate the number of PM Customer Attendances on a given Wednesday
	public int calcNumPMWednesday(String date){
		
		String sqlQuery = "select * FROM Customers_Attendance";
		connection = SqliteConnection.Connector();
		int numPM_Wednesday = 0; 	
	    try {
			
	    	statement = connection.createStatement();
			 	resultSet = statement.executeQuery(sqlQuery);
			 	
			 	//Checks to see whether the day of week is wednesday and sets corresponding values
			 	while(resultSet.next()){
			 		if(resultSet.getString("AMPM").equals("PM") && resultSet.getString("Day_of_Week").equals("WEDNESDAY")&& resultSet.getString("Date").equals(date)){			 			
			 			numPM_Wednesday++;
			 		}
			 	}
			 	
			 	statement.close();
			 	resultSet.close();
			 	connection.close();
			 	
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();		
		}
	    return numPM_Wednesday;	
	}
	
	//method to calculate the number of PM Customer Attendances on a given Thursday
	public int calcNumPMThursday(String date){
		
		String sqlQuery = "select * FROM Customers_Attendance";
		connection = SqliteConnection.Connector();
		int numPM_Thursday = 0; 	
	    try {
			
	    	statement = connection.createStatement();
			 	resultSet = statement.executeQuery(sqlQuery);
			 	
			 	//Checks to see whether the day of week is thursday and sets corresponding values
			 	while(resultSet.next()){
			 		if(resultSet.getString("AMPM").equals("PM") && resultSet.getString("Day_of_Week").equals("THURSDAY")&& resultSet.getString("Date").equals(date)){			 			
			 			numPM_Thursday++;
			 		}
			 	}
			 	
			 	statement.close();
			 	resultSet.close();
			 	connection.close();
			 	
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();		
		}
	    return numPM_Thursday;	
	}
	
	//method to calculate the number of PM Customer Attendances on a given Friday
	public int calcNumPMFriday(String date){
		
		String sqlQuery = "select * FROM Customers_Attendance";
		connection = SqliteConnection.Connector();
		int numPM_Friday = 0; 	
	    try {
			
	    	statement = connection.createStatement();
			 	resultSet = statement.executeQuery(sqlQuery);
			 	
			 	//Checks to see whether the day of week is friday and sets corresponding values
			 	while(resultSet.next()){
			 		if(resultSet.getString("AMPM").equals("PM") && resultSet.getString("Day_of_Week").equals("FRIDAY")&& resultSet.getString("Date").equals(date)){			 			
			 			numPM_Friday++;
			 		}
			 	}
			 	
			 	statement.close();
			 	resultSet.close();
			 	connection.close();
			 	
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();		
		}
	    return numPM_Friday;	
	}
	
	//method to calculate the number of PM Customer Attendances on a given Saturday
	public int calcNumPMSaturday(String date){
		
		String sqlQuery = "select * FROM Customers_Attendance";
		connection = SqliteConnection.Connector();
		int numPM_Saturday = 0; 	
	    try {
			
	    	statement = connection.createStatement();
			 	resultSet = statement.executeQuery(sqlQuery);
			 	
			 	//Checks to see whether the day of week is saturday and sets corresponding values
			 	while(resultSet.next()){
			 		if(resultSet.getString("AMPM").equals("PM") && resultSet.getString("Day_of_Week").equals("SATURDAY")&& resultSet.getString("Date").equals(date)){			 			
			 			numPM_Saturday++;
			 		}
			 	}
			 	
			 	statement.close();
			 	resultSet.close();
			 	connection.close();
			 	
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();		
		}
	    return numPM_Saturday;	
	}
	

	



  
}
