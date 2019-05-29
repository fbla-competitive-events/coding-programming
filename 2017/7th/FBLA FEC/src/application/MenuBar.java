package application;

//import statements
import java.io.IOException;

import com.jfoenix.controls.JFXButton;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.ContentDisplay;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

//Class that has the navigation tool bar at the top which every screen uses
public class MenuBar {
	//UI Features, each button navigates to a different screen in the program
	@FXML
	protected JFXButton buttonDetails;
	@FXML
	protected JFXButton buttonSchedule;
	@FXML
	protected JFXButton buttonCustomer;
	@FXML
	protected JFXButton buttonReports;
	@FXML
	protected JFXButton buttonAbout;
	@FXML
	protected JFXButton buttonExit;
	
	public void initToolbar(Pane root, HBox hbMenu) {
		
		//The following code sets the graphic and label for each of the buttons in the navigation screen
		buttonDetails.setGraphic(new ImageView("application/ic_perm_identity_white_48pt.png"));
        buttonDetails.setText("Employee Details");
        buttonDetails.setTextFill(Color.WHITE);
        buttonDetails.setContentDisplay(ContentDisplay.TOP);
        buttonDetails.setAlignment(Pos.BOTTOM_CENTER);
        
      //The following code sets the graphic and label for each of the buttons in the navigation screen
        buttonSchedule.setGraphic(new ImageView("application/ic_date_range_white_48pt.png"));
        buttonSchedule.setText("Employee Schedule");
        buttonSchedule.setTextFill(Color.WHITE);
        buttonSchedule.setContentDisplay(ContentDisplay.TOP);
        buttonSchedule.setAlignment(Pos.BOTTOM_CENTER);
        
      //The following code sets the graphic and label for each of the buttons in the navigation screen
        buttonCustomer.setGraphic(new ImageView("application/ic_group_white_2x.png"));
        buttonCustomer.setText("Customer Attendance");
        buttonCustomer.setTextFill(Color.WHITE);
        buttonCustomer.setContentDisplay(ContentDisplay.TOP);
        buttonCustomer.setAlignment(Pos.BOTTOM_CENTER);
        
      //The following code sets the graphic and label for each of the buttons in the navigation screen
        buttonReports.setGraphic(new ImageView("application/ic_insert_chart_white_2x.png"));
        buttonReports.setText("Reports");
        buttonReports.setTextFill(Color.WHITE);
        buttonReports.setContentDisplay(ContentDisplay.TOP);
        buttonReports.setAlignment(Pos.BOTTOM_CENTER);
        
      //The following code sets the graphic and label for each of the buttons in the navigation screen
        buttonAbout.setGraphic(new ImageView("application/ic_info_outline_white_48pt.png"));
        buttonAbout.setText("About");
        buttonAbout.setTextFill(Color.WHITE);
        buttonAbout.setContentDisplay(ContentDisplay.TOP);
        buttonAbout.setAlignment(Pos.BOTTOM_CENTER);
        
      //The following code sets the graphic and label for each of the buttons in the navigation screen
        buttonExit.setGraphic(new ImageView("application/ic_clear_white_48pt.png"));
        buttonExit.setText("Exit");
        buttonExit.setTextFill(Color.WHITE);
        buttonExit.setContentDisplay(ContentDisplay.TOP);
        buttonExit.setAlignment(Pos.BOTTOM_CENTER);
      
        //The following code is for which screen to load if each button is clicked
        for(Node node: hbMenu.getChildren()){
    		if(node.getAccessibleText()!=null){
    			node.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> {
    				switch(node.getAccessibleText()){
    				case "Employee Details":
    					BorderPane pane;
    					try {
    						pane = FXMLLoader.load(getClass().getResource("Main_Menu_Employee.fxml"));
    						root.getChildren().setAll(pane);
    					} catch (IOException ex) {
    						ex.printStackTrace();
    					}
    					break;
    				case "Schedule Employee":
    					AnchorPane pane1;
    					try {
    						pane1 = FXMLLoader.load(getClass().getResource("Employee_Scheduler.fxml"));
    						root.getChildren().setAll(pane1);
    					} catch (IOException ex) {
    						ex.printStackTrace();
    					}
    					break;
    				case "Customer Attendance":
    					AnchorPane pane2;
    					try {
    						pane2 = FXMLLoader.load(getClass().getResource("Menu_Customer_Attendance.fxml"));
    						root.getChildren().setAll(pane2);
    					} catch (IOException ex) {
    						ex.printStackTrace();
    					}
    					break;
    				case "Reports":
    					BorderPane pane3;
    					try {
    						pane3 = FXMLLoader.load(getClass().getResource("ReportScreen.fxml"));
    						root.getChildren().setAll(pane3);
    					} catch (IOException ex) {
    						ex.printStackTrace();
    					}
    					break;
    				case "About":
    					BorderPane pane4;
    					try {
    						pane4 = FXMLLoader.load(getClass().getResource("About.fxml"));
    						root.getChildren().setAll(pane4);
    					} catch (IOException ex) {
    						ex.printStackTrace();
    					}
    					break;
    				
    				case "Exit":
    					System.exit(0);
    				}
    			});
    		}
    	}
	}
}
