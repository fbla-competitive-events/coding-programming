package application;

import java.awt.print.PageFormat;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ResourceBundle;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.transform.Scale;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.Callback;
import javafx.util.Duration;
import tray.notification.NotificationType;
import tray.notification.TrayNotification;
import javafx.scene.control.ListView;
import javafx.scene.control.MultipleSelectionModel;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;

import javafx.scene.control.RadioButton;

import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.print.PageLayout;
import javafx.print.PageOrientation;
import javafx.print.Paper;
import javafx.print.Printer;
import javafx.print.PrinterAttributes;
import javafx.print.PrinterJob;

public class Employee_Shift_SchedulerController1 implements Initializable {
	
	public Employee_Shift_SchedulerModel Scheduler_Table = new Employee_Shift_SchedulerModel();
	
	//Features of the UI
	@FXML
	ToggleGroup group = new ToggleGroup();
	
	@FXML
	private DatePicker dtSchedule;
	
	@FXML
	private ListView<String> listSun;
	@FXML
	private ListView<String> listMon;
	@FXML
	private ListView<String> listTues;
	@FXML
	private ListView<String> listWed;
	@FXML
	private ListView<String> listThurs;
	@FXML
	private ListView<String> listFri;
	@FXML
	private ListView<String> listSat;
	@FXML
	private ListView<String> chosenlist;
	
	@FXML
	private Label lblSun;
	@FXML
	private Label lblMon;
	@FXML
	private Label lblTues;
	@FXML
	private Label lblWed;
	@FXML
	private Label lblThurs;
	@FXML
	private Label lblFri;
	@FXML
	private Label lblSat;
	
	@FXML
	private TableView<Employee_Shift_SchedulerModel> TableEmployees;
	@FXML
	private TableColumn<Employee_Shift_SchedulerModel, String> EmployeesFirst_Name;
	@FXML
	private TableColumn<Employee_Shift_SchedulerModel, String> EmployeesLast_Name;
	@FXML
	private TableColumn<Employee_Shift_SchedulerModel, String> EmployeesID;
	@FXML
	private TextField txtSearch;
	@FXML
	private Button btAdd_Employee;
	@FXML
	private Button btSearch_Employee;
	@FXML
	private Button btDelete;
	@FXML
	private Button btRefresh;
	
	@FXML
	private GridPane grSchedule;

	private Label chosen;
	Connection connection;
    private Statement statement;
    private ResultSet resultSet;
    private PageFormat format;
    
	@FXML
	private TreeTableView<String> treeTableMenu;
	@FXML
	private TreeTableColumn<String, String> treeTableMenuColumn;
	
	public static boolean isSplashLoaded;
	
	TreeItem<String> item_l1 = new TreeItem<>("Scheduler");
	TreeItem<String> item_l2 = new TreeItem<>("Details");
	TreeItem<String> parent1 = new TreeItem<>("Employee");
	
	TreeItem<String> item_r1 = new TreeItem<>("Attendance");
	TreeItem<String> item_r2 = new TreeItem<>("Bar Chart");
	TreeItem<String> item_r3 = new TreeItem<>("Line Chart");
	TreeItem<String> parent2 = new TreeItem<>("Customer");
	
	TreeItem<String> rootie = new TreeItem<>("Menu");
	
	@FXML
	private AnchorPane root;
	
