package main;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import java.awt.Font;
import javax.swing.SwingConstants;

/**
 * This class handles the login screen GUI. I implemented this more for fun, as it wasn't necessary to have a login 
 * screen, but it does make the software feel more commercially acceptable. Default admin username is admin1, default admin
 * password is password.
 * 
 * @author Colin Sanders
 */
public class EmployeeLoginScreen extends JPanel{
	public EmployeeLoginScreen(){
		
		setBounds(0, 0, 592, 564);
		setLayout(null);
		requestFocusInWindow();

		JLabel titleLabel = new JLabel("Employee Log-In Screen");
		titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
		titleLabel.setFont(new Font("Tahoma", Font.BOLD, 13));
		titleLabel.setBounds(194, 11, 200, 35);
		add(titleLabel);
		
		JLabel userLabel = new JLabel("User");
		userLabel.setBounds(155, 72, 80, 25);
		add(userLabel);

		JTextField userText = new JTextField(20);
		userText.setBounds(270, 72, 160, 25);
		add(userText);

		JLabel passwordLabel = new JLabel("Password");
		passwordLabel.setBounds(155, 108, 80, 25);
		add(passwordLabel);

		JPasswordField passwordText = new JPasswordField(20);
		passwordText.setBounds(270, 108, 160, 25);
		add(passwordText);

		JButton loginButton = new JButton("Login");
		loginButton.setBounds(254, 504, 80, 25);
		loginButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				//To-do Check whether or not there is an employee or an admin logging in by retrieving data from the text fields
				//and comparing them to an array. Then send to the screen based off of which selection. 
				
				String employeeOverridePassword = "password";
				String employeeOverrideUser = "employee_1";
				String adminOverrideUser = "admin";
				
				if(userText.getText().equals(adminOverrideUser) && Arrays.equals(passwordText.getPassword(), employeeOverridePassword.toCharArray())){
					JOptionPane.showMessageDialog(null, "Administrator log-in successful.");
					userText.setText("");
					passwordText.setText("");
					GUI.gotoAdminScreen();
				}else if(userText.getText().equals(employeeOverrideUser) && Arrays.equals(passwordText.getPassword(), employeeOverridePassword.toCharArray())){
					JOptionPane.showMessageDialog(null, "Employee log-in successful.");
					userText.setText("");
					passwordText.setText("");
					String id_delimiter = "_";
					int idIndex = employeeOverrideUser.indexOf(id_delimiter);
					int id = Integer.parseInt(employeeOverrideUser.substring(idIndex + 1));
					GUI.goToEmployeeScreen(id);
					
				}else{
					JOptionPane.showMessageDialog(null, "Invalid username or password");
				}
			}
		});
		add(loginButton);
		
		JLabel lblYouNeverCan = new JLabel("You never can be too protected, especially at the C.E.C.! There is one admin account coded in, but ");
		lblYouNeverCan.setBounds(49, 404, 488, 43);
		add(lblYouNeverCan);
		
		JLabel lblMoreCanAlways = new JLabel("more can always be added. ");
		lblMoreCanAlways.setHorizontalAlignment(SwingConstants.CENTER);
		lblMoreCanAlways.setBounds(49, 435, 488, 14);
		add(lblMoreCanAlways);
		
		JButton btnBackToStart = new JButton("Back to Start.");
		btnBackToStart.setBounds(453, 535, 129, 18);
		btnBackToStart.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				GUI.gotoMainScreen();
			}
		});
		add(btnBackToStart);
	}
}
