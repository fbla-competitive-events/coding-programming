package fep.control;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import fep.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class MenuController implements Initializable {

	/**
	 * @author Aatif Jiwani
	 * @version 3.0
	 * 
	 * This is a controller class. This class handles all the actions of the Main Menu window. 
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

	@FXML
	private Label userLbl;

	@Override
	/**
	 * This method checks whether connection has been established. 
	 */
	public void initialize(URL location, ResourceBundle resources) {
		if (loginModel.isConnect()) {
			System.out.println("Connected");
		}

	}

	/**
	 * 
	 * @param event
	 * @throws IOException
	 * 
	 * Closes program
	 */
	public void btnExit(ActionEvent event) throws IOException{
		System.exit(0);
		
	}

	/**
	 * 
	 * @param event
	 * @throws IOException
	 * 
	 * Opens the Admin Login window with a parameter that will lead to the Add Employee window. 
	 */
	public void btnAddEmployee(ActionEvent event) throws IOException {
		((Node) event.getSource()).getScene().getWindow().hide();

		Stage primaryStage = new Stage();
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(Main.class.getResource("/fep/AdminLoginView.fxml"));
		AnchorPane mainLayout = loader.load();
		AdminLoginController adminControl = (AdminLoginController) loader.getController();
		adminControl.GetWindow(3);
		Scene scene = new Scene(mainLayout);
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	/**
	 * 
	 * @param event
	 * @throws IOException
	 * 
	 * Opens the Admin Login window with a parameter that will lead to the Edit Employee window. 
	 */
	public void btnEditEmployee(ActionEvent event) throws IOException {
		((Node) event.getSource()).getScene().getWindow().hide();

		Stage primaryStage = new Stage();
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(Main.class.getResource("/fep/AdminLoginView.fxml"));
		AnchorPane mainLayout = loader.load();
		AdminLoginController adminControl = (AdminLoginController) loader.getController();
		adminControl.GetWindow(4);
		Scene scene = new Scene(mainLayout);
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	/**
	 * 
	 * @param event
	 * @throws IOException
	 * 
	 * Opens the Admin Login window with a parameter that will lead to the Create Schedule window. 
	 */
	public void btnCreateSchedule(ActionEvent event) throws IOException {
		((Node) event.getSource()).getScene().getWindow().hide();

		Stage primaryStage = new Stage();
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(Main.class.getResource("/fep/AdminLoginView.fxml"));
		AnchorPane mainLayout = loader.load();
		AdminLoginController adminControl = (AdminLoginController) loader.getController();
		adminControl.GetWindow(6);
		Scene scene = new Scene(mainLayout);
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	/**
	 * 
	 * @param event
	 * @throws IOException
	 * 
	 * Opens the Admin Login window with a parameter that will lead to the Edit Schedule window. 
	 */
	public void btnUpdateSchedule(ActionEvent event) throws IOException {
		((Node) event.getSource()).getScene().getWindow().hide();

		Stage primaryStage = new Stage();
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(Main.class.getResource("/fep/AdminLoginView.fxml"));
		AnchorPane mainLayout = loader.load();
		AdminLoginController adminControl = (AdminLoginController) loader.getController();
		adminControl.GetWindow(7);
		Scene scene = new Scene(mainLayout);
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	/**
	 * 
	 * @param event
	 * @throws IOException
	 * 
	 * Opens the Admin Login window with a parameter that will lead to the View Employees window. 
	 */
	public void btnViewEmployee(ActionEvent event) throws IOException {
		((Node) event.getSource()).getScene().getWindow().hide();

		Stage primaryStage = new Stage();
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(Main.class.getResource("/fep/AdminLoginView.fxml"));
		AnchorPane mainLayout = loader.load();
		AdminLoginController adminControl = (AdminLoginController) loader.getController();
		adminControl.GetWindow(2);
		Scene scene = new Scene(mainLayout);
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	/**
	 * 
	 * @param event
	 * @throws IOException
	 * 
	 * Opens the Enter attendance window. 
	 */
	public void btnEnterAttend(ActionEvent event) throws IOException {
		((Node) event.getSource()).getScene().getWindow().hide();

		Stage primaryStage = new Stage();
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(Main.class.getResource("/fep/AutoEnterAttendView.fxml"));
		AnchorPane mainLayout = loader.load();
		Scene scene = new Scene(mainLayout);
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
	/**
	 * 
	 * @param event
	 * @throws IOException
	 * 
	 * Opens the View schedule window. 
	 */
	public void btnViewSchedule(ActionEvent event) throws IOException{
		((Node) event.getSource()).getScene().getWindow().hide();

		Stage primaryStage = new Stage();
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(Main.class.getResource("/fep/ScheduleView.fxml"));
		AnchorPane mainLayout = loader.load();
		Scene scene = new Scene(mainLayout);
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
	/**
	 * 
	 * @param event
	 * @throws IOException
	 * 
	 * Opens the Admin Login window with a parameter that will lead to the View Employee Attendance window. 
	 */
	public void btnViewAttendance(ActionEvent event) throws IOException{
		((Node) event.getSource()).getScene().getWindow().hide();

		Stage primaryStage = new Stage();
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(Main.class.getResource("/fep/AdminLoginView.fxml"));
		AnchorPane mainLayout = loader.load();
		AdminLoginController adminControl = (AdminLoginController) loader.getController();
		adminControl.GetWindow(5);
		Scene scene = new Scene(mainLayout);
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
	/**
	 * 
	 * @param event
	 * @throws IOException
	 * 
	 * Opens the Enter Customer Attendance window
	 */
	public void btnEditCustomer(ActionEvent event) throws IOException {
		((Node) event.getSource()).getScene().getWindow().hide();

		Stage primaryStage = new Stage();
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(Main.class.getResource("/fep/Customer.fxml"));
		AnchorPane mainLayout = loader.load();
		Scene scene = new Scene(mainLayout);
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
	/**
	 * 
	 * @param event
	 * @throws IOException
	 * 
	 * Opens the View Customer Attendance window
	 */
	public void btnViewCustomer(ActionEvent event) throws IOException{
		((Node) event.getSource()).getScene().getWindow().hide();

		Stage primaryStage = new Stage();
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(Main.class.getResource("/fep/CustomerView.fxml"));
		AnchorPane mainLayout = loader.load();
		Scene scene = new Scene(mainLayout);
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
	/**
	 * 
	 * @param event
	 * @throws IOException
	 * 
	 * Opens the Admin Login window with a parameter that will lead to the Settings window. 
	 */
	public void btnSettings(ActionEvent event) throws IOException{
		((Node) event.getSource()).getScene().getWindow().hide();

		Stage primaryStage = new Stage();
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(Main.class.getResource("/fep/AdminLoginView.fxml"));
		AnchorPane mainLayout = loader.load();
		AdminLoginController adminControl = (AdminLoginController) loader.getController();
		adminControl.GetWindow(1);
		Scene scene = new Scene(mainLayout);
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
	public void btnViewMember(ActionEvent event) throws IOException{
		((Node) event.getSource()).getScene().getWindow().hide();

		Stage primaryStage = new Stage();
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(Main.class.getResource("/fep/ViewMemberView.fxml"));
		AnchorPane mainLayout = loader.load();
		Scene scene = new Scene(mainLayout);
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
	public void btnEditMember(ActionEvent event) throws IOException{
		((Node) event.getSource()).getScene().getWindow().hide();

		Stage primaryStage = new Stage();
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(Main.class.getResource("/fep/EditMemberView.fxml"));
		AnchorPane mainLayout = loader.load();
		Scene scene = new Scene(mainLayout);
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	public void btnAddMember(ActionEvent event) throws IOException{
		((Node) event.getSource()).getScene().getWindow().hide();

		Stage primaryStage = new Stage();
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(Main.class.getResource("/fep/AddMemberView.fxml"));
		AnchorPane mainLayout = loader.load();
		Scene scene = new Scene(mainLayout);
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
	/**
	 * 
	 * @param event
	 * @throws IOException
	 * 
	 * This method will open the User Manual file that is in the program directory. 
	 * If the file has been for some reason deleted, the program will open the default browser
	 * with the link that leads to the User Manual online. 
	 * 
	 * @throws SQLException 
	 * @throws URISyntaxException 
	 */
	public void btnInstruction(ActionEvent event) throws IOException, SQLException, URISyntaxException{
		try {
			File file = new File("FEP_Instruction.pdf");
			Desktop.getDesktop().open(file);
		} catch (Exception ex) {
			Desktop d = Desktop.getDesktop();
			String url = "";
			String query = "select * from Links where rowid=1";
			PreparedStatement pst = loginModel.connection.prepareStatement(query);
			ResultSet rs = pst.executeQuery();
			while (rs.next()) {
				url = rs.getString("Link");
			}
			d.browse(new URI(url));
		}
	}
	
	/**
	 * 
	 * @param event
	 * @throws IOException
	 * @throws SQLException
	 * @throws URISyntaxException
	 * 
	 * This method will open the Documentation file that is in the program directory. 
	 * If the file has been for some reason deleted, the program will open the default browser
	 * with the link that leads to the Documentation online. 
	 */
	public void btnDocument(ActionEvent event) throws IOException, SQLException, URISyntaxException{
		try {
			File file = new File("FEP_Document.pdf");
			Desktop.getDesktop().open(file);
		} catch (Exception ex) {
			Desktop d = Desktop.getDesktop();
			String url = "";
			String query = "select * from Links where rowid=1";
			PreparedStatement pst = loginModel.connection.prepareStatement(query);
			ResultSet rs = pst.executeQuery();
			while (rs.next()) {
				url = rs.getString("Link");
			}
			d.browse(new URI(url));
		}
	
	}

}
