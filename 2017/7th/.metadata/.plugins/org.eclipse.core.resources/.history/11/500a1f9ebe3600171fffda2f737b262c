package application;

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
	private static final String CUSTOMER_SELECT_ALL_SQL = "SELECT * FROM Customers;";
	private static final String CUSTOMER_SELECT_BY_ID = "SELECT * FROM Customers WHERE ID = %s;";
	
	private static final String CUSTOMER_SEARCH_SQL = "SELECT * FROM Customers WHERE First_Name like '%%%s%%' OR Last_Name like '%%%s%%';";
	
	private static final String CUSTOMER_INSERT_SQL =
		"INSERT INTO `Customers` (`First_Name`,`Last_Name`,`Email`,`Phone`,`Address`,`DOB`) VALUES ('%s','%s','%s','%s','%s','%s');";
	
	private static final String CUSTOMER_UPDATE_SQL =
		"UPDATE Customers SET First_Name = '%s', Last_Name = '%s', Email = '%s', Phone = '%s', Address = '%s', DOB = '%s' where ID = %s";
		
	private static final String CUSTOMER_DELETE_SQL = "DELETE FROM Customers WHERE ID = %s;";
	private static final String CUSTOMER_ATTENDANCE_DELETE_SQL = "DELETE FROM Customers_Attendance WHERE ID = %s;";
	
	private Menu_CustomerModel Customers_Table_Screen = new Menu_CustomerModel();
	
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
	
	private boolean isCustomersAddNewButtonClick;
	private boolean isCustomersEditButtonClick;

	Connection connection;
	private Statement statement;
	private ResultSet resultSet;

	private int selectedCustomerId;
	
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
			if(validateFirstName() && validateLastName() && validateEmail() && validatePhone() && validateDOB() && validateAddress()){
				connection = SqliteConnection.Connector();
				statement = connection.createStatement();

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
				}
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
			Menu_CustomerModel selectedCustomer = TableCustomers.getSelectionModel().getSelectedItem();

			try {
				connection = SqliteConnection.Connector();
				statement = connection.createStatement();
				resultSet = statement.executeQuery(String.format(CUSTOMER_SELECT_BY_ID, selectedCustomer.getCustomers_ID()));

				CustomersSetAllEnable();
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
		
		if(TableCustomers.getSelectionModel().getSelectedItem()!=null){
			TableCustomers.setPlaceholder(new Label("No Customers"));	
			JFXDialogLayout content = new JFXDialogLayout();
			content.setHeading(new Text("Confirmation"));
			content.setBody(new Text("Are you sure you want to delete this customer"));
			JFXButton button = new JFXButton("Yes");
			JFXButton button1 = new JFXButton("No");
			JFXDialog dialog = new JFXDialog(stack, content, JFXDialog.DialogTransition.LEFT);  
			content.setStyle("-fx-border-color: red ; -fx-border-width: 2px ;");
			
			button.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event){
					Menu_CustomerModel selectedCustomer = TableCustomers.getSelectionModel().getSelectedItem();
					
					try {
						connection = SqliteConnection.Connector();
						statement = connection.createStatement();

						statement.executeUpdate(String.format(CUSTOMER_DELETE_SQL, selectedCustomer.getCustomers_ID()));
						statement.executeUpdate(String.format(CUSTOMER_ATTENDANCE_DELETE_SQL, selectedCustomer.getCustomers_ID()));
						
						statement.close();
						connection.close();
						
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

	//Following launch methods are to load other windows for various parts of the program
	@FXML
	private void launchScheduler(Event event) throws IOException{
		((Node)event.getSource()).getScene().getWindow().hide();
		Parent Scheduler = FXMLLoader.load(getClass().getResource("Employee_Shift_Scheduler.fxml"));
		Scene scheduler = new Scene(Scheduler);
		Stage Schedule = (Stage) ((Node) event.getSource()).getScene().getWindow();
		Schedule.hide();
		Schedule.setScene(scheduler);
		Schedule.setTitle("Scheduler");
		Schedule.show();
	}

	@FXML
	private void launchEmployeeMainMenu(Event event) throws IOException{
		((Node)event.getSource()).getScene().getWindow().hide();
		Parent Main_Menu = FXMLLoader.load(getClass().getResource("Main_Menu_Employee.fxml"));
		Scene MainMenu = new Scene(Main_Menu);
		Stage mainMenu = (Stage) ((Node) event.getSource()).getScene().getWindow();
		mainMenu.hide();
		mainMenu.setScene(MainMenu);
		mainMenu.setTitle("Main Menu");
		mainMenu.show();
	}

	@FXML
	private void launchBarChart(Event event) throws IOException{
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
	private void launchLineChart(Event event) throws IOException{
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("Customer_Attendance_Line_Chart.fxml"));
		loader.load();
		Parent p = loader.getRoot();
		Stage stage = new Stage();
		stage.setScene(new Scene(p));
		stage.setTitle("Week Customer Attendance Data");
		stage.show();
	}

	//Following validate methods are to make sure the required info is provided in the correct format,
	//otherwise show an alert
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
