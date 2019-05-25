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
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import table.Customer;

public class ViewCustomerController implements Initializable {
	/**
	 * @author Aatif Jiwani
	 * @version 3.0
	 * 
	 * This is a controller class. This class handles all the actions of the View customer attendance window. 
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


	public TableView<Customer> tableCustomer;
	public TableColumn<Customer, String> time;
	public TableColumn<Customer, String> sunday;
	public TableColumn<Customer, String> monday;
	public TableColumn<Customer, String> tuesday;
	public TableColumn<Customer, String> wednesday;
	public TableColumn<Customer, String> thursday;
	public TableColumn<Customer, String> friday;
	public TableColumn<Customer, String> saturday;

	public ComboBox<String> year;
	public ComboBox<String> weekNumber;

	public Label print;
	public Label status;
	public Label weekStart;
	public Label weekEnd;

	public TextField filterField;

	public ObservableList<Customer> list = FXCollections.observableArrayList();
	ObservableList<String> listYear = FXCollections.observableArrayList();
	ObservableList<String> listWeek = FXCollections.observableArrayList();
	public FilteredList<Customer> filterData = new FilteredList<>(list, e -> true);

	/**
	 * The following code populates the Year and Week databases with the proper values
	 */
	public void initialize(URL location, ResourceBundle resources) {
		print.setVisible(false);
		try {
			String query = "select * from Years";
			PreparedStatement pst = loginModel.connection.prepareStatement(query);
			ResultSet rs = pst.executeQuery();

			int counter = 0;
			String first = "";
			while (rs.next()) {
				if (counter == 0) {
					first = rs.getString("Year");
					listYear.add(first);
				} else {
					listYear.add(rs.getString("Year"));
				}
				counter++;
			}

			year.setItems(listYear);
			year.setValue(first);
			pst.close();
			rs.close();

			String query2 = "select * from '" + year.getValue().trim() + "Weeks'";
			PreparedStatement pst2 = loginModel.connection.prepareStatement(query2);
			ResultSet rs2 = pst2.executeQuery();

			int counterWeek = 0;
			String firstWeek = "";
			while (rs2.next()) {
				if (counterWeek == 0) {
					String temp = rs2.getString("WeekNum");
					firstWeek = temp.substring(4, 8) + " " + temp.substring(8);

					listWeek.add(firstWeek);
				} else {
					String temp = rs2.getString("WeekNum");
					listWeek.add(temp.substring(4, 8) + " " + temp.substring(8));
				}
				counter++;
			}
			weekNumber.setItems(listWeek);
			weekNumber.setValue(firstWeek);
			pst2.close();
			rs2.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
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
	
	/**
	 * 
	 * @param event
	 * @throws IOException
	 * 
	 * This method launches the Page Setup dialogue
	 */
	public void btnPrint(ActionEvent event) throws IOException {
		PrinterJob job = PrinterJob.createPrinterJob();
		WritableImage snapshot = tableCustomer.snapshot(null, null);
		ImageView iv = new ImageView(snapshot);
		//If printer exists..
		if (job != null) {
			if (job.showPageSetupDialog(tableCustomer.getScene().getWindow())) {
				status.setText("Printing page...");
				boolean success = job.printPage(iv);
				if (success) {
					status.setText("Print successful");
					job.endJob();
				}
				
			}
		//If no printer exists..
		} else if (job == null) {
			status.setText("No printers installed!");
			System.out.println("hello");
		}
	}

	/**
	 * 
	 * @param event
	 * @throws IOException
	 * 
	 * This method populates the table with the customer attendances of the selected week 
	 * of the given year. 
	 */
	public void btnView(ActionEvent event) throws IOException {
		list = FXCollections.observableArrayList();
		filterData = new FilteredList<>(list, e -> true);
		String comboYear = year.getValue();
		String textWeek = weekNumber.getValue();
		try {
			//The following lines of code update the week start/end values
			String query2 = "select * from '" + comboYear.trim() + textWeek.substring(0, 4)
					+ textWeek.substring(5) + "' where rowid='1'";
			PreparedStatement pst2 = loginModel.connection.prepareStatement(query2);
			ResultSet rs2 = pst2.executeQuery();
			weekStart.setText(rs2.getString("WeekStart"));
			weekEnd.setText(rs2.getString("WeekEnd"));
			pst2.close();
			rs2.close();
			if (weekStart.getText().isEmpty() || weekEnd.getText().isEmpty()) {
				weekStart.setText("EMPTY");
				weekEnd.setText("EMPTY");
			}

			//The following code creates a row using the Customer class
			/**
			 * @see package table
			 */
			String query = "select * from '" + comboYear.trim() + "Customer" + textWeek.substring(0, 4) + textWeek.substring(5)
					+ "'";
			PreparedStatement pst = loginModel.connection.prepareStatement(query);
			ResultSet rs = pst.executeQuery();
			while (rs.next()) {
				list.add(new Customer(rs.getString("TimeOfDay"), rs.getString("Sunday"), rs.getString("Monday"),
						rs.getString("Tuesday"), rs.getString("Wednesday"), rs.getString("Thursday"), rs.getString("Friday"),
						rs.getString("Saturday")));
			}
			pst.close();
			rs.close();

			//Establishes connection between column and table class attribute
			time.setCellValueFactory(new PropertyValueFactory<Customer, String>("time"));
			sunday.setCellValueFactory(new PropertyValueFactory<Customer, String>("sunday"));
			monday.setCellValueFactory(new PropertyValueFactory<Customer, String>("monday"));
			tuesday.setCellValueFactory(new PropertyValueFactory<Customer, String>("tuesday"));
			wednesday.setCellValueFactory(new PropertyValueFactory<Customer, String>("wednesday"));
			thursday.setCellValueFactory(new PropertyValueFactory<Customer, String>("thursday"));
			friday.setCellValueFactory(new PropertyValueFactory<Customer, String>("friday"));
			saturday.setCellValueFactory(new PropertyValueFactory<Customer, String>("saturday"));
			tableCustomer.setItems(list);
			
			status.setText("Attendance displayed!");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * This method populates the Week combobox with the proper values once a new year is selected. 
	 * The method exists because every year may have a different amount of weekly schedules created at
	 * any given time. 
	 */
	public void comboYearchange(ActionEvent event) throws IOException {
		listWeek = FXCollections.observableArrayList();
		try {
			String query2 = "select * from '" + year.getValue().trim() + "Weeks'";
			PreparedStatement pst2 = loginModel.connection.prepareStatement(query2);
			ResultSet rs2 = pst2.executeQuery();
			int counterWeek = 0;
			String firstWeek = "";
			while (rs2.next()) {
				if (counterWeek == 0) {
					String temp = rs2.getString("WeekNum");
					firstWeek = temp.substring(4, 8) + " " + temp.substring(8);

					listWeek.add(firstWeek);
				} else {
					String temp = rs2.getString("WeekNum");
					listWeek.add(temp.substring(4, 8) + " " + temp.substring(8));
				}
				counterWeek++;
			}
			weekNumber.setItems(listWeek);
			weekNumber.setValue(firstWeek);
			pst2.close();
			rs2.close();
		} catch (Exception e) {
			e.printStackTrace();
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
			filterData.setPredicate((Predicate<? super Customer>) customer -> {
				if (newValue == null || newValue.isEmpty()) {
					return true;
				}
				String lowerCaseFilter = newValue.toLowerCase();
				if (customer.getTime().toLowerCase().contains(newValue)) {
					return true;
				}
				return false;
			});
		});
		SortedList<Customer> sortedData = new SortedList<>(filterData);
		sortedData.comparatorProperty().bind(tableCustomer.comparatorProperty());
		tableCustomer.setItems(sortedData);
	}

}
