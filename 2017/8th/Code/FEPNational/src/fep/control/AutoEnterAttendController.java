package fep.control;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
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
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class AutoEnterAttendController implements Initializable {
	
	/**
	 * @author Aatif Jiwani
	 * @version 3.0
	 * 
	 * This is a controller class. This class handles all the actions of the Enter Weekly Attendance window. 
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
	public Label status;

	public int counter = 0;
	public String comboYear;
	public String textWeek;

	public ComboBox<String> year;
	public ComboBox<String> employee;
	public ComboBox<String> weekNumber;

	public Label weekStart;
	public Label weekEnd;
	public Label position;
	public Label partFull;
	public Label activity;

	ObservableList<String> listYear = FXCollections.observableArrayList();
	ObservableList<String> listWeek = FXCollections.observableArrayList();
	ObservableList<String> listEmployees = null;

	@Override
	/**
	 * This method initializes the Year and Week comboBoxes
	 */
	public void initialize(URL location, ResourceBundle resources) {
		try {
			/*
			 * The following code updates the Year and Week comboBoxes with the
			 * proper values.
			 */
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
			weekNumber.setItems(listWeek);
			weekNumber.setValue(firstWeek);
			pst2.close();
			rs2.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * This method directs the user to the manual entry of employee attendance 
	 * in case the Clock ON/OFF buttons are not working. 
	 * @param event
	 * @throws IOException
	 */
	public void btnManual(ActionEvent event) throws IOException {
		status.setText("Loading Window");

		Stage primaryStage = new Stage();
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(Main.class.getResource("/fep/EnterAttendView.fxml"));
		AnchorPane mainLayout = loader.load();
		Scene scene = new Scene(mainLayout);
		primaryStage.setScene(scene);
		primaryStage.show();

		((Node) event.getSource()).getScene().getWindow().hide();
	}

	
	/*
	 * This method returns user back to the main menu while hiding/exiting the current window. 
	 * If computer is too slow, status will change to notify user window is being loaded as fast as possible. 
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
	 * This method populates the employee comboBoxes and changes all the respective labels
	 * once the selected week and year are found. 
	 */
	public void btnSearch(ActionEvent event) throws IOException {
		comboYear = year.getValue();
		textWeek = weekNumber.getValue();
		try {
			listEmployees = FXCollections.observableArrayList();

			// The following code populates the Employee comboBox with the
			// current employees of that selected schedule.
			String query8 = "select * from '" + comboYear.trim() + "Attendance" + textWeek.substring(0, 4)
					+ textWeek.substring(5) + "'";
			PreparedStatement pst8 = loginModel.connection.prepareStatement(query8);
			ResultSet rs8 = pst8.executeQuery();

			while (rs8.next()) {

				listEmployees.add(rs8.getString("Employee"));
			}
			rs8.close();
			pst8.close();
			employee.setItems(listEmployees);
			employee.setValue(listEmployees.get(0));

			// The following code updates the Week Start/End label values
			String query = "select * from '" + comboYear.trim() + "Attendance" + textWeek.substring(0, 4)
					+ textWeek.substring(5) + "' where rowid='1'";
			PreparedStatement pst = loginModel.connection.prepareStatement(query);
			ResultSet rs = pst.executeQuery();
			weekStart.setText(rs.getString("WeekStart"));
			weekEnd.setText(rs.getString("WeekEnd"));
			pst.close();
			rs.close();
			if (weekStart.getText().isEmpty() || weekEnd.getText().isEmpty()) {
				weekStart.setText("EMPTY");
				weekEnd.setText("EMPTY");
			}

			// The following code updates the Part/Full and Position labels
			String query9 = "select * from Employees where Name=?";
			PreparedStatement pst9 = loginModel.connection.prepareStatement(query9);
			pst9.setString(1, employee.getValue());
			ResultSet rs9 = pst9.executeQuery();

			while (rs9.next()) {
				/**
				 * Fills Position and Part/Full Labels with Employee's
				 * respective Position and Employee type
				 */
				partFull.setText(rs9.getString("PartFull"));
				position.setText(rs9.getString("Position"));

			}
			pst9.close();
			rs9.close();

			String query3 = "select * from '" + comboYear.trim() + "Attendance" + textWeek.substring(0, 4)
					+ textWeek.substring(5) + "' where Employee=?";
			try {
				PreparedStatement pst3 = loginModel.connection.prepareStatement(query3);
				pst3.setString(1, employee.getValue());
				ResultSet rs3 = pst3.executeQuery();

				activity.setText(rs3.getString("Activity"));

				pst3.close();
				rs3.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		} catch (Exception ex) {
			status.setText("Error occurred");
			ex.printStackTrace();
		}
	}

	/**
	 * 
	 * This method populates the Week combobox with the proper values once a new
	 * year is selected. The method exists because every year may have a
	 * different amount of weekly schedules created at any given time.
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
				counter++;
			}
			weekNumber.setItems(listWeek);
			weekNumber.setValue(firstWeek);
			pst2.close();
			rs2.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * This method retrieves the current day of the week and time and logs it into
	 * the database with the respective employee in the Clock On portion. 
	 * @param event
	 * @throws IOException
	 */
	public void btnClockOn(ActionEvent event) throws IOException {

		String name = employee.getValue();
		LocalDate now = LocalDate.now();
		String dayWeek = dayOfWeek(now.getDayOfWeek().getValue());
		String timeStamp = new SimpleDateFormat("hh:mm a").format(Calendar.getInstance().getTime());
		int hr = Integer.parseInt(timeStamp.substring(0, timeStamp.indexOf(":")));
		String amPM = timeStamp.substring(6);
		if (Integer.parseInt(timeStamp.substring(3, 5)) >= 30) {
			if (hr == 12) {
				hr = 1;
			} else if (hr == 11) {
				hr++;
				if (amPM.equals("AM")) {
					amPM = "PM";
				} else {
					amPM = "AM";
				}
			} else {
				hr++;
			}
		}

		String currentTime = "";

		String hour = "" + hr;
		if (hour.length() == 1) {
			hour = "0" + hour;
		}

		String currentAct = "";

		String query = "select * from '" + comboYear.trim() + "Attendance" + textWeek.substring(0, 4)
				+ textWeek.substring(5) + "' where Employee=?";
		try {
			PreparedStatement pst = loginModel.connection.prepareStatement(query);
			pst.setString(1, employee.getValue());
			ResultSet rs = pst.executeQuery();

			currentTime = rs.getString(dayWeek);
			currentAct = rs.getString("Activity");

			pst.close();
			rs.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		String updateText = hour + " " + amPM + " " + currentTime.substring(6);
		String act = currentAct + "Clocked On: " + dayWeek + " at " + hour + " " + amPM + "\n";

		String query2 = "Update '" + comboYear.trim() + "Attendance" + textWeek.substring(0, 4) + textWeek.substring(5)
				+ "' set " + "Employee='" + employee.getValue() + "'" + ", " + dayWeek + "='" + updateText + "'"
				+ ", WeekStart='" + weekStart.getText() + "'" + ", WeekEnd='" + weekEnd.getText() + "'" + ", Activity='"
				+ act + "'" + " where Employee='" + employee.getValue() + "'";
		try {
			PreparedStatement pst2 = loginModel.connection.prepareStatement(query2);
			pst2.execute();
			pst2.close();

			status.setText("Clocked ON!");
		} catch (Exception ex) {

			ex.printStackTrace();
		}

		String query3 = "select * from '" + comboYear.trim() + "Attendance" + textWeek.substring(0, 4)
				+ textWeek.substring(5) + "' where Employee=?";
		try {
			PreparedStatement pst3 = loginModel.connection.prepareStatement(query3);
			pst3.setString(1, employee.getValue());
			ResultSet rs3 = pst3.executeQuery();

			activity.setText(rs3.getString("Activity"));

			pst3.close();
			rs3.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		System.out.println(query2);
		System.out.println(updateText);
	}

	
	/**
	 * This method retrieves the current day of the week and time and logs it into
	 * the database with the respective employee in the Clock Off portion. 
	 * @param event
	 * @throws IOException
	 */
	public void btnClockOf(ActionEvent event) throws IOException {
		String name = employee.getValue();
		LocalDate now = LocalDate.now();
		String dayWeek = dayOfWeek(now.getDayOfWeek().getValue());
		String timeStamp = new SimpleDateFormat("hh:mm a").format(Calendar.getInstance().getTime());
		int hr = Integer.parseInt(timeStamp.substring(0, timeStamp.indexOf(":")));
		String amPM = timeStamp.substring(6);
		if (Integer.parseInt(timeStamp.substring(3, 5)) >= 30) {
			if (hr == 12) {
				hr = 1;
			} else if (hr == 11) {
				hr++;
				if (amPM.equals("AM")) {
					amPM = "PM";
				} else {
					amPM = "AM";
				}
			} else {
				hr++;
			}
		}

		String currentTime = "";

		String hour = "" + hr;
		if (hour.length() == 1) {
			hour = "0" + hour;
		}

		String currentAct = "";

		String query = "select * from '" + comboYear.trim() + "Attendance" + textWeek.substring(0, 4)
				+ textWeek.substring(5) + "' where Employee=?";
		try {
			PreparedStatement pst = loginModel.connection.prepareStatement(query);
			pst.setString(1, employee.getValue());
			ResultSet rs = pst.executeQuery();

			currentTime = rs.getString(dayWeek);
			currentAct = rs.getString("Activity");

			pst.close();
			rs.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		String updateText = currentTime.substring(0, 8) + hour + " " + amPM;
		String act = currentAct + "Clocked Off: " + dayWeek + " at " + hour + " " + amPM + "\n";

		String query2 = "Update '" + comboYear.trim() + "Attendance" + textWeek.substring(0, 4) + textWeek.substring(5)
				+ "' set " + "Employee='" + employee.getValue() + "'" + ", " + dayWeek + "='" + updateText + "'"
				+ ", WeekStart='" + weekStart.getText() + "'" + ", WeekEnd='" + weekEnd.getText() + "'" + ", Activity='"
				+ act + "'" + " where Employee='" + employee.getValue() + "'";
		try {
			PreparedStatement pst2 = loginModel.connection.prepareStatement(query2);
			pst2.execute();
			pst2.close();

			status.setText("Clocked ON!");
		} catch (Exception ex) {

			ex.printStackTrace();
		}

		String query3 = "select * from '" + comboYear.trim() + "Attendance" + textWeek.substring(0, 4)
				+ textWeek.substring(5) + "' where Employee=?";
		try {
			PreparedStatement pst3 = loginModel.connection.prepareStatement(query3);
			pst3.setString(1, employee.getValue());
			ResultSet rs3 = pst3.executeQuery();

			activity.setText(rs3.getString("Activity"));

			pst3.close();
			rs3.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		System.out.println(query2);
		System.out.println(updateText);
	}

	/**
	 * This method takes in the integer code returned by the LocalDate and 
	 * turns it into the day of the week. 
	 * @param code Integer returned by the LocalDate object. 
	 * @return String related to the current day of the week. 
	 */
	public String dayOfWeek(int code) {
		switch (code) {
		case 1:
			return "Monday";
		case 2:
			return "Tuesday";
		case 3:
			return "Wednesday";
		case 4:
			return "Thursday";
		case 5:
			return "Friday";
		case 6:
			return "Saturday";
		case 7:
			return "Sunday";
		default:
			return "Sunday";
		}
	}

	
	/**
	 * 
	 * @param event
	 * @throws IOException
	 * 
	 *             This method updates the employee's activity times along
	 *             with position and Part/Full labels once combo box is changed.
	 */
	public void comboEmployeeChange(ActionEvent event) throws IOException {
		try {// The following code updates the Part/Full and Position labels
			String query9 = "select * from Employees where Name=?";
			PreparedStatement pst9 = loginModel.connection.prepareStatement(query9);
			pst9.setString(1, employee.getValue());
			ResultSet rs9 = pst9.executeQuery();

			while (rs9.next()) {
				/**
				 * Fills Position and Part/Full Labels with Employee's
				 * respective Position and Employee type
				 */
				partFull.setText(rs9.getString("PartFull"));
				position.setText(rs9.getString("Position"));

			}
			pst9.close();
			rs9.close();

			String query3 = "select * from '" + comboYear.trim() + "Attendance" + textWeek.substring(0, 4)
					+ textWeek.substring(5) + "' where Employee=?";
			try {
				PreparedStatement pst3 = loginModel.connection.prepareStatement(query3);
				pst3.setString(1, employee.getValue());
				ResultSet rs3 = pst3.executeQuery();

				activity.setText(rs3.getString("Activity"));

				pst3.close();
				rs3.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

}
