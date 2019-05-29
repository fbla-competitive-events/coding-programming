package application;

import javafx.fxml.FXML;

import javafx.scene.text.Text;

import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

//This is the mini-window that appears when for extra employee details
public class Employee_Profile_ViewController {
	@FXML
	private ImageView profilePic;
	@FXML
	private Text txtName;
	@FXML
	private Text txtPhone;
	@FXML
	private Text txtEmail;
	@FXML
	private Text txtDOB;
	@FXML
	private Text txtID;
	@FXML
	private Text txtAddress;
	@FXML
	private Label label;
	
	private Employee_Profile_ViewModel Info;
	//Method to display the information for the given employee: Employee_Profile_ViewModel
    public void setCurrentInfo(Employee_Profile_ViewModel currentInfo) {
        txtID.setText("Employee ID: " + currentInfo.getId());
        txtName.setText("Name: " + currentInfo.getName());
        txtPhone.setText("Phone Number: " + currentInfo.getPhone());
        txtDOB.setText("DOB: " + currentInfo.getDOB());
        txtAddress.setText("Address: " +currentInfo.getAddress());
        txtEmail.setText("Email: " + currentInfo.getEmail());
      
       
        profilePic.setImage(new Image("/application/default-user-icon.png"));

        this.Info = currentInfo;

    }


}
