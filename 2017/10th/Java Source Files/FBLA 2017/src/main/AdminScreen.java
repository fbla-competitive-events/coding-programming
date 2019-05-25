package main;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.print.PrinterException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.SwingConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.ListSelectionModel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.border.EtchedBorder;
import javax.swing.JButton;
import javax.swing.UIManager;
import javax.swing.JCheckBox;
import javax.swing.JTextField;
import javax.swing.JTable;
import javax.swing.JTable.PrintMode;
import javax.swing.table.DefaultTableModel;
import javax.swing.border.LineBorder;

import java.awt.Color;
import javax.swing.JComboBox;
import javax.swing.JDialog;

/**
 * This is the AdminScreen extension of the JPanel object, where everything to
 * do with the administrator functions are handled. Includes GUI, the creation
 * of all of the data tables, the employee editing, and the schedule management.
 * It is most likely the longest class because it does the most stuff. I imagine
 * that a lot of it could be cut up.
 * 
 * @author Colin Sanders
 */
@SuppressWarnings("serial")
public class AdminScreen extends JPanel {
	private JTable table;

	private JTextField textBestCustomer;
	private JTextField textBestTime;

	private JCheckBox chckbxSaturday;
	private JCheckBox chckbxSunday;
	private JCheckBox chckbxMonday;
	private JCheckBox chckbxTuesday;
	private JCheckBox chckbxWednesday;
	private JCheckBox chckbxThursday;
	private JCheckBox chckbxFriday;

	JComboBox comboBoxSundayStart;
	JComboBox comboBoxSundayFinish;
	JComboBox comboBoxMondayStart;
	JComboBox comboBoxMondayFinish;
	JComboBox comboBoxTuesdayStart;
	JComboBox comboBoxTuesdayFinish;
	JComboBox comboBoxWednesdayStart;
	JComboBox comboBoxWednesdayFinish;
	JComboBox comboBoxThursdayStart;
	JComboBox comboBoxThursdayFinish;
	JComboBox comboBoxFridayStart;
	JComboBox comboBoxFridayFinish;
	JComboBox comboBoxSaturdayStart;
	JComboBox comboBoxSaturdayFinish;

	JList list;
	DefaultListModel listModel;
	JTable table2;
	DefaultTableModel dtm2;
	DefaultTableModel dtm;

	ArrayList<String> timesArrayList = new ArrayList<String>();
	String[] timeArray;
	String[] blankArray = { " " };

	int currentEmployeeID = 1;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public AdminScreen() {
		// Adding, changing, removing employees.
		// Creating, editing, and generating the employee schedule
		// Add customer attendance
		// Viewing the customer report.

		// Soooooo much GUI stuff...
		setBounds(0, 0, 592, 564);
		setLayout(new GridLayout(0, 1, 0, 0));

		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.BOTTOM);

		JPanel panelHome = new JPanel();
		tabbedPane.addTab("Home", panelHome);
		panelHome.setLayout(null);

		JLabel lblWelcomeAdmin = new JLabel(
				"<html> <center>  Welcome administrator and/or other figure of power! Please select a tab from below to perform your many admin-esque duties.</center> </html>");
		lblWelcomeAdmin.setHorizontalAlignment(SwingConstants.CENTER);
		lblWelcomeAdmin.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblWelcomeAdmin.setBounds(133, 102, 320, 85);
		panelHome.add(lblWelcomeAdmin);

		JLabel lblAdministratorHomePanel = new JLabel("Administrator Home Panel");
		lblAdministratorHomePanel.setFont(new Font("Tahoma", Font.BOLD, 15));
		lblAdministratorHomePanel.setHorizontalAlignment(SwingConstants.CENTER);
		lblAdministratorHomePanel.setBounds(181, 11, 208, 22);
		panelHome.add(lblAdministratorHomePanel);

		JLabel lblTabDescriptions = new JLabel("Tab Descriptions:");
		lblTabDescriptions.setBounds(248, 223, 105, 14);
		panelHome.add(lblTabDescriptions);

		JLabel lblEmployeesLets = new JLabel(
				"<html><center> Employees - Let's you edit your roster of employees and change their schedule. </center> </html>");
		lblEmployeesLets.setHorizontalAlignment(SwingConstants.CENTER);
		lblEmployeesLets.setBounds(92, 260, 421, 35);
		panelHome.add(lblEmployeesLets);

		JLabel lblScheduleLets = new JLabel(
				"<html> <center> Schedule - Let's you see the schedule for all of your employees. You can even print it!</center> </html>");
		lblScheduleLets.setHorizontalAlignment(SwingConstants.CENTER);
		lblScheduleLets.setBounds(75, 306, 449, 35);
		panelHome.add(lblScheduleLets);

		JLabel lblCustomersLets = new JLabel(
				"<html> <center> Customers - Let's you see some data on your customers, and print out a list of ALL of them. </center> </html>");
		lblCustomersLets.setHorizontalAlignment(SwingConstants.CENTER);
		lblCustomersLets.setBounds(75, 353, 461, 35);
		panelHome.add(lblCustomersLets);

