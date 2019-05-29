package application;

import java.time.LocalDate;

import javafx.beans.property.SimpleStringProperty;

public class LibraryBook {

	public final SimpleStringProperty bookId;
	public final SimpleStringProperty bookName;
	public final SimpleStringProperty authorName;
	public boolean isCheckedOut;
	public SimpleStringProperty checkedOutBy;
	public SimpleStringProperty checkedOutId;
	public LocalDate checkedOutDate;
	public LocalDate returnDate;
	public SimpleStringProperty borrowerName;
	
	public LibraryBook(String bookId, String bookName, String authorName, boolean isCheckedOut, String checkedOutBy, 
			String checkedOutId, String borrowerName, LocalDate checkedOutDate, LocalDate returnDate) {
		
		this.bookId = new SimpleStringProperty(bookId);
		this.bookName = new SimpleStringProperty(bookName);
		this.authorName = new SimpleStringProperty(authorName);
		this.isCheckedOut = isCheckedOut;
		this.checkedOutBy = new SimpleStringProperty(checkedOutBy);
		this.checkedOutId = new SimpleStringProperty(checkedOutId);
		this.checkedOutDate = checkedOutDate;
		this.returnDate = returnDate;
		this.borrowerName = new SimpleStringProperty(borrowerName);
	}
	
	public String getIsCheckedOut() {
		if(isCheckedOut) 
			return ("Checked Out");
		else
			return ("Available");
	}

	public void setCheckedOut(boolean isCheckedOut) {
		this.isCheckedOut = isCheckedOut;
	}

	public String getBookId() {
		return bookId.get();
	}

	public String getBookName() {
		return bookName.get();
	}

	public String getAuthorName() {
		return authorName.get();
	}

	public String toString() {
		
		return ("Name: " + bookName + "\t" + "ID: " + bookId + "\t" +
		"Author: " + authorName);
	}
	
	public String getCheckedOutBy() {
		return checkedOutBy.get();
	}
	
	public String getCheckedOutId() {
		return checkedOutId.get();
	}
	
	public LocalDate getCheckedOutDate() {
		return checkedOutDate;
	}
	
	public LocalDate getReturnDate() {
		return returnDate;
	}
	
	public String getBorrowerName() {
		return borrowerName.get();
	}



}
