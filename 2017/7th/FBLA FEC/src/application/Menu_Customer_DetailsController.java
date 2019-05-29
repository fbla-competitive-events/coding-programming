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
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import tray.notification.NotificationType;
import tray.notification.TrayNotification;

public class Menu_Customer_DetailsController extends MenuBar implements Initializable {
	
	//SQL Queries
	
	//selects all Customers from the Customer Table
	private static final String CUSTOMER_SELECT_ALL_SQL = "SELECT * FROM Customers;";
	
	//Selects a customer with a given ID
	private static final String CUSTOMER_SELECT_BY_ID = "SELECT * FROM Customers WHERE ID = %s;";
	
	//Displays customers with given first and last names
	private static final String CUSTOMER_SEARCH_SQL = "SELECT * FROM Customers WHERE First_Name like '%%%s%%' OR Last_Name like '%%%s%%';";
	
	//Inserts a new customer into the database
	private static final String CUSTOMER_INSERT_SQL =
		"INSERT INTO `Customers` (`First_Name`,`Last_Name`,`Email`,`Phone`,`Address`,`DOB`) VALUES ('%s','%s','%s','%s','%s','%s');";
	
	//Updates a given customer's field values
	private static final String CUSTOMER_UPDATE_SQL =
		"UPDATE Customers SET First_Name = '%s', Last_Name = '%s', Email = '%s', Phone = '%s', Address = '%s', DOB = '%s' where ID = %s";
		
	//Delete a customer
	private static final String CUSTOMER_DELETE_SQL = "DELETE FROM Customers WHERE ID = %s;";
	//Delete a customer attendance
	private static final String CUSTOMER_ATTENDANCE_DELETE_SQL = "DELETE FROM Customers_Attendance WHERE ID = %s;";
	
	//class to do calculations and database operations for customers
	private Menu_CustomerModel Customers_Table_Screen = new Menu_CustomerModel();
	
	//UI Features
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
	private JFXDatePicker dtDOB;
	
	@FXML
	private Button btnSave;
	@FXML
	private Button btnClear;
	@FXML
	private Button btOK;
	
	@FXML
	private TextField txtSearch;
	@FXML
	private TableView<Menu_CustomerModel> TableCustomers;
	@FXML
	private TableColumn<Menu_CustomerModel, String> CustomersID;
	@FXML
	private TableColumn<Menu_CustomerModel, String> CustomersFirst_Name;
	@FXML
	private TableColumn<Menu_CustomerModel, String> CustomersLast_Name;
	@FXML
	private TableColumn<Menu_CustomerModel, String> CustomersEmail;
	
	
	//for saving purposes, checks whether the user wishes to add or delete
	private boolean isCustomersAddNewButtonClick;
	private boolean isCustomersEditButtonClick;

	//Connection, statement, and resultSet for the database operations
	Connection connection;
	private Statement statement;
	private ResultSet resultSet;

	//id of the selected Customer
	private int selectedCustomerId;
	
	//Some more UI Features
	@FXML
	private JFXDrawer topDrawer;
	@FXML
	private HBox hbMenu;
	
	@FXML
	private BorderPane root;
	
	public static BorderPane rootP;
	
	@FXML
	private Button btnAdd;
	@FXML
	private Button btnEdit;
	@FXML
	private Button btnDelete;
	
	@FXML
	private StackPane stack;
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		CustomersFirst_Name.setCellValueFactory(new PropertyValueFactory<Menu_CustomerModel,String>("CustomersFirst_Name")); 
		CustomersLast_Name.setCellValueFactory(new PropertyValueFactory<Menu_CustomerModel,String>("CustomersLast_Name"));
		CustomersID.setCellValueFactory(new PropertyValueFactory<Menu_CustomerModel,String>("Customers_ID"));
		CustomersEmail.setCellValueFactory(new PropertyValueFactory<Menu_CustomerModel,String>("Customers_Email"));

		//Fills in the customers in the customer table
		TableCustomers.setItems(Customers_Table_Screen.getDataFromSqlAndAddToObservableList(CUSTOMER_SELECT_ALL_SQL));
		
