package main;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;

import javax.swing.JTextField;
import javax.swing.JDialog;
import javax.swing.JButton;
import javax.swing.JComboBox;

import java.awt.Insets;

/**
 * Class that handles the data and GUI of the Customer Screen. 
 * 
 * @author Colin Sanders
 */
public class CustomerScreen extends JPanel {

	private JTable table;
	private DefaultTableModel dtm;

	public CustomerScreen() {
		setBounds(0, 0, 592, 564);
		setLayout(null);

		table = new JTable();
		table.setBorder(new LineBorder(new Color(0, 0, 0)));
		dtm = new DefaultTableModel(0, 0){
			@Override
			public boolean isCellEditable(int row, int column){
				return false;
			}
		};
		String[] columnNames = new String[] { "Check-in #", "Customer ID", "Day", "Time" };
		

		dtm.setColumnIdentifiers(columnNames);
		table.setModel(dtm);
		
		//Fills dataTable array with information from database.
		try{
			//get connection
			Connection conn1 = DriverManager.getConnection(DatabaseConn.getConnection(), DatabaseConn.getUsername(), DatabaseConn.getPassword());
			
			//write statement
			Statement stmt3 = conn1.createStatement();
			
			//execute query
			ResultSet rs1 = stmt3.executeQuery("select check_in.checkin_id, customers.first_name, customers.last_name, check_in.date, check_in.time from check_in inner join customers on customers.customer_id = check_in.customer_id ORDER BY `check_in`.`checkin_id` ASC ;");
			
			//process result set
			while(rs1.next()){
				Object[] data = new Object[4];
				data[0] = rs1.getInt(1);
				data[1] = (rs1.getString(2) + " " + rs1.getString(3));
				data[2] = rs1.getString(4);
				data[3] = rs1.getString(5);
				
				dtm.insertRow(0, data);
			}
			rs1.close();
			
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setBounds(10, 309, 572, 244);
		add(scrollPane);

		JLabel lblCustomerCheckin = new JLabel("Customer Check-In");
		lblCustomerCheckin.setHorizontalAlignment(SwingConstants.CENTER);
		lblCustomerCheckin.setFont(new Font("Tahoma", Font.BOLD, 13));
		lblCustomerCheckin.setBounds(219, 23, 159, 14);
		add(lblCustomerCheckin);

		JLabel lblPastCustomerCheckins = new JLabel("Past Customer Check-Ins");
		lblPastCustomerCheckins.setHorizontalAlignment(SwingConstants.CENTER);
		lblPastCustomerCheckins.setBounds(213, 284, 165, 14);
		add(lblPastCustomerCheckins);

		JButton btnBackToStart = new JButton("Back to Start.");
		btnBackToStart.setBounds(10, 11, 129, 18);
		btnBackToStart.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				GUI.gotoMainScreen();
			}
		});
		add(btnBackToStart);
		
		JLabel lblIfYoureA = new JLabel("If you're a new customer, click here: ");
		lblIfYoureA.setHorizontalAlignment(SwingConstants.CENTER);
		lblIfYoureA.setBounds(194, 72, 217, 14);
		add(lblIfYoureA);
		
		JButton btnNewCustomers = new JButton("New Customers");
		btnNewCustomers.setMargin(new Insets(2, 10, 2, 10));
		btnNewCustomers.setBounds(244, 97, 120, 23);
		btnNewCustomers.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				JDialog dialog = null;
				JPanel panel = new JPanel();
				panel.setLayout(new GridLayout(4, 2));
				
				JLabel nameFirstLabel = new JLabel(" Enter your first name:");
				JLabel nameLastLabel = new JLabel(" Enter your last name:");
				JTextField firstNameTextField = new JTextField();
			    JTextField lastNameTextField = new JTextField();
				
			    JLabel buttonLabel = new JLabel(" Press this button to register and check in at the current time.");
			    JButton checkinButton = new JButton("Check in.");
			    
			    panel.add(nameFirstLabel);
			    panel.add(firstNameTextField);
			    panel.add(nameLastLabel);
			    panel.add(lastNameTextField);
			    panel.add(buttonLabel);
			    panel.add(checkinButton);
			    
			    dialog = new JDialog();
			    dialog.getContentPane().add(panel);
			    dialog.pack();
			    dialog.setLocationRelativeTo(null);
			    dialog.setVisible(true);
			    
			    checkinButton.addActionListener(new ActionListener(){
					@Override
					public void actionPerformed(ActionEvent e) {
						if(hasNonalphanumeric(firstNameTextField.getText()) || hasNonalphanumeric(lastNameTextField.getText())){
							JOptionPane.showMessageDialog(null, "Your name can only include letters and numbers.");
						}else{
							try{
								Connection conn = DriverManager.getConnection(DatabaseConn.getConnection(), DatabaseConn.getUsername(), DatabaseConn.getPassword());
								Statement stmt = conn.createStatement();
								Statement stmt2 = conn.createStatement();
								Statement stmt3 = conn.createStatement();
								Statement stmt4 = conn.createStatement();
								
								ResultSet rs = stmt2.executeQuery("select MAX(customer_id) from customers");
								int id = 0;
								while(rs.next()){
									id = rs.getInt(1) + 1;
								}
								
								String sql1 = "insert into customers "
											+ "(customer_id, first_name, last_name, num_of_visits)"
											+ " values ('"+ id + "', '" + firstNameTextField.getText()+ "', '" + lastNameTextField.getText() + "', '" + "1');";
								stmt.executeUpdate(sql1);
								stmt.close();			
								
								stmt = conn.createStatement();
								String sql2 = "insert into check_in (date, time, customer_id) values (current_date(), current_time(), '"+ id +"');";
								stmt.executeUpdate(sql2);
								
								Object[] data = new Object[4];
								ResultSet rsCheckinID = stmt3.executeQuery("select MAX(checkin_id) from check_in");
								
								int checkinID = 0;
								while(rsCheckinID.next()){
									checkinID = rsCheckinID.getInt(1);
								}
								System.out.println(checkinID);
								
								ResultSet checkinData = stmt4.executeQuery("select * from check_in where checkin_id = " + checkinID + ";");
								
								data[0] = checkinID;
								data[1] = firstNameTextField.getText() + " " + lastNameTextField.getText();
								while(checkinData.next()){
									data[2] = checkinData.getObject(2);
									data[3] = checkinData.getObject(3);
								}
								
								dtm.insertRow(0, data);
								
								rs.close();
								rsCheckinID.close();
								checkinData.close();
								
								stmt.close();
								stmt2.close();
								stmt3.close();
								stmt4.close();
								
								conn.close();
								
								JOptionPane.showMessageDialog(null, "Successfully registered and checked in!");
								
								
								
							}catch(Exception exc){
								JOptionPane.showMessageDialog(null, "There was an error writing to the database...");
								exc.printStackTrace();
							}
						}
					}
			    });
			}
		});
		add(btnNewCustomers);
		
		JLabel lblReturningCustomers = new JLabel("Returning customers, click here:");
		lblReturningCustomers.setHorizontalAlignment(SwingConstants.CENTER);
		lblReturningCustomers.setBounds(202, 155, 198, 14);
		add(lblReturningCustomers);
		
		JButton btnReturningCustomers = new JButton("Returning Customers");
		btnReturningCustomers.setMargin(new Insets(2, 10, 2, 10));
		btnReturningCustomers.setBounds(216, 180, 170, 23);
		btnReturningCustomers.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try{
					Connection conn = DriverManager.getConnection(DatabaseConn.getConnection(), DatabaseConn.getUsername(), DatabaseConn.getPassword());
					Statement stmt = conn.createStatement();
					
					ResultSet rs1 = stmt.executeQuery("select distinct * from customers");
					ArrayList<String> names = new ArrayList<String>();
					
					while(rs1.next()){
						String name = rs1.getString(2) + " " + rs1.getString(3);
						names.add(name);
					}
					
					JComboBox<Object> nameSelection = new JComboBox<>(names.toArray());
					JPanel panel = new JPanel(new GridLayout(0, 1));
					panel.add(nameSelection);
					
					int selection = JOptionPane.showOptionDialog(null,
					        panel, 
					        "Select your name and press check in", 
					        JOptionPane.OK_CANCEL_OPTION, 
					        JOptionPane.PLAIN_MESSAGE, 
					        null, 
					        new String[]{"Check In", "Cancel"}, // checkin = 0, cancel = 1
					        "default");
					
					if(selection == 0){
						String option = nameSelection.getSelectedItem().toString();
						String[] seperateNames = option.trim().split("\\s+");
						
						Statement stmt2 = conn.createStatement();
						ResultSet rs2 = stmt2.executeQuery("select * from customers where first_name = '" + seperateNames[0] + "' and last_name = '" + seperateNames[1] + "';");
						int id = 0;
						int numOfVisits = 0;
						while(rs2.next()){
							id = rs2.getInt(1);
							numOfVisits = rs2.getInt(4);
						}
						
						Statement stmt3 = conn.createStatement();
						stmt3.executeUpdate("insert into check_in (date, time, customer_id) values (current_date(), current_time(), '" + id + "');");
						
						numOfVisits++;
						
						Statement stmt4 = conn.createStatement();
						stmt4.executeUpdate("update customers set num_of_visits = '" + numOfVisits + "' where customer_id = '" + id + "';");
						
						Object[] data = new Object[4];
						ResultSet rsCheckinID = stmt3.executeQuery("select MAX(checkin_id) from check_in");
						
						int checkinID = 0;
						while(rsCheckinID.next()){
							checkinID = rsCheckinID.getInt(1);
						}
						System.out.println(checkinID);
						
						Statement stmt5 = conn.createStatement();
						ResultSet checkinData = stmt5.executeQuery("select * from check_in where checkin_id = " + checkinID + ";");
						
						data[0] = checkinID;
						data[1] = seperateNames[0] + " " + seperateNames[1];
						while(checkinData.next()){
							data[2] = checkinData.getObject(2);
							data[3] = checkinData.getObject(3);
						}
						
						dtm.insertRow(0, data);
						
					}else{
						return;
					}
				}catch(Exception e){
					JOptionPane.showMessageDialog(null, "There was an error. Please try again later.");
					e.printStackTrace();
				}
				
			}
		});
		add(btnReturningCustomers);

		requestFocusInWindow();
	}
	
	public boolean hasNonalphanumeric(String s){
		return s.matches("^.*[^a-zA-Z0-9 ].*$");
	}
}
