package com.staleyhighschool.fbla.gui.lib.users;

import com.staleyhighschool.fbla.Main;
import com.staleyhighschool.fbla.gui.util.ToolTipText;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class AddUser {

  private Scene addUser;
  private BorderPane layout;
  private HBox buttons;
  private HBox input;
  private VBox mainContent;

  private Button createUser;

  private TextField title;
  private TextField author;
  private ToggleGroup type;
  private RadioButton student;
  private RadioButton teacher;
  private boolean tOrS;

  public AddUser() {
    tOrS = false;

    type = new ToggleGroup();

    student = new RadioButton("Student");
    teacher = new RadioButton("Teacher");

    title = new TextField();
    author = new TextField();
    title.setPromptText("First Name");
    author.setPromptText("Last Name");

    teacher.setToggleGroup(type);
    student.setToggleGroup(type);

    generateButtons();

    buttons = new HBox(10);
    input = new HBox(10);
    mainContent = new VBox(20);

    buttons.getChildren().add(createUser);
    input.getChildren().addAll(title, author);

    mainContent.getChildren().addAll(input, buttons, teacher, student);

    layout = new BorderPane();
    layout.setLeft(Main.generateNavigation());
    layout.setCenter(mainContent);

    addUser = new Scene(layout, 960, 540);

  }

  public Scene getAddUser() {
    return addUser;
  }

  private void newUser() {
    if (teacher.isSelected()) {
      tOrS = true;
    } else {
      tOrS = false;
    }
    Main.manageUsers.appendList(Main.library.addUser(title.getText(), author.getText(), tOrS));
    Main.changeScene(Main.manageUsers.getManageUsers());
  }

  private void generateButtons() {
    createUser = new Button("Add User");

    createUser.setOnAction(e -> newUser());
    createUser.setTooltip(new Tooltip(ToolTipText.CREATE_USER_MANAGE_USERS));
  }
}
