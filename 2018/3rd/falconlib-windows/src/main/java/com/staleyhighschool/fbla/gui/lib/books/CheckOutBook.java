package com.staleyhighschool.fbla.gui.lib.books;

import com.staleyhighschool.fbla.Main;
import com.staleyhighschool.fbla.gui.util.Alert;
import com.staleyhighschool.fbla.gui.util.ToolTipText;
import com.staleyhighschool.fbla.library.Book;
import com.staleyhighschool.fbla.library.Library;
import com.staleyhighschool.fbla.users.User;
import com.staleyhighschool.fbla.util.enums.AccountType;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class CheckOutBook {

  private int totalRows;

  private Scene checkOutBook;
  private BorderPane layout;
  private ScrollPane mainContent;
  private GridPane userList;
  private VBox checkMenu;

  private Book bookToCheck;
  private User user;
  private String userID;

  public CheckOutBook(Book book) {

    userID = "";

    bookToCheck = book;
    checkMenu = generateCheckMenu();

    userList = populateUserList();

    mainContent = new ScrollPane();
    mainContent.setContent(userList);

    layout = new BorderPane();
    layout.setLeft(Main.generateNavigation());
    layout.setCenter(mainContent);
    layout.setRight(checkMenu);

    checkOutBook = new Scene(layout, 960, 540);
  }

  private boolean checkOut() {
    System.out.println(user.getUserID());
    return Library.connection.userCheckOut(user, bookToCheck);
  }

  public Scene getCheckOutBook() {
    return checkOutBook;
  }

  private VBox generateCheckMenu() {
    VBox box = new VBox();
    Text title = new Text("Book Title: " + bookToCheck.getBookTitle());
    Text bookID = new Text("Book ID: " + bookToCheck.getBookID());
    Text currentUser = new Text("User ID: " + userID);

    Button check = new Button("Check Out");

    check.setOnAction(e -> {
      boolean pass = checkOut();
      if (pass) {
        Alert.display("Checked Out",
            ("Book: " + bookToCheck.getBookTitle() + "(" + bookToCheck.getBookID() + ") " +
                "checked out to -> User: " + user.getUserID()));
      } else {
        Alert.display("Failed", ("User: " + user.getUserID() + " has too many books"));
      }
      Main.manageBooks.refresh();
      Main.changeScene(Main.manageBooks.getManageBooks());
    });

    check.setTooltip(new Tooltip(ToolTipText.CHECKOUT_BOOK_MANAGE_BOOKS));

    box.getChildren().addAll(title, bookID, currentUser, check);

    return box;
  }

  private GridPane populateUserList() {
    totalRows = 0;
    GridPane pane = new GridPane();

    pane.setPadding(new Insets(4));
    pane.setVgap(4);
    pane.setHgap(8);

    int wRow = 1;
    int tRow = 0;

    Text uIDTag = new Text("ID");
    Text fNameTag = new Text("First Name");
    Text lNameTag = new Text("Last Name");
    Text aTypeTag = new Text("Account Type");

    pane.add(uIDTag, 1, tRow);
    pane.add(fNameTag, 2, tRow);
    pane.add(lNameTag, 3, tRow);
    pane.add(aTypeTag, 4, tRow);

    for (User user : Library.userList) {

      String type = null;

      if (user.getAccountType() == AccountType.TEACHER) {
        type = "teacher";
      } else if (user.getAccountType() == AccountType.STUDENT) {
        type = "student";
      }

      Text uID = new Text(user.getUserID());
      Text fName = new Text(user.getFirstName());
      Text lName = new Text(user.getLastName());
      Text aType = new Text(type);
      Button select = new Button("Select user");

      select.setOnAction(e -> {
        this.user = user;
        userID = user.getUserID();
        checkMenu = generateCheckMenu();
        layout.setRight(checkMenu);
      });

      pane.add(uID, 1, wRow);
      pane.add(fName, 2, wRow);
      pane.add(lName, 3, wRow);
      pane.add(aType, 4, wRow);
      pane.add(select, 5, wRow);

      wRow++;
      totalRows = wRow;
    }

    return pane;
  }
}
