package main;

import java.awt.Component;
import java.awt.Font;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/**
 * This class is an extension of a JPanel for the main screen, where all of the GUI is implemented. 
 * 
 * @author Colin Sanders
 */
@SuppressWarnings("serial")
public class MainScreen extends JPanel{
	
	public MainScreen(){
		setBounds(0, 0, 592, 564);
		setLayout(null);
		
		JLabel lblWelcomeToFec = new JLabel("C.E.C. Start Screen"); //Title Label
		lblWelcomeToFec.setHorizontalAlignment(SwingConstants.CENTER);
		lblWelcomeToFec.setFont(new Font("Open Sans Semibold", Font.PLAIN, 13));
		lblWelcomeToFec.setBounds(new Rectangle(196, 11, 197, 18));
		lblWelcomeToFec.setAlignmentX(Component.CENTER_ALIGNMENT);
		add(lblWelcomeToFec);
		
		JButton btnCustomer = new JButton("Customer Check-In"); //Button to activate the CustomerScreen GUI.
		btnCustomer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				GUI.gotoCustomerScreen();
			}
		});
		btnCustomer.setBounds(227, 474, 154, 23);
		add(btnCustomer);
		
		JButton btnEmployee = new JButton("Employee Log-In"); //Button to activate the EmployeeScreen GUI
		btnEmployee.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				GUI.gotoLoginScreen();
			}
		});
		btnEmployee.setBounds(227, 508, 154, 23);
		add(btnEmployee);
		
		JLabel lblLogo = new JLabel(""); //Blank Label to hold the logo. 
		lblLogo.setBounds(0, 40, 582, 435);
		
		//Gets the logo from the resources folder, resizes it to fit in the label created.
		lblLogo.setIcon(new ImageIcon(new ImageIcon(this.getClass().getResource("/logo.png")).getImage().getScaledInstance(590, 250, Image.SCALE_DEFAULT))); 
		add(lblLogo);
		
	}
}
