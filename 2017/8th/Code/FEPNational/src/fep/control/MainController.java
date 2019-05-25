package fep.control;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import fep.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class MainController {
	/**
	 * @author Aatif Jiwani
	 * @version 3.0
	 * 
	 * This is a controller class. This class handles all the actions of the main screen window. 
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
	
	@FXML
	/**
	 * 
	 * @param event
	 * @throws IOException
	 * 
	 * This method checks if the program is being opened for the first time or not. 
	 * If it is being opened for the first time, it will lead the user to the Create Manager window. 
	 * Otherwise, it will open the main menu. 
	 *
	 * To check whether program is being opened for the first time, the database has a default employee with name: "admin" 
	 * and ID: "admin". If this user exists, it will open the "Create Manager" window which will then delete this user. 
	 * 
	 */
	public void btnContinue(ActionEvent event) throws IOException {
		try {
			String query = "select * from Employees";
			PreparedStatement pst = loginModel.connection.prepareStatement(query);
			ResultSet rs = pst.executeQuery();
			String test = rs.getString("Name");
			String testID = rs.getString("ID");
			pst.close();
			rs.close();

			//If user "admin" exists...
			if (test.equals("admin") && testID.equals("admin")) {
				status.setText("Loading window");

				Stage primaryStage2 = new Stage();
				FXMLLoader loader2 = new FXMLLoader();
				loader2.setLocation(Main.class.getResource("/fep/CreateManagerView.fxml"));
				AnchorPane mainLayout2 = loader2.load();
				Scene scene2 = new Scene(mainLayout2);
				primaryStage2.setScene(scene2);
				primaryStage2.show();
				((Node) event.getSource()).getScene().getWindow().hide();
			//else...
			} else {
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
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 
	 * @param event
	 * @throws IOException
	 * 
	 * This method checks if the program is being opened for the first time or not. 
	 * If it is being opened for the first time, it will lead the user to the Create Manager window. 
	 * Otherwise, it will open the Admin login window. 
	 */
	public void btnSettings(ActionEvent event) throws IOException {
		try {
			String query = "select * from Employees";
			PreparedStatement pst = loginModel.connection.prepareStatement(query);
			ResultSet rs = pst.executeQuery();
			String test = rs.getString("Name");
			String testID = rs.getString("ID");
			pst.close();
			rs.close();

			if (test.equals("admin") && testID.equals("admin")) {
				status.setText("Loading window");

				Stage primaryStage = new Stage();
				FXMLLoader loader = new FXMLLoader();
				loader.setLocation(Main.class.getResource("/fep/CreateManagerView.fxml"));
				AnchorPane mainLayout = loader.load();
				Scene scene = new Scene(mainLayout);
				primaryStage.setScene(scene);
				primaryStage.show();
				((Node) event.getSource()).getScene().getWindow().hide();

			} else {
				status.setText("Loading window");

				Stage primaryStage = new Stage();
				FXMLLoader loader = new FXMLLoader();
				loader.setLocation(Main.class.getResource("/fep/AdminLoginView.fxml"));
				AnchorPane mainLayout = loader.load();
				AdminLoginController adminControl = (AdminLoginController) loader.getController();
				adminControl.GetWindow(1);
				Scene scene = new Scene(mainLayout);
				primaryStage.setScene(scene);
				primaryStage.show();
				((Node) event.getSource()).getScene().getWindow().hide();

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * This method opens the User manual.
	 * @param event
	 * @throws IOException
	 * @throws SQLException
	 * @throws URISyntaxException
	 */
	public void btnInstruction(ActionEvent event) throws IOException, SQLException, URISyntaxException {

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
	 * This method opens the program's documentation
	 * @param event
	 * @throws IOException
	 * @throws URISyntaxException
	 * @throws SQLException
	 */
	public void btnDocument(ActionEvent event) throws IOException, URISyntaxException, SQLException {
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
