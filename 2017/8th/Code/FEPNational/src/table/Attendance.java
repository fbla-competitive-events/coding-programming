package table;

import javafx.beans.property.SimpleStringProperty;


public class Attendance {
	
	/**
	 * @author Aatif Jiwani
	 * @version 3.0
	 */

	/**
	 * !IMPORTANT!
	 * The following code is used for the Table Views in the program. 
	 * JavaFX tables require information to be stored in an object so therefore,
	 * information for each employee's weekly schedule is inserted in an instance
	 * of this object. 
	 * 
	 * Refer to the "ViewAttendController.java" and "ViewScheduleController.java" for further clarification.
	 */


	/*Pre-defined global variables
	 * Each of these variables are used in at least one method in this class. 
	 */
	
	private final SimpleStringProperty name;
	private final SimpleStringProperty id;
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
	public Attendance(String name, String id, String sunday, String monday, String tuesday, String wednesday,
			String thursday, String friday, String saturday) {
		super();
		this.name = new SimpleStringProperty(name);
		this.id = new SimpleStringProperty(id);
		this.sunday = new SimpleStringProperty(sunday);
		this.monday = new SimpleStringProperty(monday);
		this.tuesday = new SimpleStringProperty(tuesday);
		this.wednesday = new SimpleStringProperty(wednesday);
		this.thursday = new SimpleStringProperty(thursday);
		this.friday = new SimpleStringProperty(friday);
		this.saturday = new SimpleStringProperty(saturday);
	}

	
	/*
	 * The following methods are variable getters
	 */
	
	public String getName() {
		return name.get();
	}

	public String getId() {
		return id.get();
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
