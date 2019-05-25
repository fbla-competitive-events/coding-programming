package table;

import javafx.beans.property.SimpleStringProperty;

public class Member {
	/**
	 * @author Aatif Jiwani
	 * @version 3.0
	 */

	/**
	 * !IMPORTANT!
	 * The following code is used for the Table Views in the program. 
	 * JavaFX tables require information to be stored in an object so therefore,
	 * information for each member's credentials is inserted in an instance
	 * of this object. 
	 * 
	 * Refer to the "ViewMemberController.java" for further clarification.
	 */


	/*Pre-defined global variables
	 * Each of these variables are used in at least one method in this class. 
	 */
	
	private final SimpleStringProperty name;
	private final SimpleStringProperty phoneNumber;
	private final SimpleStringProperty email;
	private final SimpleStringProperty under18;
	private final SimpleStringProperty address;
	private final SimpleStringProperty city;
	private final SimpleStringProperty state;
	private final SimpleStringProperty postal;
	private final SimpleStringProperty birthdate;
	private final SimpleStringProperty joindate;
	
	/*
	 * Class constructor
	 * These act as the class's setters 
	 */
	public Member(String name, String phoneNumber, String email, String under18, String address, String city, String state,
			String postal, String birthdate, String joindate) {
		super();
		this.name = new SimpleStringProperty(name);
		this.phoneNumber = new SimpleStringProperty(phoneNumber);
		this.email = new SimpleStringProperty(email);
		this.under18 = new SimpleStringProperty(under18);
		this.address = new SimpleStringProperty(address);
		this.city = new SimpleStringProperty(city);
		this.state = new SimpleStringProperty(state);
		this.postal = new SimpleStringProperty(postal);
		this.birthdate = new SimpleStringProperty(birthdate);
		this.joindate = new SimpleStringProperty(joindate);
	}

	/*
	 * The following methods are variable getters
	 */
	public String getName() {
		return name.get();
	}

	public String getPhoneNumber() {
		return phoneNumber.get();
	}

	public String getEmail() {
		return email.get();
	}

	public String getUnder18() {
		return under18.get();
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

	public String getJoindate() {
		return joindate.get();
	}

	
}
