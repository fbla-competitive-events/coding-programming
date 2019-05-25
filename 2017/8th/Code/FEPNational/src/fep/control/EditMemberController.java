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
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class EditMemberController implements Initializable {

	/**
	 * @author Aatif Jiwani
	 * @version 3.0
	 * 
	 * This is a controller class. This class handles all the actions of the Edit Member window. 
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

	public ComboBox<String> state;
	public TextField name;
	public TextField phoneNumber;
	public TextField email;
	public TextField address;
	public TextField city;
	public TextField postal;
	public TextField phoneQuery;

	public DatePicker birthQuery;
	public DatePicker joindate;
	public Label employeeStatus;
	public Label birthdate;
	

	private int rowID = 0;

	ObservableList<String> listStates = FXCollections.observableArrayList("AL", "AK", "AZ", "AR", "CA ", "CO", "CT",
			"DE", "DC", "FL", "GA", "HI", "ID", "IN", "IA", "KS", "KY", "LA", "ME", "MD", "MA", "MI", "MN", "MS", "MO",
			"MT", "NE", "NV", "NH", "NJ", "NM", "NY", "NC", "ND", "OH", "OK", "OR", "PA", "RI", "SC", "SD", "TN", "TX",
			"UT", "VT", "VA", "WA", "WV", "WI", "WY");

	@Override
	/*
	 * (non-Javadoc)
	 * 
	 * @see javafx.fxml.Initializable#initialize(java.net.URL,
	 * java.util.ResourceBundle) Initializes comboBoxes
	 */
	public void initialize(URL location, ResourceBundle resources) {
		state.setItems(listStates);

	}

	/**
	 * This method retrieves the member with the associated phone number and birthdate and
	 * populates the empty text fields with the respective values
	 * @param event
	 * @throws IOException
	 */
	public void btnSearch(ActionEvent event) throws IOException {
		String pQuery = phoneQuery.getText();
		String dateQuery = dateAssemble(birthQuery);
		try {
			System.out.println("Searching for: " + pQuery + " " + dateQuery);
			String query = "select * from Membership where PhoneNumber=? AND Birthdate=?";
			PreparedStatement pst = loginModel.connection.prepareStatement(query);
			pst.setString(1, pQuery);
			pst.setString(2, dateQuery);
			ResultSet rs = pst.executeQuery();

			String value = rs.getString("Name");

			while (rs.next()) {
				name.setText(value);
				phoneNumber.setText(pQuery);
				email.setText(rs.getString("Email"));
				address.setText(rs.getString("Address"));
				city.setText(rs.getString("City"));
				state.setValue(rs.getString("State"));
				postal.setText(rs.getString("Postal"));
				birthdate.setText(dateQuery);
				joindate.setValue(dateReturn(rs.getString("JoinDate")));
			}

			pst.close();
			rs.close();

			employeeStatus.setText("Member found!");
		} catch (Exception e) {
			employeeStatus.setText("Member not found!");
			e.printStackTrace();
		}
	}

	
	/**
	 * Updates the member with new values
	 * @param event
	 * @throws IOException
	 */
	public void btnUpdate(ActionEvent event) throws IOException {
		try {

			String query5 = "Update Membership set Name='" + name.getText() + "'" + ",PhoneNumber='"
					+ phoneNumber.getText() + "'" + ",Email='" + email.getText() + "'" + ",Address='"
					+ address.getText() + "'" + ",City='" + city.getText() + "'" + ",State='" + state.getValue() + "'"
					+ ",Postal='" + postal.getText() + "'" + ",Birthdate='" + birthdate.getText() + "'" + ",JoinDate='"
					+ dateAssemble(joindate) + "' where Birthdate='" + birthdate.getText() + "'";
			PreparedStatement pst = loginModel.connection.prepareStatement(query5);

			pst.execute();
			pst.close();
			System.out.println("Console: Member updated");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * 
	 * @param event
	 * @throws IOException
	 * 
	 * This method returns user back to the main menu while hiding/exiting the current window. 
	 * If computer is too slow, status will change to notify user window is being loaded as fast as possible. 
	 */
	public void btnMenu(ActionEvent event) throws IOException {
		employeeStatus.setText("Loading window");
		Stage primaryStage = new Stage();
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(Main.class.getResource("/fep/MenuView.fxml"));
		AnchorPane mainLayout = loader.load();
		Scene scene = new Scene(mainLayout);
		primaryStage.setScene(scene);
		primaryStage.show();
		((Node) event.getSource()).getScene().getWindow().hide();
	}

	public void btnDelete(ActionEvent event) throws IOException {
		try {

			String query2 = "Delete from Membership where Birthdate=?";
			PreparedStatement pst2 = loginModel.connection.prepareStatement(query2);
			pst2.setString(1, birthdate.getText());

			pst2.execute();
			pst2.close();
			employeeStatus.setText("Member Deleted!");

			name.setText("");
			email.setText("");
			address.setText("");
			city.setText("");
			postal.setText("");
			birthdate.setText("N/A");
			phoneNumber.setText("");
			phoneQuery.setText("");

			System.out.println("Console: User deleted");
		} catch (Exception e) {
			employeeStatus.setText("Cannot delete!");
			e.printStackTrace();
		}
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
	
	/**
	 * Associated method to the PhoneNumber textfield
	 */
	public void releasePhone() throws IOException {
		filterReleased(phoneNumber);
	}
	
	/**
	 * Associated method to the PhoneNumber query
	 */
	public void releaseQuery() throws IOException {
		filterReleased(phoneQuery);
	}
	
	
	/**
	 * Algorithm that automatically adds the "-" while typing 
	 * the customer's phone number
	 * @throws IOException
	 */
	public void filterReleased(TextField phoneNumber) throws IOException {
		String text = phoneNumber.getText();
		int length = text.length();
		if (length == 3 || length == 7){
			phoneNumber.setText(text + "-");
			phoneNumber.positionCaret(phoneNumber.getText().length());
		}
	}
	
	

}