	public static AnchorPane rootP;
	
	
	@Override
    public void initialize(URL location, ResourceBundle resources) {
		treeTableMenu.setStyle("-fx-focus-color: transparent;");
    	treeTableMenuColumn.setStyle("-fx-focus-color: transparent;");

		
		//setting the values of each column in the table
		EmployeesFirst_Name.setCellValueFactory(new PropertyValueFactory<Employee_Shift_SchedulerModel,String>("EmployeesFirst_Name")); 
        EmployeesLast_Name.setCellValueFactory(new PropertyValueFactory<Employee_Shift_SchedulerModel,String>("EmployeesLast_Name"));
        EmployeesID.setCellValueFactory(new PropertyValueFactory<Employee_Shift_SchedulerModel,String>("EmployeesID"));
        
        //loading the employee information into the table 
        TableEmployees.setItems(Scheduler_Table.getDataFromSqlAndAddToObservableList("SELECT * FROM EMPLOYEES"));
   
        
        dtSchedule.setEditable(false);
        dtSchedule.setDisable(false);
        
        //checking what day it is today and loading the respective schedule
        String now = LocalDate.now().getDayOfWeek().toString();
        if(now == "SUNDAY"){
			
			lblSun.setText(LocalDate.now().toString());
			lblMon.setText(LocalDate.now().plusDays(1).toString());
			lblTues.setText(LocalDate.now().plusDays(2).toString());
			lblWed.setText(LocalDate.now().plusDays(3).toString());
			lblThurs.setText(LocalDate.now().plusDays(4).toString());
			lblFri.setText(LocalDate.now().plusDays(5).toString());
			lblSat.setText(LocalDate.now().plusDays(6).toString());

		}
		else if(now == "MONDAY"){
			
			lblMon.setText(LocalDate.now().toString());
			lblTues.setText(LocalDate.now().plusDays(1).toString());
			lblWed.setText(LocalDate.now().plusDays(2).toString());
			lblThurs.setText(LocalDate.now().plusDays(3).toString());
			lblFri.setText(LocalDate.now().plusDays(4).toString());
			lblSat.setText(LocalDate.now().plusDays(5).toString());
			lblSun.setText(LocalDate.now().minusDays(1).toString());

		}
		else if(now == "TUESDAY"){
			
			lblTues.setText(LocalDate.now().toString());
			lblWed.setText(LocalDate.now().plusDays(1).toString());
			lblThurs.setText(LocalDate.now().plusDays(2).toString());
			lblFri.setText(LocalDate.now().plusDays(3).toString());
			lblSat.setText(LocalDate.now().plusDays(4).toString());
			lblSun.setText(LocalDate.now().minusDays(2).toString());
			lblMon.setText(LocalDate.now().minusDays(1).toString());

		}
		else if(now == "WEDNESDAY"){
			
			lblWed.setText(LocalDate.now().toString());
			lblThurs.setText(LocalDate.now().plusDays(1).toString());
			lblFri.setText(LocalDate.now().plusDays(2).toString());
			lblSat.setText(LocalDate.now().plusDays(3).toString());
			lblSun.setText(LocalDate.now().minusDays(3).toString());
			lblMon.setText(LocalDate.now().minusDays(2).toString());
			lblTues.setText(LocalDate.now().minusDays(1).toString());

		}
		else if(now == "THURSDAY"){
			
			lblThurs.setText(LocalDate.now().toString());
			lblFri.setText(LocalDate.now().plusDays(1).toString());
			lblSat.setText(LocalDate.now().plusDays(2).toString());
			lblSun.setText(LocalDate.now().minusDays(4).toString());
			lblMon.setText(LocalDate.now().minusDays(3).toString());
			lblTues.setText(LocalDate.now().minusDays(2).toString());
			lblWed.setText(LocalDate.now().minusDays(1).toString());

		}
		else if(now == "FRIDAY"){
			
			lblFri.setText(LocalDate.now().toString());
			lblSat.setText(LocalDate.now().plusDays(1).toString());
			lblSun.setText(LocalDate.now().minusDays(5).toString());
			lblMon.setText(LocalDate.now().minusDays(4).toString());
			lblTues.setText(LocalDate.now().minusDays(3).toString());
			lblWed.setText(LocalDate.now().minusDays(2).toString());
			lblThurs.setText(LocalDate.now().minusDays(1).toString());

		}
		else if(now == "SATURDAY"){
			
			lblSat.setText(LocalDate.now().toString());
			lblSun.setText(LocalDate.now().minusDays(6).toString());
			lblMon.setText(LocalDate.now().minusDays(5).toString());
			lblTues.setText(LocalDate.now().minusDays(4).toString());
			lblWed.setText(LocalDate.now().minusDays(3).toString());
			lblThurs.setText(LocalDate.now().minusDays(2).toString());
			lblFri.setText(LocalDate.now().minusDays(1).toString());
		}
        
        //Loading each of the days with the employees scheduled that day

        
        parent1.getChildren().setAll(item_l2, item_l1);
		parent2.getChildren().setAll(item_r1, item_r2, item_r3);
		rootie.getChildren().setAll(parent1, parent2);
		
		treeTableMenuColumn.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<String, String>, ObservableValue<String>>(){
			@Override
			public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<String, String> param){
				return new SimpleStringProperty(param.getValue().getValue());
				
			}
		});
	
		treeTableMenu.setRoot(rootie);
		treeTableMenu.setShowRoot(false);
		parent1.setExpanded(true);
		parent2.setExpanded(true);
		rootP = root;
		treeTableMenu.getSelectionModel().select(item_l1);

		 treeTableMenu.getSelectionModel()
	        .selectedItemProperty()
	        .addListener((observable, oldValue, newValue) -> {
	        	if(newValue.getValue() == "Details"){
	        		BorderPane pane;
					try {
						pane = FXMLLoader.load(getClass().getResource("Main_Menu_Employee.fxml"));
						root.getChildren().setAll(pane);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
	        	}
	        	else if(newValue.getValue() == "Scheduler"){
	        		AnchorPane pane;
					try {
						pane = FXMLLoader.load(getClass().getResource("Employee_Shift_Scheduler.fxml"));
						root.getChildren().setAll(pane);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
	        	}
	        	else if(newValue.getValue() == "Attendance"){
	        		AnchorPane pane;
					try {
						pane = FXMLLoader.load(getClass().getResource("Menu_Customer.fxml"));
						root.getChildren().setAll(pane);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
	        	}
	        	else if(newValue.getValue() == "Bar Chart"){
	        		FXMLLoader loader = new FXMLLoader();
	    	        loader.setLocation(getClass().getResource("AMPM_Bar_Chart.fxml"));
	    	        try {
						loader.load();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	    	        Parent p = loader.getRoot();
	    	        Stage stage = new Stage();
	    	        stage.setScene(new Scene(p));
	    	        stage.setTitle("All Customer Attendance Data");
	    	        stage.show();
	        	}
	        	else if(newValue.getValue() == "Line Chart"){
	        		FXMLLoader loader = new FXMLLoader();
	    	        loader.setLocation(getClass().getResource("Customer_Attendance_Line_Chart.fxml"));
	    	        try {
						loader.load();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	    	        Parent p = loader.getRoot();
	    	        Stage stage = new Stage();
	    	        stage.setScene(new Scene(p));
	    	        stage.setTitle("Week Customer Attendance Data");
	    	        stage.show();
	        	}
	        }
	        );
        
    }
	
	//Method that is run when a date is chosen on the date picker
	@FXML
	public void setOnDatePickerChosen(Event event)
	{
		if(dtSchedule.getValue()!= null){
			if(validateDate()){
				String day_of_week = dtSchedule.getValue().getDayOfWeek().toString();
				if(day_of_week == "SUNDAY"){
					
					lblSun.setText(dtSchedule.getValue().toString());
					lblMon.setText(dtSchedule.getValue().plusDays(1).toString());
					lblTues.setText(dtSchedule.getValue().plusDays(2).toString());
					lblWed.setText(dtSchedule.getValue().plusDays(3).toString());
					lblThurs.setText(dtSchedule.getValue().plusDays(4).toString());
					lblFri.setText(dtSchedule.getValue().plusDays(5).toString());
					lblSat.setText(dtSchedule.getValue().plusDays(6).toString());

				}
				else if(day_of_week == "MONDAY"){
					
					lblMon.setText(dtSchedule.getValue().toString());
					lblTues.setText(dtSchedule.getValue().plusDays(1).toString());
					lblWed.setText(dtSchedule.getValue().plusDays(2).toString());
					lblThurs.setText(dtSchedule.getValue().plusDays(3).toString());
					lblFri.setText(dtSchedule.getValue().plusDays(4).toString());
					lblSat.setText(dtSchedule.getValue().plusDays(5).toString());
					lblSun.setText(dtSchedule.getValue().minusDays(1).toString());

				}
				else if(day_of_week == "TUESDAY"){
					
					lblTues.setText(dtSchedule.getValue().toString());
					lblWed.setText(dtSchedule.getValue().plusDays(1).toString());
					lblThurs.setText(dtSchedule.getValue().plusDays(2).toString());
					lblFri.setText(dtSchedule.getValue().plusDays(3).toString());
					lblSat.setText(dtSchedule.getValue().plusDays(4).toString());
					lblSun.setText(dtSchedule.getValue().minusDays(2).toString());
					lblMon.setText(dtSchedule.getValue().minusDays(1).toString());

				}
				else if(day_of_week == "WEDNESDAY"){
					
					lblWed.setText(dtSchedule.getValue().toString());
					lblThurs.setText(dtSchedule.getValue().plusDays(1).toString());
					lblFri.setText(dtSchedule.getValue().plusDays(2).toString());
					lblSat.setText(dtSchedule.getValue().plusDays(3).toString());
					lblSun.setText(dtSchedule.getValue().minusDays(3).toString());
					lblMon.setText(dtSchedule.getValue().minusDays(2).toString());
					lblTues.setText(dtSchedule.getValue().minusDays(1).toString());

				}
				else if(day_of_week == "THURSDAY"){
					
					lblThurs.setText(dtSchedule.getValue().toString());
					lblFri.setText(dtSchedule.getValue().plusDays(1).toString());
					lblSat.setText(dtSchedule.getValue().plusDays(2).toString());
					lblSun.setText(dtSchedule.getValue().minusDays(4).toString());
					lblMon.setText(dtSchedule.getValue().minusDays(3).toString());
					lblTues.setText(dtSchedule.getValue().minusDays(2).toString());
					lblWed.setText(dtSchedule.getValue().minusDays(1).toString());

				}
				else if(day_of_week == "FRIDAY"){
					
					lblFri.setText(dtSchedule.getValue().toString());
					lblSat.setText(dtSchedule.getValue().plusDays(1).toString());
					lblSun.setText(dtSchedule.getValue().minusDays(5).toString());
					lblMon.setText(dtSchedule.getValue().minusDays(4).toString());
					lblTues.setText(dtSchedule.getValue().minusDays(3).toString());
					lblWed.setText(dtSchedule.getValue().minusDays(2).toString());
					lblThurs.setText(dtSchedule.getValue().minusDays(1).toString());

				}
				else if(day_of_week == "SATURDAY"){
					
					lblSat.setText(dtSchedule.getValue().toString());
					lblSun.setText(dtSchedule.getValue().minusDays(6).toString());
					lblMon.setText(dtSchedule.getValue().minusDays(5).toString());
					lblTues.setText(dtSchedule.getValue().minusDays(4).toString());
					lblWed.setText(dtSchedule.getValue().minusDays(3).toString());
					lblThurs.setText(dtSchedule.getValue().minusDays(2).toString());
					lblFri.setText(dtSchedule.getValue().minusDays(1).toString());

				}
				
				 //Loads in employee information based on given date
				
				
			}
		}		
	}
	
	@FXML 
	private void setAllEnable(){
	
	        btAdd_Employee.setDisable(false);
	        TableEmployees.setDisable(false);
	        btDelete.setDisable(false);
	}
	
	//Based on what day is chosen, the respective chooseEmp__ is called which shows to the user that the day is selected to schedule
	@FXML 
	private void chooseEmpSun(Event event){
		 	setAllEnable();
		 	chosen = lblSun;
		 	chosenlist = listSun;
		 	chosenlist.setStyle("-fx-background-color: #3c3c3c;");
		 	
		 	listMon.setStyle("-fx-background-color: white;");
		 	listTues.setStyle("-fx-background-color: white;");
			listWed.setStyle("-fx-background-color: white;");
			listThurs.setStyle("-fx-background-color: white;");
			listFri.setStyle("-fx-background-color: white;");
			listSat.setStyle("-fx-background-color: white;");
			
	}
	
	@FXML 
	private void chooseEmpMon(Event event){
		 	setAllEnable();
		 	chosen = lblMon;
		 	chosenlist = listMon;
		 	chosenlist.setStyle("-fx-background-color: #3c3c3c;");
		 	
		 	listSun.setStyle("-fx-background-color: white;");
		 	listTues.setStyle("-fx-background-color: white;");
			listWed.setStyle("-fx-background-color: white;");
			listThurs.setStyle("-fx-background-color: white;");
			listFri.setStyle("-fx-background-color: white;");
			listSat.setStyle("-fx-background-color: white;");
	}
	
	@FXML 
	private void chooseEmpTues(Event event){
		 	setAllEnable();
		 	chosen = lblTues;
		 	chosenlist = listTues;
		 	chosenlist.setStyle("-fx-background-color: #3c3c3c;");
		 	
		 	listMon.setStyle("-fx-background-color: white;");
		 	listSun.setStyle("-fx-background-color: white;");
			listWed.setStyle("-fx-background-color: white;");
			listThurs.setStyle("-fx-background-color: white;");
			listFri.setStyle("-fx-background-color: white;");
			listSat.setStyle("-fx-background-color: white;");
	}
	
	@FXML 
	private void chooseEmpWed(Event event){
		 	setAllEnable();
		 	chosen = lblWed;
		 	chosenlist = listWed;
		 	chosenlist.setStyle("-fx-background-color: #3c3c3c;");
		 	
		 	listMon.setStyle("-fx-background-color: white;");
		 	listTues.setStyle("-fx-background-color: white;");
			listSun.setStyle("-fx-background-color: white;");
			listThurs.setStyle("-fx-background-color: white;");
			listFri.setStyle("-fx-background-color: white;");
			listSat.setStyle("-fx-background-color: white;");
	}
	
	@FXML 
	private void chooseEmpThurs(Event event){
		 	setAllEnable();
		 	chosen = lblThurs;
		 	chosenlist = listThurs;
		 	chosenlist.setStyle("-fx-background-color: #3c3c3c;");
		 	
		 	listMon.setStyle("-fx-background-color: white;");
		 	listTues.setStyle("-fx-background-color: white;");
			listWed.setStyle("-fx-background-color: white;");
			listSun.setStyle("-fx-background-color: white;");
			listFri.setStyle("-fx-background-color: white;");
			listSat.setStyle("-fx-background-color: white;");
	}
	
	@FXML 
	private void chooseEmpFri(Event event){
		 	setAllEnable();
		 	chosen = lblFri;
		 	chosenlist = listFri;
		 	chosenlist.setStyle("-fx-background-color: #3c3c3c;");
		 	
		 	listMon.setStyle("-fx-background-color: white;");
		 	listTues.setStyle("-fx-background-color: white;");
			listWed.setStyle("-fx-background-color: white;");
			listThurs.setStyle("-fx-background-color: white;");
			listSun.setStyle("-fx-background-color: white;");
			listSat.setStyle("-fx-background-color: white;");
	}
	
	@FXML 
	private void chooseEmpSat(Event event){
		 	setAllEnable();
		 	chosen = lblSat;
		 	chosenlist = listSat;
		 	chosenlist.setStyle("-fx-background-color: #3c3c3c;");
		 	
		 	listMon.setStyle("-fx-background-color: white;");
		 	listTues.setStyle("-fx-background-color: white;");
			listWed.setStyle("-fx-background-color: white;");
			listThurs.setStyle("-fx-background-color: white;");
			listFri.setStyle("-fx-background-color: white;");
			listSun.setStyle("-fx-background-color: white;");
	}
	
	//Adds an employee to the schedule if it was selected from bottom table
	@FXML 
	private void addEmployeeClicked(Event event){
		
        if(TableEmployees.getSelectionModel().getSelectedItem()!=null && chosenlist != null) {
        		int count = 0;
        		Employee_Shift_SchedulerModel getSelectedRow = TableEmployees.getSelectionModel().getSelectedItem();
	        	String sqlQuery = "select * FROM Employees_Schedule where ID = "+getSelectedRow.getEmployeesID()+" AND Date = '"+chosen.getText()+"';";
	        	String sqlQuery1 = "select * FROM Employees where ID = "+getSelectedRow.getEmployeesID()+";";
	        	 
	        	try{
	        		connection = SqliteConnection.Connector();
			        statement = connection.createStatement();
		            resultSet = statement.executeQuery(sqlQuery);
		            
		            while(resultSet.next()){
		            	count++;
		            }
		            resultSet.close();
		            resultSet = statement.executeQuery(sqlQuery1);

		            if(count == 0){
		            	 int rowsAffected = statement.executeUpdate("insert into `Employees_Schedule` " +
		            	
	                     "(`ID`,`Date`, `AM/PM`)"+
	                     "values ("+resultSet.getString("ID")+",'"+chosen.getText()+"','" + "AM" + "'"
	                    
	                     +");");
		            }
		            else{
		            	NotificationType notificationType = NotificationType.ERROR;
			            TrayNotification tray = new TrayNotification();
			            tray.setTitle("Employee Repeat");
			            tray.setMessage("This employee is already working this day");
			            tray.setNotificationType(notificationType);
			            tray.showAndDismiss(Duration.millis(5000));
		            }
		            
		            
		            statement.close();
		            resultSet.close();
		            connection.close();
		        
	        	}
	        	catch (SQLException e) {
		            e.printStackTrace();
		            
		        }
	               	 
	            
        }
        else if(chosenlist == null){
        	 Alert alert = new Alert(AlertType.WARNING);
			 alert.setTitle("No date selected");
			 alert.setHeaderText(null);
			 alert.setContentText("Please select a date by clicking on the list below the desired date on the weekly schedule");
			 alert.showAndWait();
        }
        else{
        	    NotificationType notificationType = NotificationType.ERROR;
	            TrayNotification tray = new TrayNotification();
	            tray.setTitle("No Employee Selected");
	            tray.setMessage("Please select an Employee");
	            tray.setNotificationType(notificationType);
	            tray.showAndDismiss(Duration.millis(5000));
        }
        
        
	}
	

	

    //Method to print the schedule
    @FXML  
    private void doPrint(Event event) throws InvocationTargetException{
	   Printer printer = Printer.getDefaultPrinter();
	   PrinterJob job = PrinterJob.createPrinterJob();
	   
	   if(printer != null){
		   PageLayout pageLayout = printer.createPageLayout(Paper.A4, PageOrientation.LANDSCAPE, Printer.MarginType.DEFAULT);
		   double scaleX
	       = pageLayout.getPrintableWidth() / grSchedule.getBoundsInParent().getWidth();
		   double scaleY
	       = pageLayout.getPrintableHeight() / grSchedule.getBoundsInParent().getHeight();
		   Scale scale = new Scale(scaleX, scaleY);
	  	   grSchedule.getTransforms().add(scale);
	  	   
	  	 if(job.printPage(pageLayout, grSchedule)){
	        	job.endJob();
	        	grSchedule.getTransforms().remove(scale);        	
	    	}
	    	else{
	    		NotificationType notificationType = NotificationType.ERROR;
	            TrayNotification tray = new TrayNotification();
	            tray.setTitle("Printing error");
	            tray.setMessage("Try turning on your printers");
	            tray.setNotificationType(notificationType);
	            tray.showAndDismiss(Duration.millis(10000));
	    	}
	   }
	   else if(printer == null){
    		//tray notification printer not found
    		NotificationType notificationType = NotificationType.ERROR;
            TrayNotification tray = new TrayNotification();
            tray.setTitle("Printer not found");
            tray.setMessage("Please set default printer/turn printer on");
            tray.setNotificationType(notificationType);
            tray.showAndDismiss(Duration.millis(10000));
    		
    	}
    	
    }
   
   //Method called when user wants to delete an employee schedule on a certain day
   @FXML
   private void schedulerDelete(Event event){
	 	if(chosenlist.getSelectionModel().getSelectedItem()!=null){
	 		String getSelectedRow = chosenlist.getSelectionModel().getSelectedItem().substring(0,1);
	 		String sqlQuery = "delete FROM Employees_Schedule where ID = "+getSelectedRow+" AND Date = '"+chosen.getText()+"';";
	        try {
	        	connection = SqliteConnection.Connector();
		        statement = connection.createStatement();
	             
	             statement.executeUpdate(sqlQuery);
	             
	        	 
	             
	            
	             statement.close();
	             connection.close();

	        }
	        catch (SQLException e) {
	            e.printStackTrace();
	        }


	        
	 	}
	 	else{
	 		NotificationType notificationType = NotificationType.ERROR;
	 		TrayNotification tray = new TrayNotification();
	 		tray.setTitle("No Date Selected");
	 		tray.setMessage("To delete, please select from weekly schedule list");
	 		tray.setNotificationType(notificationType);
	 		tray.showAndDismiss(Duration.millis(25000));
	 	}        
   }
    
    //Following methods launch the other screens for various parts of the program
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
   	private void launchCustomerScreen(Event event) throws IOException{
	 	((Node)event.getSource()).getScene().getWindow().hide();
	 	Parent CustomerScreen = FXMLLoader.load(getClass().getResource("Menu_Customer.fxml"));
	 	Scene customer_screen = new Scene(CustomerScreen);
	 	Stage Customer_Screen = (Stage) ((Node) event.getSource()).getScene().getWindow();
	 	Customer_Screen.hide();
	 	Customer_Screen.setScene(customer_screen);
	 	Customer_Screen.setTitle("Customer Screen");
	 	Customer_Screen.show();
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
   
   //Re-loads in the employees to the employee table
   @FXML
   private void setRefreshButtonClick(Event event){
       TableEmployees.setItems(Scheduler_Table.getDataFromSqlAndAddToObservableList("SELECT * FROM Employees;"));//sql Query
       txtSearch.clear();
   }
   
   //Searches for a customer
   @FXML
   private void setSearchButtonClick(Event event){
       String sqlQuery = "select * FROM Employees where ID = '"+txtSearch.getText()+"';";
       TableEmployees.setItems(Scheduler_Table.getDataFromSqlAndAddToObservableList(sqlQuery));
   }
   
   //Gives an alert if a user tries to schedule without selecting a date
   private boolean validateDate(){
		 if(dtSchedule.getValue() != null){
			 return true;
		 }
		 else{
			 Alert alert = new Alert(AlertType.WARNING);
			 alert.setTitle("Validate Date");
			 alert.setHeaderText(null);
			 alert.setContentText("Please Choose a Date");
			 alert.showAndWait();
			 
			 return false;
		 }
		 
	 }  
}