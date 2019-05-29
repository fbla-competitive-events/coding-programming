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

public class Main_Menu_EmployeeModel{
	
	//Employee information variables
	private final SimpleIntegerProperty EmployeesID;
	private final SimpleStringProperty EmployeesFirst_Name;
	private final SimpleStringProperty EmployeesLast_Name;
	private final SimpleStringProperty EmployeesEmail;
	private final SimpleStringProperty EmployeesPhone;
	private final SimpleStringProperty EmployeesAddress;
	private final SimpleStringProperty EmployeesDOB;
    Connection connection;
	
    private Statement statement;
    private ResultSet resultSet;
    
    //Constructors
	public Main_Menu_EmployeeModel(int EmployeesID, String EmployeesFirst_Name, String EmployeesLast_Name, String EmployeesEmail, String EmployeesPhone, String EmployeesAddress, String EmployeesDOB){

		this.EmployeesID = new SimpleIntegerProperty(EmployeesID);
		this.EmployeesFirst_Name = new SimpleStringProperty(EmployeesFirst_Name);
		this.EmployeesLast_Name = new SimpleStringProperty(EmployeesLast_Name);
		this.EmployeesEmail = new SimpleStringProperty(EmployeesEmail);
		this.EmployeesPhone = new SimpleStringProperty(EmployeesPhone);
		this.EmployeesAddress = new SimpleStringProperty(EmployeesAddress);
		this.EmployeesDOB = new SimpleStringProperty(EmployeesDOB);
	}
	
	//Constructor with no parameters
	public Main_Menu_EmployeeModel(){

		this.EmployeesID = new SimpleIntegerProperty(0);
		this.EmployeesFirst_Name = new SimpleStringProperty("");
		this.EmployeesLast_Name = new SimpleStringProperty("");
		this.EmployeesEmail = new SimpleStringProperty("");
		this.EmployeesPhone = new SimpleStringProperty("");
		this.EmployeesAddress = new SimpleStringProperty("");
		this.EmployeesDOB = new SimpleStringProperty("");
	}
	

	//Adds the employees to an Observable List to eventually display on the table 
	public ObservableList<Main_Menu_EmployeeModel> getDataFromSqlAndAddToObservableList(String query){
	        ObservableList<Main_Menu_EmployeeModel> employeeTableData = FXCollections.observableArrayList();
	        try {
	        	connection = SqliteConnection.Connector();
	            statement = connection.createStatement();
	            resultSet = statement.executeQuery(query); 
	            if(resultSet.isBeforeFirst()){    
	            	while(resultSet.next()){
		                employeeTableData.add(new Main_Menu_EmployeeModel(
		                        resultSet.getInt("ID"),
		                        resultSet.getString("First_Name"),
		                        resultSet.getString("Last_Name"),
		                        resultSet.getString("Email"),
		                        resultSet.getString("Phone"),
		                        resultSet.getString("Address"),
		                        resultSet.getString("DOB")));
		            }
	            } 	
	            else{
	            	return null;
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

	    //getters and setters
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
	    
	    public String getEmployeesEmail() {
	        return EmployeesEmail.get();
	    }

	    public SimpleStringProperty EmployeesEmail() {
	        return EmployeesEmail();
	    }

	    public void setEmployeesEmail(String EmployeesEmail) {
	        this.EmployeesEmail.set(EmployeesEmail);
	    }
	    
	    public String getEmployeesPhone() {
	        return EmployeesPhone.get();
	    }

	    public SimpleStringProperty EmployeesPhone() {
	        return EmployeesPhone();
	    }

	    public void setEmployeesPhone(String EmployeesPhone) {
	        this.EmployeesPhone.set(EmployeesPhone);
	    }
	    
	    public String getEmployeesAddress() {
	        return EmployeesAddress.get();
	    }

	    public SimpleStringProperty EmployeesAddress() {
	        return EmployeesAddress();
	    }

	    public void setEmployeesAddress(String EmployeesAddress) {
	        this.EmployeesAddress.set(EmployeesAddress);
	    }
	    
	    public String getEmployeesDOB() {
	        return EmployeesDOB.get();
	    }

	    public SimpleStringProperty EmployeesDOB() {
	        return EmployeesDOB();
	    }

	    public void setEmployeesDOB(String EmployeesDOB) {
	        this.EmployeesDOB.set(EmployeesDOB);
	    }
	    
	    
	    
	    

	    
}


