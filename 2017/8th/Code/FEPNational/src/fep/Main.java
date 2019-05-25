package fep;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class Main extends Application {
	/**
	 * @author Aatif Jiwani
	 * @version 3.0
	 */

	/**
	 * !IMPORTANT!
	 * All Try and Catches are meant to notify the user of their mistake if their input
	 * is not proper. 
	 */
	
	
	/*Pre-defined global variables
	 * Each of these variables are used in at least one method in this class. 
	 */
	private Stage primaryStage;
	private AnchorPane mainLayout;
	
	@Override
	/*
	 * (non-Javadoc)
	 * @see javafx.application.Application#start(javafx.stage.Stage)
	 * Initializes window
	 */
	public void start(Stage primaryStage) throws IOException {
		this.primaryStage = primaryStage;
		this.primaryStage.setTitle("Fire Entertainment Plaza Database");
		showMainView();
	}

	/*
	 * This method links the FXML design file to the current window. 
	 * This way, every component will have it's proper styling characteristics. 
	 */
	private void showMainView() throws IOException{
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(Main.class.getResource("MainVieww.fxml"));
		mainLayout = loader.load();
		Scene scene = new Scene(mainLayout);
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	/*
	 * Main method
	 * Creates window
	 */
	public static void main(String[] args) {
		launch(args);
	}
	
	
}
