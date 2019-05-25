package fep.control;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;

import fep.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class AdminLoginController implements Initializable {
	/**
	 * @author Aatif Jiwani
	 * @version 3.0
	 * 
	 * This is a controller class. This class handles all the actions of the Admin Login window. 
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
	
	public int windowDirect = 0;
	
	public Label status;
	
	public TextField manager;
	public TextField username;
	
	public PasswordField password;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		
	}
	
	//Finds out what window to load once proper admin credentials are entered
	public void GetWindow(int window){
		windowDirect = window;
	}
	
	/*
	 * This method directs the user back to the Main Menu window and hides/exists the current window. 
	 */
	public void btnMenu(ActionEvent event) throws IOException{
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
	 * The following methods leads the user to the desired window once proper admin
	 * credentials are entered.
	 */
	public void btnLogin(ActionEvent event) throws IOException{
		
		//Retrieves inputed credentials
		String ID = manager.getText().trim();
		String adminUser = username.getText().trim();
		String adminPass = password.getText().trim();
		try {
			//Query that retrieves the employee position that correlates to inputed ID number
			String query = "select * from employees where ID=?";
			PreparedStatement pst = loginModel.connection.prepareStatement(query);
			pst.setString(1, ID);
			ResultSet rs = pst.executeQuery();
			
			String position = rs.getString("Position");
			pst.close();
			rs.close();
			
			//If the ID number matches that of a manager...
			if (position.trim().equals("Manager")){
				//The following query retrieves the current admin username and password
				//currently in the database. 
				String query2 = "select * from Administrator where rowid='1'";
				String queryUser = "";
				String queryPass = "";
				PreparedStatement pst2 = loginModel.connection.prepareStatement(query2);
				ResultSet rs2 = pst2.executeQuery();
				while (rs2.next()){
					queryUser = rs2.getString("user");
					queryPass = rs2.getString("pass");
				}
				pst2.close();
				rs2.close();
				
				//If the inputed credentials match the database credentials...
				if (queryUser.equals(adminUser) && queryPass.equals(adminPass)){
					/*The switch statements use the original windowDirect integer initialized by the constructor
					*and leads the user to the proper window. 
					*
					*Each switch statement creates the desired window and hides/exits the current window. 
					*/
					switch(windowDirect){
					case 1:
						//Opens settings
						status.setText("Loading Window");
						Stage primaryStage = new Stage();
						FXMLLoader loader = new FXMLLoader();
						loader.setLocation(Main.class.getResource("/fep/SettingsView.fxml"));
						AnchorPane mainLayout = loader.load();
						Scene scene = new Scene(mainLayout);
						primaryStage.setScene(scene);
						primaryStage.show();
						((Node) event.getSource()).getScene().getWindow().hide();
						break;
					case 2:
						
						//Opens View Employee window
						status.setText("Loading Window");
						Stage primaryStage2 = new Stage();
						FXMLLoader loader2 = new FXMLLoader();
						loader2.setLocation(Main.class.getResource("/fep/EmployeeView.fxml"));
						AnchorPane mainLayout2 = loader2.load();
						Scene scene2 = new Scene(mainLayout2);
						primaryStage2.setScene(scene2);
						primaryStage2.show();
						((Node) event.getSource()).getScene().getWindow().hide();
						break;
					case 3:
						//Opens Add Employee window
						status.setText("Loading Window");
						Stage primaryStage3 = new Stage();
						FXMLLoader loader3 = new FXMLLoader();
						loader3.setLocation(Main.class.getResource("/fep/AddEmployeeView.fxml"));
						AnchorPane mainLayout3 = loader3.load();
						Scene scene3 = new Scene(mainLayout3);
						primaryStage3.setScene(scene3);
						primaryStage3.show();
						((Node) event.getSource()).getScene().getWindow().hide();
						break;
					case 4:
						//Open Edit Employee window
						status.setText("Loading Window");
						Stage primaryStage4 = new Stage();
						FXMLLoader loader4 = new FXMLLoader();
						loader4.setLocation(Main.class.getResource("/fep/UpdateEmployeeView.fxml"));
						AnchorPane mainLayout4 = loader4.load();
						Scene scene4 = new Scene(mainLayout4);
						primaryStage4.setScene(scene4);
						primaryStage4.show();
						((Node) event.getSource()).getScene().getWindow().hide();
						break;
					case 5:
						//Open Employee Attendance window
						status.setText("Loading Window");
						Stage primaryStage5 = new Stage();
						FXMLLoader loader5 = new FXMLLoader();
						loader5.setLocation(Main.class.getResource("/fep/AttendView.fxml"));
						AnchorPane mainLayout5 = loader5.load();
						Scene scene5 = new Scene(mainLayout5);
						primaryStage5.setScene(scene5);
						primaryStage5.show();
						((Node) event.getSource()).getScene().getWindow().hide();
						break;
					case 6:
						//Open Create Schedule window
						status.setText("Loading Window");
						Stage primaryStage6 = new Stage();
						FXMLLoader loader6 = new FXMLLoader();
						loader6.setLocation(Main.class.getResource("/fep/CreateScheduleView.fxml"));
						AnchorPane mainLayout6 = loader6.load();
						Scene scene6 = new Scene(mainLayout6);
						primaryStage6.setScene(scene6);
						primaryStage6.show();
						((Node) event.getSource()).getScene().getWindow().hide();
						break;
					case 7:
						//Open Edit Schedule window.
						status.setText("Loading Window");
						Stage primaryStage7 = new Stage();
						FXMLLoader loader7 = new FXMLLoader();
						loader7.setLocation(Main.class.getResource("/fep/UpdateScheduleView.fxml"));
						AnchorPane mainLayout7 = loader7.load();
						Scene scene7 = new Scene(mainLayout7);
						primaryStage7.setScene(scene7);
						primaryStage7.show();
						((Node) event.getSource()).getScene().getWindow().hide();
						break;
					} 
				//If credentials are not correct...
				} else {
					status.setText("User Password incorrect!");
					manager.setText("");
					username.setText("");
					password.setText("");
				}
			}
			
			//If an error occurs...
		} catch (Exception e){
			status.setText("Error occurred!");
			e.printStackTrace();
		}
	}
	
		


}
