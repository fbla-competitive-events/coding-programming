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

public class UpdateScheduleController implements Initializable {
	/**
	 * @author Aatif Jiwani
	 * @version 3.0
	 * 
	 * This is a controller class. This class handles all the actions of the Edit Schedule window. 
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

	public DatePicker weekStart;
	public DatePicker weekEnd;
	public Label position;
	public Label partFull;
	public Label status;

	ObservableList<String> listTime = FXCollections.observableArrayList("OF", "12", "1", "2", "3", "4", "5", "6", "7",
			"8", "9", "10", "11");
	ObservableList<String> listAMPM = FXCollections.observableArrayList("OF", "AM", "PM");
	ObservableList<String> listYear = FXCollections.observableArrayList();
	ObservableList<String> listWeek = FXCollections.observableArrayList();
	ObservableList<String> listEmployees = null;
	ObservableList<ComboBox<String>> listStart = null;
	ObservableList<ComboBox<String>> listEnd = null;

	/*
	 * (non-Javadoc)
	 * @see javafx.fxml.Initializable#initialize(java.net.URL, java.util.ResourceBundle)
	 * 
	 * This method intializes all comboBoxes with their proper values
	 */
	public void initialize(URL location, ResourceBundle resources) {
		listStart = FXCollections.observableArrayList(sunFrom, sunTo, monFrom, monTo, tueFrom,
				tueTo, wedFrom, wedTo, thuFrom, thuTo, friFrom, friTo, satFrom, satTo); 
		
		listEnd = FXCollections.observableArrayList(sunAM, sunPM, monAM, monPM, tueAM, tuePM, wedAM, wedPM, thuAM, thuPM, friAM, 
				friPM, satAM, satPM);
		
		//The following lines of code update the timing comboboxes with their proper values. 
		for (int i = 0; i < listStart.size(); i++){
			listStart.get(i).setItems(listTime);
			listEnd.get(i).setItems(listAMPM);
		}
		
		//The following code updates the Year and Week comboBoxes with the selected values. 
		try {
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
	 * 
	 * @param event
	 * @throws IOException
	 * 
	 * This method finds the selected schedule and displays the scheduled times
	 * of the default employee in the combobox.  
	 */
	public void btnSearch(ActionEvent event) throws IOException {
		comboYear = year.getValue();
		textWeek = weekNumber.getValue();
		try {
			listEmployees = FXCollections.observableArrayList();

			String query8 = "select * from '" + comboYear.trim() + textWeek.substring(0, 4) + textWeek.substring(5)
					+ "'";
			PreparedStatement pst8 = loginModel.connection.prepareStatement(query8);
			ResultSet rs8 = pst8.executeQuery();

			while (rs8.next()) {

				listEmployees.add(rs8.getString("Employee"));
			}
			rs8.close();
			pst8.close();
			employee.setItems(listEmployees);
			employee.setValue(listEmployees.get(0));

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

			String query2 = "select * from '" + comboYear.trim() + textWeek.substring(0, 4) + textWeek.substring(5)
					+ "'" + " where employee=?";
			PreparedStatement pst2 = loginModel.connection.prepareStatement(query2);
			pst2.setString(1, employee.getValue());
			ResultSet rs2 = pst2.executeQuery();

			/**
			 * If employee exists...
			 */
			while (rs2.next()) {
				/**
				 * The following lines of code fill comboBoxes with proper schedule times
				 * of the selected employee
				 * 
				 * @see method queryReturn
				 */
				weekEnd.setValue(dateReturn(rs2.getString("WeekEnd")));
				weekStart.setValue(dateReturn(rs2.getString("WeekStart")));

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
			status.setText("Error occurred!");
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param event
	 * @throws IOException
	 * 
	 * This method directs the user back to the Main Menu while also hiding/exiting the current window.
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
	 * @param event
	 * @throws IOException
	 * @see method resetCombos
	 */
	public void btnReset(ActionEvent event) throws IOException {
		resetCombos();
		employee.setValue(employee.getItems().get(0));
		status.setText("Values reset.");
	}

	/**
	 * 
	 * @param event
	 * @throws IOException
	 * 
	 * This method updates the schedule with the new values for the selected employee.
	 */
	public void btnUpdate(ActionEvent event) throws IOException {
		try {
			/**
			 * Defines query to update given information to the
			 * employee's schedule
			 */
			String query = "Update '" + comboYear.trim() + textWeek.substring(0, 4) + textWeek.substring(5) + "' set "
					+ "Employee='" + employee.getValue() + "'" + ", Sunday='" + sunFrom.getValue() + " "
					+ sunAM.getValue() + "" + " - " + sunTo.getValue() + " " + sunPM.getValue() + "'" + ", Monday='"
					+ monFrom.getValue() + " " + monAM.getValue() + "" + " - " + monTo.getValue() + " "
					+ monPM.getValue() + "'" + ", Tuesday='" + tueFrom.getValue() + " " + tueAM.getValue() + "" + " - "
					+ tueTo.getValue() + " " + tuePM.getValue() + "'" + ", Wednesday='" + wedFrom.getValue() + " "
					+ wedAM.getValue() + "" + " - " + wedTo.getValue() + " " + wedPM.getValue() + "'" + ", Thursday='"
					+ thuFrom.getValue() + " " + thuAM.getValue() + "" + " - " + thuTo.getValue() + " "
					+ thuPM.getValue() + "'" + ", Friday='" + friFrom.getValue() + " " + friAM.getValue() + "" + " - "
					+ friTo.getValue() + " " + friPM.getValue() + "'" + ", Saturday='" + satFrom.getValue() + " "
					+ satAM.getValue() + "" + " - " + satTo.getValue() + " " + satPM.getValue() + "'" + ", WeekStart='"
					+ dateAssemble(weekStart) + "'" + ", WeekEnd='" + dateAssemble(weekEnd) + "'" + " where Employee='"
					+ employee.getValue() + "'";
			PreparedStatement pst = loginModel.connection.prepareStatement(query);

			pst.execute();
			pst.close();

			/**
			 * If the Week End and Week Start times for the
			 * schedule have been updated, the algorithm below defines a query
			 * to update it in the respective Attendance table.
			 */
			String query2 = "Update '" + comboYear.trim() + "Attendance" + textWeek.substring(0, 4)
					+ textWeek.substring(5) + "' set " + "Employee='" + employee.getValue() + "'" + ", WeekStart='"
					+ dateAssemble(weekStart) + "'" + ", WeekEnd='" + dateAssemble(weekEnd) + "'" + " where Employee='"
					+ employee.getValue() + "'";
			PreparedStatement pst2 = loginModel.connection.prepareStatement(query2);

			pst2.execute();
			status.setText("Employee Schedule Updated!");
		} catch (Exception e) {
			status.setText("Error occurred!");
			e.printStackTrace();

		}
	}

	/**
	 * @param String s
	 * 		String of scheduling times from database. 
	 * @return String array with proper combo Box values
	 * 
	 * This method returns proper comboBox values so the program can set 
	 * each comboBox with the proper values that correspond to the selected employee. 
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

	/**
	 * This method resets all comboBoxes
	 */
	public void resetCombos() {
		for (int i = 0; i < listStart.size(); i++){
			listStart.get(i).setValue("OF");
			listEnd.get(i).setValue("OF");
		}
	}

	/**
	 * 
	 * @param event
	 * @throws IOException
	 * 
	 * This method populates the Week combobox with the proper values once a new year is selected. 
	 * The method exists because every year may have a different amount of weekly schedules created at
	 * any given time. 
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
	 * This method updates the employee's scheduled times along with position and Part/Full labels
	 * once combo box is changed. 
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

			String query2 = "select * from '" + comboYear.trim() + textWeek.substring(0, 4) + textWeek.substring(5)
					+ "'" + " where employee=?";
			PreparedStatement pst2 = loginModel.connection.prepareStatement(query2);
			pst2.setString(1, employee.getValue());
			ResultSet rs2 = pst2.executeQuery();

			/**
			 * If employee exists...
			 */
			while (rs2.next()) {
				weekEnd.setValue(dateReturn(rs2.getString("WeekEnd")));
				weekStart.setValue(dateReturn(rs2.getString("WeekStart")));

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
