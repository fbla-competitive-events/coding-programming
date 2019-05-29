package application;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXRadioButton;

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
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Menu_CustomerController extends MenuBar implements Initializable {
	private static final String CUSTOMER_ATTENDANCE_SELECT_SQL =
		"SELECT Customers_Attendance.*, Customers.ID, Customers.First_Name, Customers.Last_Name FROM" + 
			" Customers_Attendance INNER JOIN Customers ON Customers_Attendance.ID=Customers.ID WHERE Customers_Attendance.ID = %s;";
	
	private static final String CUSTOMER_ATTENDANCE_SELECT_ALL_SQL =
		"SELECT Customers_Attendance.*, Customers.ID, Customers.First_Name, Customers.Last_Name FROM" + 
			" Customers_Attendance INNER JOIN Customers ON Customers_Attendance.ID=Customers.ID;";
	
	private static final String CUSTOMER_ATTENDANCE_INSERT_SQL =
		"INSERT INTO `Customers_Attendance` (ID, `Date`,`AMPM`,`Day_of_Week`) values (%s,'%s','%s','%s');";
	
	private static final String CUSTOMER_ATTENDANCE_DELETE_SQL =
		"DELETE FROM Customers_Attendance WHERE ID = %s AND Date = '%s' AND AMPM = '%s';";
					
	private Menu_Customer_AttendanceModel Customers_Table_Attendance_Screen = new Menu_Customer_AttendanceModel();

	// UI Features
	@FXML
	AnchorPane ancpane;
	@FXML
	StackPane stack;
	@FXML
	private Button btnAdd;
	@FXML
	private Button btnShowAllAtt;
	@FXML
	private ToggleGroup group;
	@FXML
	private JFXRadioButton rdAM;
	@FXML
	private JFXRadioButton rdPM;
	@FXML
	private JFXDatePicker dtAttendance;
	@FXML
	private Text lblCustomerAtt;
	@FXML
	private Button btnSearchCust;
	@FXML
	private TableView<Menu_Customer_AttendanceModel> TableCustomerAttendance;
	@FXML
	private TableColumn<Menu_Customer_AttendanceModel, String> CustomerAttFirst_Name;
	@FXML
	private TableColumn<Menu_Customer_AttendanceModel, String> CustomerAttLast_Name;
	@FXML
	private TableColumn<Menu_Customer_AttendanceModel, String> CustomerAttDate;
	@FXML
	private TableColumn<Menu_Customer_AttendanceModel, String> CustomerAttAMPM;
	@FXML
	private JFXDrawer topDrawer;
	@FXML
	private HBox hbMenu;
	@FXML
	private Button btnDelete;
	@FXML
	private Button btnSave;
	@FXML
	private Label lblCustomerName;

	Connection connection;
	private Statement statement;

	@FXML
	private AnchorPane root;

	private int customerID = -1;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		CustomerAttFirst_Name.setCellValueFactory(new PropertyValueFactory<Menu_Customer_AttendanceModel, String>("Customers_FirstName"));
		CustomerAttLast_Name.setCellValueFactory(new PropertyValueFactory<Menu_Customer_AttendanceModel, String>("Customers_LastName"));
		CustomerAttDate.setCellValueFactory(new PropertyValueFactory<Menu_Customer_AttendanceModel, String>("Customers_Date"));
		CustomerAttAMPM.setCellValueFactory(new PropertyValueFactory<Menu_Customer_AttendanceModel, String>("Customers_AMPM"));

		rdAM.setSelected(true);
		CustomersAttendanceSetAllDisable();

		dtAttendance.setEditable(false);

		initToolbar(root, hbMenu);
	}

	public void setCustomer(Menu_CustomerModel customer) {
		this.customerID = customer.getCustomers_ID();
		this.lblCustomerName.setText(String.format("%s %s", customer.getCustomersFirst_Name(), customer.getCustomersLast_Name()));

		TableCustomerAttendance.setItems(
			Customers_Table_Attendance_Screen.getDataFromSqlAndAddToObservableList(
				String.format(CUSTOMER_ATTENDANCE_SELECT_SQL, customerID)));
		CustomersAttendanceSetAllEnable();
	}
	
	// Display Customer Attendance Text Fields
	private void CustomersAttendanceSetAllDisable() {
		btnDelete.setDisable(true);
		btnSave.setDisable(true);
		dtAttendance.setDisable(true);
		rdAM.setDisable(true);
		rdPM.setDisable(true);
		// TableCustomerAttendance.setDisable(true);
	}

	// Enable CustomerAttendance Fields
	private void CustomersAttendanceSetAllEnable() {
		btnDelete.setDisable(false);
		btnSave.setDisable(false);
		dtAttendance.setDisable(false);
		rdAM.setDisable(false);
		rdPM.setDisable(false);
		// TableCustomerAttendance.setDisable(false);
	}

	// Method to save a new Customer Attendance
	@FXML
	private void setCustomerAttendanceSaveButtonClick(Event event) {
		if (customerID != -1) {
			try {
				if (validateAttendanceDate()) {
					connection = SqliteConnection.Connector();
					statement = connection.createStatement();

					statement.executeUpdate(
						String.format(CUSTOMER_ATTENDANCE_INSERT_SQL, 
							customerID,
							dtAttendance.getValue().toString(),
							AMPM(),
							dtAttendance.getValue().getDayOfWeek().toString()));

					TableCustomerAttendance.setItems(
						Customers_Table_Attendance_Screen.getDataFromSqlAndAddToObservableList(
							String.format(CUSTOMER_ATTENDANCE_SELECT_SQL, customerID)));

					/*
					 * CustomersAttendanceSetAllClear();
					 * btShowAllAtt.setDisable(false);
					 */
					statement.close();
					connection.close();
				}

			} catch (SQLException e) {
				e.printStackTrace();
			}

		} else {
			JFXDialogLayout content = new JFXDialogLayout();
			content.setHeading(new Text("No Customer Selected"));
			content.setBody(new Text("To add, please select a customer"));
			JFXButton button = new JFXButton("Okay");
			JFXDialog dialog = new JFXDialog(stack, content, JFXDialog.DialogTransition.LEFT);
			content.setStyle("-fx-border-color: red ; -fx-border-width: 2px ;");
			button.setOnAction(e -> { dialog.close(); });
			content.setActions(button);

			dialog.show();
		}

	}
	
	// Checks to see whether AM or PM is selected
	public String AMPM() {
		if (rdAM.isSelected()) {
			return "AM";
		} else {
			return "PM";
		}
	}

	@FXML
	private void LaunchCustomerDetails(Event event) throws IOException {
		((Node) event.getSource()).getScene().getWindow().hide();
		Parent CustomerScreen = FXMLLoader.load(getClass().getResource("Menu_Customer_Details.fxml"));
		Scene customer_screen = new Scene(CustomerScreen);
		Stage Customer_Screen = (Stage) ((Node) event.getSource()).getScene().getWindow();
		Customer_Screen.hide();
		Customer_Screen.setScene(customer_screen);
		Customer_Screen.setTitle("Infinity Family Entertainment Center");
		Customer_Screen.show();

	}

	// Method for deleting a customer attendance
	@FXML
	private void setCustomerAttendanceDeleteButtonClick(Event event) {
		if (TableCustomerAttendance.getSelectionModel().getSelectedItem() != null) {
			Menu_Customer_AttendanceModel getSelectedRow = 
				TableCustomerAttendance.getSelectionModel().getSelectedItem();
			
			JFXDialogLayout content = new JFXDialogLayout();
			content.setHeading(new Text("Confirmation"));
			content.setBody(new Text("Are you sure you want to delete this attendance record?"));
			JFXButton btnYes = new JFXButton("Yes");
			JFXButton btnNo = new JFXButton("No");
			JFXDialog dialog = new JFXDialog(stack, content, JFXDialog.DialogTransition.LEFT);
			content.setStyle("-fx-border-color: red ; -fx-border-width: 2px ;");

			btnYes.setOnAction(e -> {
				try {
					connection = SqliteConnection.Connector();
					statement = connection.createStatement();

					statement.executeUpdate(String.format(CUSTOMER_ATTENDANCE_DELETE_SQL, 
						getSelectedRow.getCustomers_ID(), getSelectedRow.getCustomers_Date(), getSelectedRow.getCustomers_AMPM()));
					
					TableCustomerAttendance.setItems(
						Customers_Table_Attendance_Screen.getDataFromSqlAndAddToObservableList(String.format(CUSTOMER_ATTENDANCE_SELECT_SQL, customerID)));
						
					statement.close();
					connection.close();
				} 
				catch (SQLException ex) {
					ex.printStackTrace();
				}
				
				dialog.close();
			});

			btnNo.setOnAction(e -> { dialog.close(); });

			content.setActions(btnYes, btnNo);
			dialog.show();

		} else {
			JFXDialogLayout content = new JFXDialogLayout();
			content.setHeading(new Text("No Attendance Selected"));
			content.setBody(new Text("Please select an attendance from the table."));
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

	// Method to refresh customer attendance table
	@FXML
	private void setShowAllAttClick(Event event) {
		lblCustomerName.setText("");
		TableCustomerAttendance.setItems(
			Customers_Table_Attendance_Screen.getDataFromSqlAndAddToObservableList(CUSTOMER_ATTENDANCE_SELECT_ALL_SQL));
	}

	private boolean validateAttendanceDate() {
		if (dtAttendance.getValue() != null) {
			return true;
		} else {
			JFXDialogLayout content = new JFXDialogLayout();
			content.setHeading(new Text("Error Date"));
			content.setBody(new Text("Please select a valid date."));
			JFXButton button = new JFXButton("Okay");
			JFXDialog dialog = new JFXDialog(stack, content, JFXDialog.DialogTransition.LEFT);
			content.setStyle("-fx-border-color: red ; -fx-border-width: 2px ;");
			button.setOnAction(e -> { dialog.close(); });
			content.setActions(button);

			dialog.show();

			return false;
		}
	}
}
