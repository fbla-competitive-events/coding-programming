package application;

import java.time.LocalDate;

import javafx.beans.property.SimpleStringProperty;

/** 
 * Class BorrowedBook 
 * A BorrowedBook object is created when a borrower borrows a book from the library. 
 * It keeps track of the book, borrower and borrow date. 
 * The leadingDays is the number of days leading up to the due date OR the 
 * number of days past the due date for overdue books.  
 */
public class BorrowedBook {
	
	private SimpleStringProperty bookId, bookName, borrowerId, borrowerName, leadingDays, borrowDate;
	
	public BorrowedBook (SimpleStringProperty bookId, SimpleStringProperty bookName, SimpleStringProperty borrowerId,
			SimpleStringProperty borrowerName, SimpleStringProperty leadingDays, SimpleStringProperty borrowDate) {
		
		this.bookId = bookId;
		this.bookName = bookName;
		this.borrowerId = borrowerId;
		this.borrowerName = borrowerName;
		this.leadingDays = leadingDays;
		this.borrowDate = borrowDate;
		
	}

	public String getBookId() {
		return bookId.get();
	}

	public void setBookId(SimpleStringProperty bookId) {
		this.bookId = bookId;
	}

	public String getBookName() {
		return bookName.get();
	}

	public void setBookName(SimpleStringProperty bookName) {
		this.bookName = bookName;
	}

	public String getBorrowerId() {
		return borrowerId.get();
	}

	public void setBorrowerId(SimpleStringProperty borrowerId) {
		this.borrowerId = borrowerId;
	}

	public String getBorrowerName() {
		return borrowerName.get();
	}

	public void setBorrowerName(SimpleStringProperty borrowerName) {
		this.borrowerName = borrowerName;
	}

	public String getLeadingDays() {
		return leadingDays.get();
	}

	public void setLeadingDays(SimpleStringProperty leadingDays) {
		this.leadingDays = leadingDays;
	}
	
	public String getBorrowDate() {
		return borrowDate.get();
	}

	public void setBorrowDate(SimpleStringProperty borrowDate) {
		this.borrowDate = borrowDate;
	}
	
}
