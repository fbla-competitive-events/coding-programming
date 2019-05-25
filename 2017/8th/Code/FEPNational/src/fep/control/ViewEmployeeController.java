package fep.control;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;
import java.util.function.Predicate;

import fep.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.print.PrinterJob;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import table.Employee;

public class ViewEmployeeController implements Initializable {
	/**
	 * @author Aatif Jiwani
	 * @version 3.0
	 * 
	 * This is a controller class. This class handles all the actions of the View employees window. 
	 */

	/**
	 * !IMPORTANT!
	 * All Try and Catches are meant to notify the user of their mistake if their input
	 * is not proper. 
	 */
	
	
	/*Pre-defined global variables
	 * Each of these variables are used in at least one method in this class. 
	 */
	public LoginModel loginModel = new LoginModel();

	public TableView<Employee> tableEmployee;
	public TableColumn<Employee, String> name;
	public TableColumn<Employee, String> id;
	public TableColumn<Employee, String> position;
	public TableColumn<Employee, String> partFull;
	public TableColumn<Employee, String> address;
	public TableColumn<Employee, String> city;
	public TableColumn<Employee, String> state;
	public TableColumn<Employee, String> postal;
	public TableColumn<Employee, String> birthdate;
	public TableColumn<Employee, String> hiredate;

	public Label print;
	public Label status;

	public TextField filterField;

	public ObservableList<Employee> list = FXCollections.observableArrayList();

	public FilteredList<Employee> filterData = new FilteredList<>(list, e -> true);

	@Override
	/**
	 * 
	 * This method populates the table with the current employees of the company.
	 */
	public void initialize(URL location, ResourceBundle resources) {
		try {
			print.setVisible(false);

			//The following code creates a row using the Employee class
			/**
			 * @see package table
			 */
			String query = "select * from Employees";
			PreparedStatement pst = loginModel.connection.prepareStatement(query);
			ResultSet rs = pst.executeQuery();
			while (rs.next()) {
				list.add(new Employee(rs.getString("Name"), rs.getString("ID"), rs.getString("Position"),
						rs.getString("PartFull"), rs.getString("Address"), rs.getString("City"), rs.getString("State"),
						rs.getString("Postal"), rs.getString("Birthdate"), rs.getString("HireDate")));
			}

			//Establishes connection between column and table class attribute

			name.setCellValueFactory(new PropertyValueFactory<Employee, String>("name"));
			id.setCellValueFactory(new PropertyValueFactory<Employee, String>("id"));
			position.setCellValueFactory(new PropertyValueFactory<Employee, String>("position"));
			partFull.setCellValueFactory(new PropertyValueFactory<Employee, String>("partFull"));
			address.setCellValueFactory(new PropertyValueFactory<Employee, String>("address"));
			city.setCellValueFactory(new PropertyValueFactory<Employee, String>("city"));
			state.setCellValueFactory(new PropertyValueFactory<Employee, String>("state"));
			postal.setCellValueFactory(new PropertyValueFactory<Employee, String>("postal"));
			birthdate.setCellValueFactory(new PropertyValueFactory<Employee, String>("birthdate"));
			hiredate.setCellValueFactory(new PropertyValueFactory<Employee, String>("hiredate"));
			tableEmployee.setItems(list);
			
			pst.close();
			rs.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * 
	 * @param event
	 * @throws IOException
	 * 
	 * This method launches the Page Setup dialogue
	 */
	public void btnPrint(ActionEvent event) throws IOException {

		PrinterJob job = PrinterJob.createPrinterJob();
		tableEmployee.setScaleX(0.80);
		tableEmployee.setScaleY(0.80);
		WritableImage snapshot = tableEmployee.snapshot(null, null);
		ImageView iv = new ImageView(snapshot);
		//If any printer exists...
		if (job != null) {
			if (job.showPageSetupDialog(tableEmployee.getScene().getWindow())) {
				print.setVisible(true);
				//tableEmployee.setScaleX(0.80);
				//tableEmployee.setScaleY(0.80);
				status.setText("Printing page...");
				boolean success = job.printPage(iv);
				if (success) {
					status.setText("Print successful");
					print.setVisible(false);
					job.endJob();
					tableEmployee.setScaleX(1.0);
					tableEmployee.setScaleY(1.0);
				}
			}
		//If no printers exist...
		} else if (job == null) {
			status.setText("No printers installed!");
			System.out.println("hello");
		}
	}

	/**
	 * If keyboard button is pressed for the filtering, the table will automatically
	 * re-populate with the related values. 
	 * 
	 * @throws IOException
	 * 
	 * 
	 */
	public void filterReleased() throws IOException {
		filterField.textProperty().addListener((observableValue, oldValue, newValue) -> {
			filterData.setPredicate((Predicate<? super Employee>) employee -> {
				if (newValue == null || newValue.isEmpty()) {
					return true;
				}
				String lowerCaseFilter = newValue.toLowerCase();
				if (employee.getName().toLowerCase().contains(newValue)) {
					return true;
				}
				return false;
			});
		});
		SortedList<Employee> sortedData = new SortedList<>(filterData);
		sortedData.comparatorProperty().bind(tableEmployee.comparatorProperty());
		tableEmployee.setItems(sortedData);
	}

	/**
	 * 
	 * @param event
	 * @throws IOException
	 * 
	 * This method directs the user to the Main Menu while also hiding/exiting current window. 
	 */
	public void btnMenu(ActionEvent event) throws IOException {
		status.setText("Loading window");
		Stage primaryStage = new Stage();
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(Main.class.getResource("/fep/MenuView.fxml"));
		AnchorPane mainLayout = loader.load();
		Scene scene = new Scene(mainLayout);
		primaryStage.setScene(scene);
		primaryStage.show();
		((Node) event.getSource()).getScene().getWindow().hide();

	}

}
