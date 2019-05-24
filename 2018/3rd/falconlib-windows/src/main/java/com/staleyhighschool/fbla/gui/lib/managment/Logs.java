package com.staleyhighschool.fbla.gui.lib.managment;

import com.staleyhighschool.fbla.Main;
import com.staleyhighschool.fbla.library.Library;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;

public class Logs {

  private Scene logs;

  public Logs() {

    String saveDirectoryPathString = Library.logging.getDocumentsDirectory();

    Text saveDirectoryPathText = new Text(saveDirectoryPathString);

    BorderPane layout = new BorderPane();
    layout.setLeft(Main.generateNavigation());
    layout.setCenter(saveDirectoryPathText);

    logs = new Scene(layout, 960, 540);
  }

  public Scene getLogs() {
    return logs;
  }


}
