package fep.control;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.Period;
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
import table.Member;

public class ViewMemberController implements Initializable{

	/**
	 * @author Aatif Jiwani
	 * @version 3.0
	 * 
	 * This is a controller class. This class handles all the actions of the View member window. 
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

	public TableView<Member> tableMember;
	public TableColumn<Member, String> name;
	public TableColumn<Member, String> phoneNumber;
	public TableColumn<Member, String> email;
	public TableColumn<Member, String> under18;
	public TableColumn<Member, String> address;
	public TableColumn<Member, String> city;
	public TableColumn<Member, String> state;
	public TableColumn<Member, String> postal;
	public TableColumn<Member, String> birthdate;
	public TableColumn<Member, String> joindate;

	public Label print;
	public Label status;

	public TextField filterField;

	public ObservableList<Member> list = FXCollections.observableArrayList();

	public FilteredList<Member> filterData = new FilteredList<>(list, e -> true);

	@Override
	/**
	 * 
	 * This method populates the table with the current members of the company.
	 */
	public void initialize(URL location, ResourceBundle resources) {
		try {
			print.setVisible(false);

			//The following code creates a row using the Member class
			/**
			 * @see package table
			 */
			String query = "select * from Membership";
			PreparedStatement pst = loginModel.connection.prepareStatement(query);
			ResultSet rs = pst.executeQuery();
			while (rs.next()) {
				list.add(new Member(rs.getString("Name"), rs.getString("PhoneNumber"), rs.getString("Email"),
						check18(rs.getString("Birthdate")), rs.getString("Address"), rs.getString("City"), rs.getString("State"),
						rs.getString("Postal"), rs.getString("Birthdate"), rs.getString("JoinDate")));
			}

			//Establishes connection between column and table class attribute

			name.setCellValueFactory(new PropertyValueFactory<Member, String>("name"));
			phoneNumber.setCellValueFactory(new PropertyValueFactory<Member, String>("phoneNumber"));
			email.setCellValueFactory(new PropertyValueFactory<Member, String>("email"));
			under18.setCellValueFactory(new PropertyValueFactory<Member, String>("under18"));
			address.setCellValueFactory(new PropertyValueFactory<Member, String>("address"));
			city.setCellValueFactory(new PropertyValueFactory<Member, String>("city"));
			state.setCellValueFactory(new PropertyValueFactory<Member, String>("state"));
			postal.setCellValueFactory(new PropertyValueFactory<Member, String>("postal"));
			birthdate.setCellValueFactory(new PropertyValueFactory<Member, String>("birthdate"));
			joindate.setCellValueFactory(new PropertyValueFactory<Member, String>("joindate"));
			tableMember.setItems(list);

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
		tableMember.setScaleX(0.70);
		tableMember.setScaleY(0.70);
		WritableImage snapshot = tableMember.snapshot(null, null);
		ImageView iv = new ImageView(snapshot);
		//If any printer exists...
		if (job != null) {
			if (job.showPageSetupDialog(tableMember.getScene().getWindow())) {
				print.setVisible(true);
				//tableMember.setScaleX(0.75);
				//tableMember.setScaleY(0.75);
				status.setText("Printing page...");
				boolean success = job.printPage(iv);
				if (success) {
					status.setText("Print successful");
					print.setVisible(false);
					job.endJob();
					tableMember.setScaleX(1.0);
					tableMember.setScaleY(1.0);
				}
			}
		//If no printers exist...
		} else if (job == null) {
			status.setText("No printers installed!");
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
			filterData.setPredicate((Predicate<? super Member>) member -> {
				if (newValue == null || newValue.isEmpty()) {
					return true;
				}
				String lowerCaseFilter = newValue.toLowerCase();
				if (member.getName().toLowerCase().contains(newValue)) {
					return true;
				}
				return false;
			});
		});
		SortedList<Member> sortedData = new SortedList<>(filterData);
		sortedData.comparatorProperty().bind(tableMember.comparatorProperty());
		tableMember.setItems(sortedData);
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
	 * This method checks whether the associated member is under or older than 18.
	 * @param s String of member's birthdate
	 * @return A String stating whether they are older or younger than 18. 
	 */
	public String check18(String s){
		LocalDate birthdate = dateReturn(s);
        LocalDate now = LocalDate.now();
        Period p = Period.between(birthdate, now);
        
        if (p.getYears() < 18){
        	return "Under 18";
        } else {
        	return "18 and older";
        }
	}
	
	/**
	 * 
	 * @param s String returned from the database
	 * @return LocalDate value of the associated String 
	 */
	public LocalDate dateReturn(String s) {
		int month = Integer.parseInt(s.substring(0, s.indexOf("/")));
		int first = s.indexOf("/"), second = s.indexOf("/", first + 1);
		int day = Integer.parseInt(s.substring(first + 1, second));
		first = second;
		int year = Integer.parseInt(s.substring(first + 1));

		return LocalDate.of(year, month, day);
	}

}
	


