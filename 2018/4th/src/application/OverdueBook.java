package application;

import javafx.beans.property.SimpleStringProperty;

public class OverdueBook {
	
	private SimpleStringProperty bookId, bookName, borrowerId, returnDate, fine;
	
	public OverdueBook (SimpleStringProperty bookId, SimpleStringProperty bookName, SimpleStringProperty borrowerId,
			SimpleStringProperty returnDate, SimpleStringProperty fine) {
		
		this.bookId = bookId;
		this.bookName = bookName;
		this.borrowerId = borrowerId;
		this.returnDate = returnDate;
		this.fine = fine;
		
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

	public String getReturnDate() {
		return returnDate.get();
	}

	public void setReturnDate(SimpleStringProperty returnDate) {
		this.returnDate = returnDate;
	}

	public String getFine() {
		return fine.get();
	}

	public void setFine(SimpleStringProperty fine) {
		this.fine = fine;
	}
	
	
}
