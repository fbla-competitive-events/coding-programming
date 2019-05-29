package application;

//import statements
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

//Employee information including shifts(AM vs PM) and Scheduled Date
public class Employee_Shift_SchedulerModel {
	private final SimpleIntegerProperty EmployeesID;
	private final SimpleStringProperty EmployeesFirst_Name;
	private final SimpleStringProperty EmployeesLast_Name;
	
	//Connection, statement, and resultSet for database connection
	Connection connection;
    private Statement statement;
    private ResultSet resultSet;
    
    //Constructors
    public Employee_Shift_SchedulerModel(int EmployeesID, String Date, String LastName, String FirstName){
		this.EmployeesID = new SimpleIntegerProperty(EmployeesID);
		this.EmployeesLast_Name = new SimpleStringProperty(LastName);
		this.EmployeesFirst_Name = new SimpleStringProperty(FirstName);
	}
    
    //constructor with ID, FirstName, and LastName
    public Employee_Shift_SchedulerModel(int EmployeesID, String FirstName, String LastName){

		this.EmployeesID = new SimpleIntegerProperty(EmployeesID);	
		this.EmployeesLast_Name = new SimpleStringProperty(LastName);
		this.EmployeesFirst_Name = new SimpleStringProperty(FirstName);
	}
	
	public Employee_Shift_SchedulerModel(){
		this.EmployeesID = new SimpleIntegerProperty(0);
		this.EmployeesFirst_Name = new SimpleStringProperty("");
		this.EmployeesLast_Name = new SimpleStringProperty("");
	}
	
	//Adds new Employees to Observable List to eventually be loaded into the table
	public ObservableList<String> getDataFromSqlAndAddToObservableList(String query){
        ObservableList<String> employeeTableData = FXCollections.observableArrayList();
        try {
        	connection = SqliteConnection.Connector();
            statement = connection.createStatement();
            resultSet = statement.executeQuery(query); 
           
            while(resultSet.next()){
                employeeTableData.add(
                        resultSet.getString("First_Name") + " " +
                        resultSet.getString("Last_Name") + " :" + resultSet.getString("ID")
                        );
            }
            
            statement.close();
            resultSet.close();
            connection.close();
        } 
        catch (SQLException e) {
            e.printStackTrace();
        }
        return employeeTableData;

    }
	
	//Add new Employees to the Employees_Schedule Table with their scheduler information
	public ObservableList<String> getDataFromSqlAndAddToObservableListSchedule(String query){
		ObservableList<String> employeeTableData = FXCollections.observableArrayList();
        try {
        	connection = SqliteConnection.Connector();
            statement = connection.createStatement();
            resultSet = statement.executeQuery(query); 
           
            while(resultSet.next()){
                employeeTableData.add(
                        resultSet.getString("ID") + ": " + resultSet.getString("First_Name") +" "+
                        resultSet.getString("Last_Name") +" "+resultSet.getString("Shift")
                        );
            }
            
            statement.close();
            resultSet.close();
            connection.close();
        } 
        catch (SQLException e) {
            e.printStackTrace();
        }
        return employeeTableData;

    }

		// getters and setters
	    public Integer getEmployeesID() {
	        return EmployeesID.get();
	    }

	    public SimpleIntegerProperty EmployeesID() {
	        return EmployeesID();
	    }

	    public void setEmployeesID(Integer EmployeesID) {
	        this.EmployeesID.set(EmployeesID);
	    }
	    
	    public String getEmployeesFirst_Name() {
	        return EmployeesFirst_Name.get();
	    }

	    public SimpleStringProperty EmployeesFirst_Name() {
	        return EmployeesFirst_Name();
	    }

	    public void setEmployeesFirst_Name(String EmployeesFirst_Name) {
	        this.EmployeesFirst_Name.set(EmployeesFirst_Name);
	    }
	    
	    public String getEmployeesLast_Name() {
	        return EmployeesLast_Name.get();
	    }

	    public SimpleStringProperty EmployeesLast_Name() {
	        return EmployeesLast_Name();
	    }

	    public void setEmployeesLast_Name(String EmployeesLast_Name) {
	        this.EmployeesLast_Name.set(EmployeesLast_Name);
	    }
}