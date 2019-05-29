package application;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXDrawer;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class ReportScreenController extends MenuBar implements Initializable {    
    @FXML
	private TreeTableView<String> treeTableMenu;
	@FXML
	private TreeTableColumn<String, String> treeTableMenuColumn;
	@FXML
	private JFXDrawer topDrawer;
	@FXML
	private HBox hbMenu;
	
	@FXML
	private Button btBar;
	
	@FXML
	private Button btLine;
	
	@FXML
	private BorderPane root;
	
	public static BorderPane rootP;
	
	
    @Override
    public void initialize(URL location, ResourceBundle resources) {
    	initToolbar(root, hbMenu);
    }	
    
    @FXML
	private void loadBar(Event event) throws IOException{
    	FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("AMPM_Bar_Chart.fxml"));
        loader.load();
        Parent p = loader.getRoot();
        Stage stage = new Stage();
        stage.setScene(new Scene(p));
        stage.setTitle("All Customer Attendance Data");
        stage.show();
	}
    
    @FXML
   	private void loadLine(Event event) throws IOException{
    	FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("Customer_Attendance_Line_Chart.fxml"));
        loader.load();
        Parent p = loader.getRoot();
        Stage stage = new Stage();
        stage.setScene(new Scene(p));
        stage.setTitle("Week Customer Attendance Data");
        stage.show();
   	}
}


	

