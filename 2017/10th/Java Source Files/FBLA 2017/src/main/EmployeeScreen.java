package main;

import javax.swing.JPanel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.JButton;

public class EmployeeScreen extends JPanel{
	
	public EmployeeScreen() {
		setBounds(0, 0, 592, 564);
		setLayout(null);
		
		JLabel lblEmployeeScreen = new JLabel("<html>Employee Screen</html>");
		lblEmployeeScreen.setHorizontalAlignment(SwingConstants.CENTER);
		lblEmployeeScreen.setBounds(204, 30, 165, 22);
		add(lblEmployeeScreen);
		
		JLabel lblName = new JLabel("Name: ");
		lblName.setBounds(175, 88, 46, 14);
		add(lblName);
		
		JLabel lblNameGoesHere = new JLabel("<html>Name Goes Here</html>");
		lblNameGoesHere.setBounds(333, 88, 89, 14);
		add(lblNameGoesHere);
		
		JLabel lblMonday = new JLabel("Monday");
		lblMonday.setHorizontalAlignment(SwingConstants.CENTER);
		lblMonday.setBounds(26, 197, 48, 14);
		add(lblMonday);
		
		JLabel lblTuesday = new JLabel("<html>Tuesday</html>");
		lblTuesday.setHorizontalAlignment(SwingConstants.CENTER);
		lblTuesday.setBounds(101, 197, 50, 14);
		add(lblTuesday);
		
		JLabel lblWednesday = new JLabel("<html>Wednesday</html>");
		lblWednesday.setHorizontalAlignment(SwingConstants.CENTER);
		lblWednesday.setBounds(174, 197, 70, 14);
		add(lblWednesday);
		
		JLabel lblThursday = new JLabel("<html>Thursday</html>");
		lblThursday.setHorizontalAlignment(SwingConstants.CENTER);
		lblThursday.setBounds(265, 197, 56, 14);
		add(lblThursday);
		
		JLabel lblFriday = new JLabel("<html>Friday</html>");
		lblFriday.setHorizontalAlignment(SwingConstants.CENTER);
		lblFriday.setBounds(346, 197, 46, 14);
		add(lblFriday);
		
		JLabel lblSaturday = new JLabel("<html>Saturday</html>");
		lblSaturday.setHorizontalAlignment(SwingConstants.CENTER);
		lblSaturday.setBounds(428, 197, 50, 14);
		add(lblSaturday);
		
		JLabel lblSunday = new JLabel("<html>Sunday</html>");
		lblSunday.setHorizontalAlignment(SwingConstants.CENTER);
		lblSunday.setBounds(512, 197, 50, 14);
		add(lblSunday);
		
		JLabel lblMonTime = new JLabel("12AM - 12PM");
		lblMonTime.setHorizontalAlignment(SwingConstants.CENTER);
		lblMonTime.setBounds(13, 231, 64, 14);
		add(lblMonTime);
		
		JLabel lblSchedule = new JLabel("Schedule");
		lblSchedule.setHorizontalAlignment(SwingConstants.CENTER);
		lblSchedule.setBounds(243, 152, 86, 14);
		add(lblSchedule);
		
		JLabel lblTuesTime = new JLabel("12AM - 12PM");
		lblTuesTime.setHorizontalAlignment(SwingConstants.CENTER);
		lblTuesTime.setBounds(92, 231, 64, 14);
		add(lblTuesTime);
		
		JLabel lblWedTime = new JLabel("12AM - 12PM");
		lblWedTime.setHorizontalAlignment(SwingConstants.CENTER);
		lblWedTime.setBounds(176, 231, 64, 14);
		add(lblWedTime);
		
		JLabel lblThursTime = new JLabel("12AM - 12PM");
		lblThursTime.setHorizontalAlignment(SwingConstants.CENTER);
		lblThursTime.setBounds(260, 231, 64, 14);
		add(lblThursTime);
		
		JLabel lblFriTime = new JLabel("12AM - 12PM");
		lblFriTime.setHorizontalAlignment(SwingConstants.CENTER);
		lblFriTime.setBounds(340, 231, 64, 14);
		add(lblFriTime);
		
		JLabel lblSatTime = new JLabel("12AM - 12PM");
		lblSatTime.setHorizontalAlignment(SwingConstants.CENTER);
		lblSatTime.setBounds(420, 231, 64, 14);
		add(lblSatTime);
		
		JLabel lblSunTime = new JLabel("12AM - 12PM");
		lblSunTime.setHorizontalAlignment(SwingConstants.CENTER);
		lblSunTime.setBounds(498, 231, 77, 14);
		add(lblSunTime);
		
		JButton buttonBackToStart = new JButton("Back to Start.");
		buttonBackToStart.setBounds(453, 535, 129, 18);
		buttonBackToStart.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				GUI.gotoMainScreen();
			}
		});
		add(buttonBackToStart);
	}
	
	public EmployeeScreen(int employeeID) {
		setBounds(0, 0, 592, 564);
		setLayout(null);
		
		JLabel lblEmployeeScreen = new JLabel("<html>Employee Screen</html>");
		lblEmployeeScreen.setHorizontalAlignment(SwingConstants.CENTER);
		lblEmployeeScreen.setBounds(204, 30, 165, 22);
		add(lblEmployeeScreen);
		
		JLabel lblName = new JLabel("Name: ");
		lblName.setBounds(175, 88, 46, 14);
		add(lblName);
		
		JLabel lblNameGoesHere = new JLabel("<html>Name Goes Here</html>");
		lblNameGoesHere.setBounds(333, 88, 89, 14);
		add(lblNameGoesHere);
		
		JLabel lblMonday = new JLabel("Monday");
		lblMonday.setHorizontalAlignment(SwingConstants.CENTER);
		lblMonday.setBounds(26, 197, 48, 14);
		add(lblMonday);
		
		JLabel lblTuesday = new JLabel("<html>Tuesday</html>");
		lblTuesday.setHorizontalAlignment(SwingConstants.CENTER);
		lblTuesday.setBounds(101, 197, 50, 14);
		add(lblTuesday);
		
		JLabel lblWednesday = new JLabel("<html>Wednesday</html>");
		lblWednesday.setHorizontalAlignment(SwingConstants.CENTER);
		lblWednesday.setBounds(174, 197, 70, 14);
		add(lblWednesday);
		
		JLabel lblThursday = new JLabel("<html>Thursday</html>");
		lblThursday.setHorizontalAlignment(SwingConstants.CENTER);
		lblThursday.setBounds(265, 197, 56, 14);
		add(lblThursday);
		
		JLabel lblFriday = new JLabel("<html>Friday</html>");
		lblFriday.setHorizontalAlignment(SwingConstants.CENTER);
		lblFriday.setBounds(346, 197, 46, 14);
		add(lblFriday);
		
		JLabel lblSaturday = new JLabel("<html>Saturday</html>");
		lblSaturday.setHorizontalAlignment(SwingConstants.CENTER);
		lblSaturday.setBounds(428, 197, 50, 14);
		add(lblSaturday);
		
		JLabel lblSunday = new JLabel("<html>Sunday</html>");
		lblSunday.setHorizontalAlignment(SwingConstants.CENTER);
		lblSunday.setBounds(512, 197, 50, 14);
		add(lblSunday);
		
		JLabel lblMonTime = new JLabel("12AM - 12PM");
		lblMonTime.setHorizontalAlignment(SwingConstants.CENTER);
		lblMonTime.setBounds(13, 231, 64, 14);
		add(lblMonTime);
		
		JLabel lblSchedule = new JLabel("Schedule");
		lblSchedule.setHorizontalAlignment(SwingConstants.CENTER);
		lblSchedule.setBounds(243, 152, 86, 14);
		add(lblSchedule);
		
		JLabel lblTuesTime = new JLabel("12AM - 12PM");
		lblTuesTime.setHorizontalAlignment(SwingConstants.CENTER);
		lblTuesTime.setBounds(92, 231, 64, 14);
		add(lblTuesTime);
		
		JLabel lblWedTime = new JLabel("12AM - 12PM");
		lblWedTime.setHorizontalAlignment(SwingConstants.CENTER);
		lblWedTime.setBounds(176, 231, 64, 14);
		add(lblWedTime);
		
		JLabel lblThursTime = new JLabel("12AM - 12PM");
		lblThursTime.setHorizontalAlignment(SwingConstants.CENTER);
		lblThursTime.setBounds(260, 231, 64, 14);
		add(lblThursTime);
		
		JLabel lblFriTime = new JLabel("12AM - 12PM");
		lblFriTime.setHorizontalAlignment(SwingConstants.CENTER);
		lblFriTime.setBounds(340, 231, 64, 14);
		add(lblFriTime);
		
		JLabel lblSatTime = new JLabel("12AM - 12PM");
		lblSatTime.setHorizontalAlignment(SwingConstants.CENTER);
		lblSatTime.setBounds(420, 231, 64, 14);
		add(lblSatTime);
		
		JLabel lblSunTime = new JLabel("12AM - 12PM");
		lblSunTime.setHorizontalAlignment(SwingConstants.CENTER);
		lblSunTime.setBounds(498, 231, 77, 14);
		add(lblSunTime);
		
		JButton buttonBackToStart = new JButton("Back to Start.");
		buttonBackToStart.setBounds(453, 535, 129, 18);
		buttonBackToStart.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				GUI.gotoMainScreen();
			}
		});
		add(buttonBackToStart);
		
		try {
			Connection conn = DriverManager.getConnection(DatabaseConn.getConnection(), DatabaseConn.getUsername(), DatabaseConn.getPassword());
			Statement stmt1 = conn.createStatement();
			ResultSet rs1 = stmt1.executeQuery(
					"select employees.first_name, employees.last_name, schedule.monday_start, schedule.monday_finish, schedule.tuesday_start, schedule.tuesday_finish, schedule.wednesday_start, schedule.wednesday_finish, schedule.thursday_start, schedule.thursday_finish, schedule.friday_start, schedule.friday_finish, schedule.saturday_start, schedule.saturday_finish, schedule.sunday_start, schedule.sunday_finish from schedule inner join employees on schedule.employee_id = employees.employee_id where employees.employee_id = " + employeeID + ";");

			while (rs1.next()) {
				String name = rs1.getString(1) + " " + rs1.getString(2);
				lblNameGoesHere.setText(name);
				
				String monday;
				String tuesday;
				String wednesday;
				String thursday;
				String friday;
				String saturday;
				String sunday;
				
				if (rs1.getInt(3) == -1) {
					monday = AdminScreen.getTime12(rs1.getInt(3));
				} else {
					monday = (AdminScreen.getTime12(rs1.getInt(3)) + " - " + AdminScreen.getTime12(rs1.getInt(4)));

				}

				if (rs1.getInt(5) == -1) {
					tuesday = AdminScreen.getTime12(rs1.getInt(5));
				} else {
					tuesday = (AdminScreen.getTime12(rs1.getInt(5)) + " - " + AdminScreen.getTime12(rs1.getInt(6)));
				}
				
				if (rs1.getInt(7) == -1) {
					wednesday = AdminScreen.getTime12(rs1.getInt(7));
				} else {
					wednesday = (AdminScreen.getTime12(rs1.getInt(7)) + " - " + AdminScreen.getTime12(rs1.getInt(8)));
				}
				
				if (rs1.getInt(9) == -1) {
					thursday = AdminScreen.getTime12(rs1.getInt(9));
				} else {
					thursday = (AdminScreen.getTime12(rs1.getInt(9)) + " - " + AdminScreen.getTime12(rs1.getInt(10)));
				}
				
				if (rs1.getInt(11) == -1) {
					friday = AdminScreen.getTime12(rs1.getInt(11));
				} else {
					friday = (AdminScreen.getTime12(rs1.getInt(11)) + " - " + AdminScreen.getTime12(rs1.getInt(12)));
				}
				
				if (rs1.getInt(13) == -1) {
					saturday = AdminScreen.getTime12(rs1.getInt(13));
				} else {
					saturday = (AdminScreen.getTime12(rs1.getInt(13)) + " - " + AdminScreen.getTime12(rs1.getInt(14)));
				}
				
				if (rs1.getInt(15) == -1) {
					sunday = AdminScreen.getTime12(rs1.getInt(15));
				} else {
					sunday = (AdminScreen.getTime12(rs1.getInt(15)) + " - " + AdminScreen.getTime12(rs1.getInt(16)));
				}
				
				lblMonTime.setText(monday);
				lblTuesTime.setText(tuesday);
				lblWedTime.setText(wednesday);
				lblThursTime.setText(thursday);
				lblFriTime.setText(friday);
				lblSatTime.setText(saturday);
				lblSunTime.setText(sunday);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
