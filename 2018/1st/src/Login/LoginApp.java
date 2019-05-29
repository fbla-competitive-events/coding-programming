package Login;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class LoginApp extends Application {
    private static Stage stage1;

    public void start(Stage stage)throws Exception{
        Parent root = (Parent)(FXMLLoader.load(getClass().getResource("Login.fxml")));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Library Manager Login");
        stage1 = stage;
        stage.show();
    }

    public static Stage getStage(){
        return stage1;
    }

    public static void main(String[] args){
        launch(args);
    }
}
