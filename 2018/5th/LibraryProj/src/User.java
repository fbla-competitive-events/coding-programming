import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public abstract class User implements Serializable{		//user class is declared abstract because you cannot create a user
														//used as a base class for the subclasses Teacher and Student
	
	private static final long serialVersionUID = -6424551499889459613L; 	// this allows saving in files
	public final static int numberOfBooksAllowedForStudents = 3;	 //default number of books each student can check out
	public final static int daysPerBookAllowedForStudents = 10;    //default time allowed for each book
	public final static int numberOfBooksAllowedForTeachers = 5;   //default number of books each teacher can check out
	public final static int daysPerBookAllowedForTeachers = 14;    //default time allowed for each book
	private String name;									//stores name
	private ArrayList<Book> booksCheckedOut; 				//stores list of all books checked out
	
	
	/**
	 * Constructor creating a new user
	 * @param userName - name of user to be made
	 */
	public User(String userName){            
		this.name = userName.trim().toLowerCase();
		booksCheckedOut = new ArrayList<Book>();
	}
	
	/**
	 * checks out the given book for the user
	 * @param book - Book that will be checked out by this user
	 */
	public void checkOutBook(Book book){   	
		if (getClass() == Student.class && booksCheckedOut.size() == numberOfBooksAllowedForStudents){
			return;
		}
		if (getClass() == Teacher.class && booksCheckedOut.size() == numberOfBooksAllowedForTeachers){
			return;
		}
		booksCheckedOut.add(book); //adds book
		book.setUserCheckedOut(this);
		Date now = new Date(); //next few lines set the due date of the book
		book.setDateCheckedOut(now);
		Date due = null;
		if (getClass() == Student.class){
			due = new Date(now.getTime() + (this.daysPerBookAllowedForStudents * 86400000)); //this tells the computer how to calculate the due date
		}
		else if (getClass() == Teacher.class){
			due = new Date((now.getTime() + (this.daysPerBookAllowedForTeachers * 86400000))); //this tells the computer how to calculate the due date
		}
		book.setDateDue(due);
	}	
	
	/**
	 * checks in and resets all dates
	 * @param book - Book that will be checked in
	 */
	public void checkInBook(Book book){ 
		booksCheckedOut.remove(book);
		book.resetDateCheckedOut();  
		book.resetDateDue();
		book.resetUserCheckedOut();
	}
	
	/**
	 * 
	 * @return name of user
	 */
	public String getName(){ 	
		return this.name;
	}
	
	/**
	 * mutator method to change name of user
	 * @param newName - new name for user
	 */
	public void setName(String newName){    	
		this.name = newName.trim().toLowerCase();
	}
	
	/**
	 * 
	 * @param otherUser - other user to compare with
	 * @return true or false depending on whether 2 users are the same
	 */
	public boolean equals(User otherUser){ 	
		return (this.getName().equals(otherUser.getName()));
	}
	
	/**
	 * 
	 * @return books checked out by this user
	 */
	public ArrayList<Book> getBooksCheckedOut(){ 
		return this.booksCheckedOut;
	}
	
	/**
	 * prints out name of user
	 */
	@Override public String toString(){	
		return this.name;
	}
}
