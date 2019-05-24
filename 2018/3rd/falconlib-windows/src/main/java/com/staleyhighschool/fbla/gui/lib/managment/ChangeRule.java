package com.staleyhighschool.fbla.gui.lib.managment;

import com.staleyhighschool.fbla.Main;
import com.staleyhighschool.fbla.library.Library;
import com.staleyhighschool.fbla.util.enums.AccountType;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class ChangeRule {

  private Scene scene;
  BorderPane layout;
  VBox mainContent;

  private String ruleName;

  public ChangeRule(String ruleName) {
    this.ruleName = ruleName;
    mainContent = generateContent();
    layout = new BorderPane();
    layout.setLeft(Main.generateNavigation());
    layout.setCenter(mainContent);
    scene = new Scene(layout, 960, 540);
  }

  private VBox generateContent() {
    VBox box = new VBox();

    HBox tBox = new HBox();
    HBox sBox = new HBox();

    Text rule = new Text(ruleName);

    Text teacherTag = new Text("Teacher: ");
    TextField teacher = new TextField(
        Double.toString(Library.connection.getRule(AccountType.TEACHER, ruleName)));
    tBox.getChildren().addAll(teacherTag, teacher);

    Text studentTag = new Text("Student: ");
    TextField student = new TextField(
        Double.toString(Library.connection.getRule(AccountType.STUDENT, ruleName)));
    sBox.getChildren().addAll(studentTag, student);

    Button update = new Button("Update");

    update.setOnAction(e -> {
      Library.connection
          .setRule(AccountType.TEACHER, ruleName, Double.parseDouble(teacher.getText()));
      Library.connection
          .setRule(AccountType.STUDENT, ruleName, Double.parseDouble(student.getText()));
      Main.manageRules.reload();
      Main.changeScene(Main.manageRules.getScene());
    });

    box.getChildren().addAll(rule, tBox, sBox, update);

    return box;
  }

  public Scene getScene() {
    return scene;
  }
}
