package fep.control;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;

import javax.swing.JOptionPane;

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

public class CreateScheduleController implements Initializable {
	/**
	 * @author Aatif Jiwani
	 * @version 3.0
	 * 
	 * This is a controller class. This class handles all the actions of the Create Schedule window. 
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

	public TextField weekNumber;
	public DatePicker weekStart;
	public DatePicker weekEnd;
	public Label position;
	public Label partFull;
	public Label status;

	ObservableList<String> listTime = FXCollections.observableArrayList("OF", "12", "1", "2", "3", "4", "5", "6", "7",
			"8", "9", "10", "11");
	ObservableList<String> listAMPM = FXCollections.observableArrayList("OF", "AM", "PM");
	ObservableList<String> listYear = FXCollections.observableArrayList();
	ObservableList<String> listEmployees = null;
	ObservableList<ComboBox<String>> listStart = null;
	ObservableList<ComboBox<String>> listEnd = null;
	
	/*
	 * (non-Javadoc)
	 * @see javafx.fxml.Initializable#initialize(java.net.URL, java.util.ResourceBundle)
	 * 
	 * This method fills all the comboBoxes with the proper values as well as fill the Year comboBox
	 * with the proper values. 
	 */
	public void initialize(URL location, ResourceBundle resources) {
		
		listStart = FXCollections.observableArrayList(sunFrom, sunTo, monFrom, monTo, tueFrom,
				tueTo, wedFrom, wedTo, thuFrom, thuTo, friFrom, friTo, satFrom, satTo); 
		
		listEnd = FXCollections.observableArrayList(sunAM, sunPM, monAM, monPM, tueAM, tuePM, wedAM, wedPM, thuAM, thuPM, friAM, 
				friPM, satAM, satPM);
		
		for (int i = 0; i < listStart.size(); i++){
			listStart.get(i).setItems(listTime);
			listEnd.get(i).setItems(listAMPM);
		}
		
		//This try/catch block fills the Year comboBox with the proper values. 
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
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/*
	 * This method directs the user back to the Main Menu. 
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

	/*
	 * This method updates the created tables with the proper employee and their respective schedule
	 * 
	 * @see method btnCreate
	 */
	public void btnAdd(ActionEvent event) throws IOException {
		//If week start/end dates are empty...
		if (weekEnd.getValue() == null ||  weekStart.getValue() == null) {
			status.setText("Please enter Week Start/End dates");
			
		//else...
		} else {
			try {
				
				//The following code retrieves the ID number of the given employee
				//IDs are needed for the schedule
				String name = employee.getValue();
				String id = "";
				String query = "select * from Employees where name=?";
				PreparedStatement pst = loginModel.connection.prepareStatement(query);
				pst.setString(1, employee.getValue());
				ResultSet rs = pst.executeQuery();

				while (rs.next()) {
					id = rs.getString("ID");
				}
				pst.close();
				rs.close();

				/*
				 * The following code inserts the given times selected from the ComboBoxes for the 
				 * selected employee into the database. 
				 */
				String query1 = "Update '" + comboYear.trim() + "Week" + textWeek.trim()
						+ "' set " + "Employee='" + employee.getValue() + "'" + ", Sunday='" + sunFrom.getValue() + " "
						+ sunAM.getValue() + "" + " - " + sunTo.getValue() + " " + sunPM.getValue() + "'" + ", Monday='"
						+ monFrom.getValue() + " " + monAM.getValue() + "" + " - " + monTo.getValue() + " "
						+ monPM.getValue() + "'" + ", Tuesday='" + tueFrom.getValue() + " " + tueAM.getValue() + ""
						+ " - " + tueTo.getValue() + " " + tuePM.getValue() + "'" + ", Wednesday='" + wedFrom.getValue()
						+ " " + wedAM.getValue() + "" + " - " + wedTo.getValue() + " " + wedPM.getValue() + "'"
						+ ", Thursday='" + thuFrom.getValue() + " " + thuAM.getValue() + "" + " - " + thuTo.getValue()
						+ " " + thuPM.getValue() + "'" + ", Friday='" + friFrom.getValue() + " " + friAM.getValue() + ""
						+ " - " + friTo.getValue() + " " + friPM.getValue() + "'" + ", Saturday='" + satFrom.getValue()
						+ " " + satAM.getValue() + "" + " - " + satTo.getValue() + " " + satPM.getValue() + "'"
						+ ", WeekStart='" + dateAssemble(weekStart) + "'" + ", WeekEnd='" + dateAssemble(weekEnd) + "'"
						+ " where Employee='" + employee.getValue() + "'";
				PreparedStatement pst1 = loginModel.connection.prepareStatement(query1);

				pst1.execute();
				pst1.close();

				/**
				 * The following code updates the attendance schedules for the selected employee. 
				 * It sets the default times to OF OF - OF OF because the employee has not yet clocked in
				 * 
				 * Once the employee comes in for their designated shift, they will go into the attendance window
				 * and log in their time. 
				 */
				String query2 = "Update '" + comboYear.trim() + "AttendanceWeek" + textWeek.trim() + 
						"' set " + "Employee='" + employee.getValue() + "'" + ", WeekStart='"
						+ dateAssemble(weekStart) + "'" + ", WeekEnd='" + dateAssemble(weekEnd) + "'" + " where Employee='"
						+ employee.getValue() + "'";
				PreparedStatement pst2 = loginModel.connection.prepareStatement(query2);

				pst2.execute();

				
				//The following code removes the selected employee from the comboBox
				//to prevent from duplicate entries. 
				employee.getItems().remove(name);
				if (employee.getItems().size() > 0){
					String update = employee.getItems().get(0);
					employee.setValue(update);
				} else {
					employee.getItems().add("");
					employee.setValue("");
				}
				status.setText("Employee Schedule Added!");

				/* 
				 * Resets comboBoxes
				 * @see method resetCombos
				 */
				resetCombos();
			} catch (Exception e) {
				status.setText("Error occurred");
				e.printStackTrace();
			}
		}
	}

	//This method resets all comboBoxes incase the user makes a mistake
	//@see method resetCombos. 
	public void btnReset(ActionEvent event) throws IOException {
		resetCombos();
		status.setText("Values reset.");

	}

	/*
	 * This method creates 3 separate tables for employee schedules, employee attendance, and customer attendance. 
	 * Also, lets the SQLdatabase know that this selected week has been added and no duplicates should be created. 
	 */
	public void btnCreate(ActionEvent event) throws IOException {
		comboYear = year.getValue().trim();
		textWeek = weekNumber.getText();

		boolean check = true;
		try {

			int txtWeek = Integer.parseInt(textWeek);
			//Checks if the inputed week is valid. (0 - 52)
			if (txtWeek <= 52 && txtWeek >= 1) {
				String query = "select * from '" + comboYear + "Weeks'";
				PreparedStatement pst = loginModel.connection.prepareStatement(query);
				ResultSet rs = pst.executeQuery();

				//Checks whether the inputed week already exists. If it does, variable check will evaluate to false. 
				//Otherwise, it will remain true. 
				if (textWeek.equals("")) {
					check = false;
				} else {
					while (rs.next()) {
						String value = "" + comboYear + "Week" + textWeek;
						String compare = rs.getString("WeekNum");
						if (value.equals(compare)) {
							check = false;
							break;
						}
					}
				}
				
				pst.close();
				rs.close();
				//If week does not exist..
				if (check) {
					//The following quote creates and executes queries to create 3 separate tables and inserts
					//the inputed week number into the database letting it know this week is being added. 
					resetCombos();
					
						//Creates Employee Schedule. 
					String query2 = "CREATE TABLE '" + comboYear + "Week" + textWeek + "' "
							+ "(Employee text, ID text, Sunday text, Monday text, Tuesday text, Wednesday text,"
							+ "Thursday text, Friday text, Saturday text, WeekStart text, WeekEnd text)";
					PreparedStatement pst2 = loginModel.connection.prepareStatement(query2);
					pst2.execute();
					pst2.close();

						//Creates Employee attendance. 
					String query5 = "CREATE TABLE '" + comboYear + "AttendanceWeek" + textWeek + "' "
							+ "(Employee text, ID text, Sunday text, Monday text, Tuesday text, Wednesday text,"
							+ "Thursday text, Friday text, Saturday text, WeekStart text, WeekEnd text, Activity text)";
					PreparedStatement pst5 = loginModel.connection.prepareStatement(query5);
					pst5.execute();
					pst5.close();

						//Inserts week value into database. 
					String query3 = "insert into '" + comboYear + "Weeks' (WeekNum) values (?)";
					PreparedStatement pst3 = loginModel.connection.prepareStatement(query3);
					pst3.setString(1, "" + comboYear + "Week" + textWeek);
					pst3.execute();
					pst3.close();

						//Creates customer attendance. 
					String query6 = "CREATE TABLE '" + comboYear + "CustomerWeek" + textWeek + "' "
							+ "(TimeOfDay text, Sunday text, Monday text, Tuesday text, Wednesday text,"
							+ "Thursday text, Friday text, Saturday text)";
					PreparedStatement pst6 = loginModel.connection.prepareStatement(query6);
					pst6.execute();
					pst6.close();
					
					/*
					 * UNCONVENTIONAL METHOD:
					 * Lines 342-378
					 * 
					 * There are two for loops that add values to the same table. The reason is that the time values need to be
					 * in order. If there was only one loop, then the times would be all jumbled up. 
					 * 
					 * Please view the View Customer Attendance window for clarification. 
					 */

						//Fills customer attendance with default values. 
					for (int i = 1; i < listTime.size(); i++) {
						String query7 = "insert into '" + comboYear + "CustomerWeek" + textWeek + "'"
								+ " (TimeOfDay, Sunday, Monday, Tuesday, Wednesday,"
								+ "Thursday, Friday, Saturday) values (?,?,?,?,?,?,?,?)";
						PreparedStatement pst7 = loginModel.connection.prepareStatement(query7);
						pst7.setString(1, listTime.get(i) + " AM");
						pst7.setString(2, "0");
						pst7.setString(3, "0");
						pst7.setString(4, "0");
						pst7.setString(5, "0");
						pst7.setString(6, "0");
						pst7.setString(7, "0");
						pst7.setString(8, "0");
						pst7.execute();
						pst7.close();

					}

						//Fills customer attendance with default values. 
					for (int i = 1; i < listTime.size(); i++) {
						String query7 = "insert into '" + comboYear + "CustomerWeek" + textWeek + "'"
								+ " (TimeOfDay, Sunday, Monday, Tuesday, Wednesday,"
								+ "Thursday, Friday, Saturday) values (?,?,?,?,?,?,?,?)";
						PreparedStatement pst7 = loginModel.connection.prepareStatement(query7);
						pst7.setString(1, listTime.get(i) + " PM");
						pst7.setString(2, "0");
						pst7.setString(3, "0");
						pst7.setString(4, "0");
						pst7.setString(5, "0");
						pst7.setString(6, "0");
						pst7.setString(7, "0");
						pst7.setString(8, "0");
						pst7.execute();
						pst7.close();

					}

					String query8 = "select * from Employees";
					PreparedStatement pst8 = loginModel.connection.prepareStatement(query8);
					ResultSet rs8 = pst8.executeQuery();

					/*The following code adds every employee available on the database to the 
					 * created tables. 
					 * 
					 * This is meant to be a failsafe. In case the program crashes immediately after the user
					 * creates the schedule, they can simply go to the "Edit Schedule" window and edit the desired employees. 
					 * 
					 * In version 1.0 of the program, this failsafe was NOT in place and since the "Edit Schedule" requires employees
					 * to be added, when the program crashed, it would cause an error making the program useless. 
					 * 
					 * Although this code is quite complicated, it is very simple. It retrieves all the employees from the database
					 * and fills them into the tables with default values. 
					 * 
					 * @see method btnAdd for updating employee schedules when new values are added. 
					 */
					listEmployees = FXCollections.observableArrayList();
					while (rs8.next()) {
						String temp = rs8.getString("Name");
						String id = rs8.getString("ID");
						String query11 = "insert into '" + comboYear + "Week" + textWeek + "'"
								+ " (Employee, ID, Sunday, Monday, Tuesday, Wednesday, Thursday, Friday, "
								+ "Saturday, WeekStart, WeekEnd) values (?,?,?,?,?,?,?,?,?,?,?)";
						PreparedStatement pst11 = loginModel.connection.prepareStatement(query11);
						pst11.setString(1, temp);
						pst11.setString(2, id);
						pst11.setString(3, sunFrom.getValue() + " " + sunAM.getValue() + "" + " - " + sunTo.getValue()
								+ " " + sunPM.getValue());
						pst11.setString(4, monFrom.getValue() + " " + monAM.getValue() + "" + " - " + monTo.getValue()
								+ " " + monPM.getValue());
						pst11.setString(5, tueFrom.getValue() + " " + tueAM.getValue() + "" + " - " + tueTo.getValue()
								+ " " + tuePM.getValue());
						pst11.setString(6, wedFrom.getValue() + " " + wedAM.getValue() + "" + " - " + wedTo.getValue()
								+ " " + wedPM.getValue());
						pst11.setString(7, thuFrom.getValue() + " " + thuAM.getValue() + "" + " - " + thuTo.getValue()
								+ " " + thuPM.getValue());
						pst11.setString(8, friFrom.getValue() + " " + friAM.getValue() + "" + " - " + friTo.getValue()
								+ " " + friPM.getValue());
						pst11.setString(9, satFrom.getValue() + " " + satAM.getValue() + "" + " - " + satTo.getValue()
								+ " " + satPM.getValue());
						pst11.setString(10, "");
						pst11.setString(11, "");
						pst11.execute();
						pst11.close();

						/**
						 * Defines query to add Employee name
						 * and ID to respective Attendance Table Sets default
						 * Clock On/Off times of OF OF - OF OF
						 */
						String query12 = "insert into '" + comboYear + "AttendanceWeek" + textWeek + "'"
								+ " (Employee, ID, Sunday, Monday, Tuesday, Wednesday, Thursday, Friday, "
								+ "Saturday, WeekStart, WeekEnd, Activity) values (?,?,?,?,?,?,?,?,?,?,?,?)";
						PreparedStatement pst12 = loginModel.connection.prepareStatement(query12);
						pst12.setString(1, temp);
						pst12.setString(2, id);
						pst12.setString(3, "OF OF - OF OF");
						pst12.setString(4, "OF OF - OF OF");
						pst12.setString(5, "OF OF - OF OF");
						pst12.setString(6, "OF OF - OF OF");
						pst12.setString(7, "OF OF - OF OF");
						pst12.setString(8, "OF OF - OF OF");
						pst12.setString(9, "OF OF - OF OF");
						pst12.setString(10, "");
						pst12.setString(11, "");
						pst12.setString(12, "");
						pst12.execute();
						pst12.close();
						listEmployees.add(temp);
					}
					rs8.close();
					pst8.close();
					employee.setItems(listEmployees);
					employee.setValue(listEmployees.get(0));

					//Query that finds current selected comboBox. 
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

					status.setText("Employee Schedule Added!");

					//if week already exists
				} else {
					status.setText("Week Already Added!");
					throw new Exception();
				}
				//If week is not valid. 
			} else {
				status.setText("Invalid Week #");
				throw new Exception();
			}
			//If error occurs. 
		} catch (Exception e) {
			status.setText("Error occurred!");
			e.printStackTrace();
		}
	}

	//This method resets all combo Box values in case the user makes a mistake. 
	public void resetCombos() {
		for (int i = 0; i < listStart.size(); i++){
			listStart.get(i).setValue("OF");
			listEnd.get(i).setValue("OF");
		}
	}

	/*
	 * This method changes the Position and Part/Full label values to that that 
	 * corresponds to the selected employee when the comboBox value is changed. 
	 */
	public void comboEmployee(ActionEvent event) throws IOException {
		resetCombos();
		try {
			String query9 = "select * from Employees where Name=?";
			PreparedStatement pst9 = loginModel.connection.prepareStatement(query9);
			pst9.setString(1, employee.getValue());
			ResultSet rs9 = pst9.executeQuery();

			while (rs9.next()) {

				partFull.setText(rs9.getString("PartFull"));
				position.setText(rs9.getString("Position"));

			}
			pst9.close();
			rs9.close();
		//If error occurs...
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

}
