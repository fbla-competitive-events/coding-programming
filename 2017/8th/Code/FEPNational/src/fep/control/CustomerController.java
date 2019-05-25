package fep.control;

import java.awt.List;
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
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class CustomerController implements Initializable {
	/**
	 * @author Aatif Jiwani
	 * @version 3.0
	 * 
	 * This is a controller class. This class handles all the actions of the Enter customer attendance window. 
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

	public ComboBox<String> year;
	public ComboBox<String> week;
	public ComboBox<String> day;

	public Label status;
	public Label weekEnd;
	public Label weekStart;

	public TextField am12;
	public TextField am11;
	public TextField am10;
	public TextField am09;
	public TextField am08;
	public TextField am07;
	public TextField am06;
	public TextField am05;
	public TextField am04;
	public TextField am03;
	public TextField am02;
	public TextField am01;
	public TextField pm12;
	public TextField pm11;
	public TextField pm10;
	public TextField pm09;
	public TextField pm08;
	public TextField pm07;
	public TextField pm06;
	public TextField pm05;
	public TextField pm04;
	public TextField pm03;
	public TextField pm02;
	public TextField pm01;

	public String comboYear;
	public String comboWeek;

	ObservableList<String> listYear = FXCollections.observableArrayList();
	ObservableList<String> listWeek = FXCollections.observableArrayList();
	ObservableList<String> listEmployees = null;
	ObservableList<String> listDay = FXCollections.observableArrayList("Sunday", "Monday", "Tuesday", "Wednesday",
			"Thursday", "Friday", "Saturday");
	String[] times = new String[] { "12 AM", "1 AM", "2 AM", "3 AM", "4 AM", "5 AM", "6 AM", "7 AM", "8 AM", "9 AM",
			"10 AM", "11 AM", "12 PM", "1 PM", "2 PM", "3 PM", "4 PM", "5 PM", "6 PM", "7 PM", "8 PM", "9 PM", "10 PM",
			"11 PM" };
	ObservableList<TextField> texts;

	@Override
	/*
	 * (non-Javadoc)
	 * @see javafx.fxml.Initializable#initialize(java.net.URL, java.util.ResourceBundle)
	 * 
	 * This method populates the Year and Week comboboxes. 
	 */
	public void initialize(URL location, ResourceBundle resources) {
		texts = FXCollections.observableArrayList(am12, am01, am02, am03, am04, am05, am06, am07, am08, am09, am10,
				am11, pm12, pm01, pm02, pm03, pm04, pm05, pm06, pm07, pm08, pm09, pm10, pm11);

		
		try {
			//The following code populates the Year combobox with proper values
			String query = "select * from Years";
			PreparedStatement pst = loginModel.connection.prepareStatement(query);
			ResultSet rs = pst.executeQuery();

			int counter = 0;
			String first = "";
			while (rs.next()) {
				if (counter == 0) {
					first = rs.getString("Year");
					listYear.add(first);
				} else {
					listYear.add(rs.getString("Year"));
				}
				counter++;
			}

			year.setItems(listYear);
			year.setValue(first);
			pst.close();
			rs.close();

			//The following code populates the Week combobox with the proper values
			String query2 = "select * from '" + year.getValue().trim() + "Weeks'";
			PreparedStatement pst2 = loginModel.connection.prepareStatement(query2);
			ResultSet rs2 = pst2.executeQuery();

			int counterWeek = 0;
			String firstWeek = "";
			while (rs2.next()) {
				if (counterWeek == 0) {
					String temp = rs2.getString("WeekNum");
					firstWeek = temp.substring(4, 8) + " " + temp.substring(8);

					listWeek.add(firstWeek);
				} else {
					String temp = rs2.getString("WeekNum");
					listWeek.add(temp.substring(4, 8) + " " + temp.substring(8));
				}
				counter++;
			}
			week.setItems(listWeek);
			week.setValue(firstWeek);
			pst2.close();
			rs2.close();
			//If error occurs...
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/*
	 * This method finds the selected weekly customer attendance log of the selected year and populates
	 * the values into the text fields that correspond to the default day of the week, in this case Sunday. 
	 */
	public void btnSearch(ActionEvent event) throws IOException {
		day.setItems(listDay);
		day.setValue(day.getItems().get(0));
		comboYear = year.getValue();
		comboWeek = week.getValue();
		try {
			//Updates week start/end values
			String query = "select * from '" + comboYear.trim() + comboWeek.substring(0, 4) + comboWeek.substring(5)
					+ "' where rowid='1'";
			PreparedStatement pst = loginModel.connection.prepareStatement(query);
			ResultSet rs = pst.executeQuery();
			while (rs.next()) {
				weekStart.setText(rs.getString("WeekStart"));
				weekEnd.setText(rs.getString("WeekEnd"));
			}

			pst.close();
			rs.close();

			//The following code updates the text fields with proper values. 
			String comboDay = day.getValue().trim();
			for (int i = 0; i < times.length; i++) {
				String query2 = "select * from '" + comboYear.trim() + "Customer" + comboWeek.substring(0, 4)
						+ comboWeek.substring(5) + "' where TimeOfDay=?";
				PreparedStatement pst2 = loginModel.connection.prepareStatement(query2);
				pst2.setString(1, times[i].trim());
				ResultSet rs2 = pst2.executeQuery();
				String value = "";
				while (rs2.next()) {
					value = rs2.getString(comboDay);
				}
				texts.get(i).setText(value);
				pst2.close();
				rs2.close();

			}
			status.setText("Data loaded!");
		} catch (Exception e) {
			e.printStackTrace();
			status.setText("Error occurred");

		}

	}

	/*
	 * @see method resetTexts
	 */
	public void btnReset(ActionEvent event) throws IOException {
		resetTexts();
	}

	/*
	 * This method updates the database with new inputed values of the selected day of the week. 
	 * It takes the values from each text field and updates them in the database. 
	 */
	public void btnUpdate(ActionEvent event) throws IOException {
		try {
			String comboDay = day.getValue();
			for (int i = 0; i < times.length; i++) {
				String query = "Update '" + comboYear.trim() + "Customer" + comboWeek.substring(0, 4)
						+ comboWeek.substring(5) + "' set " + "" + comboDay + "='" + texts.get(i).getText()
						+ "' where TimeOfDay='" + times[i].trim() + "'";
				PreparedStatement pst = loginModel.connection.prepareStatement(query);
				pst.execute();
				pst.close();
			}
			status.setText("Data updated!");

		} catch (Exception e) {
			e.printStackTrace();
			status.setText("Error occurred!");

		}
	}

	/*
	 * This method directs the user back to the Main Menu while also hiding/exiting current window. 
	 */
	public void btnMenu(ActionEvent event) throws IOException {
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

	/*
	 * This method resets all text fields in case the user makes a mistake. 
	 */
	public void resetTexts() {
		for (int i = 0; i < texts.size(); i++) {
			texts.get(i).setText("0");
		}

	}

	/*
	 * This method changes the Customer text field values to that of what the current selected day of the week is. 
	 * For instance, when Monday is selected, the program will populate the text fields with the proper customer attendance
	 * of that day of the selected week of the selected year.  
	 */
	public void comboDayChange(ActionEvent event) throws IOException {
		System.out.println("heelo");
		try {
			String comboDay = day.getValue().trim();
			for (int i = 0; i < times.length; i++) {
				String query2 = "select * from '" + comboYear.trim() + "Customer" + comboWeek.substring(0, 4)
						+ comboWeek.substring(5) + "' where TimeOfDay=?";
				PreparedStatement pst2 = loginModel.connection.prepareStatement(query2);
				pst2.setString(1, times[i].trim());
				ResultSet rs2 = pst2.executeQuery();
				String value = "";
				while (rs2.next()) {
					value = rs2.getString(comboDay);
				}
				texts.get(i).setText(value);
				pst2.close();
				rs2.close();

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*
	 * This method populates the Week combobox with the proper values once a new year is selected. 
	 * The method exists because every year may have a different amount of weekly schedules created at
	 * any given time. 
	 */
	public void comboYearChange(ActionEvent event) throws IOException {
		listWeek = FXCollections.observableArrayList();
		try {
			String query2 = "select * from '" + year.getValue().trim() + "Weeks'";
			PreparedStatement pst2 = loginModel.connection.prepareStatement(query2);
			ResultSet rs2 = pst2.executeQuery();
			int counterWeek = 0;
			String firstWeek = "";
			while (rs2.next()) {
				if (counterWeek == 0) {
					String temp = rs2.getString("WeekNum");
					firstWeek = temp.substring(4, 8) + " " + temp.substring(8);

					listWeek.add(firstWeek);
				} else {
					String temp = rs2.getString("WeekNum");
					listWeek.add(temp.substring(4, 8) + " " + temp.substring(8));
				}

			}
			week.setItems(listWeek);
			week.setValue(firstWeek);
			pst2.close();
			rs2.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}