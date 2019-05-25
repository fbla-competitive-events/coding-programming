package table;

import javafx.beans.property.SimpleStringProperty;

public class Employee {
	
	/**
	 * @author Aatif Jiwani
	 * @version 3.0
	 */

	/**
	 * !IMPORTANT!
	 * The following code is used for the Table Views in the program. 
	 * JavaFX tables require information to be stored in an object so therefore,
	 * information for each employee's credentials is inserted in an instance
	 * of this object. 
	 * 
	 * Refer to the "ViewEmployeeController.java" for further clarification.
	 */


	/*Pre-defined global variables
	 * Each of these variables are used in at least one method in this class. 
	 */
	private final SimpleStringProperty name;
	private final SimpleStringProperty id;
	private final SimpleStringProperty position;
	private final SimpleStringProperty partFull;
	private final SimpleStringProperty address;
	private final SimpleStringProperty city;
	private final SimpleStringProperty state;
	private final SimpleStringProperty postal;
	private final SimpleStringProperty birthdate;
	private final SimpleStringProperty hiredate;
	
	/*
	 * Class constructor
	 * These act as the class's setters 
	 */
	public Employee(String name, String id, String position, String partFull, String address, String city, String state,
			String postal, String birthdate, String hiredate) {
		super();
		this.name = new SimpleStringProperty(name);
		this.id = new SimpleStringProperty(id);
		this.position = new SimpleStringProperty(position);
		this.partFull = new SimpleStringProperty(partFull);
		this.address = new SimpleStringProperty(address);
		this.city = new SimpleStringProperty(city);
		this.state = new SimpleStringProperty(state);
		this.postal = new SimpleStringProperty(postal);
		this.birthdate = new SimpleStringProperty(birthdate);
		this.hiredate = new SimpleStringProperty(hiredate);
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

	public String getPosition() {
		return position.get();
	}

	public String getPartFull() {
		return partFull.get();
	}

	public String getAddress() {
		return address.get();
	}

	public String getCity() {
		return city.get();
	}

	public String getState() {
		return state.get();
	}

	public String getPostal() {
		return postal.get();
	}

	public String getBirthdate() {
		return birthdate.get();
	}

	public String getHiredate() {
		return hiredate.get();
	}
	
	

}
