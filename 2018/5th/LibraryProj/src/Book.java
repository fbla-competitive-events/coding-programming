import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class Book implements Serializable{ //this implementation allows data of each book to be saved to the file

	private static final long serialVersionUID = 3096963456589535774L;
	public static int IDNumber = 10000; 	//this static variable is what allows each book to have a unique ID
	private String name;    				//book name 
	private int bookID;  					//book id number
	private String authorName;				//name of author
	private Date dateCheckedOut;   			//date checked out
	private Date dateDue;					//date due
	private User userCheckedOut;			//user that checked the book out

	/**
	 * Constructor to create new book
	 * @param name - name of book to be created
	 * @param authorName - name of author of book to be created
	 */
	public Book(String name, String authorName){ 		
		this.name = name.trim();
		this.authorName = authorName.trim();
		this.bookID = getNewIDNumber();
		this.dateCheckedOut = null;
		this.dateDue = null;
		userCheckedOut = null;
	}
	
	/**
	 * compares two books
	 * @param otherBook - other book used for comparison
	 * @return true or false whether or not two books are equal
	 */
	public boolean equals(Book otherBook){     
		return (this.getName().equals(otherBook.getName()) && this.getID() == otherBook.getID());
	}
	
	/**
	 * method that generates a unique ID number for all books
	 * @return unique ID number
	 */
	public int getNewIDNumber(){  				
		return ++IDNumber;
	}
	
	/**
	 * 
	 * @return name of book
	 */
	public String getName(){        			
		return this.name;
	}
	
	/**
	 * 
	 * @return date the book was checked out
	 */
	public Date getDateCheckedOut(){		
		return this.dateCheckedOut;
	}
	
	/**
	 * mutator method changes the date the book was checked out
	 * @param newDate - new Date for when book was checked out
	 */
	public void setDateCheckedOut(Date newDate){	
		this.dateCheckedOut = newDate;
	}
	
	/**
	 * resets the date checked out
	 */
	public void resetDateCheckedOut(){			
		this.dateCheckedOut = null;
	}
	
	/**
	 * 
	 * @return user that has checked out the current book
	 */
	public User getUserCheckedOut(){			
		return this.userCheckedOut;
	}
	
	/**
	 * mutator method allowing a new user to be assigned
	 * @param u - user to set that has checked out the book
	 */
	public void setUserCheckedOut(User u){   	
		this.userCheckedOut = u;
	}
	
	/**
	 * resets the user that checked out the book to null
	 */
	public void resetUserCheckedOut(){			
		this.userCheckedOut = null;
	}
	
	/**
	 * 
	 * @return due date for book
	 */
	public Date getDateDue(){ 				
		return this.dateDue;
	}
	
	/**
	 * sets new due date
	 * @param newDate - new date to be set
	 */
	public void setDateDue(Date newDate){		
		this.dateDue = newDate;
	}
	
	/**
	 * resets book due date
	 */
	public void resetDateDue(){  	 	 		
		this.dateDue = null;
	}
	
	/**
	 * 
	 * @return book id
	 */
	public int getID(){							
		return this.bookID;
	}
	
	/**
	 * 
	 * @return book author name
	 */
	public String getAuthorName(){	 		
		return this.authorName;
	}
	
	/**
	 * prints out information of book
	 */
	@Override public String toString(){					
		return (this.name + " by "+ this.authorName + ", ID: " + this.bookID);
	}
}
