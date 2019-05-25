package table;

import javafx.beans.property.SimpleStringProperty;

public class Customer {
	
	/**
	 * @author Aatif Jiwani
	 * @version 3.0
	 */

	/**
	 * !IMPORTANT!
	 * The following code is used for the Table Views in the program. 
	 * JavaFX tables require information to be stored in an object so therefore,
	 * information for each week's customer attendance is inserted in an instance
	 * of this object. 
	 * 
	 * Refer to the "ViewCustomerController.java" for further clarification.
	 */


	/*Pre-defined global variables
	 * Each of these variables are used in at least one method in this class. 
	 */
	private final SimpleStringProperty time;
	private final SimpleStringProperty sunday;
	private final SimpleStringProperty monday;
	private final SimpleStringProperty tuesday;
	private final SimpleStringProperty wednesday;
	private final SimpleStringProperty thursday;
	private final SimpleStringProperty friday;
	private final SimpleStringProperty saturday;
	
	/*
	 * Class constructor
	 * These act as the class's setter
	 */
	public Customer(String time, String sunday, String monday, String tuesday, String wednesday, String thursday,
			String friday, String saturday) {
		super();
		this.time = new SimpleStringProperty(time);
		this.sunday = new SimpleStringProperty(sunday);
		this.monday = new SimpleStringProperty(monday);
		this.tuesday = new SimpleStringProperty(tuesday);
		this.wednesday = new SimpleStringProperty(wednesday);
		this.thursday = new SimpleStringProperty(thursday);
		this.friday = new SimpleStringProperty(friday);
		this.saturday = new SimpleStringProperty(saturday);
	}
	
	/*
	 * The following methods are class getters. 
	 */

	public String getTime() {
		return time.get();
	}

	public String getSunday() {
		return sunday.get();
	}

	public String getMonday() {
		return monday.get();
	}

	public String getTuesday() {
		return tuesday.get();
	}

	public String getWednesday() {
		return wednesday.get();
	}

	public String getThursday() {
		return thursday.get();
	}

	public String getFriday() {
		return friday.get();
	}

	public String getSaturday() {
		return saturday.get();
	}
	
	
}
