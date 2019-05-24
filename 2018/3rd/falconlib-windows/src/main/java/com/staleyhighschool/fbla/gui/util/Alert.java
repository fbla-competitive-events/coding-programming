package com.staleyhighschool.fbla.gui.util;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class Alert {

  public static void display(String title, String message) {
    Stage window = new Stage();

    //Block events to other windows
    window.initModality(Modality.APPLICATION_MODAL);
    window.setTitle(title);
    window.setMinWidth(250);

    Label label = new Label();
    label.setText(message);
    Button closeButton = new Button("Close this window");
    closeButton.setOnAction(e -> window.close());

    VBox layout = new VBox(10);
    layout.getChildren().addAll(label, closeButton);
    layout.setAlignment(Pos.CENTER);

    //Display window and wait for it to be closed before returning
    Scene scene = new Scene(layout);
    window.setScene(scene);
    window.showAndWait();
  }

}
