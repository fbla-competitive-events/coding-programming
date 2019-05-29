package application;

//import statement
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

//Controller class for login screen
public class LoginScreenController implements Initializable {
	//Model for database operations
	public LoginScreenModel loginModel = new LoginScreenModel();

	//UI Features
	@FXML
	private JFXTextField txtUsername;

	@FXML
	private JFXPasswordField txtPassword;

	@FXML
	private Pane rootpane;

	@FXML
	private StackPane stack;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		Platform.runLater(() -> rootpane.requestFocus());
	}

	// Method that launches the next screen when sign in button is pressed
	// and if login is successful, if not, it gives an alert
	public void Login(ActionEvent event) throws IOException, SQLException {
		loginAction(event);
	}

	// Method to listen for Enter key and use it as a way to login as well
	@FXML
	public void handleEnterPressed(KeyEvent event) throws IOException, SQLException {
		if (event.getCode() == KeyCode.ENTER) {
			loginAction(event);
		}
	}

	//Code to login the user if inputted username and password is correct
	private void loginAction(Event event) throws IOException {
		try {
			if (loginModel.isLogin(txtUsername.getText(), txtPassword.getText())) {
				loginModel.getConnection().close();
				((Node) event.getSource()).getScene().getWindow().hide();
				Parent Main_Menu = FXMLLoader.load(getClass().getResource("MainMenu.fxml"));
				Scene MainMenu = new Scene(Main_Menu);
				Stage mainMenu = (Stage) ((Node) event.getSource()).getScene().getWindow();
				mainMenu.hide();
				mainMenu.setScene(MainMenu);
				mainMenu.setTitle("Infinity Family Entertainment Center");
				mainMenu.show();
			} else {
				//if username or password are incorrect, the user will be alerted
				
				JFXDialogLayout content = new JFXDialogLayout();
				content.setHeading(new Text("Error Login"));
				content.setBody(new Text("Incorrect Username or Password"));
				JFXButton button = new JFXButton("Okay");
				JFXDialog dialog = new JFXDialog(stack, content, JFXDialog.DialogTransition.LEFT);
				content.setStyle("-fx-border-color: red ; -fx-border-width: 2px ;");
				button.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent event) {
						dialog.close();
					}
				});
				content.setActions(button);

				dialog.show();

				txtUsername.clear();
				txtPassword.clear();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
