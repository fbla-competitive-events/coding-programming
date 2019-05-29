package application;

//importing all packages needed for this class
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.print.PageLayout;
import javafx.print.PageOrientation;
import javafx.print.Paper;
import javafx.print.Printer;
import javafx.print.PrinterJob;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.transform.Scale;
import javafx.util.Duration;
import tray.notification.NotificationType;
import tray.notification.TrayNotification;

public class Customer_Attendance_Line_ChartController implements Initializable{
	
	//DatePicker to choose which week attendance to display
	@FXML
	private DatePicker dtWeek;
	
	//Label to display which week's attendance is shown
	@FXML
	private Label lblWeek;
	
	@FXML
	private Pane Screen;
	
	@FXML
	private NumberAxis CustomerNumberAxis;
	
	@FXML
	private Button btnPrint;
	
	//Array of Strings where String[0] is Sunday, String[1] is Monday, ...
	private String[] week = new String[7];
	
	Customer_Attendance_Report_Model linechart = new Customer_Attendance_Report_Model();
	
	//XYChart Series for AM Customer Attendances and PM Customer Attendances
	XYChart.Series<String, Integer> seriesAM;
	XYChart.Series<String, Integer> seriesPM;
	
	@FXML
	private LineChart<String,Integer> lineChartAttendance;
	
	@Override
	public void initialize(URL url, ResourceBundle rb){
		seriesAM = new XYChart.Series<>();
		seriesPM = new XYChart.Series<>();
		
		seriesAM.setName("AM");
		seriesPM.setName("PM");
		
		dtWeek.setEditable(false);
		lblWeek.setText("Week of " + LocalDate.now().toString());
		
        //Loads in this week's data
		setDates(LocalDate.now());	   
		addDataToXYChart();
		lineChartAttendance.getData().addAll(seriesAM);   
		lineChartAttendance.getData().addAll(seriesPM);  

	}
	
	//Set days of week's string values to their respective dates
	@FXML
	public void setDates(LocalDate date)
	{
		String day_of_week = date.getDayOfWeek().toString();

		if(day_of_week == "SUNDAY"){
			
			for(int i = 0; i < 7; i++){			
				week[i] = date.plusDays(i).toString(); 
			}
			
		}
		else if(day_of_week == "MONDAY"){
			
			for(int i = 1; i < 7; i++){			
				week[i] = date.plusDays(i-1).toString(); 
			}
			for(int i = 0; i < 1; i++){
				week[i] = (date.minusDays(1-i).toString());
			}
			
		}
		else if(day_of_week == "TUESDAY"){
			for(int i = 2; i < 7; i++){			
				week[i] = date.plusDays(i-2).toString(); 
			}
			for(int i = 0; i < 2; i++){
				week[i] = (date.minusDays(2-i).toString());
			}
		}
		else if(day_of_week == "WEDNESDAY"){
			for(int i = 3; i < 7; i++){			
				week[i] = date.plusDays(i-3).toString(); 
			}
			for(int i = 0; i < 3; i++){
				week[i] = (date.minusDays(3-i).toString());
			}
		}
		else if(day_of_week == "THURSDAY"){
			
			for(int i = 4; i < 7; i++){			
				week[i] = date.plusDays(i-4).toString(); 
			}
			for(int i = 0; i < 4; i++){
				week[i] = (date.minusDays(4-i).toString());
			}
		}
		else if(day_of_week == "FRIDAY"){
			
			for(int i = 5; i < 7; i++){			
				week[i] = date.plusDays(i-5).toString(); 
			}
			for(int i = 0; i < 5; i++){
				week[i] = (date.minusDays(5-i).toString());
			}	
		}
		else if(day_of_week == "SATURDAY"){
			
			for(int i = 6; i < 7; i++){			
				week[i] = date.plusDays(i-6).toString(); 
			}
			for(int i = 0; i < 6; i++){
				week[i] = (date.minusDays(6-i).toString());
			}	
		}	
	}
	
