package fep.control;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLDataException;
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

public class CreateManagerController implements Initializable {
	/**
	 * @author Aatif Jiwani
	 * @version 3.0
	 * 
	 * This is a controller class. This class handles all the actions of the Create Manager window. 
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
	public TextField ID;
	public TextField address;
	public TextField city;
	public TextField postal;
	public DatePicker birthdate;
	public DatePicker hiredate;

	public Label status;
	public Label position;
	public Label partFull;

	ObservableList<String> listStates = FXCollections.observableArrayList("AL", "AK", "AZ", "AR", "CA ", "CO", "CT",
			"DE", "DC", "FL", "GA", "HI", "ID", "IN", "IA", "KS", "KY", "LA", "ME", "MD", "MA", "MI", "MN", "MS", "MO",
			"MT", "NE", "NV", "NH", "NJ", "NM", "NY", "NC", "ND", "OH", "OK", "OR", "PA", "RI", "SC", "SD", "TN", "TX",
			"UT", "VT", "VA", "WA", "WV", "WI", "WY");

	//Populates State Combobox with states. 
	public void initialize(URL location, ResourceBundle resources) {
		state.setItems(listStates);
	}

	//Directs users back to the Main Menu while also hiding/exiting current window. 
	public void btnMenu(ActionEvent event) throws IOException {
		status.setText("Loading Window");

		Stage primaryStage = new Stage();
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(Main.class.getResource("/fep/MainVieww.fxml"));
		AnchorPane mainLayout = loader.load();
		Scene scene = new Scene(mainLayout);
		primaryStage.setScene(scene);
		primaryStage.show();
		((Node) event.getSource()).getScene().getWindow().hide();
	}

	//This method creates a manager which also acts as one of the admins of this program
	public void btnCreate(ActionEvent event) throws IOException {
		try {
			
			//If State comboBox fields and other textFields are not blank...
			if (!state.getValue().equals("--") || !position.getText().equals("Select Position")
					|| !partFull.getText().equals("Select Type")) {

				//Query that sets up adding employee into database
				String query = "insert into Employees (Name,ID,Position,PartFull,Address,City,State,Postal,Birthdate,HireDate) values"
						+ " (?,?,?,?,?,?,?,?,?,?)";
				PreparedStatement pst = loginModel.connection.prepareStatement(query);

				//Inserts values into query
				pst.setString(1, name.getText());
				pst.setString(2, ID.getText());
				pst.setString(3, position.getText());
				pst.setString(4, partFull.getText());
				pst.setString(5, address.getText());
				pst.setString(6, city.getText());
				pst.setString(7, state.getValue());
				pst.setString(8, postal.getText());
				pst.setString(9, dateAssemble(birthdate));
				pst.setString(10, dateAssemble(hiredate));

				pst.execute();
				pst.close();

				System.out.println("Console: Employee added");

				
				//If for some reason there is a predefined schedule, the following code will 
				//add this employee to these schedules. 
				String query2 = "select * from Years";
				PreparedStatement pst2 = loginModel.connection.prepareStatement(query2);
				ResultSet rs = pst2.executeQuery();

				while (rs.next()) {
					String query6 = "select * from '" + rs.getString("Year") + "Weeks'";
					PreparedStatement pst6 = loginModel.connection.prepareStatement(query6);
					ResultSet rs6 = pst6.executeQuery();
					while (rs6.next()) {
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

				status.setText("Employee Added!");

			} else {
				status.setText("Error Occurred");
				throw new SQLDataException();
			}

			//Deletes default admin user (Admin user was used to check whether program was being opened for the first time
			//or not. 
			String query2 = "Delete from Employees where ID=?";
			PreparedStatement pst2 = loginModel.connection.prepareStatement(query2);
			pst2.setString(1, "admin");

			pst2.execute();
			pst2.close();
			
			//Loads Main Menu window

			status.setText("Loading window");
			Stage primaryStage = new Stage();
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("/fep/MenuView.fxml"));
			AnchorPane mainLayout = loader.load();
			Scene scene = new Scene(mainLayout);
			primaryStage.setScene(scene);
			primaryStage.show();
			((Node) event.getSource()).getScene().getWindow().hide();	
			
			//If error occurs. 
		} catch (Exception ex) {

			System.out.println("Console: Error occurred while adding employee");
			ex.printStackTrace();
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
