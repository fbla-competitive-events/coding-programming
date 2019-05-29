package application;

//Import statements
import java.net.URL;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDrawer;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

//This class is the controller for the screen that shows details about Me and the Program
public class AboutScreenController extends MenuBar implements Initializable {
	
	//UI Features on the screen
    @FXML
	private TreeTableView<String> treeTableMenu;
	@FXML
	private TreeTableColumn<String, String> treeTableMenuColumn;
	@FXML
	private JFXDrawer topDrawer;
	@FXML
	private HBox hbMenu;
	
	//Pane name (for the purposes of changing the screen on the window)
	@FXML
	private BorderPane root;
	
	public static BorderPane rootP;
	
	
    @Override
    public void initialize(URL location, ResourceBundle resources) {
    	//This method simply puts the navigation menu at the top of the screen
    	initToolbar(root, hbMenu);
    }	
    
}


	

