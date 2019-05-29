package application;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;

import javafx.beans.property.SimpleStringProperty;

public class Teacher extends Borrower {
	
	protected static int bookLimit, dayLimit;
	protected static double finePerDay;
	
	public Teacher(String id, String lastName, String firstName) {	
		super();
		this.id = new SimpleStringProperty(id);
		this.lastName = new SimpleStringProperty(lastName);
		this.firstName = new SimpleStringProperty(firstName);
		bookIds = new ArrayList<SimpleStringProperty>();
		this.borrowedDates = new ArrayList<LocalDate>();
		this.returnDates = new ArrayList<LocalDate>();
	}
	
	public Teacher(String id, String lastName, String firstName, ArrayList<SimpleStringProperty> bookIds,
			ArrayList <LocalDate> borrDates, ArrayList <LocalDate> retDates) {
		super();
		this.id = new SimpleStringProperty(id);
		this.lastName = new SimpleStringProperty(lastName);
		this.firstName = new SimpleStringProperty(firstName);
		this.bookIds = bookIds;
		this.borrowedDates = borrDates;
		this.returnDates = retDates;
	}
}
