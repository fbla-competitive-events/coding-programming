package application;

//import statements
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXTextField;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

//Controller for the Employee Details screen
public class Main_Menu_EmployeeController extends MenuBar implements Initializable {
	
	//SQL queries
	//selects all field for every employee
	private static final String EMP_SELECT_ALL_SQL = "SELECT * FROM Employees;";
	
	//selects Employees with a specified id
	private static final String EMP_SELECT_BY_ID_SQL = "SELECT * FROM Employees where ID = %s;";
	
	//selects Employees with given Name etc.
	private static final String EMP_SEARCH_SQL =
		"SELECT * FROM Employees WHERE First_Name like '%%%s%%' OR Last_Name like '%%%s%%';";
	
	//Insert employee into database
	private static final String EMP_INSERT_SQL =
		"INSERT INTO `Employees` (`First_Name`,`Last_Name`,`Email`,`Phone`, `Address`,`DOB`) values ('%s', '%s', '%s', '%s', '%s', '%s');";
	
	//updates an employee with new field values
	private static final String EMP_UPDATE_SQL =
		"UPDATE Employees SET First_Name = '%s', Last_Name = '%s', Email = '%s', Phone = '%s', Address = '%s', DOB = '%s' WHERE ID = %s;";
	
	//delete employee with given id
	private static final String EMP_DELETE_BY_ID_SQL = "DELETE FROM Employees WHERE ID = %s";
	
	//delete employee shift
	private static final String EMP_DELETE_SCHEDULE_BY_ID_SQL = "DELETE from Employees_Schedule WHERE ID = %s";
	
	//class to do database operations and calculations
	private Main_Menu_EmployeeModel Employee_Table_Screen = new Main_Menu_EmployeeModel();

	// Features of the UI
	@FXML
	private Button btnAdd;
	@FXML
	private StackPane stack;
	@FXML
	private Button btnEdit;
	@FXML
	private Button btnDelete;
	@FXML
	private Button btnSave;
	@FXML
	private Button btnClear;
	@FXML
	private JFXTextField txtFirst_Name;
	@FXML
	private JFXTextField txtLast_Name;
	@FXML
	private JFXTextField txtEmail;
	@FXML
	private JFXTextField txtPhone;
	@FXML
	private JFXTextField txtAddress;
	@FXML
	private TextField txtSearch;
	@FXML
	private JFXDatePicker dtDOB;
	@FXML
	private TableView<Main_Menu_EmployeeModel> TableEmployees;
	@FXML
	private TableColumn<Main_Menu_EmployeeModel, String> EmployeesFirst_Name;
	@FXML
	private TableColumn<Main_Menu_EmployeeModel, String> EmployeesLast_Name;
	@FXML
	private TableColumn<Main_Menu_EmployeeModel, String> EmployeesID;
	@FXML
	private TableColumn<Main_Menu_EmployeeModel, String> EmployeesEmail;
	@FXML
	private JFXDrawer topDrawer;
	@FXML
	private HBox hbMenu;

	//for the purposes of the save method, checks whether the user is adding or editing
	private boolean isMainMenuAddNewButtonClick;
	private boolean isMainMenuEditButtonClick;

	//Connection, statement, and resultSet for database operations
	Connection connection;
	private Statement statement;
	private ResultSet resultSet;

	//ID of the selected employee
	private int selectedEmpId;

	//Pane name for the purposes of changing the screen on the same window
	@FXML
	private BorderPane root;

	public static BorderPane rootP;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// "Configures" the value of each column in the table

		//Sets certain buttons on the screen disable so user cannot edit them immediately
		MainMenuSetAllDisable();
		
		//Configures each column with values to expect
		EmployeesFirst_Name.setCellValueFactory(new PropertyValueFactory<Main_Menu_EmployeeModel, String>("EmployeesFirst_Name"));
		EmployeesLast_Name.setCellValueFactory(new PropertyValueFactory<Main_Menu_EmployeeModel, String>("EmployeesLast_Name"));
		EmployeesID.setCellValueFactory(new PropertyValueFactory<Main_Menu_EmployeeModel, String>("EmployeesID"));
		EmployeesEmail.setCellValueFactory(new PropertyValueFactory<Main_Menu_EmployeeModel, String>("EmployeesEmail"));