		//Displays the navigation bar at the top of the screen
		initToolbar(root, hbMenu);
		CustomersSetAllDisable();
	}
	
	private void disableAddEditDeleteButtons(boolean enableInd) {
		btnAdd.setDisable(enableInd);
		btnEdit.setDisable(enableInd);
		btnDelete.setDisable(enableInd);
	}
	
	private void disableSaveClearButtons(boolean enableInd) {
		btnSave.setDisable(enableInd);
		btnClear.setDisable(enableInd);
	}

	//Method called when the user wishes to add a new customer
	@FXML
	private void setCustomersAddNewButtonClick(Event event){
		CustomersSetAllEnable();
		isCustomersAddNewButtonClick = true;
		disableAddEditDeleteButtons(true);
	}

	//Enables the text fields for users to enter information
	private void CustomersSetAllEnable(){
		txtFirst_Name.setDisable(false);
		txtLast_Name.setDisable(false);
		txtEmail.setDisable(false);
		txtPhone.setDisable(false);
		txtAddress.setDisable(false);
		dtDOB.setDisable(false);

		disableSaveClearButtons(false);
		disableAddEditDeleteButtons(true);
	}

	//Disables Customer text fields
	private void CustomersSetAllDisable(){
		txtFirst_Name.setDisable(true);
		txtLast_Name.setDisable(true);
		txtEmail.setDisable(true);
		txtPhone.setDisable(true);
		txtAddress.setDisable(true);
		dtDOB.setDisable(true);

		disableSaveClearButtons(true);
		disableAddEditDeleteButtons(false);
	}

	//Clear Customer Fields
	private void CustomersSetAllClear(){
		txtFirst_Name.clear();
		txtLast_Name.clear();
		txtEmail.clear();
		txtPhone.clear();
		txtAddress.clear();
		dtDOB.setValue(null);
	}

	//Method called when clear button is clicked
	@FXML
	private void CustomersSetAllClear(Event event){
		txtFirst_Name.clear();
		txtLast_Name.clear();
		txtEmail.clear();
		txtPhone.clear();
		txtAddress.clear();
		dtDOB.setValue(null);
		
		disableSaveClearButtons(true);
		disableAddEditDeleteButtons(false);
		CustomersSetAllDisable();
	}

	//Method called when user saves a new customer
	@FXML
	private void setCustomerSaveButtonClick(Event event){
		try{	       
			//Checks to see whether all field values are valid
			if(validateFirstName() && validateLastName() && validateEmail() && validatePhone() && validateDOB() && validateAddress()){
				connection = SqliteConnection.Connector();
				statement = connection.createStatement();
				
				//if the user wishes to add, the new customer is inserted into the database
				if(isCustomersAddNewButtonClick){
					isCustomersEditButtonClick = true;
					btnEdit.setDisable(false);
					btnDelete.setDisable(false);
					statement.executeUpdate(
						String.format(CUSTOMER_INSERT_SQL, 
							txtFirst_Name.getText(), 
							txtLast_Name.getText(),
							txtEmail.getText(),
							txtPhone.getText(),
							txtAddress.getText(),
							dtDOB.getValue().toString()));
				}//if the user wishes to edit, the customer values are updated in the database
				else if (isCustomersEditButtonClick){
					isCustomersAddNewButtonClick = false;
					btnAdd.setDisable(false);
					btnDelete.setDisable(false);;
					statement.executeUpdate(
						String.format(CUSTOMER_UPDATE_SQL, 
							txtFirst_Name.getText(),
							txtLast_Name.getText(),
							txtEmail.getText(),
							txtPhone.getText(),
							txtAddress.getText(),
							dtDOB.getValue(),
							selectedCustomerId));
				}

				statement.close();
				connection.close();

				//Clears all text fields on the screen once the Customer is saved
				CustomersSetAllClear();
				CustomersSetAllDisable();
				TableCustomers.setItems(Customers_Table_Screen.getDataFromSqlAndAddToObservableList(CUSTOMER_SELECT_ALL_SQL));
				isCustomersEditButtonClick=false;
				isCustomersAddNewButtonClick = false;
			}	        	
		}
		catch (SQLException e){
			e.printStackTrace();
		}

	}

	//Loads in customer data to text field when user wishes to edit a Customer's information
	@FXML
	private void setCustomerEditButtonClick(Event event){

		if(TableCustomers.getSelectionModel().getSelectedItem()!=null) {
			btnAdd.setDisable(true);
			btnDelete.setDisable(true);
			
			//saves the selected customer as an object
			Menu_CustomerModel selectedCustomer = TableCustomers.getSelectionModel().getSelectedItem();

			try {
				connection = SqliteConnection.Connector();
				statement = connection.createStatement();
				resultSet = statement.executeQuery(String.format(CUSTOMER_SELECT_BY_ID, selectedCustomer.getCustomers_ID()));

				//Enables all text fields 
				CustomersSetAllEnable();
				
				//Sets the text field values to that of the selected customer
				while(resultSet.next()) {
					txtFirst_Name.setText(resultSet.getString("First_Name"));
					txtLast_Name.setText(resultSet.getString("Last_Name"));
					txtEmail.setText(resultSet.getString("Email"));
					txtPhone.setText(resultSet.getString("Phone"));
					txtAddress.setText(resultSet.getString("Address"));
					dtDOB.setValue(LocalDate.parse(resultSet.getString("DOB")));
					selectedCustomerId = resultSet.getInt("ID");
				}

				isCustomersEditButtonClick = true;
			}
			catch (SQLException e) {
				e.printStackTrace();
			}

		}
		else{
			//Displays an error message if no customer was selected
			JFXDialogLayout content = new JFXDialogLayout();
			content.setHeading(new Text("No Customer Selected"));
			content.setBody(new Text("To edit please select a Customer"));
			JFXButton button = new JFXButton("Okay");
			JFXDialog dialog = new JFXDialog(stack, content, JFXDialog.DialogTransition.LEFT);  
			content.setStyle("-fx-border-color: red ; -fx-border-width: 2px ;");
			button.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event){
					dialog.close();
				}
			});
			content.setActions(button);
			
			dialog.show();
		}

	}

	//Method called when user wishes to delete a customer
	@FXML
	private void setCustomerDeleteButtonClick(Event event){
		//A message is displayed first to confirm whether the user wishes to delete the selected employee
		if(TableCustomers.getSelectionModel().getSelectedItem()!=null){
			TableCustomers.setPlaceholder(new Label("No Customers"));	
			JFXDialogLayout content = new JFXDialogLayout();
			content.setHeading(new Text("Confirmation"));
			content.setBody(new Text("Are you sure you want to delete this customer"));
			JFXButton button = new JFXButton("Yes");
			JFXButton button1 = new JFXButton("No");
			JFXDialog dialog = new JFXDialog(stack, content, JFXDialog.DialogTransition.LEFT);  
			content.setStyle("-fx-border-color: red ; -fx-border-width: 2px ;");
			
			//If the user wishes to delete the selected employee, the employee is deleted
			button.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event){
					//Selected customer is saved as an object
					Menu_CustomerModel selectedCustomer = TableCustomers.getSelectionModel().getSelectedItem();
					
					try {
						connection = SqliteConnection.Connector();
						statement = connection.createStatement();

						statement.executeUpdate(String.format(CUSTOMER_DELETE_SQL, selectedCustomer.getCustomers_ID()));
						statement.executeUpdate(String.format(CUSTOMER_ATTENDANCE_DELETE_SQL, selectedCustomer.getCustomers_ID()));
						
						statement.close();
						connection.close();
						
						//Sets updated table with new updated customers
						TableCustomers.setItems(Customers_Table_Screen.getDataFromSqlAndAddToObservableList(CUSTOMER_SELECT_ALL_SQL));
						dialog.close();
					}
					catch (SQLException e) {
						e.printStackTrace();
					}

				}
				
			});
			button1.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event){
					dialog.close();
				}
			});
			
			content.setActions(button, button1);
			dialog.show();
				}	
		else{
			JFXDialogLayout content = new JFXDialogLayout();
			content.setHeading(new Text("No Customer Selected"));
			content.setBody(new Text("To delete, please select a Customer"));
			JFXButton button = new JFXButton("Okay");
			JFXDialog dialog = new JFXDialog(stack, content, JFXDialog.DialogTransition.LEFT);  
			content.setStyle("-fx-border-color: red ; -fx-border-width: 2px ;");
			button.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event){
					dialog.close();
				}
			});
			content.setActions(button);
			
			dialog.show();
		}
		
	}

	//Method to search for Customer by given ID
	@FXML
	private void setCustomerSearchButtonClick(Event event){
		String sqlQuery = String.format(CUSTOMER_SELECT_BY_ID, txtSearch.getText());
		TableCustomers.setItems(Customers_Table_Screen.getDataFromSqlAndAddToObservableList(sqlQuery));
	}

	//Method called to refresh Customer Table
	@FXML
	private void setCustomerRefreshButtonClick(Event event){
		TableCustomers.setItems(Customers_Table_Screen.getDataFromSqlAndAddToObservableList(CUSTOMER_SELECT_ALL_SQL));//sql Query
		txtSearch.clear();
	}

	//Method to search for a Customer based on typed name
	@FXML
	public void setOnSearchKeyPressed(KeyEvent event) throws IOException{
		if(txtSearch.getText()!=""){
			String sqlQuery = String.format(CUSTOMER_SEARCH_SQL, txtSearch.getText(), txtSearch.getText());
			if(Customers_Table_Screen.getDataFromSqlAndAddToObservableList(sqlQuery)==null){
				TableCustomers.setPlaceholder(new Label("No Customer With Given Name"));
			}
			TableCustomers.setItems(Customers_Table_Screen.getDataFromSqlAndAddToObservableList(sqlQuery));
		}
		else{
			TableCustomers.setItems(Customers_Table_Screen.getDataFromSqlAndAddToObservableList(CUSTOMER_SELECT_ALL_SQL));
		}
	} 

	

	//Following validate methods are to make sure the required info is provided in the correct format,
	//otherwise show an alert. For more information about how these methods work, please visit Main_Menu_EmployeeController.java and see the comments 
	//atop these methods in that class. They work similarly.
	
	private boolean validateFirstName(){
		Pattern p = Pattern.compile("[a-zA-z]+");
		Matcher m = p.matcher(txtFirst_Name.getText());
		if(m.find() && m.group().equals(txtFirst_Name.getText())){
			return true;
		}
		else{
			JFXDialogLayout content = new JFXDialogLayout();
			content.setHeading(new Text("Error First Name"));
			content.setBody(new Text("Please enter a valid first name."));
			JFXButton button = new JFXButton("Okay");
			JFXDialog dialog = new JFXDialog(stack, content, JFXDialog.DialogTransition.LEFT);  
			content.setStyle("-fx-border-color: red ; -fx-border-width: 2px ;");
			button.setOnAction(e -> { dialog.close(); });
			content.setActions(button);
			
			dialog.show();
			txtFirst_Name.clear();
			txtFirst_Name.requestFocus();
			
			return false;
		}
	}


	private boolean validateLastName(){
		Pattern p = Pattern.compile("[a-zA-z]+");
		Matcher m = p.matcher(txtLast_Name.getText());
		if(m.find() && m.group().equals(txtLast_Name.getText())){
			return true;
		}
		else{
			JFXDialogLayout content = new JFXDialogLayout();
			content.setHeading(new Text("Error Last Name"));
			content.setBody(new Text("Please enter a valid last name."));
			JFXButton button = new JFXButton("Okay");
			JFXDialog dialog = new JFXDialog(stack, content, JFXDialog.DialogTransition.LEFT);  
			content.setStyle("-fx-border-color: red ; -fx-border-width: 2px ;");
			button.setOnAction(e -> { dialog.close(); });
			content.setActions(button);
			
			dialog.show();
			txtLast_Name.clear();
			txtLast_Name.requestFocus();
			
			return false;
		}
	}

	private boolean validateEmail(){
		Pattern p = Pattern.compile("[a-zA-Z0-9][a-zA-Z0-9._-]*@[a-zA-Z0-9]+([.][a-zA-Z]+)+");
		Matcher m = p.matcher(txtEmail.getText());
		if(m.find() && m.group().equals(txtEmail.getText())){
			return true;
		}
		else{
			JFXDialogLayout content = new JFXDialogLayout();
			content.setHeading(new Text("Error Email"));
			content.setBody(new Text("Please enter a valid email address."));
			JFXButton button = new JFXButton("Okay");
			JFXDialog dialog = new JFXDialog(stack, content, JFXDialog.DialogTransition.LEFT);  
			content.setStyle("-fx-border-color: red ; -fx-border-width: 2px ;");
			button.setOnAction(e -> { dialog.close(); });
			content.setActions(button);
			
			dialog.show();
			txtEmail.clear();
			txtEmail.requestFocus();
			
			return false;
		}
	}

	private boolean validatePhone(){
		Pattern p = Pattern.compile("[0-9]{3}[-][0-9]{3}[-][0-9]{4}");
		Matcher m = p.matcher(txtPhone.getText());
		if(m.find() && m.group().equals(txtPhone.getText())){
			return true;
		}
		else{
			JFXDialogLayout content = new JFXDialogLayout();
			content.setHeading(new Text("Error Phone Number"));
			content.setBody(new Text("Please enter a valid phone number (aaa-aaa-aaaa)."));
			JFXButton button = new JFXButton("Okay");
			JFXDialog dialog = new JFXDialog(stack, content, JFXDialog.DialogTransition.LEFT);  
			content.setStyle("-fx-border-color: red ; -fx-border-width: 2px ;");
			button.setOnAction(e -> { dialog.close(); });
			content.setActions(button);
			
			dialog.show();
			txtPhone.clear();		
			txtPhone.requestFocus();
	
			return false;
		}
	}

	private boolean validateDOB(){
		if(dtDOB.getValue() != null){
			return true;
		}
		else{
			JFXDialogLayout content = new JFXDialogLayout();
			content.setHeading(new Text("Error Date"));
			content.setBody(new Text("Please enter a valid date."));
			JFXButton button = new JFXButton("Okay");
			JFXDialog dialog = new JFXDialog(stack, content, JFXDialog.DialogTransition.LEFT);  
			content.setStyle("-fx-border-color: red ; -fx-border-width: 2px ;");
			button.setOnAction(e -> { dialog.close(); });
			content.setActions(button);
			
			dialog.show();
			
			return false;
		}

	}

	private boolean validateAddress(){
		if(txtAddress.getText() != null){
			return true;
		}
		else{
			JFXDialogLayout content = new JFXDialogLayout();
			content.setHeading(new Text("Error Address"));
			content.setBody(new Text("Please enter a valid address."));
			JFXButton button = new JFXButton("Okay");
			JFXDialog dialog = new JFXDialog(stack, content, JFXDialog.DialogTransition.LEFT);  
			content.setStyle("-fx-border-color: red ; -fx-border-width: 2px ;");
			button.setOnAction(e -> { dialog.close(); });
			content.setActions(button);
			
			dialog.show();
		
			txtAddress.requestFocus();
			
			return false;
		} 
	}


	
	//Method to automatically add dashes when user types in a phone number
	@FXML
	public void setOnPhoneKeyReleased(KeyEvent event) throws IOException{
		if(txtPhone.getText().length()==3){
			txtPhone.setText(txtPhone.getText()+"-");
			txtPhone.positionCaret(4);
		}
		else if(txtPhone.getText().length()==7){
			txtPhone.setText(txtPhone.getText()+"-");
			txtPhone.positionCaret(8);
		}
		else if(txtPhone.getText().length()>12 ){
			txtPhone.setText(txtPhone.getText().substring(0,12));
			txtPhone.positionCaret(12);
		}
	}

	//Method called when a user selects a customer from the table
	@FXML
	public void setOKClicked(Event event) throws IOException{
		if(TableCustomers.getSelectionModel().getSelectedItem()!=null) {
			Menu_CustomerModel selectedCustomer = TableCustomers.getSelectionModel().getSelectedItem();
			
			AnchorPane pane;
			try {
				FXMLLoader loader = new FXMLLoader(getClass().getResource("Menu_Customer_Attendance.fxml"));

				pane = loader.load();
				Menu_CustomerController controller = 
					    loader.<Menu_CustomerController>getController();
			   controller.setCustomer(selectedCustomer);

			   root.getChildren().setAll(pane);
			} catch (IOException e) {
				e.printStackTrace();
			}
		
		}
		else{
			NotificationType notificationType = NotificationType.ERROR;
			TrayNotification tray = new TrayNotification();
			tray.setTitle("No Customer Selected");
			tray.setMessage("Please select a customer");
			tray.setNotificationType(notificationType);
			tray.showAndDismiss(Duration.millis(5000));
		}
	}
}
