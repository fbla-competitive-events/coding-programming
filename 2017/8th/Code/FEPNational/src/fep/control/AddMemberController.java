package fep.control;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.SQLDataException;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXDatePicker;

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

public class AddMemberController implements Initializable {

	/**
	 * @author Aatif Jiwani
	 * @version 3.0
	 * 
	 *          This is a controller class. This class handles all the actions
	 *          of the Add Member window.
	 */

	/**
	 * !IMPORTANT! All Try and Catches are meant to notify the user of their
	 * mistake if their input is not proper.
	 */

	/*
	 * Pre-defined global variables Each of these variables are used in at least
	 * one method in this class.
	 */

	public LoginModel loginModel = new LoginModel();

	public ComboBox<String> state;
	public TextField name;
	public TextField phoneNumber;
	public TextField email;
	public TextField address;
	public TextField city;
	public TextField postal;

	public DatePicker birthdate;
	public DatePicker joindate;

	public Label status;

	public AnchorPane pane;

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

	/*
	 * This method adds the member to the SQLite database
	 */
	public void btnAdd(ActionEvent event) {
		try {
			if (!state.getValue().equals("--")) {

				// Query that inserts values from combo boxes and text fields
				// into database.
				String query = "insert into Membership (Name,PhoneNumber,Email,Address,City,State,Postal,Birthdate,JoinDate) values"
						+ " (?,?,?,?,?,?,?,?,?)";
				PreparedStatement pst = loginModel.connection.prepareStatement(query);

				// Inserts values into query
				pst.setString(1, name.getText());
				pst.setString(2, phoneNumber.getText());
				pst.setString(3, email.getText());
				pst.setString(4, address.getText());
				pst.setString(5, city.getText());
				pst.setString(6, state.getValue());
				pst.setString(7, postal.getText());
				pst.setString(8, dateAssemble(birthdate));
				pst.setString(9, dateAssemble(joindate));

				pst.execute();
				pst.close();

				System.out.println("Member added");

				status.setText("Member Added!");

			} else {
				status.setText("Error Occurred");
				throw new SQLDataException();
			}

			// If any exception occurs...
		} catch (Exception ex) {
			status.setText("Error Occurred");
			System.out.println("Console: Error occurred while adding employee");
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
	public void btnBack(ActionEvent event) throws IOException {
		status.setText("Loading Window");

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
	 * Algorithm that automatically adds the "-" while typing 
	 * the customer's phone number
	 * @throws IOException
	 */
	public void filterReleased() throws IOException {
		String text = phoneNumber.getText();
		System.out.println(phoneNumber.getCaretPosition());
		int length = text.length();
		if (length == 3 || length == 7) {
			phoneNumber.setText(text + "-");
			phoneNumber.positionCaret(phoneNumber.getText().length());
		}
	}

}
