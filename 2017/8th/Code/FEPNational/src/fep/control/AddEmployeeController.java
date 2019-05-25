package fep.control;



import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLDataException;
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

public class AddEmployeeController implements Initializable {
	
	/**
	 * @author Aatif Jiwani
	 * @version 3.0
	 * 
	 * This is a controller class. This class handles all the actions of the Add Employee window. 
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
	public TextField ID;
	public TextField address;
	public TextField city;
	public TextField postal;
	public DatePicker birthdate;
	public DatePicker hiredate;

	public Label status;

	public AnchorPane pane;

	ObservableList<String> listPosition = FXCollections.observableArrayList("Manager", "Assistant Manager",
			"Floor Desk Receptionist", "Floor Supervisor", "Equipment Supervisor", "Janitor", "Security Guard");

	ObservableList<String> listPartFull = FXCollections.observableArrayList("Part Time", "Full Time");

	ObservableList<String> listStates = FXCollections.observableArrayList("AL", "AK", "AZ", "AR", "CA ", "CO", "CT",
			"DE", "DC", "FL", "GA", "HI", "ID", "IN", "IA", "KS", "KY", "LA", "ME", "MD", "MA", "MI", "MN", "MS", "MO",
			"MT", "NE", "NV", "NH", "NJ", "NM", "NY", "NC", "ND", "OH", "OK", "OR", "PA", "RI", "SC", "SD", "TN", "TX",
			"UT", "VT", "VA", "WA", "WV", "WI", "WY");

	/*
	 * (non-Javadoc)
	 * @see javafx.fxml.Initializable#initialize(java.net.URL, java.util.ResourceBundle)
	 * Initializes comboBoxes and checks whether program is connected to SQLite database. 
	 */
	public void initialize(URL location, ResourceBundle resources) {
		System.out.println("Connected");

		position.setItems(listPosition);
		partFull.setItems(listPartFull);
		state.setItems(listStates);

	}

	/*
	 * This method adds the employee to the SQLite database
	 */
	public void btnAdd(ActionEvent event) {
		try {
			//Checks whether inputed ID already exists
			String queryTest = "SELECT COUNT(*) AS rowcount FROM Employees WHERE ID='" + ID.getText() + "'";
			PreparedStatement pstTest = loginModel.connection.prepareStatement(queryTest);
			ResultSet rsTest = pstTest.executeQuery();
			int count = rsTest.getInt("rowcount");
			
			//If ID exists, status is changed
			if (count >= 1) {
				status.setText("ID already used!");
			} else {
				//If ID does not exist, code proceeds to add employee into database
				if (!state.getValue().equals("--") || !position.getValue().equals("Select Position")
						|| !partFull.getValue().equals("Select Type")) {

					//Query that inserts values from combo boxes and text fields into database. 
					String query = "insert into Employees (Name,ID,Position,PartFull,Address,City,State,Postal,Birthdate,HireDate) values"
							+ " (?,?,?,?,?,?,?,?,?,?)";
					PreparedStatement pst = loginModel.connection.prepareStatement(query);
					
					//Inserts values into query
					pst.setString(1, name.getText());
					pst.setString(2, ID.getText());
					pst.setString(3, position.getValue());
					pst.setString(4, partFull.getValue());
					pst.setString(5, address.getText());
					pst.setString(6, city.getText());
					pst.setString(7, state.getValue());
					pst.setString(8, postal.getText());
					pst.setString(9, dateAssemble(birthdate));
					pst.setString(10, dateAssemble(hiredate));

					pst.execute();
					pst.close();

					System.out.println("Employee added");

					//The following code makes sure that this new employee is added to previous schedules 
					//in the case that the employer needs to immediately schedule him/her
					
					String query2 = "select * from Years";
					PreparedStatement pst2 = loginModel.connection.prepareStatement(query2);
					ResultSet rs = pst2.executeQuery();

					//If schedules exist...
					while (rs.next()) {
						//The lines of code in this while loop select every schedule currently on the database
						//and adds the employee credentials to them. 
						
						String query6 = "select * from '" + rs.getString("Year") + "Weeks'";
						PreparedStatement pst6 = loginModel.connection.prepareStatement(query6);
						ResultSet rs6 = pst6.executeQuery();
						
						//If individual schedules exist within each fiscal year...
						while (rs6.next()) {
							//The following code sets default values for the new employee in every 
							//schedule on the database. 
							String query3 = "insert into '" + rs6.getString("WeekNum")
									+ "' (Employee, ID, Sunday, Monday, Tuesday, Wednesday, Thursday, Friday, "
									+ "Saturday, WeekStart, WeekEnd) values (?,?,?,?,?,?,?,?,?,?,?)";
							PreparedStatement pst3 = loginModel.connection.prepareStatement(query3);
							pst3.setString(1, name.getText());
							pst3.setString(2, ID.getText());
							pst3.setString(3, "OF OF - OF OF");
							pst3.setString(4, "OF OF - OF OF");
							pst3.setString(5, "OF OF - OF OF");
							pst3.setString(6, "OF OF - OF OF");
							pst3.setString(7, "OF OF - OF OF");
							pst3.setString(8, "OF OF - OF OF");
							pst3.setString(9, "OF OF - OF OF");
							pst3.setString(10, "");
							pst3.setString(11, "");
							pst3.execute();
							pst3.close();

							String query4 = "insert into '" + rs6.getString("WeekNum").substring(0, 4) + "Attendance"
									+ rs6.getString("WeekNum").substring(4)
									+ "' (Employee, ID, Sunday, Monday, Tuesday, Wednesday, Thursday, Friday, "
									+ "Saturday, WeekStart, WeekEnd) values (?,?,?,?,?,?,?,?,?,?,?)";
							PreparedStatement pst4 = loginModel.connection.prepareStatement(query4);
							pst4.setString(1, name.getText());
							pst4.setString(2, ID.getText());
							pst4.setString(3, "OF OF - OF OF");
							pst4.setString(4, "OF OF - OF OF");
							pst4.setString(5, "OF OF - OF OF");
							pst4.setString(6, "OF OF - OF OF");
							pst4.setString(7, "OF OF - OF OF");
							pst4.setString(8, "OF OF - OF OF");
							pst4.setString(9, "OF OF - OF OF");
							pst4.setString(10, "OF OF - OF OF");
							pst4.setString(11, "OF OF - OF OF");
							pst4.execute();
							pst4.close();
						}
						pst6.close();
						rs6.close();

					}
					pst2.close();
					rs.close();
					System.out.println("Console: Weekly Schedule and Attendance updated");

					/**
					 * Resets text fields and combo boxes
					 */
					name.setText("");
					ID.setText("");
					address.setText("");
					city.setText("");
					postal.setText("");
					birthdate.setValue(LocalDate.now());
					hiredate.setValue(LocalDate.now());
					position.setValue("Manager");

					status.setText("Employee Added!");

				} else {
					status.setText("Error Occurred");
					throw new SQLDataException();
				}

				pstTest.close();
				rsTest.close();
			}
			//If any exception occurs...
		} catch (Exception ex) {
			status.setText("Error Occurred");
			System.out.println("Console: Error occurred while adding employee");
			ex.printStackTrace();
		}
	}

	/*
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

}