		// Sets the values of the table from the Employee Screen
		TableEmployees.setItems(Employee_Table_Screen.getDataFromSqlAndAddToObservableList("SELECT * FROM EMPLOYEES"));
		dtDOB.setEditable(false);

		//Method that displays navigation menu on top of screen
		initToolbar(root, hbMenu);
	}

	// Method that runs when user wishes to add a new employee
	@FXML
	private void setMainMenuAddNewButtonClick(Event event) {
		MainMenuSetAllEnable();
		MainMenuSetAllClear();
		btnAdd.setDisable(true);
		btnEdit.setDisable(true);
		btnDelete.setDisable(true);
		isMainMenuAddNewButtonClick = true;
	}

	// Enables the field so the user can type the employee information
	private void MainMenuSetAllEnable() {
		txtFirst_Name.setDisable(false);
		txtLast_Name.setDisable(false);
		txtPhone.setDisable(false);
		txtAddress.setDisable(false);
		txtEmail.setDisable(false);
		dtDOB.setDisable(false);

		btnSave.setDisable(false);
		btnClear.setDisable(false);

	}

	// Disables all text fields
	private void MainMenuSetAllDisable() {
		txtFirst_Name.setDisable(true);
		txtLast_Name.setDisable(true);
		txtPhone.setDisable(true);
		txtAddress.setDisable(true);
		txtEmail.setDisable(true);
		dtDOB.setDisable(true);

		btnSave.setDisable(true);
		btnClear.setDisable(true);
	}

	// Clears all text fields
	private void MainMenuSetAllClear() {
		txtFirst_Name.clear();
		txtLast_Name.clear();
		txtPhone.clear();
		txtAddress.clear();
		txtEmail.clear();
		dtDOB.setValue(null);
	}
	
	private void resetButtons() {
		btnAdd.setDisable(false);
		btnEdit.setDisable(false);
		btnDelete.setDisable(false);
		btnSave.setDisable(true);
		btnClear.setDisable(true);
	}

	// Method called when clear button is clicked
	@FXML
	private void MainMenuSetAllClear(Event event) {
		MainMenuSetAllClear();
		resetButtons();
	}

	// Saves the given employee information to the Employee database and loads
	// it into the table
	@FXML
	private void setMainMenuSaveButtonClick(Event event) {
		try {

			//Checks to see if user has inputted valid values for each field
			if (validateFirstName() && validateLastName() && validateEmail() && validatePhone() && validateDOB()
					&& validateAddress()) {

				connection = SqliteConnection.Connector();
				statement = connection.createStatement();
				
				//if the main menu add button was clicked, the employee is inserted
				if (isMainMenuAddNewButtonClick) {
					btnEdit.setDisable(false);
					btnDelete.setDisable(false);
					statement.executeUpdate(
						String.format(EMP_INSERT_SQL, txtFirst_Name.getText(), txtLast_Name.getText(),
							txtEmail.getText(), txtPhone.getText(), txtAddress.getText(), dtDOB.getValue().toString()));
				} //if the main menu edit button was clicked, the employee values are updated
				else if (isMainMenuEditButtonClick) {
					btnAdd.setDisable(false);
					btnDelete.setDisable(false);
					statement.executeUpdate(
						String.format(EMP_UPDATE_SQL, txtFirst_Name.getText(), txtLast_Name.getText(),
							txtEmail.getText(), txtPhone.getText(), txtAddress.getText(), dtDOB.getValue().toString(), selectedEmpId));
				}

				statement.close();
				connection.close();

				//Once it is saved it clears values
				MainMenuSetAllClear();
				MainMenuSetAllDisable();
				resetButtons();
				
				//Sets the table of Employees with the new Employees and field values
				TableEmployees.setItems(
						Employee_Table_Screen.getDataFromSqlAndAddToObservableList(EMP_SELECT_ALL_SQL));
				isMainMenuEditButtonClick = false;
				isMainMenuAddNewButtonClick = false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	// Loads in the information into the text fields of the employee that is
	// trying to be edited
	@FXML
	private void setMainMenuEditButtonClick(Event event) {

		if (TableEmployees.getSelectionModel().getSelectedItem() != null) {
			btnAdd.setDisable(true);
			btnDelete.setDisable(true);
			
			//gets the exact employee that was selected
			Main_Menu_EmployeeModel selectedEmp = TableEmployees.getSelectionModel().getSelectedItem();
		
			try {
				connection = SqliteConnection.Connector();
				statement = connection.createStatement();
				resultSet = statement.executeQuery(String.format(EMP_SELECT_BY_ID_SQL, selectedEmp.getEmployeesID()));

				MainMenuSetAllEnable();
				
				//Sets all text fields to the values of the selected employee
				while (resultSet.next()) {
					txtFirst_Name.setText(resultSet.getString("First_Name"));
					txtLast_Name.setText(resultSet.getString("Last_Name"));
					txtEmail.setText(resultSet.getString("Email"));
					txtPhone.setText(resultSet.getString("Phone"));
					txtAddress.setText(resultSet.getString("Address"));
					dtDOB.setValue(LocalDate.parse(resultSet.getString("DOB")));
					selectedEmpId = resultSet.getInt("ID");
					isMainMenuEditButtonClick = true;
				}

			} catch (SQLException e) {
				e.printStackTrace();
			}

		} else {
			//if no employee is selected it alerts the user
			JFXDialogLayout content = new JFXDialogLayout();
			content.setHeading(new Text("No Employee Selected"));
			content.setBody(new Text("To edit please select an employee from the  table"));
			JFXButton button = new JFXButton("Okay");
			JFXDialog dialog = new JFXDialog(stack, content, JFXDialog.DialogTransition.LEFT);
			content.setStyle("-fx-border-color: red ; -fx-border-width: 2px ;");
			
			//if the user clicks the ok button on the dialogbox, it will exit the dialog box
			button.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					dialog.close();
				}
			});
			content.setActions(button);

			dialog.show();

		}

	}

	// Deletes an employee from the Employee Database and refreshes the table
	@FXML
	private void setMainMenuDeleteButtonClick(Event event) {
		if (TableEmployees.getSelectionModel().getSelectedItem() != null) {
			
			//If the user clicks delete, it confirms whether the user wants to delete the employee
			JFXDialogLayout content = new JFXDialogLayout();
			content.setHeading(new Text("Confirmation"));
			content.setBody(new Text("Are you sure you want to delete this employee."));
			JFXButton button = new JFXButton("Yes");
			JFXButton button1 = new JFXButton("No");
			JFXDialog dialog = new JFXDialog(stack, content, JFXDialog.DialogTransition.LEFT);
			content.setStyle("-fx-border-color: red ; -fx-border-width: 2px ;");
			//If the user says yes, the employee is deleted
			button.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					TableEmployees.setPlaceholder(new Label("No Employees"));
					if (TableEmployees.getSelectionModel().getSelectedItem() != null) {
						Main_Menu_EmployeeModel selectedEmp = TableEmployees.getSelectionModel().getSelectedItem();
						
						try {
							connection = SqliteConnection.Connector();
							statement = connection.createStatement();

							//Deletes employees from both the employee table and the employee_schedule table
							statement.executeUpdate(String.format(EMP_DELETE_BY_ID_SQL, selectedEmp.getEmployeesID()));
							statement.executeUpdate(String.format(EMP_DELETE_SCHEDULE_BY_ID_SQL, selectedEmp.getEmployeesID()));

							//Sets the table of employee to the updated employees
							TableEmployees.setItems(Employee_Table_Screen
									.getDataFromSqlAndAddToObservableList(EMP_SELECT_ALL_SQL));
							statement.close();
							connection.close();

						} catch (SQLException e) {
							e.printStackTrace();
						}
						dialog.close();
					}

				}
			});
			//If the user says no, the dialog box is exited
			button1.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					dialog.close();
				}
			});

			content.setActions(button, button1);
			dialog.show();
		} else {
			JFXDialogLayout content = new JFXDialogLayout();
			content.setHeading(new Text("No Employee Selected"));
			content.setBody(new Text("To delete, please select a Employee"));
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
		}

	}

	// Method to search for an employee based on given ID
	@FXML
	private void setMainMenuSearchButtonClick(Event event) {
		String sqlQuery = String.format(EMP_SELECT_BY_ID_SQL, txtSearch.getText());
		TableEmployees.setItems(Employee_Table_Screen.getDataFromSqlAndAddToObservableList(sqlQuery));
	}

	// Method to refresh employee table
	@FXML
	private void setMainMenuRefreshButtonClick(Event event) {
		TableEmployees.setItems(Employee_Table_Screen.getDataFromSqlAndAddToObservableList(EMP_SELECT_ALL_SQL));
		txtSearch.clear();
	}

	// The following launch methods are for loading other screens in the program
	// when their respective buttons are clicked

	

	// Shows the extra employee information by launching a mini-window
	@FXML
	private void setMainMenuViewButtonClick(Event event) throws IOException {

		if (TableEmployees.getSelectionModel().getSelectedItem() != null) {
			Employee_Profile_ViewModel info = new Employee_Profile_ViewModel(
					TableEmployees.getSelectionModel().getSelectedItem().getEmployeesID().toString());
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("Employee_Profile_View.fxml"));
			loader.load();
			Parent p = loader.getRoot();
			Stage stage = new Stage();
			stage.setScene(new Scene(p));

			//Loads the profiler view for the given employee
			Employee_Profile_ViewController profileView = loader.getController();
			profileView.setCurrentInfo(info);
			stage.show();
		} else {
			JFXDialogLayout content = new JFXDialogLayout();
			content.setHeading(new Text("No Employee Selected"));
			content.setBody(new Text("Please select an employee to view"));
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
		}

	}

	// The following validate methods check to see whether the Employee
	// information is valid/ given in the right format
	private boolean validateFirstName() {
		Pattern p = Pattern.compile("[a-zA-z]+");
		Matcher m = p.matcher(txtFirst_Name.getText());
		if (m.find() && m.group().equals(txtFirst_Name.getText())) {
			return true;
		} else {
			//displays a message if the user doesn't enter a valid first name
			JFXDialogLayout content = new JFXDialogLayout();
			content.setHeading(new Text("Error First Name"));
			content.setBody(new Text("Please enter a valid first name."));
			JFXButton button = new JFXButton("Okay");
			JFXDialog dialog = new JFXDialog(stack, content, JFXDialog.DialogTransition.LEFT);
			
			//sets the border to red of the alert message
			content.setStyle("-fx-border-color: red ; -fx-border-width: 2px ;");
			button.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					dialog.close();
				}
			});
			content.setActions(button);

			dialog.show();
			txtFirst_Name.clear();

			txtFirst_Name.requestFocus();

			return false;
		}
	}

	//Makes sure the last name entered is valid
	private boolean validateLastName() {
		
		//Makes sure it is all letters and no numbers, etc.
		Pattern p = Pattern.compile("[a-zA-z]+");
		Matcher m = p.matcher(txtLast_Name.getText());
		if (m.find() && m.group().equals(txtLast_Name.getText())) {
			return true;
		} else {
			JFXDialogLayout content = new JFXDialogLayout();
			content.setHeading(new Text("Error Last Name"));
			content.setBody(new Text("Please enter a valid last name."));
			JFXButton button = new JFXButton("Okay");
			JFXDialog dialog = new JFXDialog(stack, content, JFXDialog.DialogTransition.LEFT);
			content.setStyle("-fx-border-color: red ; -fx-border-width: 2px ;");
			//if the ok button on the dialog box is clicked, the error message exits
			button.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					dialog.close();
				}
			});
			content.setActions(button);

			dialog.show();
			txtLast_Name.clear();

			//brings cursor to the last name text field
			txtLast_Name.requestFocus();

			return false;
		}
	}

	//Validates the format of the given email
	private boolean validateEmail() {
		
		//Makes sure the email begins with characters, then the @ symbol, then some more characters, then a dot, and finally some more characters
		Pattern p = Pattern.compile("[a-zA-Z0-9][a-zA-Z0-9._-]*@[a-zA-Z0-9]+([.][a-zA-Z]+)+");
		Matcher m = p.matcher(txtEmail.getText());
		if (m.find() && m.group().equals(txtEmail.getText())) {
			return true;
		} else {
			//Dialog box to alert user that their inputed email address is of invalid format
			JFXDialogLayout content = new JFXDialogLayout();
			content.setHeading(new Text("Error Email"));
			content.setBody(new Text("Please enter a valid email address."));
			JFXButton button = new JFXButton("Okay");
			JFXDialog dialog = new JFXDialog(stack, content, JFXDialog.DialogTransition.LEFT);
			//sets the border of the dialgo box to red
			content.setStyle("-fx-border-color: red ; -fx-border-width: 2px ;");
			button.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					dialog.close();
				}
			});
			content.setActions(button);

			dialog.show();

			txtEmail.clear();
			txtEmail.requestFocus();

			return false;
		}
	}

	//Validates the format of the phone number (***-***-****)
	private boolean validatePhone() {
		Pattern p = Pattern.compile("[0-9]{3}[-][0-9]{3}[-][0-9]{4}");
		Matcher m = p.matcher(txtPhone.getText());
		if (m.find() && m.group().equals(txtPhone.getText())) {
			return true;
		} else {
			JFXDialogLayout content = new JFXDialogLayout();
			content.setHeading(new Text("Error Phone Number"));
			content.setBody(new Text("Please enter a valid phone number (aaa-aaa-aaaa)."));
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
			txtPhone.clear();

			txtPhone.requestFocus();

			return false;

		}
	}

	//validates that a date of birth was inputted and the value is not null
	private boolean validateDOB() {
		if (dtDOB.getValue() != null) {
			return true;
		} else {
			JFXDialogLayout content = new JFXDialogLayout();
			content.setHeading(new Text("Error Date"));
			content.setBody(new Text("Please enter a valid date."));
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

			return false;
		}

	}

	//validates that an Address was inputed and the text field value is not null
	private boolean validateAddress() {
		if (txtAddress.getText() != null) {
			return true;
		} else {
			JFXDialogLayout content = new JFXDialogLayout();
			content.setHeading(new Text("Error Address"));
			content.setBody(new Text("Please enter a valid address."));
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

			txtAddress.requestFocus();

			return false;
		}
	}

	// Method to seach for employee based on name
	@FXML
	public void setOnSearchKeyPressed(KeyEvent event) throws IOException {
		if (txtSearch.getText() != "") {
			String sqlQuery = String.format(EMP_SEARCH_SQL, txtSearch.getText(), txtSearch.getText());
			if (Employee_Table_Screen.getDataFromSqlAndAddToObservableList(sqlQuery) == null) {
				TableEmployees.setPlaceholder(new Label("No Employee With Given Name"));
			}
			TableEmployees.setItems(Employee_Table_Screen.getDataFromSqlAndAddToObservableList(sqlQuery));
		} else {
			TableEmployees.setItems(Employee_Table_Screen.getDataFromSqlAndAddToObservableList(EMP_SELECT_ALL_SQL));
		}
	}

	// Method to automatically put in dashes when the user types in the phone
	// number
	@FXML
	public void setOnPhoneKeyReleased(KeyEvent event) throws IOException {
		if (txtPhone.getText().length() == 3) {
			txtPhone.setText(txtPhone.getText() + "-");
			txtPhone.positionCaret(4);
		} else if (txtPhone.getText().length() == 7) {
			txtPhone.setText(txtPhone.getText() + "-");
			txtPhone.positionCaret(8);
		} else if (txtPhone.getText().length() > 12) {
			txtPhone.setText(txtPhone.getText().substring(0, 12));
			txtPhone.positionCaret(12);
		}
	}

	// Method to launch extra employee information window if the employee is
	// double clicked on the table
	@FXML
	public void handleDoubleClick(MouseEvent mouseEvent) throws IOException {
		if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
			//Counts the number of mouse clicks that happened
			if (mouseEvent.getClickCount() == 2) {
				if (TableEmployees.getSelectionModel().getSelectedItem() != null) {
					Employee_Profile_ViewModel info = new Employee_Profile_ViewModel(
							TableEmployees.getSelectionModel().getSelectedItem().getEmployeesID().toString());
					FXMLLoader loader = new FXMLLoader();
					loader.setLocation(getClass().getResource("Employee_Profile_View.fxml"));
					loader.load();
					Parent p = loader.getRoot();
					Stage stage = new Stage();
					stage.setScene(new Scene(p));

					Employee_Profile_ViewController profileView = loader.getController();
					profileView.setCurrentInfo(info);
					stage.show();
				} else {
					JFXDialogLayout content = new JFXDialogLayout();
					content.setHeading(new Text("No Employee Selected"));
					content.setBody(new Text("Please select an employee to view."));
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
				}
			}
		}
	}
}
