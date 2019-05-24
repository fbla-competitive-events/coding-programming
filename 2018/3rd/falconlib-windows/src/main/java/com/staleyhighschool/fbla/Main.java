package com.staleyhighschool.fbla;

import com.staleyhighschool.fbla.gui.lib.Home;
import com.staleyhighschool.fbla.gui.lib.books.ManageBooks;
import com.staleyhighschool.fbla.gui.lib.managment.ControlPanel;
import com.staleyhighschool.fbla.gui.lib.managment.Logs;
import com.staleyhighschool.fbla.gui.lib.managment.ManageRules;
import com.staleyhighschool.fbla.gui.lib.users.ManageUsers;
import com.staleyhighschool.fbla.gui.util.ToolTipText;
import com.staleyhighschool.fbla.library.Library;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Main extends Application {

  public static final String APP_TITLE = "Falcon Library";

  public static Stage window;

  public static Library library;

  public static Home home;
  public static Logs logs;
  public static ManageBooks manageBooks;
  public static ManageUsers manageUsers;
  public static ControlPanel controlPanel;
  public static ManageRules manageRules;

  public static void main(String[] args) {
    library = new Library();
    launch(args);
  }

  @Override
  public void start(Stage primaryStage) throws Exception {
    window = primaryStage;

    home = new Home();
    logs = new Logs();
    manageBooks = new ManageBooks();
    manageUsers = new ManageUsers();
    controlPanel = new ControlPanel();
    manageRules = new ManageRules();
    window.setTitle(APP_TITLE + " | Home");

    window.setScene(home.getHome());
    window.show();

    window.setOnCloseRequest(e -> closeProgram());
  }

  public static VBox generateNavigation() {
    Button mHome = new Button("Home");
    Button mBooks = new Button("Manage Books");
    Button mUsers = new Button("Manage Users");
    Button mCP = new Button("Control Panel");

    VBox navigation = new VBox();

    navigation.getChildren().addAll(mHome, mBooks, mUsers, mCP);

    mHome.setOnAction(e -> {
      window.setScene(home.getHome());
      window.setTitle(APP_TITLE + home.getName());
    });
    mBooks.setOnAction(e -> {
      window.setScene(manageBooks.getManageBooks());
      manageBooks.refresh();
      window.setTitle(APP_TITLE + Main.manageBooks.getName());
    });
    mUsers.setOnAction(e -> {
      window.setScene(manageUsers.getManageUsers());
      manageUsers.refresh();
      window.setTitle(APP_TITLE + Main.manageUsers.getName());
    });
    mCP.setOnAction(e -> {
      window.setScene(controlPanel.getScene());
      window.setTitle(APP_TITLE + Main.controlPanel.getName());
    });

    mHome.setTooltip(new Tooltip(ToolTipText.HOME_MAIN_NAVIGATION));
    mBooks.setTooltip(new Tooltip(ToolTipText.MANAGE_BOOKS_MAIN_NAVIGATION));
    mUsers.setTooltip(new Tooltip(ToolTipText.MANAGE_USERS_MAIN_NAVIGATION));
    mCP.setTooltip(new Tooltip(ToolTipText.CONTROL_PANEL_MAIN_NAVIGATION));

    return navigation;
  }

  private void closeProgram() {
    Library.connection.closeConnection();
    Library.logging.closeLog();
  }

  public static void changeScene(Scene scene) {
    window.setScene(scene);
  }
}
