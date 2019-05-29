package application;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Menu_Customer_AttendanceModel {
	// Information variables for customer attendances

	private final SimpleIntegerProperty Customers_ID;
	private final SimpleStringProperty Customers_Date;
	private final SimpleStringProperty Customers_AMPM;
	private final SimpleStringProperty Customers_FirstName;
	private final SimpleStringProperty Customers_LastName;

	Connection connection;
	private Statement statement;
	private ResultSet resultSet;

	// Constructors
	public Menu_Customer_AttendanceModel(int CustomersID, String CustomersDate, String CustomersAMPM,
			String Customers_FirstName, String Customers_LastName) {
		this.Customers_Date = new SimpleStringProperty(CustomersDate);
		this.Customers_AMPM = new SimpleStringProperty(CustomersAMPM);
		this.Customers_ID = new SimpleIntegerProperty(CustomersID);
		this.Customers_FirstName = new SimpleStringProperty(Customers_FirstName);
		this.Customers_LastName = new SimpleStringProperty(Customers_LastName);
	}

	public Menu_Customer_AttendanceModel() {

		this.Customers_Date = new SimpleStringProperty("");
		this.Customers_AMPM = new SimpleStringProperty("");
		this.Customers_ID = new SimpleIntegerProperty(0);
		this.Customers_FirstName = new SimpleStringProperty("");
		this.Customers_LastName = new SimpleStringProperty("");
	}

	// Adds the customer attendances to an Observable List to eventually display
	// on the table
	public ObservableList<Menu_Customer_AttendanceModel> getDataFromSqlAndAddToObservableList(String query) {
		ObservableList<Menu_Customer_AttendanceModel> customerAttendanceTableData = FXCollections.observableArrayList();
		try {
			connection = SqliteConnection.Connector();
			statement = connection.createStatement();
			resultSet = statement.executeQuery(query);

			while (resultSet.next()) {
				customerAttendanceTableData.add(new Menu_Customer_AttendanceModel(
						resultSet.getInt("ID"),
						resultSet.getString("Date"), 
						resultSet.getString("AMPM"), 
						resultSet.getString("First_Name"),
						resultSet.getString("Last_Name")));
			}

			statement.close();
			resultSet.close();
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return customerAttendanceTableData;

	}

	// setters and getters
	public String getCustomers_Date() {
		return Customers_Date.get();
	}

	public SimpleStringProperty Customers_Date() {
		return Customers_Date();
	}

	public void setCustomers_Date(String Customers_Date) {
		this.Customers_Date.set(Customers_Date);
	}

	public String getCustomers_AMPM() {
		return Customers_AMPM.get();
	}

	public SimpleStringProperty Customers_AMPM() {
		return Customers_AMPM();
	}

	public void setCustomers_AMPM(String Customers_AMPM) {
		this.Customers_AMPM.set(Customers_AMPM);
	}

	public String getCustomers_FirstName() {
		return Customers_FirstName.get();
	}

	public SimpleStringProperty Customers_FirstName() {
		return Customers_FirstName();
	}

	public void setCustomers_FirstName(String Customers_FirstName) {
		this.Customers_FirstName.set(Customers_FirstName);
	}

	public String getCustomers_LastName() {
		return Customers_LastName.get();
	}

	public SimpleStringProperty Customers_LastName() {
		return Customers_LastName();
	}

	public void setCustomers_LastName(String Customers_LastName) {
		this.Customers_LastName.set(Customers_LastName);
	}

	public int getCustomers_ID() {
		return Customers_ID.get();
	}

	public SimpleIntegerProperty Customers_ID() {
		return Customers_ID();
	}

	public void setCustomers_ID(int Customers_ID) {
		this.Customers_ID.set(Customers_ID);
	}

}
