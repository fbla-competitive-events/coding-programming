package com.staleyhighschool.fbla.gui.lib.users;

import com.staleyhighschool.fbla.Main;
import com.staleyhighschool.fbla.database.Connector;
import com.staleyhighschool.fbla.library.Book;
import com.staleyhighschool.fbla.library.Library;
import com.staleyhighschool.fbla.users.User;
import java.util.ArrayList;
import java.util.List;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

public class ViewBooks {

  private Scene viewBooks;
  private BorderPane layout;
  private GridPane bookList;
  private ScrollPane mainContent;

  private List<Book> selectedBooks;
  private List<CheckBox> checkBoxes;
  private List<Integer> indexOfSelected;

  private User user;

  public ViewBooks(User user) {

    this.user = user;

    bookList = generateBookList();
    mainContent = new ScrollPane();
    mainContent.setContent(bookList);

    layout = new BorderPane();
    layout.setLeft(Main.generateNavigation());
    layout.setCenter(mainContent);

    viewBooks = new Scene(layout, 960, 540);
  }

  public Scene getScene() {
    return viewBooks;
  }

  private GridPane generateBookList() {
    GridPane pane = new GridPane();

    pane.setPadding(new Insets(4));
    pane.setVgap(4);
    pane.setHgap(8);

    checkBoxes = new ArrayList<>();
    selectedBooks = new ArrayList<>();
    List<Book> userBooks = user.getUserBooks();

    int wRow = 1;
    int tRow = 0;

    Text titleTag = new Text("Title");
    Text authorTag = new Text("Author");
    Text idTag = new Text("ID");
    Text isOutTag = new Text("Is Out");
    Text isLateTag = new Text("Is Late");

    pane.add(titleTag, 1, tRow);
    pane.add(authorTag, 2, tRow);
    pane.add(idTag, 3, tRow);
    pane.add(isOutTag, 4, tRow);
    pane.add(isLateTag, 5, tRow);

    for (Book book : userBooks) {

      String isOutT = "false";
      String isLateT = "false";

      if (book.isOut()) {
        isOutT = "true";
      } else {
        isOutT = "false";
      }
      if (book.isLate()) {
        isLateT = "true";
      } else {
        isLateT = "false";
      }

      CheckBox title = new CheckBox(book.getBookTitle());
      Text author = new Text(book.getBookAuthor());
      Text id = new Text(book.getBookID());
      Text isOut = new Text(isOutT);
      Text isLate = new Text(isLateT);
      Text date = new Text(Connector.dateFormat.format(book.getDateOut()));

      Button returnBook = new Button("Return");

      returnBook.setOnAction(e -> {
        Library.connection.userReturnBook(book);
        refresh();
      });

      checkBoxes.add(title);

      pane.add(title, 1, wRow);
      pane.add(author, 2, wRow);
      pane.add(id, 3, wRow);
      pane.add(isOut, 4, wRow);
      pane.add(isLate, 5, wRow);
      pane.add(date, 6, wRow);
      pane.add(returnBook, 7, wRow);

      wRow++;
    }

    return pane;
  }

  public void refresh() {
    bookList = generateBookList();
    mainContent.setContent(bookList);
  }

}
