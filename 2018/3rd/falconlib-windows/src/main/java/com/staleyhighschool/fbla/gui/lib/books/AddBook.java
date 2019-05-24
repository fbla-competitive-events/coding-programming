package com.staleyhighschool.fbla.gui.lib.books;

import com.staleyhighschool.fbla.Main;
import com.staleyhighschool.fbla.gui.util.ToolTipText;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class AddBook {

  private Scene addBook;
  private BorderPane layout;
  private HBox buttons;
  private HBox input;
  private VBox mainContent;

  Button createBook;

  private TextField title;
  private TextField author;

  public AddBook() {
    title = new TextField();
    author = new TextField();
    title.setPromptText("Title");
    author.setPromptText("Author");

    generateButtons();

    buttons = new HBox(10);
    input = new HBox(10);
    mainContent = new VBox(20);

    buttons.getChildren().add(createBook);
    input.getChildren().addAll(title, author);

    mainContent.getChildren().addAll(input, buttons);

    layout = new BorderPane();
    layout.setLeft(Main.generateNavigation());
    layout.setCenter(mainContent);

    addBook = new Scene(layout, 960, 540);

  }

  public Scene getAddBook() {
    return addBook;
  }

  private void newBook() {
    Main.manageBooks.appendList(Main.library.addBook(title.getText(), author.getText()));
    Main.changeScene(Main.manageBooks.getManageBooks());
  }

  private void generateButtons() {
    createBook = new Button("Add Book");

    createBook.setOnAction(e -> newBook());
    createBook.setTooltip(new Tooltip(ToolTipText.CREATE_BOOK_MANAGE_BOOKS));
  }
}
