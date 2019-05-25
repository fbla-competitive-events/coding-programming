package main;

import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * This class acts as a utility class to first initialize, and then switch between all of
 * the GUI objects created in the program. 
 * 
 * @author Colin Sanders
 */
public class GUI {

	//Main window frame of the program.
	public static JFrame frame; 
	
	//A temporary variable used to clear the current panel and check to make sure the panel isn't duplicated
	private static JPanel currentPanel; 	
	
	//The four JPanels used in the program, which contain all of the GUI assets and data handlers
	static MainScreen mainScreen;
	static EmployeeLoginScreen loginScreen;
	static CustomerScreen customerScreen;
	static AdminScreen adminScreen;
	static EmployeeScreen employeeScreen;
	
	/**
	 * GUI default object constructor, where the frame is initialized and set up to my liking. This also initiallizes
	 * all of the panels used throughout the program all at once, which accounts for the 1-2 second start-up time when
	 * the program first begins. 
	 */
	public GUI(){
		frame = new JFrame("FEC Project - Colin Sanders");
		frame.getContentPane().setLayout(null);
		frame.setSize(600, 600);
		frame.setVisible(true);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		currentPanel = new JPanel();
		frame.setResizable(false);
		
		mainScreen = new MainScreen();
		loginScreen = new EmployeeLoginScreen();
		customerScreen = new CustomerScreen();
		adminScreen = new AdminScreen();
		employeeScreen = new EmployeeScreen();
		
		
		gotoMainScreen();
	}
	
	/**
	 * Method used to change the current panel to the Main Screen panel.
	 */
	public static void gotoMainScreen(){
		frame.remove(currentPanel);
		currentPanel = mainScreen;
		frame.add(currentPanel);
		
		frame.getContentPane().validate();
		frame.getContentPane().repaint();
	}
	
	/**
	 * Method used to change the current panel to the Login Screen panel.
	 */
	public static void gotoLoginScreen(){
		frame.remove(currentPanel);
		currentPanel = loginScreen;
		frame.add(currentPanel);
		
		frame.getContentPane().validate();
		frame.getContentPane().repaint();
	}
	
	/**
	 * Method used to change the current panel to the Customer Screen panel. 
	 */
	public static void gotoCustomerScreen(){
		frame.remove(currentPanel);	
		currentPanel = customerScreen;
		frame.add(currentPanel);
		
		frame.getContentPane().validate();
		frame.getContentPane().repaint();
	}
	
	/**
	 * Method used to change the current panel to the Admin Screen panel. 
	 */
	public static void gotoAdminScreen(){
		frame.remove(currentPanel);
		currentPanel = adminScreen;
		frame.add(currentPanel);
		
		frame.getContentPane().validate();
		frame.getContentPane().repaint();
	}
	
	public static void goToEmployeeScreen(){
		frame.remove(currentPanel);
		currentPanel = employeeScreen;
		frame.add(currentPanel);
		
		frame.getContentPane().validate();
		frame.getContentPane().repaint();
	}
	
	public static void goToEmployeeScreen(int id){
		frame.remove(currentPanel);
		employeeScreen = new EmployeeScreen(id);
		currentPanel = employeeScreen;
		frame.add(currentPanel);
		
		frame.getContentPane().validate();
		frame.getContentPane().repaint();
	}
}
