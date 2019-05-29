package Login;

import AdminDash.AdminController;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import StudentDash.StudentsController;
import Database.dbConnect;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class LoginController implements Initializable {
    LoginModel loginModel = new LoginModel();

    @FXML private Label status;
    @FXML private TextField username;
    @FXML private PasswordField password;
    @FXML private ComboBox combobox;
    @FXML private Button loginbutton;
    @FXML private Label loginstatus;

    public void initialize(URL url, ResourceBundle rb){
        if(this.loginModel.isDatabaseConnected()) {
            this.status.setText("Connected");
        }
        else{
            this.status.setText("Not Connected");
        }

        this.combobox.setItems(FXCollections.observableArrayList(Options.values()));
    }

    @FXML
    public void Login(ActionEvent event){
        try{
            if((Options)this.combobox.getValue() != null) {
                if (this.loginModel.isLogin(this.username.getText(), this.password.getText(), ((Options) this.combobox.getValue()).toString())) {
                    Stage stage = (Stage) this.loginbutton.getScene().getWindow();
                    stage.close();
                    if (((Options) this.combobox.getValue()).toString().equals("Admin")) {
                        adminLogin();
                    } else if (((Options) this.combobox.getValue()).toString().equals("Student")) {
                        studentLogin();
                    }
                } else {
                    this.loginstatus.setText("Wrong username or password");
                }
            }
            else{
                loginstatus.setText("Select user type");
            }
        }
        catch(Exception localException){
            System.err.println("Error here" + localException + " Line number " + localException.getStackTrace()[0].getLineNumber());
        }
    }

    public void studentLogin(){
        try{
            Stage userStage = new Stage();
            FXMLLoader loader = new FXMLLoader();
            Pane root = (Pane)loader.load(getClass().getResource("/StudentDash/studentdash.fxml").openStream());

            StudentsController studentsController = (StudentsController)loader.getController();

            Scene scene = new Scene(root);
            userStage.setScene(scene);
            userStage.setTitle(getName(username.getText(), password.getText()));
            userStage.setResizable(false);
            userStage.show();
        }
        catch(IOException e){
            System.err.print("Error" + e);
        }
    }

    public void adminLogin(){
        try{
            Stage adminStage = new Stage();
            FXMLLoader adminLoader = new FXMLLoader();
            Pane adminroot = (Pane)adminLoader.load(getClass().getResource("/AdminDash/admin.fxml"));

            AdminController adminController = (AdminController)adminLoader.getController();

            Scene scene = new Scene(adminroot);
            adminStage.setScene(scene);
            adminStage.setTitle("Admin Dashboard");
            adminStage.setResizable(false);
            adminStage.show();
        }
        catch(IOException e){
            System.err.print("Error" + e);
        }
    }

    public String getName(String username, String password){
        try {
            Connection connection = new dbConnect().getConnection();
            String sqlfinder = "SELECT * FROM login WHERE username ='" + username + "' and password ='" + password + "'";
            ResultSet rs = connection.createStatement().executeQuery(sqlfinder);
            return rs.getString(1);
        }
        catch(SQLException e){
            System.out.print("login error " + e);
        }
        return null;
    }
}