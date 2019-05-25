package fep.control;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class SettingsController implements Initializable {
	/**
	 * @author Aatif Jiwani
	 * @version 3.0
	 * 
	 * This is a controller class. This class handles all the actions of the Settings window. 
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

	public Label statusAdd;
	public Label statusDelete;
	public Label statusReset;

	public TextField add;

	public int deleteCounter = 0;

	public ComboBox<String> delete;

	public PasswordField current;
	public PasswordField newPass;
	public PasswordField confirm;

	ObservableList<String> list = FXCollections.observableArrayList();
	ObservableList<String> weeks = FXCollections.observableArrayList();

	/**
	 * This method populates the Delete year comboBox with the current years available
	 * in the database. 
	 */
	public void initialize(URL location, ResourceBundle resources) {
		String query = "select * from 'Years'";
		try {
			PreparedStatement pst = loginModel.connection.prepareStatement(query);
			ResultSet rs = pst.executeQuery();
			while (rs.next()) {
				list.add(rs.getString("Year"));
			}
			delete.setItems(list);
			delete.setValue(delete.getItems().get(0));
			pst.close();
			rs.close();
		} catch (Exception e) {

		}
	}

	/**
	 * 
	 * @param event
	 * @throws IOException
	 * 
	 * This method adds the inputed year to the database so it can be used for schedules. 
	 */
	public void btnAddYear(ActionEvent event) throws IOException {
		String value = add.getText();
		try {
			
			int yearVal = Integer.parseInt(value);
			/*If inputed year is valid... 
			 * This program only works for 4 number years. This program will be long updates once
			 * 5 number years start. 
			 */
			if (yearVal > 2015 && value.trim().length() == 4) {
				//Inserts year value into the Year table that holds all the current years on the 
				//database. 
				String query = "insert into 'Years' (Year) values (?)";
				PreparedStatement pst = loginModel.connection.prepareStatement(query);
				pst.setString(1, value.trim());
				pst.execute();
				pst.close();

				//The following code creates a table for the year to hold weekly schedule logs. 
				String query2 = "CREATE TABLE '" + value.trim() + "Weeks' (WeekNum)";
				PreparedStatement pst2 = loginModel.connection.prepareStatement(query2);
				pst2.execute();
				pst2.close();
				
				delete.getItems().add(value);
				statusAdd.setText("Year added successfully!");
				
			} else {
				statusAdd.setText("Not a valid year. (YYYY)");
			}

		} catch (Exception e) {
			statusAdd.setText("Error occurred!");
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param event
	 * @throws IOException
	 * 
	 * This method deletes the selected year and deletes all the tables associated with it. 
	 */
	public void btnDeleteYear(ActionEvent event) throws IOException {
		//This is a failsafe to make sure that the user does not accidently delete the selected year. 
		//If the button is selected once, it will prompt the user to click the button again.
		if (deleteCounter % 2 == 0) {
			statusDelete.setText("Are you sure? Click again");
			deleteCounter++;
		} else {
			String yearVal = delete.getValue();
			weeks = FXCollections.observableArrayList();

			try {
				//The following code retrieves all the weekly schedules of the selected year
				String query = "select * from '" + yearVal.trim() + "Weeks'";
				PreparedStatement pst = loginModel.connection.prepareStatement(query);
				ResultSet rs = pst.executeQuery();
				while (rs.next()) {
					weeks.add(rs.getString("WeekNum"));
				}
				pst.close();
				rs.close();

				//For every week of the selected year, delete the employee schedule, employee attendance, and customer
				//attendance. 
				for (int i = 0; i < weeks.size(); i++) {
					String value = weeks.get(i);
					String query2 = "DROP TABLE '" + value + "'";
					PreparedStatement pst2 = loginModel.connection.prepareStatement(query2);
					pst2.execute();
					pst2.close();

					String query3 = "DROP TABLE '" + value.substring(0, 4) + "Customer" + value.substring(4) + "'";
					PreparedStatement pst3 = loginModel.connection.prepareStatement(query3);
					pst3.execute();
					pst3.close();

					String query4 = "DROP TABLE '" + value.substring(0, 4) + "Attendance" + value.substring(4) + "'";
					PreparedStatement pst4 = loginModel.connection.prepareStatement(query4);
					pst4.execute();
					pst4.close();
				}

				String query5 = "DROP TABLE '" + yearVal + "Weeks'";
				PreparedStatement pst5 = loginModel.connection.prepareStatement(query5);
				pst5.execute();
				pst5.close();

				String query6 = "Delete from 'Years' where Year=?";
				PreparedStatement pst6 = loginModel.connection.prepareStatement(query6);
				pst6.setString(1, yearVal.trim());
				pst6.execute();
				pst6.close();

				statusDelete.setText("Year Deleted");
				deleteCounter++;
				
				delete.getItems().remove(yearVal);
				delete.setValue(delete.getItems().get(0));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 
	 * @param event
	 * @throws IOException
	 * 
	 * This program resets the administrator password
	 */
	public void btnResetPassword(ActionEvent event) throws IOException {
		
		String query = "select * from Administrator where rowid='1'";
		try {
			//The following code finds the current admin password in the database and stores it into a
			//variable. 
			PreparedStatement pst = loginModel.connection.prepareStatement(query);
			ResultSet rs = pst.executeQuery();
			String oldPass = rs.getString("pass");
			pst.close();
			rs.close();
			
			//If current password equals the inputed password. 
			if (current.getText().trim().equals(oldPass)) {
				String newPassword = newPass.getText().trim();
				String confirmed = confirm.getText().trim();
				
				//If the new password equals the repeated password
				if (newPassword.equals(confirmed)) {
					String query2 = "update Administrator set pass='" + newPassword + "' where rowid='1'";
					PreparedStatement pst2 = loginModel.connection.prepareStatement(query2);
					pst2.execute();
					pst2.close();
					statusReset.setText("Password updated!");
					current.setText("");
					newPass.setText("");
					confirm.setText("");
				} else {
					statusReset.setText("Passwords do not match!");

				}
			} else {
				statusReset.setText("Wrong password!");
			}
		} catch (Exception e) {
			statusReset.setText("Error occurred!");
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param event
	 * @throws IOException
	 * 
	 * Closes program
	 */
	public void btnExit(ActionEvent event) throws IOException {
		System.exit(0);
	}

	/**
	 * 
	 * @param event
	 * @throws IOException
	 * 
	 * This method directs the user to the Main Menu while also hiding/exiting current window.
	 */
	public void btnMenu(ActionEvent event) throws IOException {
		((Node) event.getSource()).getScene().getWindow().hide();

		Stage primaryStage = new Stage();
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(Main.class.getResource("/fep/MenuView.fxml"));
		AnchorPane mainLayout = loader.load();
		Scene scene = new Scene(mainLayout);
		primaryStage.setScene(scene);
		primaryStage.show();
	}

}
