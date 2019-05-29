package application;
/* **********************************************************************************************************************************************
 * This is a class 'Borrower', which contains the general features of a typical borrower from a school library. 
 * The Borrower may be a student or teacher, and therefore both the 'Student' and 'Teacher' classes inherit from the Borrower class. 
 * The instance variables of the Borrower class that the subclasses Student and Teacher inherit are: 
 * - id (must be unique for EVERY student and teacher)
 * - numDaysBorrowed (the number of days a book has been borrowed)
 * - daysOverLimit (the number of days beyond the return date that a book has been kept for)
 * - last_name (last name of the borrower)
 * - first_name (first name of the borrower)
 * - dateBorrowed (the date that a book has been borrowed. This is an object of the 'Date' class)
 * - returnDate (the date that a book is to be returned without a fine. Also an object of the 'Date' class)
 * **********************************************************************************************************************************************
 */


import java.util.*;

import javafx.beans.property.SimpleStringProperty;

import java.time.*;

// the access type of the instance variables must be 'protected' to allow access from the subclasses 'Student' and 'Teacher'.
public class Borrower {
	
	protected SimpleStringProperty id;
	protected SimpleStringProperty lastName, firstName; 
	protected ArrayList<SimpleStringProperty> bookIds;
	protected ArrayList<LocalDate> borrowedDates;
	protected ArrayList<LocalDate> returnDates;
//______________________________________________________________________________________________________

	/* default constructor */
	public Borrower() {
		
	}
//_____________________________________________________________________________________________________
		
	/* Constructor that takes the unique id, last name, first name, and date borrowed as parameters.
	   Also sets up these variables as well as the return date, number of days borrowed and 
	   number of days over limit. */
	
	public Borrower(String id, String lastName, String firstName) {
		
		this.id = new SimpleStringProperty(id);
		this.lastName = new SimpleStringProperty(lastName);
		this.firstName = new SimpleStringProperty(firstName);		
		bookIds = null;
	}
	
//_________________________________________________________________________________________________
	
	
	
	public String toString() {
		
		return ("ID:" + id + "\tName: " + lastName + ", " + firstName);
	}

	public String getId() {
		return id.get();
	}

	public void setId(SimpleStringProperty id) {
		this.id = id;
	}

	public String getLastName() {
		return lastName.get();
	}

	public void setLastName(SimpleStringProperty lastName) {
		this.lastName = lastName;
	}

	public String getFirstName() {
		return firstName.get();
	}

	public void setFirstName(SimpleStringProperty firstName) {
		this.firstName = firstName;
	}

	public ArrayList<String> getBookIds() {
		ArrayList<String> myBookIds = new ArrayList<String>();
		for(int i=0; i < bookIds.size(); i++ ) {
			myBookIds.add(bookIds.get(i).get());
		}
		return myBookIds;
	}
	
	

	public ArrayList<LocalDate> getBorrowedDates() {
		return borrowedDates;
	}

	public ArrayList<LocalDate> getReturnDates() {
		return returnDates;
	}

	public void setBookIds(ArrayList<SimpleStringProperty> bookIds) {
		this.bookIds = bookIds;
	}

}
