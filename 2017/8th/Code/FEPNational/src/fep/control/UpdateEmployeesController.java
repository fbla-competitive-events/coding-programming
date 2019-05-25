package fep.control;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.ResourceBundle;

import fep.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.print.PrinterJob;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class UpdateEmployeesController implements Initializable {
	/**
	 * @author Aatif Jiwani
	 * @version 3.0
	 * 
	 * This is a controller class. This class handles all the actions of the Edit Employees window. 
	 */

	/**
	 * !IMPORTANT!
	 * All Try and Catches are meant to notify the user of their mistake if their input
	 * is not proper. 
	 */
	
	
	/*Pre-defined global variables
	 * Each of these variables are used in at least one method in this class. 
	 */

	public LoginModel loginModel = new LoginModel();

	public ComboBox<String> position;
	public ComboBox<String> partFull;
	public ComboBox<String> state;
	public TextField name;
	public TextField address;
	public TextField city;
	public TextField postal;
	public DatePicker birthdate;
	public DatePicker hiredate;
	public TextField query;

	public Label status;
	public Label employeeStatus;
	

	ObservableList<String> listPosition = FXCollections.observableArrayList("Manager", "Assistant Manager",
			"Floor Desk Receptionist", "Floor Supervisor", "Equipment Supervisor", "Janitor", "Security Guard");

	ObservableList<String> listPartFull = FXCollections.observableArrayList("Part Time", "Full Time");

	ObservableList<String> listStates = FXCollections.observableArrayList("AL", "AK", "AZ", "AR", "CA ", "CO", "CT",
			"DE", "DC", "FL", "GA", "HI", "ID", "IN", "IA", "KS", "KY", "LA", "ME", "MD", "MA", "MI", "MN", "MS", "MO",
			"MT", "NE", "NV", "NH", "NJ", "NM", "NY", "NC", "ND", "OH", "OK", "OR", "PA", "RI", "SC", "SD", "TN", "TX",
			"UT", "VT", "VA", "WA", "WV", "WI", "WY");

	/**
	 * 
	 * This method populates all comboBoxes with their proper, respective values. 
	 */
	public void initialize(URL location, ResourceBundle resources) {
		position.setItems(listPosition);
		partFull.setItems(listPartFull);
		state.setItems(listStates);
		

	}

	/**
	 * 
	 * @param event
	 * @throws IOException
	 * 
	 * This method finds the employee that correlates to the given ID number. If employee does not 
	 * exist, the status label will change. 
	 */
	public void btnSearch(ActionEvent event) throws IOException {
		String q = query.getText();
		String qry = "select * from Employees where id=? ";
		try {
			//The following code finds the employee with given ID in database. 
			PreparedStatement pst = loginModel.connection.prepareStatement(qry);
			pst.setString(1, q);
			ResultSet rs = pst.executeQuery();

			//This line of code is induces an error if there is no employee with the given ID. Once error occurs,
			//it will trigger th catch block. 
			String value = rs.getString("Name");

			//Updates credential text fields and comboboxes. 
			while (rs.next()) {
				name.setText(value);
				status.setText(rs.getString("ID"));
				address.setText(rs.getString("Address"));
				city.setText(rs.getString("City"));
				postal.setText(rs.getString("Postal"));
				birthdate.setValue(dateReturn((rs.getString("Birthdate"))));
				hiredate.setValue(dateReturn((rs.getString("HireDate"))));
				position.setValue(rs.getString("Position"));
				state.setValue(rs.getString("State"));
				partFull.setValue(rs.getString("PartFull"));
			}
			pst.close();
			rs.close();
			employeeStatus.setText("Employee found!");
		} catch (Exception e) {
			employeeStatus.setText("Employee not found!");
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param event
	 * @throws IOException
	 * 
	 * This method updates the selected employee with the given values. 
	 */
	public void btnUpdate(ActionEvent event) throws IOException {
		try {

			String query5 = "Update Employees set Name='" + name.getText() + "'" + ",ID='" + status.getText() + "'"
					+ ",Position='" + position.getValue() + "'" + ",PartFull='" + partFull.getValue() + "'"
					+ ",Address='" + address.getText() + "'" + ",City='" + city.getText() + "'" + ",State='"
					+ state.getValue() + "'" + ",Postal='" + postal.getText() + "'" + ",Birthdate='"
					+ dateAssemble(birthdate) + "'" + ",HireDate='" + dateAssemble(hiredate) + "' where ID='" + status.getText()
					+ "'";
			PreparedStatement pst = loginModel.connection.prepareStatement(query5);

			pst.execute();
			pst.close();
			System.out.println("Console: Employee updated");

			/*
			 * The following lines of code update the employee's name in every schedule ever created
			 * that they are in. 
			 */
			String query2 = "select * from Years";
			PreparedStatement pst2 = loginModel.connection.prepareStatement(query2);
			ResultSet rs2 = pst2.executeQuery();
			while (rs2.next()) {
				String query6 = "select * from '" + rs2.getString("Year") + "Weeks'";
				PreparedStatement pst6 = loginModel.connection.prepareStatement(query6);
				ResultSet rs6 = pst6.executeQuery();
				while (rs6.next()) {
					String week = rs6.getString("WeekNum");
					System.out.println(week);
					String query3 = "Update '" + week + "' set Employee='" + name.getText() + "' where ID='"
							+ status.getText() + "'";
					PreparedStatement pst3 = loginModel.connection.prepareStatement(query3);
					pst3.execute();
					pst3.close();

					String query4 = "Update '" + rs6.getString("WeekNum").substring(0, 4) + "Attendance"
								+ rs6.getString("WeekNum").substring(4)
								+ "' set Employee='" + name.getText() + "' where ID='"
							+ status.getText() + "'";
					PreparedStatement pst4 = loginModel.connection.prepareStatement(query4);
					pst4.execute();
					pst4.close();
				}
				pst6.close();
				rs6.close();
			}
			pst2.close();
			rs2.close();
			
			employeeStatus.setText("Employee updated!");
			name.setText("");
			status.setText("N/A");
			address.setText("");
			city.setText("");
			postal.setText("");
			birthdate.setValue(LocalDate.now());
			hiredate.setValue(LocalDate.now());
			position.setValue("Manager");
			query.setText("");
			
		} catch (Exception a) {
			employeeStatus.setText("Unable to update employee!");
			a.printStackTrace();
		}
	}

	/**
	 * 
	 * @param event
	 * @throws IOException
	 * 
	 * This method deletes the selected employee from the database. 
	 */
	public void btnDelete(ActionEvent event) throws IOException {
		try {
			/**
			 * Defining query to delete employee using their ID number
			 */
			String query2 = "Delete from Employees where ID=?";
			PreparedStatement pst2 = loginModel.connection.prepareStatement(query2);
			pst2.setString(1, status.getText());

			pst2.execute();
			pst2.close();
			employeeStatus.setText("Employee Deleted!");

			name.setText("");
			status.setText("N/A");
			address.setText("");
			city.setText("");
			postal.setText("");
			birthdate.setValue(LocalDate.now());
			hiredate.setValue(LocalDate.now());
			position.setValue("Manager");
			query.setText("");

			System.out.println("Console: User deleted");
		} catch (Exception e) {
			employeeStatus.setText("Unable to delete employee!");
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * @param event
	 * @throws IOException
	 * 
	 * This method directs the user back to the Main Menu while also hiding/exiting current window. 
	 */
	public void btnMenu(ActionEvent event) throws IOException {

		status.setText("Loading window");
		Stage primaryStage = new Stage();
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(Main.class.getResource("/fep/MenuView.fxml"));
		AnchorPane mainLayout = loader.load();
		Scene scene = new Scene(mainLayout);
		primaryStage.setScene(scene);
		primaryStage.show();
		((Node) event.getSource()).getScene().getWindow().hide();

	}

	/**
	 * 
	 * @param j DatePicker object with pre-defined date values
	 * @return A string in the format of MM/DD/YYYY. This is essentially the 
	 * unimplemented toString() method of the DatePicker object
	 */
	public String dateAssemble(DatePicker j) {
		int year = j.getValue().getYear();
		int month = j.getValue().getMonthValue();
		int day = j.getValue().getDayOfMonth();
		
		return month + "/" + day + "/" + year;
	}
	
	/**
	 * 
	 * @param s String returned from the database
	 * @return LocalDate value of the associated String 
	 */
	public LocalDate dateReturn(String s) {
		int month = Integer.parseInt(s.substring(0, s.indexOf("/")));
		int first = s.indexOf("/"), second = s.indexOf("/", first + 1);
		int day = Integer.parseInt(s.substring(first + 1, second));
		first = second;
		int year = Integer.parseInt(s.substring(first + 1));

		return LocalDate.of(year, month, day);
	}
}
