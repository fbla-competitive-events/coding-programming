package com.staleyhighschool.fbla.gui.lib.books;

import com.staleyhighschool.fbla.Main;
import com.staleyhighschool.fbla.database.Connector;
import com.staleyhighschool.fbla.gui.util.ToolTipText;
import com.staleyhighschool.fbla.library.Book;
import com.staleyhighschool.fbla.library.Library;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;


public class ManageBooks {

  private String TAG = (this.getClass().getName() + ": ");

  private Scene manageBooks;
  private BorderPane layout;
  private ScrollPane mainContent;
  private GridPane bookList;

  private List<Book> selectedBooks;
  private List<CheckBox> checkBoxes;
  private List<Integer> indexOfSelected;

  private int totalRows;

  private final String name = " | Manage Books";

  public ManageBooks() {
    bookList = populateBookList();
    mainContent = new ScrollPane();
    mainContent.setContent(bookList);

    layout = new BorderPane();
    layout.setLeft(Main.generateNavigation());
    layout.setCenter(mainContent);
    layout.setTop(topButtons());

    manageBooks = new Scene(layout, 960, 540);
  }

  public Scene getManageBooks() {
    return manageBooks;
  }

  public String getName() {
    return name;
  }

  public void refresh() {
    bookList = populateBookList();
    mainContent.setContent(bookList);
  }

  private HBox topButtons() {
    HBox hBox = new HBox(10);

    Button addBook = new Button("Add Book");
    Button returnBooks = new Button("Return Selected");
    Button delete = new Button("Delete Selected");
    Button refresh = new Button("Refresh List");

    addBook.setOnAction(e -> Main.changeScene(new AddBook().getAddBook()));

    returnBooks.setOnAction(e -> {
      checkSelected();
      Main.library.returnBook(selectedBooks);
      deselectAll();
      refresh();
    });
    delete.setOnAction(e -> {
      checkSelected();
      Collections.sort(indexOfSelected);
      int runs = 0;
      for (int row : indexOfSelected) {
        checkBoxes.remove(row - runs);
        deleteRow(bookList, row - runs + 1);
        runs++;
      }
      Main.library.deleteBook(selectedBooks);
      deselectAll();
      refresh();
    });

    refresh.setOnAction(e -> {
      refresh();
    });

    addBook.setTooltip(new Tooltip(ToolTipText.ADD_BOOK_MANAGE_BOOKS));
    returnBooks.setTooltip(new Tooltip(ToolTipText.RETURN_BOOK_MANAGE_BOOKS));
    delete.setTooltip(new Tooltip(ToolTipText.DELETE_BOOK_MANAGE_BOOKS));
    refresh.setTooltip(new Tooltip(ToolTipText.REFRESH_LIST_MANAGE_BOOKS));

    hBox.getChildren().addAll(addBook, returnBooks, delete, refresh);

    return hBox;
  }

  private GridPane populateBookList() {
    totalRows = 0;
    if (bookList != null) {
    }
    GridPane pane = new GridPane();

    pane.setPadding(new Insets(4));
    pane.setVgap(4);
    pane.setHgap(8);

    checkBoxes = new ArrayList<>();
    selectedBooks = new ArrayList<>();

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

    for (Book book : Library.bookList) {

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
      Button checkOutBook = new Button("Check Out");
      Button delete = new Button("Delete");

      returnBook.setOnAction(e -> {
        Library.connection.userReturnBook(book);
        refresh();
      });

      checkOutBook.setOnAction(e -> Main.changeScene(new CheckOutBook(book).getCheckOutBook()));

      delete.setOnAction(e -> {
        Library.connection.deleteBook(book);
        deleteRow(bookList, Library.bookList.indexOf(book) + 1);
        refresh();
      });

      checkBoxes.add(title);

      pane.add(title, 1, wRow);
      pane.add(author, 2, wRow);
      pane.add(id, 3, wRow);
      pane.add(isOut, 4, wRow);
      pane.add(isLate, 5, wRow);

      if (book.isOut()) {
        pane.add(date, 6, wRow);
        pane.add(returnBook, 7, wRow);
        pane.add(delete, 8, wRow);
      } else {
        pane.add(checkOutBook, 6, wRow);
        pane.add(delete, 7, wRow);
      }

      wRow++;
      totalRows = wRow;
    }

    return pane;
  }

  private void checkSelected() {
    indexOfSelected = new ArrayList<>();
    System.out.println(TAG + "Books: " + Library.bookList.size() + " Checks: " + checkBoxes.size());
    for (int i = 0; i < checkBoxes.size(); i++) {
      if (checkBoxes.get(i).isSelected()) {
        selectedBooks.add(Library.bookList.get(i));
        indexOfSelected.add(i);
      }
    }
  }

  private void deselectAll() {
    for (CheckBox box : checkBoxes) {
      box.setSelected(false);
    }
  }

  void appendList(Book book) {

    String isOutT = "false";
    String isLateT = "false";

    if (book.isOut()) {
      isOutT = "true";
    }
    if (book.isLate()) {
      isLateT = "true";
    }

    CheckBox title = new CheckBox(book.getBookTitle());
    Text author = new Text(book.getBookAuthor());
    Text id = new Text(book.getBookID());
    Text isOut = new Text(isOutT);
    Text isLate = new Text(isLateT);
    Text date = new Text(Connector.dateFormat.format(book.getDateOut()));

    Button returnBook = new Button("Return");
    Button checkOutBook = new Button("Check Out");
    Button delete = new Button("Delete");

    System.out.println(delete.toString());
    returnBook.setOnAction(e -> Library.connection.userReturnBook(book));
    delete.setOnAction(e -> {
      Library.connection.deleteBook(book);
      deleteRow(bookList, Library.bookList.indexOf(book) + 1);
    });

    checkBoxes.add(title);

    bookList.add(title, 1, totalRows);
    bookList.add(author, 2, totalRows);
    bookList.add(id, 3, totalRows);
    bookList.add(isOut, 4, totalRows);
    bookList.add(isLate, 5, totalRows);

    if (book.isOut()) {
      bookList.add(date, 6, totalRows);
      bookList.add(returnBook, 7, totalRows);
      bookList.add(delete, 8, totalRows);
    } else {
      bookList.add(checkOutBook, 6, totalRows);
      bookList.add(delete, 7, totalRows);
    }

    totalRows++;
  }

  private void deleteRow(GridPane grid, final int row) {
    Set<Node> deleteNodes = new HashSet<>();
    for (Node child : grid.getChildren()) {
      // get index from child
      Integer rowIndex = GridPane.getRowIndex(child);

      // handle null values for index=0
      int r = rowIndex == null ? 0 : rowIndex;

      if (r > row) {
        // decrement rows for rows after the deleted row
        GridPane.setRowIndex(child, r - 1);
      } else if (r == row) {
        // collect matching rows for deletion
        deleteNodes.add(child);
      }
    }

    // remove nodes from row
    grid.getChildren().removeAll(deleteNodes);
  }
}
