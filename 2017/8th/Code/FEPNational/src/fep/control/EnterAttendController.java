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
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class EnterAttendController implements Initializable {

	/**
	 * @author Aatif Jiwani
	 * @version 3.0
	 * 
	 *          This is a controller class. This class handles all the actions
	 *          of the manual Enter employee attendance window.
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

	public int counter = 0;
	public String comboYear;
	public String textWeek;

	public ComboBox<String> year;
	public ComboBox<String> employee;
	public ComboBox<String> sunFrom;
	public ComboBox<String> monFrom;
	public ComboBox<String> tueFrom;
	public ComboBox<String> wedFrom;
	public ComboBox<String> thuFrom;
	public ComboBox<String> friFrom;
	public ComboBox<String> satFrom;

	public ComboBox<String> sunTo;
	public ComboBox<String> monTo;
	public ComboBox<String> tueTo;
	public ComboBox<String> wedTo;
	public ComboBox<String> thuTo;
	public ComboBox<String> friTo;
	public ComboBox<String> satTo;

	public ComboBox<String> sunAM;
	public ComboBox<String> monAM;
	public ComboBox<String> tueAM;
	public ComboBox<String> wedAM;
	public ComboBox<String> thuAM;
	public ComboBox<String> friAM;
	public ComboBox<String> satAM;

	public ComboBox<String> sunPM;
	public ComboBox<String> monPM;
	public ComboBox<String> tuePM;
	public ComboBox<String> wedPM;
	public ComboBox<String> thuPM;
	public ComboBox<String> friPM;
	public ComboBox<String> satPM;
	public ComboBox<String> weekNumber;

	public Label weekStart;
	public Label weekEnd;
	public Label position;
	public Label partFull;
	public Label status;

	public final String ACT = "Manually Clocked ON/OFF\n";
	public String currAct = "";

	ObservableList<String> listTime = FXCollections.observableArrayList("OF", "12", "1", "2", "3", "4", "5", "6", "7",
			"8", "9", "10", "11");
	ObservableList<String> listAMPM = FXCollections.observableArrayList("OF", "AM", "PM");
	ObservableList<String> listYear = FXCollections.observableArrayList();
	ObservableList<String> listWeek = FXCollections.observableArrayList();
	ObservableList<String> listEmployees = null;
	ObservableList<ComboBox<String>> listStart = null;
	ObservableList<ComboBox<String>> listEnd = null;

	public void initialize(URL location, ResourceBundle resources) {
		listStart = FXCollections.observableArrayList(sunFrom, sunTo, monFrom, monTo, tueFrom, tueTo, wedFrom, wedTo,
				thuFrom, thuTo, friFrom, friTo, satFrom, satTo);

		listEnd = FXCollections.observableArrayList(sunAM, sunPM, monAM, monPM, tueAM, tuePM, wedAM, wedPM, thuAM,
				thuPM, friAM, friPM, satAM, satPM);

		/*
		 * The following code initalizes all timing comboBoxes with the proper
		 * values
		 */
		for (int i = 0; i < listStart.size(); i++) {
			listStart.get(i).setItems(listTime);
			listEnd.get(i).setItems(listAMPM);
		}

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

	/*
	 * This method retrieves the Clock ON/OFF values of every employee of the
	 * selected weekly schedule. It only displays the Clock ON/OFF value of the
	 * default employee in the comboBox.
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

			String query2 = "select * from '" + comboYear.trim() + "Attendance" + textWeek.substring(0, 4)
					+ textWeek.substring(5) + "'" + " where employee=?";
			PreparedStatement pst2 = loginModel.connection.prepareStatement(query2);
			pst2.setString(1, employee.getValue());
			ResultSet rs2 = pst2.executeQuery();

			/**
			 * If employee exists...
			 */
			while (rs2.next()) {
				/**
				 * The following code fills comboBoxes with proper schedule
				 * times of the selected employee
				 * 
				 * @see method queryReturn
				 */
				currAct = rs2.getString("Activity");

				String[] r = queryReturn(rs2.getString("Sunday"));
				sunFrom.setValue(r[0]);
				sunAM.setValue(r[1]);
				sunTo.setValue(r[2]);
				sunPM.setValue(r[3]);

				r = queryReturn(rs2.getString("Monday"));
				monFrom.setValue(r[0]);
				monAM.setValue(r[1]);
				monTo.setValue(r[2]);
				monPM.setValue(r[3]);

				r = queryReturn(rs2.getString("Tuesday"));
				tueFrom.setValue(r[0]);
				tueAM.setValue(r[1]);
				tueTo.setValue(r[2]);
				tuePM.setValue(r[3]);

				r = queryReturn(rs2.getString("Wednesday"));
				wedFrom.setValue(r[0]);
				wedAM.setValue(r[1]);
				wedTo.setValue(r[2]);
				wedPM.setValue(r[3]);

				r = queryReturn(rs2.getString("Thursday"));
				thuFrom.setValue(r[0]);
				thuAM.setValue(r[1]);
				thuTo.setValue(r[2]);
				thuPM.setValue(r[3]);

				r = queryReturn(rs2.getString("Friday"));
				friFrom.setValue(r[0]);
				friAM.setValue(r[1]);
				friTo.setValue(r[2]);
				friPM.setValue(r[3]);

				r = queryReturn(rs2.getString("Saturday"));
				satFrom.setValue(r[0]);
				satAM.setValue(r[1]);
				satTo.setValue(r[2]);
				satPM.setValue(r[3]);

			}
			status.setText("Attendance Found!");
		} catch (Exception e) {
			status.setText("Error occurred!");
			e.printStackTrace();
		}
	}

	/*
	 * This method directs the user back to the Main Menu while hiding/exiting
	 * the current window.
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
	 * @see method resetCombos
	 */
	public void btnReset(ActionEvent event) throws IOException {
		resetCombos();
		employee.setValue(employee.getItems().get(0));
	}

	/*
	 * This method updates the employee attendance with the selected ON/OFF
	 * times.
	 */
	public void btnUpdate(ActionEvent event) throws IOException {
		try {
			String query = "Update '" + comboYear.trim() + "Attendance" + textWeek.substring(0, 4)
					+ textWeek.substring(5) + "' set " + "Employee='" + employee.getValue() + "'" + ", Sunday='"
					+ sunFrom.getValue() + " " + sunAM.getValue() + "" + " - " + sunTo.getValue() + " "
					+ sunPM.getValue() + "'" + ", Monday='" + monFrom.getValue() + " " + monAM.getValue() + "" + " - "
					+ monTo.getValue() + " " + monPM.getValue() + "'" + ", Tuesday='" + tueFrom.getValue() + " "
					+ tueAM.getValue() + "" + " - " + tueTo.getValue() + " " + tuePM.getValue() + "'" + ", Wednesday='"
					+ wedFrom.getValue() + " " + wedAM.getValue() + "" + " - " + wedTo.getValue() + " "
					+ wedPM.getValue() + "'" + ", Thursday='" + thuFrom.getValue() + " " + thuAM.getValue() + "" + " - "
					+ thuTo.getValue() + " " + thuPM.getValue() + "'" + ", Friday='" + friFrom.getValue() + " "
					+ friAM.getValue() + "" + " - " + friTo.getValue() + " " + friPM.getValue() + "'" + ", Saturday='"
					+ satFrom.getValue() + " " + satAM.getValue() + "" + " - " + satTo.getValue() + " "
					+ satPM.getValue() + "'" + ", WeekStart='" + weekStart.getText() + "'" + ", WeekEnd='"
					+ weekEnd.getText() + "'" + ", Activity='" + currAct + ACT + "'" + " where Employee='"
					+ employee.getValue() + "'";
			PreparedStatement pst = loginModel.connection.prepareStatement(query);

			pst.execute();
			pst.close();

			status.setText("Attendance updated!");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param String
	 *            s String of Clock ON/OFF times from database.
	 * @return String array with proper combo Box values
	 * 
	 *         This method returns proper comboBox values so the program can set
	 *         each comboBox with the proper values that correspond to the
	 *         selected employee.
	 * 
	 */
	public String[] queryReturn(String s) {
		String[] value = new String[4];
		int first = 0;
		int spaceIndex = s.indexOf(" ");
		String q = s.substring(first, spaceIndex);
		first = spaceIndex;
		value[0] = q;
		spaceIndex = s.indexOf(" ", spaceIndex + 1);
		q = s.substring(first + 1, spaceIndex);
		first = spaceIndex;
		value[1] = q;
		spaceIndex = s.indexOf(" ", spaceIndex + 1);
		q = s.substring(first + 1, spaceIndex);
		first = spaceIndex;
		spaceIndex = s.indexOf(" ", spaceIndex + 1);
		q = s.substring(first + 1, spaceIndex);
		first = spaceIndex;
		value[2] = q;
		spaceIndex = s.indexOf(" ", spaceIndex + 1);
		q = s.substring(first + 1);
		first = spaceIndex;
		value[3] = q;

		System.out.println("Console: Query returned");
		return value;
	}

	/*
	 * This method resets all comboBoxes in case the user makes a mistake.
	 */
	public void resetCombos() {
		for (int i = 0; i < listStart.size(); i++) {
			listStart.get(i).setValue("OF");
			listEnd.get(i).setValue("OF");
		}
	}

	/**
	 * 
	 * This method populates the Week combobox with the proper values once a new
	 * year is selected. The method exists because every year may have a
	 * different amount of weekly schedules created at any given time.
	 */
	public void comboYearchange(ActionEvent event) throws IOException {
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
	 * 
	 * @param event
	 * @throws IOException
	 * 
	 *             This method updates the employee's Clock ON/OFF times along
	 *             with position and Part/Full labels once combo box is changed.
	 */
	public void comboEmployeeChange(ActionEvent event) throws IOException {
		comboYear = year.getValue();
		textWeek = weekNumber.getValue();
		try {
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

			String query2 = "select * from '" + comboYear.trim() + "Attendance" + textWeek.substring(0, 4)
					+ textWeek.substring(5) + "'" + " where employee=?";
			PreparedStatement pst2 = loginModel.connection.prepareStatement(query2);
			pst2.setString(1, employee.getValue());
			ResultSet rs2 = pst2.executeQuery();

			/**
			 * If employee exists...
			 */
			while (rs2.next()) {

				String[] r = queryReturn(rs2.getString("Sunday"));
				sunFrom.setValue(r[0]);
				sunAM.setValue(r[1]);
				sunTo.setValue(r[2]);
				sunPM.setValue(r[3]);

				r = queryReturn(rs2.getString("Monday"));
				monFrom.setValue(r[0]);
				monAM.setValue(r[1]);
				monTo.setValue(r[2]);
				monPM.setValue(r[3]);

				r = queryReturn(rs2.getString("Tuesday"));
				tueFrom.setValue(r[0]);
				tueAM.setValue(r[1]);
				tueTo.setValue(r[2]);
				tuePM.setValue(r[3]);

				r = queryReturn(rs2.getString("Wednesday"));
				wedFrom.setValue(r[0]);
				wedAM.setValue(r[1]);
				wedTo.setValue(r[2]);
				wedPM.setValue(r[3]);

				r = queryReturn(rs2.getString("Thursday"));
				thuFrom.setValue(r[0]);
				thuAM.setValue(r[1]);
				thuTo.setValue(r[2]);
				thuPM.setValue(r[3]);

				r = queryReturn(rs2.getString("Friday"));
				friFrom.setValue(r[0]);
				friAM.setValue(r[1]);
				friTo.setValue(r[2]);
				friPM.setValue(r[3]);

				r = queryReturn(rs2.getString("Saturday"));
				satFrom.setValue(r[0]);
				satAM.setValue(r[1]);
				satTo.setValue(r[2]);
				satPM.setValue(r[3]);

			}
			status.setText("Schedule Found!");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void btnAuto(ActionEvent event) throws IOException {
		status.setText("Loading window");
		Stage primaryStage = new Stage();
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(Main.class.getResource("/fep/AutoEnterAttendView.fxml"));
		AnchorPane mainLayout = loader.load();
		Scene scene = new Scene(mainLayout);
		primaryStage.setScene(scene);
		primaryStage.show();
		((Node) event.getSource()).getScene().getWindow().hide();
	}

}
