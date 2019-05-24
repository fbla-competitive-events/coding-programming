package com.staleyhighschool.fbla.gui.lib.users;

import com.staleyhighschool.fbla.Main;
import com.staleyhighschool.fbla.gui.util.ToolTipText;
import com.staleyhighschool.fbla.library.Library;
import com.staleyhighschool.fbla.users.User;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class ChangeUserInfo {

  private Scene scene;
  BorderPane layout;
  VBox mainContent;

  User user;

  public ChangeUserInfo(User user) {
    this.user = user;
    mainContent = generateContent();
    layout = new BorderPane();
    layout.setLeft(Main.generateNavigation());
    layout.setCenter(mainContent);
    scene = new Scene(layout, 960, 540);
  }

  public Scene getScene() {
    return scene;
  }

  private VBox generateContent() {
    VBox box = new VBox();

    HBox tBox = new HBox();
    HBox sBox = new HBox();

    Text rule = new Text(user.getUserID());

    Text firstNameTag = new Text("First Name: ");
    TextField firstName = new TextField(user.getFirstName());
    tBox.getChildren().addAll(firstNameTag, firstName);

    Text lastNameTag = new Text("Last Name: ");
    TextField lastName = new TextField(user.getLastName());
    sBox.getChildren().addAll(lastNameTag, lastName);

    Button update = new Button("Update");

    update.setOnAction(e -> {
      Library.connection.editUser(user, "firstName", firstName.getText());
      Library.connection.editUser(user, "lastName", lastName.getText());
      Main.manageUsers.refresh();
      Main.changeScene(Main.manageUsers.getManageUsers());
    });

    update.setTooltip(new Tooltip(ToolTipText.UPDATE_USER_MANAGE_USERS));

    box.getChildren().addAll(rule, tBox, sBox, update);

    return box;
  }
}
