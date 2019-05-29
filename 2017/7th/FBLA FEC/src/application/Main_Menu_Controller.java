package application;

//import statements
import java.net.URL;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXDrawer;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

//Controller class for the first screen that loads once the user logs in
public class Main_Menu_Controller extends MenuBar implements Initializable {
	
	//UI Features
    @FXML
	private TreeTableView<String> treeTableMenu;
	@FXML
	private TreeTableColumn<String, String> treeTableMenuColumn;
	@FXML
	private JFXDrawer topDrawer;	
	@FXML
	private HBox hbMenu;
	
	//BorderPane for the purposes of changing the screen on the same window
	@FXML
	private BorderPane root;
	
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //Method that displays the navigation menu at the top of the screen
    	initToolbar(root, hbMenu);
    }
}


	

