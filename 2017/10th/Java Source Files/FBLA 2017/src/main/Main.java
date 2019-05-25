package main;

/**
 * The class that holds the main method for the program, AKA where everything begins. Also initializes a lot 
 * of variables that are used across the program, such as the lists of employees and customers.
 * 
 * @author Colin Sanders, Timber Creek High School
 * @version 1.1 - Documented Everything, post-state competition.
 * 
 * To Do - Implement connections to MySQL database, add more functionality to the customer data report,
 * 		   Add help screens, document code better, improve efficiency, etc etc...	
 */
public class Main {
	
	/**
	 * Main object constructor, adds the test employees to the arrays, and initializes the 
	 * GUI, which begins the rest of the program. 
	 */
	public Main(){
		GUI gui = new GUI();
	}
	
	/**
	 * Main method. Creates program object of the type Main.
	 */
	public static void main(String args[]){
		Main program = new Main();
	}
}
