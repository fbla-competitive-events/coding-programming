package application;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Menu_CustomerModel {
	private final SimpleStringProperty CustomersFirst_Name;
	private final SimpleStringProperty CustomersLast_Name;
	private final SimpleStringProperty Customers_Address;
	private final SimpleStringProperty Customers_Email;
	private final SimpleStringProperty Customers_Phone;
	private final SimpleStringProperty Customers_DOB;
	private final SimpleIntegerProperty Customers_ID;

	Connection connection;

	private Statement statement;

	private ResultSet resultSet;

	// constructors
	public Menu_CustomerModel(String CustomersFirst_Name, String CustomersLast_Name, String CustomersAddress,
			String CustomersEmail, String CustomersPhone, String CustomersDOB, Integer CustomersID) {

		this.CustomersFirst_Name = new SimpleStringProperty(CustomersFirst_Name);
		this.CustomersLast_Name = new SimpleStringProperty(CustomersLast_Name);
		this.Customers_Address = new SimpleStringProperty(CustomersAddress);
		this.Customers_Email = new SimpleStringProperty(CustomersEmail);
		this.Customers_Phone = new SimpleStringProperty(CustomersPhone);
		this.Customers_DOB = new SimpleStringProperty(CustomersDOB);
		this.Customers_ID = new SimpleIntegerProperty(CustomersID);
	}

	public Menu_CustomerModel() {

		this.CustomersFirst_Name = new SimpleStringProperty("");
		this.CustomersLast_Name = new SimpleStringProperty("");
		this.Customers_Address = new SimpleStringProperty("");
		this.Customers_Email = new SimpleStringProperty("");
		this.Customers_Phone = new SimpleStringProperty("");
		this.Customers_DOB = new SimpleStringProperty("");
		this.Customers_ID = new SimpleIntegerProperty(0);
	}

	// Adds the customers to an Observable List to eventually display on the
	// table
	public ObservableList<Menu_CustomerModel> getDataFromSqlAndAddToObservableList(String query) {
		ObservableList<Menu_CustomerModel> customerTableData = FXCollections.observableArrayList();
		try {
			connection = SqliteConnection.Connector();
			statement = connection.createStatement();
			resultSet = statement.executeQuery(query);

			if (resultSet.isBeforeFirst()) {
				while (resultSet.next()) {
					customerTableData.add(
							new Menu_CustomerModel(resultSet.getString("First_Name"), resultSet.getString("Last_Name"),
									resultSet.getString("Address"), resultSet.getString("Email"),
									resultSet.getString("Phone"), resultSet.getString("DOB"), resultSet.getInt("ID")));
				}
			} else {
				return null;
			}

			statement.close();
			resultSet.close();
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return customerTableData;

	}

	// getters and setters
	public String getCustomersFirst_Name() {
		return CustomersFirst_Name.get();
	}

	public SimpleStringProperty CustomersFirst_Name() {
		return CustomersFirst_Name();
	}

	public void setCustomersFirst_Name(String CustomersFirst_Name) {
		this.CustomersFirst_Name.set(CustomersFirst_Name);
	}

	public String getCustomersLast_Name() {
		return CustomersLast_Name.get();
	}

	public SimpleStringProperty CustomersLast_Name() {
		return CustomersLast_Name();
	}

	public void setCustomersLast_Name(String CustomersLast_Name) {
		this.CustomersLast_Name.set(CustomersLast_Name);
	}

	public String getCustomers_Email() {
		return Customers_Email.get();
	}

	public SimpleStringProperty Customers_Email() {
		return Customers_Email();
	}

	public void setCustomers_Email(String Customers_Email) {
		this.Customers_Email.set(Customers_Email);
	}

	public String getCustomers_Phone() {
		return Customers_Phone.get();
	}

	public SimpleStringProperty Customers_Phone() {
		return Customers_Phone();
	}

	public void setCustomers_Phone(String Customers_Phone) {
		this.Customers_Phone.set(Customers_Phone);
	}

	public String getCustomers_Address() {
		return Customers_Address.get();
	}

	public SimpleStringProperty Customers_Address() {
		return Customers_Address();
	}

	public void setCustomers_Address(String Customers_Address) {
		this.Customers_Address.set(Customers_Address);
	}

	public String getCustomers_DOB() {
		return Customers_DOB.get();
	}

	public SimpleStringProperty Customers_DOB() {
		return Customers_DOB();
	}

	public void setCustomers_DOB(String Customers_DOB) {
		this.Customers_DOB.set(Customers_DOB);
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