		// Finally, something that isn't just GUI. Well kind of... This button
		// also can send the user back to the main screen.
		JButton btnBack = new JButton("Back to the Start Screen");
		btnBack.setBounds(387, 506, 190, 22);
		btnBack.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				GUI.gotoMainScreen();
			}
		});
		panelHome.add(btnBack);

		listModel = new DefaultListModel();

		// Creates the element where the employee list can be seen and edited.
		try {
			Connection conn1 = DriverManager.getConnection(DatabaseConn.getConnection(), DatabaseConn.getUsername(), DatabaseConn.getPassword());

			Statement stmt1 = conn1.createStatement();
			ResultSet rs1 = stmt1.executeQuery("select * from employees;");

			while (rs1.next()) {
				listModel.addElement(rs1.getString(2) + " " + rs1.getString(3));
			}

		} catch (Exception e) {

		}

		JPanel panelEmployees = new JPanel();
		tabbedPane.add("Employees", panelEmployees);
		panelEmployees.setLayout(null);

		list = new JList(listModel);
		list.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setSelectedIndex(0);
		list.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent arg0) {
				try {
					Connection conn = DriverManager.getConnection(DatabaseConn.getConnection(), DatabaseConn.getUsername(), DatabaseConn.getPassword());
					Statement stmt1 = conn.createStatement();
					ResultSet rs1 = stmt1.executeQuery("select * from employees;");
					while (rs1.next()) {
						if ((list.getSelectedIndex() + 1) == rs1.getInt(1)) {
							currentEmployeeID = rs1.getInt(1);
							updateEmployeeFields();
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		list.setVisibleRowCount(8);
		list.setBounds(33, 36, 180, 400);
		panelEmployees.add(list);

		JLabel lblEmployeeList = new JLabel("Employee List");
		lblEmployeeList.setHorizontalAlignment(SwingConstants.CENTER);
		lblEmployeeList.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblEmployeeList.setBounds(58, 11, 118, 14);
		panelEmployees.add(lblEmployeeList);

		JButton btnAddEmployee = new JButton("Add Employee");
		btnAddEmployee.setFont(UIManager.getFont("Button.font"));
		btnAddEmployee.setBounds(54, 447, 135, 23);
		btnAddEmployee.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				JDialog dialog = null;
				JPanel panel = new JPanel();
				panel.setLayout(null);
				JLabel lblFirstName = new JLabel("First Name:");
				lblFirstName.setBounds(20, 28, 84, 14);
				panel.add(lblFirstName);

				JLabel lblLastName = new JLabel("Last Name: ");
				lblLastName.setBounds(20, 53, 84, 14);
				panel.add(lblLastName);

				JTextField textFieldFirst = new JTextField();
				textFieldFirst.setBounds(114, 25, 120, 20);
				panel.add(textFieldFirst);
				textFieldFirst.setColumns(10);

				JTextField textFieldLast = new JTextField();
				textFieldLast.setBounds(114, 53, 120, 20);
				panel.add(textFieldLast);
				textFieldLast.setColumns(10);

				JButton btnAddEmployee = new JButton("Add Employee");
				btnAddEmployee.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						if (textFieldFirst.getText().equals("") || textFieldFirst.getText().equals(null)
								|| textFieldLast.getText().equals("") || textFieldLast.getText().equals(null)) {
							JOptionPane.showMessageDialog(null, "Please make sure none of the text fields are blank.");
						} else {
							String firstName = textFieldFirst.getText();
							String lastName = textFieldLast.getText();

							try {
								Connection conn = DriverManager.getConnection(DatabaseConn.getConnection(), DatabaseConn.getUsername(), DatabaseConn.getPassword());
								Statement stmt1 = conn.createStatement();
								Statement stmt2 = conn.createStatement();
								Statement stmt3 = conn.createStatement();

								stmt1.executeUpdate("insert into employees (first_name, last_name) values ('"
										+ firstName + "', '" + lastName + "');");
								ResultSet rs1 = stmt3
										.executeQuery("select employee_id from employees where first_name = '"
												+ firstName + "' and last_name = '" + lastName + "';");
								int id = 0;
								while (rs1.next()) {
									id = rs1.getInt(1);
								}
								stmt2.executeUpdate("insert into schedule (employee_id) values (" + id + ")");

								Statement stmt4 = conn.createStatement();
								ResultSet rs2 = stmt1
										.executeQuery("select * from employees where employee_id = " + id + ";");

								while (rs2.next()) {
									listModel.addElement(rs2.getString(2) + " " + rs2.getString(3));

								}

								list.setModel(listModel);
								updateEmployeeDataTable();
							} catch (SQLException e1) {
								e1.printStackTrace();
							}
						}
					}
				});
				btnAddEmployee.setBounds(61, 92, 126, 23);
				panel.add(btnAddEmployee);

				dialog = new JDialog();
				dialog.getContentPane().add(panel);
				dialog.setTitle("Add Employee");
				dialog.setSize(270, 180);
				dialog.setLocationRelativeTo(null);
				dialog.setVisible(true);
			}
		});
		panelEmployees.add(btnAddEmployee);

		JButton btnRemoveEmployee = new JButton("Remove Employee");
		btnRemoveEmployee.setFont(UIManager.getFont("Button.font"));
		btnRemoveEmployee.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Object[] options = { "Yes", "No" };
				int selection = JOptionPane.showOptionDialog(null,
						"Are you sure you want to delete the selected employee? This cannot be undone.", "Warning...",
						JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
				if (selection == 0) {
					try {
						int index = list.getSelectedIndex();
						Connection conn = DriverManager.getConnection(DatabaseConn.getConnection(), DatabaseConn.getUsername(), DatabaseConn.getPassword());
						Statement stmt1 = conn.createStatement();
						Statement stmt2 = conn.createStatement();

						String option = list.getSelectedValue().toString();
						String[] seperateNames = option.trim().split("\\s+");

						ResultSet rs1 = stmt2.executeQuery("select * from employees where first_name = '"
								+ seperateNames[0] + "' and last_name = '" + seperateNames[1] + "';");
						int id = 0;
						while (rs1.next()) {
							id = rs1.getInt(1);
						}

						stmt1.executeUpdate("delete from employees where employee_id = " + id + ";");
						stmt1.executeUpdate("delete from schedule where employee_id = " + id + ";");

						listModel.remove(index);
						list.setModel(listModel);
						updateEmployeeDataTable();
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				} else {
					return;
				}
			}

		});
		btnRemoveEmployee.setBounds(51, 481, 140, 23);
		panelEmployees.add(btnRemoveEmployee);

		JLabel lblAvailability = new JLabel("Availability");
		lblAvailability.setHorizontalAlignment(SwingConstants.CENTER);
		lblAvailability.setBounds(356, 39, 72, 14);
		panelEmployees.add(lblAvailability);

		JLabel lblDays = new JLabel("Days");
		lblDays.setHorizontalAlignment(SwingConstants.CENTER);
		lblDays.setBounds(273, 71, 46, 14);
		panelEmployees.add(lblDays);

		JLabel lblTime = new JLabel("Times");
		lblTime.setHorizontalAlignment(SwingConstants.CENTER);
		lblTime.setBounds(458, 71, 46, 14);
		panelEmployees.add(lblTime);

		chckbxSunday = new JCheckBox("Sunday");
		chckbxSunday.setBounds(239, 114, 97, 23);
		chckbxSunday.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (chckbxSunday.isSelected()) {
					comboBoxSundayStart.setEnabled(true);
					comboBoxSundayFinish.setEnabled(true);
				} else {
					comboBoxSundayStart.setEnabled(false);
					comboBoxSundayFinish.setEnabled(false);
					comboBoxSundayStart.setSelectedIndex(0);
					comboBoxSundayFinish.setSelectedIndex(0);
				}
			}

		});
		panelEmployees.add(chckbxSunday);

		chckbxMonday = new JCheckBox("Monday");
		chckbxMonday.setBounds(239, 150, 97, 23);
		chckbxMonday.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (chckbxMonday.isSelected()) {
					comboBoxMondayStart.setEnabled(true);
					comboBoxMondayFinish.setEnabled(true);
				} else {
					comboBoxMondayStart.setEnabled(false);
					comboBoxMondayFinish.setEnabled(false);
					comboBoxMondayStart.setSelectedIndex(0);
					comboBoxMondayFinish.setSelectedIndex(0);
				}
			}

		});
		panelEmployees.add(chckbxMonday);

		chckbxTuesday = new JCheckBox("Tuesday");
		chckbxTuesday.setBounds(239, 187, 97, 23);
		chckbxTuesday.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (chckbxTuesday.isSelected()) {
					comboBoxTuesdayStart.setEnabled(true);
					comboBoxTuesdayFinish.setEnabled(true);
				} else {
					comboBoxTuesdayStart.setEnabled(false);
					comboBoxTuesdayFinish.setEnabled(false);
					comboBoxTuesdayStart.setSelectedIndex(0);
					comboBoxTuesdayFinish.setSelectedIndex(0);
				}
			}

		});
		panelEmployees.add(chckbxTuesday);

		chckbxWednesday = new JCheckBox("Wednesday");
		chckbxWednesday.setBounds(239, 225, 97, 23);
		chckbxWednesday.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (chckbxWednesday.isSelected()) {
					comboBoxWednesdayStart.setEnabled(true);
					comboBoxWednesdayFinish.setEnabled(true);
				} else {
					comboBoxWednesdayStart.setEnabled(false);
					comboBoxWednesdayFinish.setEnabled(false);
					comboBoxWednesdayStart.setSelectedIndex(0);
					comboBoxWednesdayFinish.setSelectedIndex(0);
				}
			}

		});
		panelEmployees.add(chckbxWednesday);

		chckbxThursday = new JCheckBox("Thursday");
		chckbxThursday.setBounds(239, 263, 97, 23);
		chckbxThursday.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (chckbxThursday.isSelected()) {
					comboBoxThursdayStart.setEnabled(true);
					comboBoxThursdayFinish.setEnabled(true);
				} else {
					comboBoxThursdayStart.setEnabled(false);
					comboBoxThursdayFinish.setEnabled(false);
					comboBoxThursdayStart.setSelectedIndex(0);
					comboBoxThursdayFinish.setSelectedIndex(0);
				}
			}

		});
		panelEmployees.add(chckbxThursday);

		chckbxFriday = new JCheckBox("Friday");
		chckbxFriday.setBounds(239, 300, 97, 23);
		chckbxFriday.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (chckbxFriday.isSelected()) {
					comboBoxFridayStart.setEnabled(true);
					comboBoxFridayFinish.setEnabled(true);
				} else {
					comboBoxFridayStart.setEnabled(false);
					comboBoxFridayFinish.setEnabled(false);
					comboBoxFridayStart.setSelectedIndex(0);
					comboBoxFridayFinish.setSelectedIndex(0);
				}
			}

		});
		panelEmployees.add(chckbxFriday);

		chckbxSaturday = new JCheckBox("Saturday");
		chckbxSaturday.setBounds(239, 335, 97, 23);
		chckbxSaturday.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (chckbxSaturday.isSelected()) {
					comboBoxSaturdayStart.setEnabled(true);
					comboBoxSaturdayFinish.setEnabled(true);
				} else {
					comboBoxSaturdayStart.setEnabled(false);
					comboBoxSaturdayFinish.setEnabled(false);
					comboBoxSaturdayStart.setSelectedIndex(0);
					comboBoxSaturdayFinish.setSelectedIndex(0);
				}
			}

		});
		panelEmployees.add(chckbxSaturday);

		JLabel lblStartingTime = new JLabel("Start Time");
		lblStartingTime.setHorizontalAlignment(SwingConstants.CENTER);
		lblStartingTime.setBounds(405, 90, 72, 14);
		panelEmployees.add(lblStartingTime);

		JLabel lblFinishingTime = new JLabel("Finish Time");
		lblFinishingTime.setHorizontalAlignment(SwingConstants.CENTER);
		lblFinishingTime.setBounds(491, 90, 72, 14);
		panelEmployees.add(lblFinishingTime);

		JButton btnOverwriteEmployeeData = new JButton("Overwrite Employee Data");
		btnOverwriteEmployeeData.setBounds(288, 413, 194, 23);
		btnOverwriteEmployeeData.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				Object[] options = { "Yes", "No" };
				int selection = JOptionPane.showOptionDialog(null, "Are you sure you want to overwrite this data?",
						"Warning...", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options,
						options[0]);
				if (selection == 0) {
					overwriteEmployee();
					updateEmployeeDataTable();
				} else {
					return;
				}
			}
		});
		panelEmployees.add(btnOverwriteEmployeeData);

		timesArrayList.add("12AM");
		timesArrayList.add("1AM");
		timesArrayList.add("2AM");
		timesArrayList.add("3AM");
		timesArrayList.add("4AM");
		timesArrayList.add("5AM");
		timesArrayList.add("6AM");
		timesArrayList.add("7AM");
		timesArrayList.add("8AM");
		timesArrayList.add("9AM");
		timesArrayList.add("10AM");
		timesArrayList.add("11AM");
		timesArrayList.add("12PM");
		timesArrayList.add("1PM");
		timesArrayList.add("2PM");
		timesArrayList.add("3PM");
		timesArrayList.add("4PM");
		timesArrayList.add("5PM");
		timesArrayList.add("6PM");
		timesArrayList.add("7PM");
		timesArrayList.add("8PM");
		timesArrayList.add("9PM");
		timesArrayList.add("10PM");
		timesArrayList.add("11PM");

		comboBoxSundayStart = new JComboBox(timesArrayList.toArray());
		comboBoxSundayStart.setBounds(410, 115, 62, 20);
		panelEmployees.add(comboBoxSundayStart);

		comboBoxSundayFinish = new JComboBox(timesArrayList.toArray());
		comboBoxSundayFinish.setBounds(496, 115, 62, 20);
		panelEmployees.add(comboBoxSundayFinish);

		comboBoxMondayStart = new JComboBox(timesArrayList.toArray());
		comboBoxMondayStart.setBounds(410, 151, 62, 20);
		panelEmployees.add(comboBoxMondayStart);

		comboBoxMondayFinish = new JComboBox(timesArrayList.toArray());
		comboBoxMondayFinish.setBounds(496, 151, 62, 20);
		panelEmployees.add(comboBoxMondayFinish);

		comboBoxTuesdayStart = new JComboBox(timesArrayList.toArray());
		comboBoxTuesdayStart.setBounds(410, 188, 62, 20);
		panelEmployees.add(comboBoxTuesdayStart);

		comboBoxTuesdayFinish = new JComboBox(timesArrayList.toArray());
		comboBoxTuesdayFinish.setBounds(496, 188, 62, 20);
		panelEmployees.add(comboBoxTuesdayFinish);

		comboBoxWednesdayStart = new JComboBox(timesArrayList.toArray());
		comboBoxWednesdayStart.setBounds(410, 226, 62, 20);
		panelEmployees.add(comboBoxWednesdayStart);

		comboBoxWednesdayFinish = new JComboBox(timesArrayList.toArray());
		comboBoxWednesdayFinish.setBounds(496, 226, 62, 20);
		panelEmployees.add(comboBoxWednesdayFinish);

		comboBoxThursdayStart = new JComboBox(timesArrayList.toArray());
		comboBoxThursdayStart.setBounds(410, 264, 62, 20);
		panelEmployees.add(comboBoxThursdayStart);

		comboBoxThursdayFinish = new JComboBox(timesArrayList.toArray());
		comboBoxThursdayFinish.setBounds(496, 264, 62, 20);
		panelEmployees.add(comboBoxThursdayFinish);

		comboBoxFridayStart = new JComboBox(timesArrayList.toArray());
		comboBoxFridayStart.setBounds(410, 301, 62, 20);
		panelEmployees.add(comboBoxFridayStart);

		comboBoxFridayFinish = new JComboBox(timesArrayList.toArray());
		comboBoxFridayFinish.setBounds(496, 303, 62, 20);
		panelEmployees.add(comboBoxFridayFinish);

		comboBoxSaturdayStart = new JComboBox(timesArrayList.toArray());
		comboBoxSaturdayStart.setBounds(410, 336, 62, 20);
		panelEmployees.add(comboBoxSaturdayStart);

		comboBoxSaturdayFinish = new JComboBox(timesArrayList.toArray());
		comboBoxSaturdayFinish.setBounds(496, 336, 62, 20);
		panelEmployees.add(comboBoxSaturdayFinish);

		updateEmployeeFields();

		JPanel panelSchedule = new JPanel();
		tabbedPane.add("Schedule", panelSchedule);
		panelSchedule.setLayout(null);

		table = new JTable();
		table.setBorder(new LineBorder(new Color(0, 0, 0)));
		dtm = new DefaultTableModel(0, 0);
		String[] columnNames = new String[] { "Name", "Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday",
				"Saturday" };
		dtm.setColumnIdentifiers(columnNames);
		updateEmployeeDataTable();
		table.setModel(dtm);
		table.setBounds(10, 53, 567, 423);
		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setSize(567, 423);
		scrollPane.setLocation(10, 53);
		panelSchedule.add(scrollPane);

		JLabel lblSchedule = new JLabel("Schedule");
		lblSchedule.setHorizontalAlignment(SwingConstants.CENTER);
		lblSchedule.setFont(new Font("Tahoma", Font.BOLD, 13));
		lblSchedule.setBounds(227, 21, 122, 14);
		panelSchedule.add(lblSchedule);

		// Button that handles the printing of the work schedule. Probably my
		// favorite part of the entire program.
		JButton btnPrintSchedule = new JButton("Print Schedule");
		btnPrintSchedule.setBounds(227, 487, 122, 23);
		btnPrintSchedule.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					MessageFormat header = new MessageFormat("Work Schedule");
					MessageFormat footer = new MessageFormat("By Colin Sanders, 2017");
					boolean complete = table.print(PrintMode.FIT_WIDTH, header, footer);
					if (complete) {
						JOptionPane.showMessageDialog(null, "Printing success! (Probably..)");
					} else {
						JOptionPane.showMessageDialog(null, "Printing cancelled.");
					}
				} catch (PrinterException pe) {
					JOptionPane.showMessageDialog(null, "Printing failed...");
				}
			}
		});
		panelSchedule.add(btnPrintSchedule);

		JPanel panelCustomers = new JPanel();
		tabbedPane.add("Customers", panelCustomers);

		add(tabbedPane);

		dtm2 = new DefaultTableModel(0, 0){
			@Override
			public boolean isCellEditable(int row, int column){
				return false;
			}
		};
		
		table2 = new JTable();
		String[] columnNames2 = new String[] {"Customer ID", "Name", "Times visisted."};
		dtm2.setColumnIdentifiers(columnNames2);
		table2.setModel(dtm2);
		// Customer data table.
		try{
			//get connection
			Connection conn1 = DriverManager.getConnection(DatabaseConn.getConnection(), DatabaseConn.getUsername(), DatabaseConn.getPassword());
			
			//write statement
			Statement stmt3 = conn1.createStatement();
			
			//execute query
			ResultSet rs1 = stmt3.executeQuery("select * from customers");
			
			//process result set
			while(rs1.next()){
				Object[] data = new Object[3];
				data[0] = rs1.getInt(1);
				data[1] = (rs1.getString(2) + " " + rs1.getString(3));
				data[2] = rs1.getString(4);
				
				dtm2.insertRow(0, data);
			}
			rs1.close();
			
		}catch(Exception e){
			e.printStackTrace();
		}
		panelCustomers.setLayout(null);

		JScrollPane scrollPane2 = new JScrollPane(table2);
		scrollPane2.setBounds(10, 216, 567, 312);
		panelCustomers.add(scrollPane2);

		JLabel lblCustomerReport = new JLabel("Customer Report");
		lblCustomerReport.setHorizontalAlignment(SwingConstants.CENTER);
		lblCustomerReport.setFont(new Font("Tahoma", Font.BOLD, 13));
		lblCustomerReport.setBounds(219, 11, 135, 14);
		panelCustomers.add(lblCustomerReport);

		JLabel lblMostVisitingCustomer = new JLabel("Most Visiting Customer: ");
		lblMostVisitingCustomer.setHorizontalAlignment(SwingConstants.CENTER);
		lblMostVisitingCustomer.setBounds(10, 90, 142, 14);
		panelCustomers.add(lblMostVisitingCustomer);

		textBestCustomer = new JTextField();
		textBestCustomer.setBounds(161, 88, 127, 23);
		textBestCustomer.setText("Click Generate.");
		textBestCustomer.setEditable(false);
		panelCustomers.add(textBestCustomer);
		textBestCustomer.setColumns(10);

		JLabel lblMostFrequentTime = new JLabel("<html>Most Frequent Time of Customer Activity:</html> ");
		lblMostFrequentTime.setBounds(316, 73, 127, 55);
		panelCustomers.add(lblMostFrequentTime);

		textBestTime = new JTextField();
		textBestTime.setBounds(441, 88, 124, 23);
		textBestTime.setText("Click Generate.");
		textBestTime.setEditable(false);
		panelCustomers.add(textBestTime);
		textBestTime.setColumns(10);

		// Same as the button that printed out the customer data.
		JButton btnPrintCustomerData = new JButton("Print Customer Data");
		btnPrintCustomerData.setBounds(327, 168, 154, 23);
		btnPrintCustomerData.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					MessageFormat header = new MessageFormat("Customer Data");
					MessageFormat footer = new MessageFormat("By Colin Sanders, 2017");
					boolean complete = table.print(PrintMode.FIT_WIDTH, header, footer);
					if (complete) {
						JOptionPane.showMessageDialog(null, "Printing success! (Probably..)");
					} else {
						JOptionPane.showMessageDialog(null, "Printing cancelled.");
					}
				} catch (PrinterException pe) {
					JOptionPane.showMessageDialog(null, "Printing failed...");
				}
			}
		});
		panelCustomers.add(btnPrintCustomerData);

		// Takes the data of the customers and processes it to find trends, such
		// as the most frequently recurring customer,
		// day, time, etc. Could definitely expand on the functionallity here,
		// it's one of the weaker sections.
		JButton btnGenerateNewReport = new JButton("Generate New Report");
		btnGenerateNewReport.setBounds(80, 168, 167, 23);
		btnGenerateNewReport.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				JOptionPane.showMessageDialog(null, "Refreshing data and generating report.");
				try{
					//Best Customer
					Connection conn = DriverManager.getConnection(DatabaseConn.getConnection(), DatabaseConn.getUsername(), DatabaseConn.getPassword());
					Statement stmt1 = conn.createStatement();
					ResultSet rs1 = stmt1.executeQuery("select MAX(num_of_visits) from customers");
					int idMaxVists = 0;
					int multipleMaxes = 0;
					while(rs1.next()){
						idMaxVists = rs1.getInt(1);
					}
					
					Statement stmt2 = conn.createStatement();
					ResultSet rs2 = stmt2.executeQuery("select * from customers where num_of_visits = " + idMaxVists + ";");
					while(rs2.next()){
						multipleMaxes++;
					}
					
					if(multipleMaxes > 1){
						JOptionPane.showMessageDialog(null, "Multiple matches found, cannot determine best customer. ");
					}else{
						Statement stmt3 = conn.createStatement();
						ResultSet rs3 = stmt3.executeQuery("select * from customers where num_of_visits = " + idMaxVists + ";");
						while(rs3.next()){
							textBestCustomer.setText(rs3.getString(2) + " " + rs3.getString(3));
						}
					}
					
					// Best Time
					Statement stmt4 = conn.createStatement();
					ResultSet rs4 = stmt4.executeQuery("select time from check_in;");
					List<String> timeList = new ArrayList<String>();
					String[] timeListUnsplit = null;
					while(rs4.next()){
						timeListUnsplit = rs4.getString(1).trim().split(":");
						timeList.add(timeListUnsplit[0]);
					}
					
					Map<String, Integer> map = new HashMap<String, Integer>();

					for(int i = 0; i < timeList.size(); i++){
					   if(map.get(timeList.get(i)) == null){
					      map.put(timeList.get(i),1);
					   }else{
					      map.put(timeList.get(i), map.get(timeList.get(i)) + 1);
					   }
					}
					int largest = 0;
					String stringOfLargest = null;
					for (Entry<String, Integer> entry : map.entrySet()) {
					   String key = entry.getKey();
					   int value = entry.getValue();
					   if( value > largest){
					      largest = value;
					      stringOfLargest = key;
					   }
					}				
					int intOfLargest = Integer.parseInt(stringOfLargest);		
					textBestTime.setText(getTime12(intOfLargest));
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		});
		panelCustomers.add(btnGenerateNewReport);
	}
	
	/**
	 * Quick method to update all of the GUI fields with the data of a specified
	 * employee.
	 * 
	 * @param id
	 */
	public void updateEmployeeFields() {
		chckbxSunday.setSelected(false);
		chckbxMonday.setSelected(false);
		chckbxTuesday.setSelected(false);
		chckbxWednesday.setSelected(false);
		chckbxThursday.setSelected(false);
		chckbxFriday.setSelected(false);
		chckbxSaturday.setSelected(false);

		try {
			Connection conn = DriverManager.getConnection(DatabaseConn.getConnection(), DatabaseConn.getUsername(), DatabaseConn.getPassword());
			Statement stmt1 = conn.createStatement();
			ResultSet rs1 = stmt1.executeQuery("select * from schedule;");

			while (rs1.next()) {
				if (rs1.getInt(16) == currentEmployeeID) {
					if (rs1.getInt(2) != -1) {
						chckbxMonday.setSelected(true);
						comboBoxMondayStart.setEnabled(true);
						comboBoxMondayFinish.setEnabled(true);

						if (rs1.getInt(2) == 0) {
							comboBoxMondayStart.setSelectedItem(timesArrayList.get(0));
						} else if (rs1.getInt(2) == 1) {
							comboBoxMondayStart.setSelectedItem(timesArrayList.get(1));
						} else if (rs1.getInt(2) == 2) {
							comboBoxMondayStart.setSelectedItem(timesArrayList.get(2));
						} else if (rs1.getInt(2) == 3) {
							comboBoxMondayStart.setSelectedItem(timesArrayList.get(3));
						} else if (rs1.getInt(2) == 4) {
							comboBoxMondayStart.setSelectedItem(timesArrayList.get(4));
						} else if (rs1.getInt(2) == 5) {
							comboBoxMondayStart.setSelectedItem(timesArrayList.get(5));
						} else if (rs1.getInt(2) == 6) {
							comboBoxMondayStart.setSelectedItem(timesArrayList.get(6));
						} else if (rs1.getInt(2) == 7) {
							comboBoxMondayStart.setSelectedItem(timesArrayList.get(7));
						} else if (rs1.getInt(2) == 8) {
							comboBoxMondayStart.setSelectedItem(timesArrayList.get(8));
						} else if (rs1.getInt(2) == 9) {
							comboBoxMondayStart.setSelectedItem(timesArrayList.get(9));
						} else if (rs1.getInt(2) == 10) {
							comboBoxMondayStart.setSelectedItem(timesArrayList.get(10));
						} else if (rs1.getInt(2) == 11) {
							comboBoxMondayStart.setSelectedItem(timesArrayList.get(11));
						} else if (rs1.getInt(2) == 12) {
							comboBoxMondayStart.setSelectedItem(timesArrayList.get(12));
						} else if (rs1.getInt(2) == 13) {
							comboBoxMondayStart.setSelectedItem(timesArrayList.get(13));
						} else if (rs1.getInt(2) == 14) {
							comboBoxMondayStart.setSelectedItem(timesArrayList.get(14));
						} else if (rs1.getInt(2) == 15) {
							comboBoxMondayStart.setSelectedItem(timesArrayList.get(15));
						} else if (rs1.getInt(2) == 16) {
							comboBoxMondayStart.setSelectedItem(timesArrayList.get(16));
						} else if (rs1.getInt(2) == 17) {
							comboBoxMondayStart.setSelectedItem(timesArrayList.get(17));
						} else if (rs1.getInt(2) == 18) {
							comboBoxMondayStart.setSelectedItem(timesArrayList.get(18));
						} else if (rs1.getInt(2) == 19) {
							comboBoxMondayStart.setSelectedItem(timesArrayList.get(19));
						} else if (rs1.getInt(2) == 20) {
							comboBoxMondayStart.setSelectedItem(timesArrayList.get(20));
						} else if (rs1.getInt(2) == 21) {
							comboBoxMondayStart.setSelectedItem(timesArrayList.get(21));
						} else if (rs1.getInt(2) == 22) {
							comboBoxMondayStart.setSelectedItem(timesArrayList.get(22));
						} else if (rs1.getInt(2) == 23) {
							comboBoxMondayStart.setSelectedItem(timesArrayList.get(23));
						}

						if (rs1.getInt(3) == 0) {
							comboBoxMondayFinish.setSelectedItem(timesArrayList.get(0));
						} else if (rs1.getInt(3) == 1) {
							comboBoxMondayFinish.setSelectedItem(timesArrayList.get(1));
						} else if (rs1.getInt(3) == 2) {
							comboBoxMondayFinish.setSelectedItem(timesArrayList.get(2));
						} else if (rs1.getInt(3) == 3) {
							comboBoxMondayFinish.setSelectedItem(timesArrayList.get(3));
						} else if (rs1.getInt(3) == 4) {
							comboBoxMondayFinish.setSelectedItem(timesArrayList.get(4));
						} else if (rs1.getInt(3) == 5) {
							comboBoxMondayFinish.setSelectedItem(timesArrayList.get(5));
						} else if (rs1.getInt(3) == 6) {
							comboBoxMondayFinish.setSelectedItem(timesArrayList.get(6));
						} else if (rs1.getInt(3) == 7) {
							comboBoxMondayFinish.setSelectedItem(timesArrayList.get(7));
						} else if (rs1.getInt(3) == 8) {
							comboBoxMondayFinish.setSelectedItem(timesArrayList.get(8));
						} else if (rs1.getInt(3) == 9) {
							comboBoxMondayFinish.setSelectedItem(timesArrayList.get(9));
						} else if (rs1.getInt(3) == 10) {
							comboBoxMondayFinish.setSelectedItem(timesArrayList.get(10));
						} else if (rs1.getInt(3) == 11) {
							comboBoxMondayFinish.setSelectedItem(timesArrayList.get(11));
						} else if (rs1.getInt(3) == 12) {
							comboBoxMondayFinish.setSelectedItem(timesArrayList.get(12));
						} else if (rs1.getInt(3) == 13) {
							comboBoxMondayFinish.setSelectedItem(timesArrayList.get(13));
						} else if (rs1.getInt(3) == 14) {
							comboBoxMondayFinish.setSelectedItem(timesArrayList.get(14));
						} else if (rs1.getInt(3) == 15) {
							comboBoxMondayFinish.setSelectedItem(timesArrayList.get(15));
						} else if (rs1.getInt(3) == 16) {
							comboBoxMondayFinish.setSelectedItem(timesArrayList.get(16));
						} else if (rs1.getInt(3) == 17) {
							comboBoxMondayFinish.setSelectedItem(timesArrayList.get(17));
						} else if (rs1.getInt(3) == 18) {
							comboBoxMondayFinish.setSelectedItem(timesArrayList.get(18));
						} else if (rs1.getInt(3) == 19) {
							comboBoxMondayFinish.setSelectedItem(timesArrayList.get(19));
						} else if (rs1.getInt(3) == 20) {
							comboBoxMondayFinish.setSelectedItem(timesArrayList.get(20));
						} else if (rs1.getInt(3) == 21) {
							comboBoxMondayFinish.setSelectedItem(timesArrayList.get(21));
						} else if (rs1.getInt(3) == 22) {
							comboBoxMondayFinish.setSelectedItem(timesArrayList.get(22));
						} else if (rs1.getInt(3) == 23) {
							comboBoxMondayFinish.setSelectedItem(timesArrayList.get(23));
						}
					} else {
						comboBoxMondayStart.setEnabled(false);
						comboBoxMondayFinish.setEnabled(false);
						chckbxMonday.setSelected(false);

						comboBoxMondayStart.setSelectedItem(timesArrayList.get(0));
						comboBoxMondayFinish.setSelectedItem(timesArrayList.get(0));
					}
					if (rs1.getInt(4) != -1) {
						chckbxTuesday.setSelected(true);
						comboBoxTuesdayStart.setEnabled(true);
						comboBoxTuesdayFinish.setEnabled(true);

						if (rs1.getInt(4) == 0) {
							comboBoxTuesdayStart.setSelectedItem(timesArrayList.get(0));
						} else if (rs1.getInt(4) == 1) {
							comboBoxTuesdayStart.setSelectedItem(timesArrayList.get(1));
						} else if (rs1.getInt(4) == 2) {
							comboBoxTuesdayStart.setSelectedItem(timesArrayList.get(2));
						} else if (rs1.getInt(4) == 3) {
							comboBoxTuesdayStart.setSelectedItem(timesArrayList.get(3));
						} else if (rs1.getInt(4) == 4) {
							comboBoxTuesdayStart.setSelectedItem(timesArrayList.get(4));
						} else if (rs1.getInt(4) == 5) {
							comboBoxTuesdayStart.setSelectedItem(timesArrayList.get(5));
						} else if (rs1.getInt(4) == 6) {
							comboBoxTuesdayStart.setSelectedItem(timesArrayList.get(6));
						} else if (rs1.getInt(4) == 7) {
							comboBoxTuesdayStart.setSelectedItem(timesArrayList.get(7));
						} else if (rs1.getInt(4) == 8) {
							comboBoxTuesdayStart.setSelectedItem(timesArrayList.get(8));
						} else if (rs1.getInt(4) == 9) {
							comboBoxTuesdayStart.setSelectedItem(timesArrayList.get(9));
						} else if (rs1.getInt(4) == 10) {
							comboBoxTuesdayStart.setSelectedItem(timesArrayList.get(10));
						} else if (rs1.getInt(4) == 11) {
							comboBoxTuesdayStart.setSelectedItem(timesArrayList.get(11));
						} else if (rs1.getInt(4) == 12) {
							comboBoxTuesdayStart.setSelectedItem(timesArrayList.get(12));
						} else if (rs1.getInt(4) == 13) {
							comboBoxTuesdayStart.setSelectedItem(timesArrayList.get(13));
						} else if (rs1.getInt(4) == 14) {
							comboBoxTuesdayStart.setSelectedItem(timesArrayList.get(14));
						} else if (rs1.getInt(4) == 15) {
							comboBoxTuesdayStart.setSelectedItem(timesArrayList.get(15));
						} else if (rs1.getInt(4) == 16) {
							comboBoxTuesdayStart.setSelectedItem(timesArrayList.get(16));
						} else if (rs1.getInt(4) == 17) {
							comboBoxTuesdayStart.setSelectedItem(timesArrayList.get(17));
						} else if (rs1.getInt(4) == 18) {
							comboBoxTuesdayStart.setSelectedItem(timesArrayList.get(18));
						} else if (rs1.getInt(4) == 19) {
							comboBoxTuesdayStart.setSelectedItem(timesArrayList.get(19));
						} else if (rs1.getInt(4) == 20) {
							comboBoxTuesdayStart.setSelectedItem(timesArrayList.get(20));
						} else if (rs1.getInt(4) == 21) {
							comboBoxTuesdayStart.setSelectedItem(timesArrayList.get(21));
						} else if (rs1.getInt(4) == 22) {
							comboBoxTuesdayStart.setSelectedItem(timesArrayList.get(22));
						} else if (rs1.getInt(4) == 23) {
							comboBoxTuesdayStart.setSelectedItem(timesArrayList.get(23));
						}

						if (rs1.getInt(5) == 0) {
							comboBoxTuesdayFinish.setSelectedItem(timesArrayList.get(0));
						} else if (rs1.getInt(5) == 1) {
							comboBoxTuesdayFinish.setSelectedItem(timesArrayList.get(1));
						} else if (rs1.getInt(5) == 2) {
							comboBoxTuesdayFinish.setSelectedItem(timesArrayList.get(2));
						} else if (rs1.getInt(5) == 3) {
							comboBoxTuesdayFinish.setSelectedItem(timesArrayList.get(3));
						} else if (rs1.getInt(5) == 4) {
							comboBoxTuesdayFinish.setSelectedItem(timesArrayList.get(4));
						} else if (rs1.getInt(5) == 5) {
							comboBoxTuesdayFinish.setSelectedItem(timesArrayList.get(5));
						} else if (rs1.getInt(5) == 6) {
							comboBoxTuesdayFinish.setSelectedItem(timesArrayList.get(6));
						} else if (rs1.getInt(5) == 7) {
							comboBoxTuesdayFinish.setSelectedItem(timesArrayList.get(7));
						} else if (rs1.getInt(5) == 8) {
							comboBoxTuesdayFinish.setSelectedItem(timesArrayList.get(8));
						} else if (rs1.getInt(5) == 9) {
							comboBoxTuesdayFinish.setSelectedItem(timesArrayList.get(9));
						} else if (rs1.getInt(5) == 10) {
							comboBoxTuesdayFinish.setSelectedItem(timesArrayList.get(10));
						} else if (rs1.getInt(5) == 11) {
							comboBoxTuesdayFinish.setSelectedItem(timesArrayList.get(11));
						} else if (rs1.getInt(5) == 12) {
							comboBoxTuesdayFinish.setSelectedItem(timesArrayList.get(12));
						} else if (rs1.getInt(5) == 13) {
							comboBoxTuesdayFinish.setSelectedItem(timesArrayList.get(13));
						} else if (rs1.getInt(5) == 14) {
							comboBoxTuesdayFinish.setSelectedItem(timesArrayList.get(14));
						} else if (rs1.getInt(5) == 15) {
							comboBoxTuesdayFinish.setSelectedItem(timesArrayList.get(15));
						} else if (rs1.getInt(5) == 16) {
							comboBoxTuesdayFinish.setSelectedItem(timesArrayList.get(16));
						} else if (rs1.getInt(5) == 17) {
							comboBoxTuesdayFinish.setSelectedItem(timesArrayList.get(17));
						} else if (rs1.getInt(5) == 18) {
							comboBoxTuesdayFinish.setSelectedItem(timesArrayList.get(18));
						} else if (rs1.getInt(5) == 19) {
							comboBoxTuesdayFinish.setSelectedItem(timesArrayList.get(19));
						} else if (rs1.getInt(5) == 20) {
							comboBoxTuesdayFinish.setSelectedItem(timesArrayList.get(20));
						} else if (rs1.getInt(5) == 21) {
							comboBoxTuesdayFinish.setSelectedItem(timesArrayList.get(21));
						} else if (rs1.getInt(5) == 22) {
							comboBoxTuesdayFinish.setSelectedItem(timesArrayList.get(22));
						} else if (rs1.getInt(5) == 23) {
							comboBoxTuesdayFinish.setSelectedItem(timesArrayList.get(23));
						}
					} else {
						comboBoxTuesdayStart.setEnabled(false);
						comboBoxTuesdayFinish.setEnabled(false);
						chckbxTuesday.setSelected(false);

						comboBoxTuesdayStart.setSelectedItem(timesArrayList.get(0));
						comboBoxTuesdayFinish.setSelectedItem(timesArrayList.get(0));
					}
					if (rs1.getInt(6) != -1) {
						chckbxWednesday.setSelected(true);
						comboBoxWednesdayStart.setEnabled(true);
						comboBoxWednesdayFinish.setEnabled(true);

						if (rs1.getInt(6) == 0) {
							comboBoxWednesdayStart.setSelectedItem(timesArrayList.get(0));
						} else if (rs1.getInt(6) == 1) {
							comboBoxWednesdayStart.setSelectedItem(timesArrayList.get(1));
						} else if (rs1.getInt(6) == 2) {
							comboBoxWednesdayStart.setSelectedItem(timesArrayList.get(2));
						} else if (rs1.getInt(6) == 3) {
							comboBoxWednesdayStart.setSelectedItem(timesArrayList.get(3));
						} else if (rs1.getInt(6) == 4) {
							comboBoxWednesdayStart.setSelectedItem(timesArrayList.get(4));
						} else if (rs1.getInt(6) == 5) {
							comboBoxWednesdayStart.setSelectedItem(timesArrayList.get(5));
						} else if (rs1.getInt(6) == 6) {
							comboBoxWednesdayStart.setSelectedItem(timesArrayList.get(6));
						} else if (rs1.getInt(6) == 7) {
							comboBoxWednesdayStart.setSelectedItem(timesArrayList.get(7));
						} else if (rs1.getInt(6) == 8) {
							comboBoxWednesdayStart.setSelectedItem(timesArrayList.get(8));
						} else if (rs1.getInt(6) == 9) {
							comboBoxWednesdayStart.setSelectedItem(timesArrayList.get(9));
						} else if (rs1.getInt(6) == 10) {
							comboBoxWednesdayStart.setSelectedItem(timesArrayList.get(10));
						} else if (rs1.getInt(6) == 11) {
							comboBoxWednesdayStart.setSelectedItem(timesArrayList.get(11));
						} else if (rs1.getInt(6) == 12) {
							comboBoxWednesdayStart.setSelectedItem(timesArrayList.get(12));
						} else if (rs1.getInt(6) == 13) {
							comboBoxWednesdayStart.setSelectedItem(timesArrayList.get(13));
						} else if (rs1.getInt(6) == 14) {
							comboBoxWednesdayStart.setSelectedItem(timesArrayList.get(14));
						} else if (rs1.getInt(6) == 15) {
							comboBoxWednesdayStart.setSelectedItem(timesArrayList.get(15));
						} else if (rs1.getInt(6) == 16) {
							comboBoxWednesdayStart.setSelectedItem(timesArrayList.get(16));
						} else if (rs1.getInt(6) == 17) {
							comboBoxWednesdayStart.setSelectedItem(timesArrayList.get(17));
						} else if (rs1.getInt(6) == 18) {
							comboBoxWednesdayStart.setSelectedItem(timesArrayList.get(18));
						} else if (rs1.getInt(6) == 19) {
							comboBoxWednesdayStart.setSelectedItem(timesArrayList.get(19));
						} else if (rs1.getInt(6) == 20) {
							comboBoxWednesdayStart.setSelectedItem(timesArrayList.get(20));
						} else if (rs1.getInt(6) == 21) {
							comboBoxWednesdayStart.setSelectedItem(timesArrayList.get(21));
						} else if (rs1.getInt(6) == 22) {
							comboBoxWednesdayStart.setSelectedItem(timesArrayList.get(22));
						} else if (rs1.getInt(6) == 23) {
							comboBoxWednesdayStart.setSelectedItem(timesArrayList.get(23));
						}

						if (rs1.getInt(7) == 0) {
							comboBoxWednesdayFinish.setSelectedItem(timesArrayList.get(0));
						} else if (rs1.getInt(7) == 1) {
							comboBoxWednesdayFinish.setSelectedItem(timesArrayList.get(1));
						} else if (rs1.getInt(7) == 2) {
							comboBoxWednesdayFinish.setSelectedItem(timesArrayList.get(2));
						} else if (rs1.getInt(7) == 3) {
							comboBoxWednesdayFinish.setSelectedItem(timesArrayList.get(3));
						} else if (rs1.getInt(7) == 4) {
							comboBoxWednesdayFinish.setSelectedItem(timesArrayList.get(4));
						} else if (rs1.getInt(7) == 5) {
							comboBoxWednesdayFinish.setSelectedItem(timesArrayList.get(5));
						} else if (rs1.getInt(7) == 6) {
							comboBoxWednesdayFinish.setSelectedItem(timesArrayList.get(6));
						} else if (rs1.getInt(7) == 7) {
							comboBoxWednesdayFinish.setSelectedItem(timesArrayList.get(7));
						} else if (rs1.getInt(7) == 8) {
							comboBoxWednesdayFinish.setSelectedItem(timesArrayList.get(8));
						} else if (rs1.getInt(7) == 9) {
							comboBoxWednesdayFinish.setSelectedItem(timesArrayList.get(9));
						} else if (rs1.getInt(7) == 10) {
							comboBoxWednesdayFinish.setSelectedItem(timesArrayList.get(10));
						} else if (rs1.getInt(7) == 11) {
							comboBoxWednesdayFinish.setSelectedItem(timesArrayList.get(11));
						} else if (rs1.getInt(7) == 12) {
							comboBoxWednesdayFinish.setSelectedItem(timesArrayList.get(12));
						} else if (rs1.getInt(7) == 13) {
							comboBoxWednesdayFinish.setSelectedItem(timesArrayList.get(13));
						} else if (rs1.getInt(7) == 14) {
							comboBoxWednesdayFinish.setSelectedItem(timesArrayList.get(14));
						} else if (rs1.getInt(7) == 15) {
							comboBoxWednesdayFinish.setSelectedItem(timesArrayList.get(15));
						} else if (rs1.getInt(7) == 16) {
							comboBoxWednesdayFinish.setSelectedItem(timesArrayList.get(16));
						} else if (rs1.getInt(7) == 17) {
							comboBoxWednesdayFinish.setSelectedItem(timesArrayList.get(17));
						} else if (rs1.getInt(7) == 18) {
							comboBoxWednesdayFinish.setSelectedItem(timesArrayList.get(18));
						} else if (rs1.getInt(7) == 19) {
							comboBoxWednesdayFinish.setSelectedItem(timesArrayList.get(19));
						} else if (rs1.getInt(7) == 20) {
							comboBoxWednesdayFinish.setSelectedItem(timesArrayList.get(20));
						} else if (rs1.getInt(7) == 21) {
							comboBoxWednesdayFinish.setSelectedItem(timesArrayList.get(21));
						} else if (rs1.getInt(7) == 22) {
							comboBoxWednesdayFinish.setSelectedItem(timesArrayList.get(22));
						} else if (rs1.getInt(7) == 23) {
							comboBoxWednesdayFinish.setSelectedItem(timesArrayList.get(23));
						}
					} else {
						comboBoxWednesdayStart.setEnabled(false);
						comboBoxWednesdayFinish.setEnabled(false);
						chckbxWednesday.setSelected(false);

						comboBoxWednesdayStart.setSelectedItem(timesArrayList.get(0));
						comboBoxWednesdayFinish.setSelectedItem(timesArrayList.get(0));
					}
					if (rs1.getInt(7) != -1) {
						chckbxThursday.setSelected(true);
						comboBoxThursdayStart.setEnabled(true);
						comboBoxThursdayFinish.setEnabled(true);

						if (rs1.getInt(8) == 0) {
							comboBoxThursdayStart.setSelectedItem(timesArrayList.get(0));
						} else if (rs1.getInt(8) == 1) {
							comboBoxThursdayStart.setSelectedItem(timesArrayList.get(1));
						} else if (rs1.getInt(8) == 2) {
							comboBoxThursdayStart.setSelectedItem(timesArrayList.get(2));
						} else if (rs1.getInt(8) == 3) {
							comboBoxThursdayStart.setSelectedItem(timesArrayList.get(3));
						} else if (rs1.getInt(8) == 4) {
							comboBoxThursdayStart.setSelectedItem(timesArrayList.get(4));
						} else if (rs1.getInt(8) == 5) {
							comboBoxThursdayStart.setSelectedItem(timesArrayList.get(5));
						} else if (rs1.getInt(8) == 6) {
							comboBoxThursdayStart.setSelectedItem(timesArrayList.get(6));
						} else if (rs1.getInt(8) == 7) {
							comboBoxThursdayStart.setSelectedItem(timesArrayList.get(7));
						} else if (rs1.getInt(8) == 8) {
							comboBoxThursdayStart.setSelectedItem(timesArrayList.get(8));
						} else if (rs1.getInt(8) == 9) {
							comboBoxThursdayStart.setSelectedItem(timesArrayList.get(9));
						} else if (rs1.getInt(8) == 10) {
							comboBoxThursdayStart.setSelectedItem(timesArrayList.get(10));
						} else if (rs1.getInt(8) == 11) {
							comboBoxThursdayStart.setSelectedItem(timesArrayList.get(11));
						} else if (rs1.getInt(8) == 12) {
							comboBoxThursdayStart.setSelectedItem(timesArrayList.get(12));
						} else if (rs1.getInt(8) == 13) {
							comboBoxThursdayStart.setSelectedItem(timesArrayList.get(13));
						} else if (rs1.getInt(8) == 14) {
							comboBoxThursdayStart.setSelectedItem(timesArrayList.get(14));
						} else if (rs1.getInt(8) == 15) {
							comboBoxThursdayStart.setSelectedItem(timesArrayList.get(15));
						} else if (rs1.getInt(8) == 16) {
							comboBoxThursdayStart.setSelectedItem(timesArrayList.get(16));
						} else if (rs1.getInt(8) == 17) {
							comboBoxThursdayStart.setSelectedItem(timesArrayList.get(17));
						} else if (rs1.getInt(8) == 18) {
							comboBoxThursdayStart.setSelectedItem(timesArrayList.get(18));
						} else if (rs1.getInt(8) == 19) {
							comboBoxThursdayStart.setSelectedItem(timesArrayList.get(19));
						} else if (rs1.getInt(8) == 20) {
							comboBoxThursdayStart.setSelectedItem(timesArrayList.get(20));
						} else if (rs1.getInt(8) == 21) {
							comboBoxThursdayStart.setSelectedItem(timesArrayList.get(21));
						} else if (rs1.getInt(8) == 22) {
							comboBoxThursdayStart.setSelectedItem(timesArrayList.get(22));
						} else if (rs1.getInt(8) == 23) {
							comboBoxThursdayStart.setSelectedItem(timesArrayList.get(23));
						}

						if (rs1.getInt(9) == 0) {
							comboBoxThursdayFinish.setSelectedItem(timesArrayList.get(0));
						} else if (rs1.getInt(9) == 1) {
							comboBoxThursdayFinish.setSelectedItem(timesArrayList.get(1));
						} else if (rs1.getInt(9) == 2) {
							comboBoxThursdayFinish.setSelectedItem(timesArrayList.get(2));
						} else if (rs1.getInt(9) == 3) {
							comboBoxThursdayFinish.setSelectedItem(timesArrayList.get(3));
						} else if (rs1.getInt(9) == 4) {
							comboBoxThursdayFinish.setSelectedItem(timesArrayList.get(4));
						} else if (rs1.getInt(9) == 5) {
							comboBoxThursdayFinish.setSelectedItem(timesArrayList.get(5));
						} else if (rs1.getInt(9) == 6) {
							comboBoxThursdayFinish.setSelectedItem(timesArrayList.get(6));
						} else if (rs1.getInt(9) == 7) {
							comboBoxThursdayFinish.setSelectedItem(timesArrayList.get(7));
						} else if (rs1.getInt(9) == 8) {
							comboBoxThursdayFinish.setSelectedItem(timesArrayList.get(8));
						} else if (rs1.getInt(9) == 9) {
							comboBoxThursdayFinish.setSelectedItem(timesArrayList.get(9));
						} else if (rs1.getInt(9) == 10) {
							comboBoxThursdayFinish.setSelectedItem(timesArrayList.get(10));
						} else if (rs1.getInt(9) == 11) {
							comboBoxThursdayFinish.setSelectedItem(timesArrayList.get(11));
						} else if (rs1.getInt(9) == 12) {
							comboBoxThursdayFinish.setSelectedItem(timesArrayList.get(12));
						} else if (rs1.getInt(9) == 13) {
							comboBoxThursdayFinish.setSelectedItem(timesArrayList.get(13));
						} else if (rs1.getInt(9) == 14) {
							comboBoxThursdayFinish.setSelectedItem(timesArrayList.get(14));
						} else if (rs1.getInt(9) == 15) {
							comboBoxThursdayFinish.setSelectedItem(timesArrayList.get(15));
						} else if (rs1.getInt(9) == 16) {
							comboBoxThursdayFinish.setSelectedItem(timesArrayList.get(16));
						} else if (rs1.getInt(9) == 17) {
							comboBoxThursdayFinish.setSelectedItem(timesArrayList.get(17));
						} else if (rs1.getInt(9) == 18) {
							comboBoxThursdayFinish.setSelectedItem(timesArrayList.get(18));
						} else if (rs1.getInt(9) == 19) {
							comboBoxThursdayFinish.setSelectedItem(timesArrayList.get(19));
						} else if (rs1.getInt(9) == 20) {
							comboBoxThursdayFinish.setSelectedItem(timesArrayList.get(20));
						} else if (rs1.getInt(9) == 21) {
							comboBoxThursdayFinish.setSelectedItem(timesArrayList.get(21));
						} else if (rs1.getInt(9) == 22) {
							comboBoxThursdayFinish.setSelectedItem(timesArrayList.get(22));
						} else if (rs1.getInt(9) == 23) {
							comboBoxThursdayFinish.setSelectedItem(timesArrayList.get(23));
						}
					} else {
						comboBoxThursdayStart.setEnabled(false);
						comboBoxThursdayFinish.setEnabled(false);
						chckbxThursday.setSelected(false);

						comboBoxThursdayStart.setSelectedItem(timesArrayList.get(0));
						comboBoxThursdayFinish.setSelectedItem(timesArrayList.get(0));
					}
					if (rs1.getInt(10) != -1) {
						chckbxFriday.setSelected(true);
						comboBoxFridayStart.setEnabled(true);
						comboBoxFridayFinish.setEnabled(true);

						if (rs1.getInt(10) == 0) {
							comboBoxFridayStart.setSelectedItem(timesArrayList.get(0));
						} else if (rs1.getInt(10) == 1) {
							comboBoxFridayStart.setSelectedItem(timesArrayList.get(1));
						} else if (rs1.getInt(10) == 2) {
							comboBoxFridayStart.setSelectedItem(timesArrayList.get(2));
						} else if (rs1.getInt(10) == 3) {
							comboBoxFridayStart.setSelectedItem(timesArrayList.get(3));
						} else if (rs1.getInt(10) == 4) {
							comboBoxFridayStart.setSelectedItem(timesArrayList.get(4));
						} else if (rs1.getInt(10) == 5) {
							comboBoxFridayStart.setSelectedItem(timesArrayList.get(5));
						} else if (rs1.getInt(10) == 6) {
							comboBoxFridayStart.setSelectedItem(timesArrayList.get(6));
						} else if (rs1.getInt(10) == 7) {
							comboBoxFridayStart.setSelectedItem(timesArrayList.get(7));
						} else if (rs1.getInt(10) == 8) {
							comboBoxFridayStart.setSelectedItem(timesArrayList.get(8));
						} else if (rs1.getInt(10) == 9) {
							comboBoxFridayStart.setSelectedItem(timesArrayList.get(9));
						} else if (rs1.getInt(10) == 10) {
							comboBoxFridayStart.setSelectedItem(timesArrayList.get(10));
						} else if (rs1.getInt(10) == 11) {
							comboBoxFridayStart.setSelectedItem(timesArrayList.get(11));
						} else if (rs1.getInt(10) == 12) {
							comboBoxFridayStart.setSelectedItem(timesArrayList.get(12));
						} else if (rs1.getInt(10) == 13) {
							comboBoxFridayStart.setSelectedItem(timesArrayList.get(13));
						} else if (rs1.getInt(10) == 14) {
							comboBoxFridayStart.setSelectedItem(timesArrayList.get(14));
						} else if (rs1.getInt(10) == 15) {
							comboBoxFridayStart.setSelectedItem(timesArrayList.get(15));
						} else if (rs1.getInt(10) == 16) {
							comboBoxFridayStart.setSelectedItem(timesArrayList.get(16));
						} else if (rs1.getInt(10) == 17) {
							comboBoxFridayStart.setSelectedItem(timesArrayList.get(17));
						} else if (rs1.getInt(10) == 18) {
							comboBoxFridayStart.setSelectedItem(timesArrayList.get(18));
						} else if (rs1.getInt(10) == 19) {
							comboBoxFridayStart.setSelectedItem(timesArrayList.get(19));
						} else if (rs1.getInt(10) == 20) {
							comboBoxFridayStart.setSelectedItem(timesArrayList.get(20));
						} else if (rs1.getInt(10) == 21) {
							comboBoxFridayStart.setSelectedItem(timesArrayList.get(21));
						} else if (rs1.getInt(10) == 22) {
							comboBoxFridayStart.setSelectedItem(timesArrayList.get(22));
						} else if (rs1.getInt(10) == 23) {
							comboBoxFridayStart.setSelectedItem(timesArrayList.get(23));
						}

						if (rs1.getInt(11) == 0) {
							comboBoxFridayFinish.setSelectedItem(timesArrayList.get(0));
						} else if (rs1.getInt(11) == 1) {
							comboBoxFridayFinish.setSelectedItem(timesArrayList.get(1));
						} else if (rs1.getInt(11) == 2) {
							comboBoxFridayFinish.setSelectedItem(timesArrayList.get(2));
						} else if (rs1.getInt(11) == 3) {
							comboBoxFridayFinish.setSelectedItem(timesArrayList.get(3));
						} else if (rs1.getInt(11) == 4) {
							comboBoxFridayFinish.setSelectedItem(timesArrayList.get(4));
						} else if (rs1.getInt(11) == 5) {
							comboBoxFridayFinish.setSelectedItem(timesArrayList.get(5));
						} else if (rs1.getInt(11) == 6) {
							comboBoxFridayFinish.setSelectedItem(timesArrayList.get(6));
						} else if (rs1.getInt(11) == 7) {
							comboBoxFridayFinish.setSelectedItem(timesArrayList.get(7));
						} else if (rs1.getInt(11) == 8) {
							comboBoxFridayFinish.setSelectedItem(timesArrayList.get(8));
						} else if (rs1.getInt(11) == 9) {
							comboBoxFridayFinish.setSelectedItem(timesArrayList.get(9));
						} else if (rs1.getInt(11) == 10) {
							comboBoxFridayFinish.setSelectedItem(timesArrayList.get(10));
						} else if (rs1.getInt(11) == 11) {
							comboBoxFridayFinish.setSelectedItem(timesArrayList.get(11));
						} else if (rs1.getInt(11) == 12) {
							comboBoxFridayFinish.setSelectedItem(timesArrayList.get(12));
						} else if (rs1.getInt(11) == 13) {
							comboBoxFridayFinish.setSelectedItem(timesArrayList.get(13));
						} else if (rs1.getInt(11) == 14) {
							comboBoxFridayFinish.setSelectedItem(timesArrayList.get(14));
						} else if (rs1.getInt(11) == 15) {
							comboBoxFridayFinish.setSelectedItem(timesArrayList.get(15));
						} else if (rs1.getInt(11) == 16) {
							comboBoxFridayFinish.setSelectedItem(timesArrayList.get(16));
						} else if (rs1.getInt(11) == 17) {
							comboBoxFridayFinish.setSelectedItem(timesArrayList.get(17));
						} else if (rs1.getInt(11) == 18) {
							comboBoxFridayFinish.setSelectedItem(timesArrayList.get(18));
						} else if (rs1.getInt(11) == 19) {
							comboBoxFridayFinish.setSelectedItem(timesArrayList.get(19));
						} else if (rs1.getInt(11) == 20) {
							comboBoxFridayFinish.setSelectedItem(timesArrayList.get(20));
						} else if (rs1.getInt(11) == 21) {
							comboBoxFridayFinish.setSelectedItem(timesArrayList.get(21));
						} else if (rs1.getInt(11) == 22) {
							comboBoxFridayFinish.setSelectedItem(timesArrayList.get(22));
						} else if (rs1.getInt(11) == 23) {
							comboBoxFridayFinish.setSelectedItem(timesArrayList.get(23));
						}
					} else {
						comboBoxFridayStart.setEnabled(false);
						comboBoxFridayFinish.setEnabled(false);
						chckbxFriday.setSelected(false);

						comboBoxFridayStart.setSelectedItem(timesArrayList.get(0));
						comboBoxFridayFinish.setSelectedItem(timesArrayList.get(0));
					}
					if (rs1.getInt(12) != -1) {
						chckbxSaturday.setSelected(true);
						comboBoxSaturdayStart.setEnabled(true);
						comboBoxSaturdayFinish.setEnabled(true);

						if (rs1.getInt(12) == 0) {
							comboBoxSaturdayStart.setSelectedItem(timesArrayList.get(0));
						} else if (rs1.getInt(12) == 1) {
							comboBoxSaturdayStart.setSelectedItem(timesArrayList.get(1));
						} else if (rs1.getInt(12) == 2) {
							comboBoxSaturdayStart.setSelectedItem(timesArrayList.get(2));
						} else if (rs1.getInt(12) == 3) {
							comboBoxSaturdayStart.setSelectedItem(timesArrayList.get(3));
						} else if (rs1.getInt(12) == 4) {
							comboBoxSaturdayStart.setSelectedItem(timesArrayList.get(4));
						} else if (rs1.getInt(12) == 5) {
							comboBoxSaturdayStart.setSelectedItem(timesArrayList.get(5));
						} else if (rs1.getInt(12) == 6) {
							comboBoxSaturdayStart.setSelectedItem(timesArrayList.get(6));
						} else if (rs1.getInt(12) == 7) {
							comboBoxSaturdayStart.setSelectedItem(timesArrayList.get(7));
						} else if (rs1.getInt(12) == 8) {
							comboBoxSaturdayStart.setSelectedItem(timesArrayList.get(8));
						} else if (rs1.getInt(12) == 9) {
							comboBoxSaturdayStart.setSelectedItem(timesArrayList.get(9));
						} else if (rs1.getInt(12) == 10) {
							comboBoxSaturdayStart.setSelectedItem(timesArrayList.get(10));
						} else if (rs1.getInt(12) == 11) {
							comboBoxSaturdayStart.setSelectedItem(timesArrayList.get(11));
						} else if (rs1.getInt(12) == 12) {
							comboBoxSaturdayStart.setSelectedItem(timesArrayList.get(12));
						} else if (rs1.getInt(12) == 13) {
							comboBoxSaturdayStart.setSelectedItem(timesArrayList.get(13));
						} else if (rs1.getInt(12) == 14) {
							comboBoxSaturdayStart.setSelectedItem(timesArrayList.get(14));
						} else if (rs1.getInt(12) == 15) {
							comboBoxSaturdayStart.setSelectedItem(timesArrayList.get(15));
						} else if (rs1.getInt(12) == 16) {
							comboBoxSaturdayStart.setSelectedItem(timesArrayList.get(16));
						} else if (rs1.getInt(12) == 17) {
							comboBoxSaturdayStart.setSelectedItem(timesArrayList.get(17));
						} else if (rs1.getInt(12) == 18) {
							comboBoxSaturdayStart.setSelectedItem(timesArrayList.get(18));
						} else if (rs1.getInt(12) == 19) {
							comboBoxSaturdayStart.setSelectedItem(timesArrayList.get(19));
						} else if (rs1.getInt(12) == 20) {
							comboBoxSaturdayStart.setSelectedItem(timesArrayList.get(20));
						} else if (rs1.getInt(12) == 21) {
							comboBoxSaturdayStart.setSelectedItem(timesArrayList.get(21));
						} else if (rs1.getInt(12) == 22) {
							comboBoxSaturdayStart.setSelectedItem(timesArrayList.get(22));
						} else if (rs1.getInt(12) == 23) {
							comboBoxSaturdayStart.setSelectedItem(timesArrayList.get(23));
						}

						if (rs1.getInt(13) == 0) {
							comboBoxSaturdayFinish.setSelectedItem(timesArrayList.get(0));
						} else if (rs1.getInt(13) == 1) {
							comboBoxSaturdayFinish.setSelectedItem(timesArrayList.get(1));
						} else if (rs1.getInt(13) == 2) {
							comboBoxSaturdayFinish.setSelectedItem(timesArrayList.get(2));
						} else if (rs1.getInt(13) == 3) {
							comboBoxSaturdayFinish.setSelectedItem(timesArrayList.get(3));
						} else if (rs1.getInt(13) == 4) {
							comboBoxSaturdayFinish.setSelectedItem(timesArrayList.get(4));
						} else if (rs1.getInt(13) == 5) {
							comboBoxSaturdayFinish.setSelectedItem(timesArrayList.get(5));
						} else if (rs1.getInt(13) == 6) {
							comboBoxSaturdayFinish.setSelectedItem(timesArrayList.get(6));
						} else if (rs1.getInt(13) == 7) {
							comboBoxSaturdayFinish.setSelectedItem(timesArrayList.get(7));
						} else if (rs1.getInt(13) == 8) {
							comboBoxSaturdayFinish.setSelectedItem(timesArrayList.get(8));
						} else if (rs1.getInt(13) == 9) {
							comboBoxSaturdayFinish.setSelectedItem(timesArrayList.get(9));
						} else if (rs1.getInt(13) == 10) {
							comboBoxSaturdayFinish.setSelectedItem(timesArrayList.get(10));
						} else if (rs1.getInt(13) == 11) {
							comboBoxSaturdayFinish.setSelectedItem(timesArrayList.get(11));
						} else if (rs1.getInt(13) == 12) {
							comboBoxSaturdayFinish.setSelectedItem(timesArrayList.get(12));
						} else if (rs1.getInt(13) == 13) {
							comboBoxSaturdayFinish.setSelectedItem(timesArrayList.get(13));
						} else if (rs1.getInt(13) == 14) {
							comboBoxSaturdayFinish.setSelectedItem(timesArrayList.get(14));
						} else if (rs1.getInt(13) == 15) {
							comboBoxSaturdayFinish.setSelectedItem(timesArrayList.get(15));
						} else if (rs1.getInt(13) == 16) {
							comboBoxSaturdayFinish.setSelectedItem(timesArrayList.get(16));
						} else if (rs1.getInt(13) == 17) {
							comboBoxSaturdayFinish.setSelectedItem(timesArrayList.get(17));
						} else if (rs1.getInt(13) == 18) {
							comboBoxSaturdayFinish.setSelectedItem(timesArrayList.get(18));
						} else if (rs1.getInt(13) == 19) {
							comboBoxSaturdayFinish.setSelectedItem(timesArrayList.get(19));
						} else if (rs1.getInt(13) == 20) {
							comboBoxSaturdayFinish.setSelectedItem(timesArrayList.get(20));
						} else if (rs1.getInt(13) == 21) {
							comboBoxSaturdayFinish.setSelectedItem(timesArrayList.get(21));
						} else if (rs1.getInt(13) == 22) {
							comboBoxSaturdayFinish.setSelectedItem(timesArrayList.get(22));
						} else if (rs1.getInt(13) == 23) {
							comboBoxSaturdayFinish.setSelectedItem(timesArrayList.get(23));
						}
					} else {
						comboBoxSaturdayStart.setEnabled(false);
						comboBoxSaturdayFinish.setEnabled(false);
						chckbxSaturday.setSelected(false);

						comboBoxSaturdayStart.setSelectedItem(timesArrayList.get(0));
						comboBoxSaturdayFinish.setSelectedItem(timesArrayList.get(0));
					}
					if (rs1.getInt(14) != -1) {
						chckbxSunday.setSelected(true);
						comboBoxSundayStart.setEnabled(true);
						comboBoxSundayFinish.setEnabled(true);

						if (rs1.getInt(14) == 0) {
							comboBoxSundayStart.setSelectedItem(timesArrayList.get(0));
						} else if (rs1.getInt(14) == 1) {
							comboBoxSundayStart.setSelectedItem(timesArrayList.get(1));
						} else if (rs1.getInt(14) == 2) {
							comboBoxSundayStart.setSelectedItem(timesArrayList.get(2));
						} else if (rs1.getInt(14) == 3) {
							comboBoxSundayStart.setSelectedItem(timesArrayList.get(3));
						} else if (rs1.getInt(14) == 4) {
							comboBoxSundayStart.setSelectedItem(timesArrayList.get(4));
						} else if (rs1.getInt(14) == 5) {
							comboBoxSundayStart.setSelectedItem(timesArrayList.get(5));
						} else if (rs1.getInt(14) == 6) {
							comboBoxSundayStart.setSelectedItem(timesArrayList.get(6));
						} else if (rs1.getInt(14) == 7) {
							comboBoxSundayStart.setSelectedItem(timesArrayList.get(7));
						} else if (rs1.getInt(14) == 8) {
							comboBoxSundayStart.setSelectedItem(timesArrayList.get(8));
						} else if (rs1.getInt(14) == 9) {
							comboBoxSundayStart.setSelectedItem(timesArrayList.get(9));
						} else if (rs1.getInt(14) == 10) {
							comboBoxSundayStart.setSelectedItem(timesArrayList.get(10));
						} else if (rs1.getInt(14) == 11) {
							comboBoxSundayStart.setSelectedItem(timesArrayList.get(11));
						} else if (rs1.getInt(14) == 12) {
							comboBoxSundayStart.setSelectedItem(timesArrayList.get(12));
						} else if (rs1.getInt(14) == 13) {
							comboBoxSundayStart.setSelectedItem(timesArrayList.get(13));
						} else if (rs1.getInt(14) == 14) {
							comboBoxSundayStart.setSelectedItem(timesArrayList.get(14));
						} else if (rs1.getInt(14) == 15) {
							comboBoxSundayStart.setSelectedItem(timesArrayList.get(15));
						} else if (rs1.getInt(14) == 16) {
							comboBoxSundayStart.setSelectedItem(timesArrayList.get(16));
						} else if (rs1.getInt(14) == 17) {
							comboBoxSundayStart.setSelectedItem(timesArrayList.get(17));
						} else if (rs1.getInt(14) == 18) {
							comboBoxSundayStart.setSelectedItem(timesArrayList.get(18));
						} else if (rs1.getInt(14) == 19) {
							comboBoxSundayStart.setSelectedItem(timesArrayList.get(19));
						} else if (rs1.getInt(14) == 20) {
							comboBoxSundayStart.setSelectedItem(timesArrayList.get(20));
						} else if (rs1.getInt(14) == 21) {
							comboBoxSundayStart.setSelectedItem(timesArrayList.get(21));
						} else if (rs1.getInt(14) == 22) {
							comboBoxSundayStart.setSelectedItem(timesArrayList.get(22));
						} else if (rs1.getInt(14) == 23) {
							comboBoxSundayStart.setSelectedItem(timesArrayList.get(23));
						}

						if (rs1.getInt(15) == 0) {
							comboBoxSundayFinish.setSelectedItem(timesArrayList.get(0));
						} else if (rs1.getInt(15) == 1) {
							comboBoxSundayFinish.setSelectedItem(timesArrayList.get(1));
						} else if (rs1.getInt(15) == 2) {
							comboBoxSundayFinish.setSelectedItem(timesArrayList.get(2));
						} else if (rs1.getInt(15) == 3) {
							comboBoxSundayFinish.setSelectedItem(timesArrayList.get(3));
						} else if (rs1.getInt(15) == 4) {
							comboBoxSundayFinish.setSelectedItem(timesArrayList.get(4));
						} else if (rs1.getInt(15) == 5) {
							comboBoxSundayFinish.setSelectedItem(timesArrayList.get(5));
						} else if (rs1.getInt(15) == 6) {
							comboBoxSundayFinish.setSelectedItem(timesArrayList.get(6));
						} else if (rs1.getInt(15) == 7) {
							comboBoxSundayFinish.setSelectedItem(timesArrayList.get(7));
						} else if (rs1.getInt(15) == 8) {
							comboBoxSundayFinish.setSelectedItem(timesArrayList.get(8));
						} else if (rs1.getInt(15) == 9) {
							comboBoxSundayFinish.setSelectedItem(timesArrayList.get(9));
						} else if (rs1.getInt(15) == 10) {
							comboBoxSundayFinish.setSelectedItem(timesArrayList.get(10));
						} else if (rs1.getInt(15) == 11) {
							comboBoxSundayFinish.setSelectedItem(timesArrayList.get(11));
						} else if (rs1.getInt(15) == 12) {
							comboBoxSundayFinish.setSelectedItem(timesArrayList.get(12));
						} else if (rs1.getInt(15) == 13) {
							comboBoxSundayFinish.setSelectedItem(timesArrayList.get(13));
						} else if (rs1.getInt(15) == 14) {
							comboBoxSundayFinish.setSelectedItem(timesArrayList.get(14));
						} else if (rs1.getInt(15) == 15) {
							comboBoxSundayFinish.setSelectedItem(timesArrayList.get(15));
						} else if (rs1.getInt(15) == 16) {
							comboBoxSundayFinish.setSelectedItem(timesArrayList.get(16));
						} else if (rs1.getInt(15) == 17) {
							comboBoxSundayFinish.setSelectedItem(timesArrayList.get(17));
						} else if (rs1.getInt(15) == 18) {
							comboBoxSundayFinish.setSelectedItem(timesArrayList.get(18));
						} else if (rs1.getInt(15) == 19) {
							comboBoxSundayFinish.setSelectedItem(timesArrayList.get(19));
						} else if (rs1.getInt(15) == 20) {
							comboBoxSundayFinish.setSelectedItem(timesArrayList.get(20));
						} else if (rs1.getInt(15) == 21) {
							comboBoxSundayFinish.setSelectedItem(timesArrayList.get(21));
						} else if (rs1.getInt(15) == 22) {
							comboBoxSundayFinish.setSelectedItem(timesArrayList.get(22));
						} else if (rs1.getInt(15) == 23) {
							comboBoxSundayFinish.setSelectedItem(timesArrayList.get(23));
						}
					} else {
						comboBoxSundayStart.setEnabled(false);
						comboBoxSundayFinish.setEnabled(false);
						chckbxSunday.setSelected(false);

						comboBoxSundayStart.setSelectedItem(timesArrayList.get(0));
						comboBoxSundayFinish.setSelectedItem(timesArrayList.get(0));
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Weird logic method I created to take all of the new employee data from
	 * the fields and checkboxes, and overwrite the currently selected employee.
	 * 
	 * @param e
	 */
	public void overwriteEmployee() {
		try {
			Connection conn = DriverManager.getConnection(DatabaseConn.getConnection(), DatabaseConn.getUsername(), DatabaseConn.getPassword());

			// Monday
			if (chckbxMonday.isSelected()) {
				Statement stmt1 = conn.createStatement();

				int start = comboBoxMondayStart.getSelectedIndex();
				int finish = comboBoxMondayFinish.getSelectedIndex();

				stmt1.executeUpdate("update schedule set monday_start = " + start + ", monday_finish = " + finish
						+ " where employee_id =" + currentEmployeeID + ";");
				stmt1.close();
			} else {
				Statement stmt1 = conn.createStatement();

				int start = -1;
				int finish = -1;

				stmt1.executeUpdate("update schedule set monday_start = " + start + ", monday_finish = " + finish
						+ " where employee_id =" + currentEmployeeID + ";");
				stmt1.close();
			}

			// Tuesday
			if (chckbxTuesday.isSelected()) {
				Statement stmt1 = conn.createStatement();

				int start = comboBoxTuesdayStart.getSelectedIndex();
				int finish = comboBoxTuesdayFinish.getSelectedIndex();

				stmt1.executeUpdate("update schedule set tuesday_start = " + start + ", tuesday_finish = " + finish
						+ " where employee_id =" + currentEmployeeID + ";");
				stmt1.close();
			} else {
				Statement stmt1 = conn.createStatement();

				int start = -1;
				int finish = -1;

				stmt1.executeUpdate("update schedule set tuesday_start = " + start + ", tuesday_finish = " + finish
						+ " where employee_id =" + currentEmployeeID + ";");
				stmt1.close();
			}

			// Wednesday
			if (chckbxWednesday.isSelected()) {
				Statement stmt1 = conn.createStatement();

				int start = comboBoxWednesdayStart.getSelectedIndex();
				int finish = comboBoxWednesdayFinish.getSelectedIndex();

				stmt1.executeUpdate("update schedule set wednesday_start = " + start + ", wednesday_finish = " + finish
						+ " where employee_id =" + currentEmployeeID + ";");
				stmt1.close();
			} else {
				Statement stmt1 = conn.createStatement();

				int start = -1;
				int finish = -1;

				stmt1.executeUpdate("update schedule set wednesday_start = " + start + ", wednesday_finish = " + finish
						+ " where employee_id =" + currentEmployeeID + ";");
				stmt1.close();
			}

			// Thursday
			if (chckbxThursday.isSelected()) {
				Statement stmt1 = conn.createStatement();

				int start = comboBoxThursdayStart.getSelectedIndex();
				int finish = comboBoxThursdayFinish.getSelectedIndex();

				stmt1.executeUpdate("update schedule set thursday_start = " + start + ", thursday_finish = " + finish
						+ " where employee_id =" + currentEmployeeID + ";");
				stmt1.close();
			} else {
				Statement stmt1 = conn.createStatement();

				int start = -1;
				int finish = -1;

				stmt1.executeUpdate("update schedule set thursday_start = " + start + ", thursday_finish = " + finish
						+ " where employee_id =" + currentEmployeeID + ";");
				stmt1.close();
			}

			// Friday
			if (chckbxFriday.isSelected()) {
				Statement stmt1 = conn.createStatement();

				int start = comboBoxFridayStart.getSelectedIndex();
				int finish = comboBoxFridayFinish.getSelectedIndex();

				stmt1.executeUpdate("update schedule set friday_start = " + start + ", friday_finish = " + finish
						+ " where employee_id =" + currentEmployeeID + ";");
				stmt1.close();
			} else {
				Statement stmt1 = conn.createStatement();

				int start = -1;
				int finish = -1;

				stmt1.executeUpdate("update schedule set friday_start = " + start + ", friday_finish = " + finish
						+ " where employee_id =" + currentEmployeeID + ";");
				stmt1.close();
			}

			// Saturday
			if (chckbxSaturday.isSelected()) {
				Statement stmt1 = conn.createStatement();

				int start = comboBoxSaturdayStart.getSelectedIndex();
				int finish = comboBoxSaturdayFinish.getSelectedIndex();

				stmt1.executeUpdate("update schedule set saturday_start = " + start + ", saturday_finish = " + finish
						+ " where employee_id =" + currentEmployeeID + ";");
				stmt1.close();
			} else {
				Statement stmt1 = conn.createStatement();

				int start = -1;
				int finish = -1;

				stmt1.executeUpdate("update schedule set saturday_start = " + start + ", saturday_finish = " + finish
						+ " where employee_id =" + currentEmployeeID + ";");
				stmt1.close();
			}

			// Sunday
			if (chckbxSunday.isSelected()) {
				Statement stmt1 = conn.createStatement();

				int start = comboBoxSundayStart.getSelectedIndex();
				int finish = comboBoxSundayFinish.getSelectedIndex();

				stmt1.executeUpdate("update schedule set sunday_start = " + start + ", sunday_finish = " + finish
						+ " where employee_id =" + currentEmployeeID + ";");
				stmt1.close();
			} else {
				Statement stmt1 = conn.createStatement();

				int start = -1;
				int finish = -1;

				stmt1.executeUpdate("update schedule set sunday_start = " + start + ", sunday_finish = " + finish
						+ " where employee_id =" + currentEmployeeID + ";");
				stmt1.close();
			}

			JOptionPane.showMessageDialog(null, "The data has been successfully overwritten.");
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null,
					"Failed to update data... Please check connection to server or try again later.");
			e.printStackTrace();
		}
	}

	public static String getTime12(int time) {
		if (time >= 12) {
			switch (time) {
			case 12:
				return "12PM";
			case 13:
				return "1PM";
			case 14:
				return "2PM";
			case 15:
				return "3PM";
			case 16:
				return "4PM";
			case 17:
				return "5PM";
			case 18:
				return "6PM";
			case 19:
				return "7PM";
			case 20:
				return "8PM";
			case 21:
				return "9PM";
			case 22:
				return "10PM";
			case 23:
				return "11PM";
			}
		} else if (time == -1) {
			return "N/A";
		}else if(time == 0){
			return "12AM";
		}else {
			return String.valueOf(time) + "AM";
		}
		return String.valueOf(time) + "AM";
	}
	
	public static <T> T mostCommon(List<T> list) {
	    Map<T, Integer> map = new HashMap<>();

	    for (T t : list) {
	        Integer val = map.get(t);
	        map.put(t, val == null ? 1 : val + 1);
	    }

	    Entry<T, Integer> max = null;

	    for (Entry<T, Integer> e : map.entrySet()) {
	        if (max == null || e.getValue() > max.getValue())
	            max = e;
	    }

	    return max.getKey();
	}
	
	public void updateEmployeeDataTable(){
		try {
			Connection conn = DriverManager.getConnection(DatabaseConn.getConnection(), DatabaseConn.getUsername(), DatabaseConn.getPassword());
			Statement stmt1 = conn.createStatement();
			ResultSet rs1 = stmt1.executeQuery(
					"select employees.first_name, employees.last_name, schedule.monday_start, schedule.monday_finish, schedule.tuesday_start, schedule.tuesday_finish, schedule.wednesday_start, schedule.wednesday_finish, schedule.thursday_start, schedule.thursday_finish, schedule.friday_start, schedule.friday_finish, schedule.saturday_start, schedule.saturday_finish, schedule.sunday_start, schedule.sunday_finish from schedule inner join employees on schedule.employee_id = employees.employee_id;");

			while (rs1.next()) {
				Object[] data = new Object[8];
				data[0] = (rs1.getString(1) + " " + rs1.getString(2));
				
				String monday;
				String tuesday;
				String wednesday;
				String thursday;
				String friday;
				String saturday;
				String sunday;
				
				if (rs1.getInt(3) == -1) {
					monday = getTime12(rs1.getInt(3));
				} else {
					monday = (getTime12(rs1.getInt(3)) + " - " + getTime12(rs1.getInt(4)));

				}

				if (rs1.getInt(5) == -1) {
					tuesday = getTime12(rs1.getInt(5));
				} else {
					tuesday = (getTime12(rs1.getInt(5)) + " - " + getTime12(rs1.getInt(6)));
				}
				
				if (rs1.getInt(7) == -1) {
					wednesday = getTime12(rs1.getInt(7));
				} else {
					wednesday = (getTime12(rs1.getInt(7)) + " - " + getTime12(rs1.getInt(8)));
				}
				
				if (rs1.getInt(9) == -1) {
					thursday = getTime12(rs1.getInt(9));
				} else {
					thursday = (getTime12(rs1.getInt(9)) + " - " + getTime12(rs1.getInt(10)));
				}
				
				if (rs1.getInt(11) == -1) {
					friday = getTime12(rs1.getInt(11));
				} else {
					friday = (getTime12(rs1.getInt(11)) + " - " + getTime12(rs1.getInt(12)));
				}
				
				if (rs1.getInt(13) == -1) {
					saturday = getTime12(rs1.getInt(13));
				} else {
					saturday = (getTime12(rs1.getInt(13)) + " - " + getTime12(rs1.getInt(14)));
				}
				
				if (rs1.getInt(15) == -1) {
					sunday = getTime12(rs1.getInt(15));
				} else {
					sunday = (getTime12(rs1.getInt(15)) + " - " + getTime12(rs1.getInt(16)));
				}

				data[1] = sunday;
				data[2] = monday;
				data[3] = tuesday;
				data[4] = wednesday;
				data[5] = thursday;
				data[6] = friday;
				data[7] = saturday;

				dtm.insertRow(0, data);
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