	@FXML
	public void addDataToXYChart(){
		seriesAM.getData().clear();
		seriesPM.getData().clear();
		
		//Calculates the amount of AM Customer Attendances for each day of the Week
		//The calcNumAM methods are in the Customer_Attendance_Report_Model class
		seriesAM.getData().add(new XYChart.Data<>("Sunday", linechart.calcNumAMSunday(week[0])));
		seriesAM.getData().add(new XYChart.Data<>("Monday", linechart.calcNumAMMonday(week[1])));
		seriesAM.getData().add(new XYChart.Data<>("Tuesday", linechart.calcNumAMTuesday(week[2])));
		seriesAM.getData().add(new XYChart.Data<>("Wednesday", linechart.calcNumAMWednesday(week[3])));			
		seriesAM.getData().add(new XYChart.Data<>("Thursday", linechart.calcNumAMThursday(week[4])));
		seriesAM.getData().add(new XYChart.Data<>("Friday", linechart.calcNumAMFriday(week[5])));
		seriesAM.getData().add(new XYChart.Data<>("Saturday", linechart.calcNumAMSaturday(week[6])));
		
		//Calculates the amount of PM Customer Attendances for each day of the Week
		//The calcNumPM methods are in the Customer_Attendance_Report_Model class
		seriesPM.getData().add(new XYChart.Data<>("Sunday", linechart.calcNumPMSunday(week[0])));
		seriesPM.getData().add(new XYChart.Data<>("Monday", linechart.calcNumPMMonday(week[1])));
		seriesPM.getData().add(new XYChart.Data<>("Tuesday", linechart.calcNumPMTuesday(week[2])));
		seriesPM.getData().add(new XYChart.Data<>("Wednesday", linechart.calcNumPMWednesday(week[3])));
		seriesPM.getData().add(new XYChart.Data<>("Thursday", linechart.calcNumPMThursday(week[4])));
		seriesPM.getData().add(new XYChart.Data<>("Friday", linechart.calcNumPMFriday(week[5])));
		seriesPM.getData().add(new XYChart.Data<>("Saturday", linechart.calcNumPMSaturday(week[6])));
	}
		
	//Method Called when a date is Chosen
	@FXML
	public void setOnDatePickerChosen(Event event){
		
		dtWeek.setEditable(false);
		lblWeek.setText("Week of " + dtWeek.getValue().toString());
		
		//Generates the chart based on the values from dtWeek
		setDates(dtWeek.getValue());
		addDataToXYChart();		
	}
		
	//Method to print the Line Chart
	@FXML  
	public void printChart(Event event) throws InvocationTargetException{
		Printer printer = Printer.getDefaultPrinter();
		PrinterJob job = PrinterJob.createPrinterJob();
		
		//if default printer is found, Scale the printer to the size of the page, based on 
		//default printer setup. Orientation should be Landscape
		try {
			btnPrint.setVisible(false);
			
			if(printer != null){
				 PageLayout pageLayout = printer.createPageLayout(Paper.A4, PageOrientation.LANDSCAPE, Printer.MarginType.DEFAULT);
				 double scaleX = pageLayout.getPrintableWidth() / Screen.getBoundsInParent().getWidth();
				 double scaleY = pageLayout.getPrintableHeight() / Screen.getBoundsInParent().getHeight();
				 Scale scale = new Scale(scaleX, scaleY);
			  	 Screen.getTransforms().add(scale);
			  	   
			if(job.printPage(pageLayout, Screen)){
			     job.endJob();
			     //Scale the chart back to normal
			     Screen.getTransforms().remove(scale);        	
			    }
			    else{
			    	NotificationType notificationType = NotificationType.ERROR;
			        TrayNotification tray = new TrayNotification();
			        tray.setTitle("Printing error");
			        tray.setMessage("Try turning on your printers");
			        tray.setNotificationType(notificationType);
			        tray.showAndDismiss(Duration.millis(10000));
			    }
			}
			//if printer is null, give a tray notification that printer is not found
			else if(printer == null){
		    	//tray notification printer not found
		    		
				NotificationType notificationType = NotificationType.ERROR;
		        TrayNotification tray = new TrayNotification();
		        tray.setTitle("Printer not found");
		        tray.setMessage("Please set default printer/turn printer on");
		        tray.setNotificationType(notificationType);
		        tray.showAndDismiss(Duration.millis(10000));	
		    }
		} finally {
			btnPrint.setVisible(true);
		}
	}
}

