package application;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Employee_Profile_ViewModel {
    //Employee information variables
	private String ID, Name, Email, Phone, Address, DOB ;
    
    Connection connection;
    Statement statement;
    ResultSet resultSet;
    
    // constructors
    public Employee_Profile_ViewModel() {
    }
    
    //constructor with id parameter
    public Employee_Profile_ViewModel(String id) {
        this.ID = id;
        setValues();
    }

    //setters
    private void setValues(){
        try {
            connection = SqliteConnection.Connector();
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM Employees where ID = '"+ID+"';");
            
            //Sets corresponding employee's values from the database based on the Employee ID
            while (resultSet.next()){
                Name=resultSet.getString("First_Name")+" "+resultSet.getString("Last_Name");
                Email = resultSet.getString("Email");
                Phone = resultSet.getString("Phone");
                Address = resultSet.getString("Address");
                DOB = resultSet.getString("DOB");
                
                resultSet.close();
                statement.close();
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    //getters
    
    //gets ID
    public String getId() {
        return ID;
    }
    
    //gets Employee name
    public String getName() {
        return Name;
    }

    //gets Employee email
    public String getEmail() {
        return Email;
    }

    //gets Employee phone number
    public String getPhone() {
        return Phone;
    }

    //gets Employee Address
    public String getAddress() {
        return Address;
    }

    //gets Employee DOB
    public String getDOB() {
        return DOB;
    }
}
