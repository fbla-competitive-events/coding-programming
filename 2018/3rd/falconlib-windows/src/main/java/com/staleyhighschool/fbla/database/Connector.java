package com.staleyhighschool.fbla.database;

import com.staleyhighschool.fbla.library.Book;
import com.staleyhighschool.fbla.library.Library;
import com.staleyhighschool.fbla.users.Student;
import com.staleyhighschool.fbla.users.Teacher;
import com.staleyhighschool.fbla.users.User;
import com.staleyhighschool.fbla.util.CreateTables;
import com.staleyhighschool.fbla.util.enums.AccountType;
import com.staleyhighschool.fbla.util.enums.IsLate;
import com.staleyhighschool.fbla.util.enums.IsOut;
import com.staleyhighschool.fbla.util.enums.LogType;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.apache.commons.lang3.time.DateUtils;

/**
 * Establishes connection with the database, gets values from database, changes values in database,
 * and adds values to database.
 */
public class Connector {

  private String TAG = (this.getClass().getName() + ": ");

  public static Connection connection;

  private final String DATABASE_NAME = "sql3223801";
  private final String DATABASE_URL = "jdbc:sqlite:falcon-lib.db";
  private final String PORT = ":3306";
  public static final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

  /**
   * Establishes connection to database
   */
  public Connector() {
    try {
      Class.forName("org.sqlite.JDBC");
      connection = DriverManager.getConnection(DATABASE_URL);
    } catch (SQLException | ClassNotFoundException e) {
      e.printStackTrace();
    }
    new CreateTables(connection);
  }

  /**
   * Gets the existing connection
   *
   * @return existing {@link Connection}
   */
  public Connection getConnection() {
    return connection;
  }

  public void closeConnection() {
    try {
      connection.close();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  /**
   * Gets list of all books in the library
   *
   * @return {@link List<Book>}
   */
  public List<Book> getLibraryBooks() {

    String query = "SELECT title, author, id, isOut, isLate, dateOut " + "FROM LibraryBooks";

    Statement statement;
    ResultSet resultSet;

    String bookTitle;
    String bookAuthor;
    String bookID;
    IsLate isLate;
    IsOut isOut;
    Date dateOut;

    List<Book> books = new ArrayList<>();
    Book book;

    try {

      statement = connection.createStatement();
      resultSet = statement.executeQuery(query);

      while (resultSet.next()) {

        if (resultSet.getInt("isLate") == 1) {
          isLate = IsLate.LATE;
        } else {
          isLate = IsLate.SAFE;
        }
        if (resultSet.getInt("isOut") == 1) {
          isOut = IsOut.OUT;
        } else {
          isOut = IsOut.IN;
        }

        bookTitle = resultSet.getString("title");
        bookAuthor = resultSet.getString("author");
        bookID = resultSet.getString("id");
        dateOut = dateFormat.parse(resultSet.getString("dateOut"));

        book = new Book(bookTitle, bookAuthor, bookID, isLate, isOut, dateOut);
        books.add(book);
      }

      resultSet.close();
      statement.close();

    } catch (SQLException | ParseException e) {
      e.printStackTrace();
    }

    return books;
  }

  /**
   * Gets list of books that the given {@link User} has
   *
   * @param user {@link User}
   * @return {@link List<Book>}
   */
  public List<Book> getUserBooks(User user) {

    List<Book> userBooks = new ArrayList<>();

    String query = "SELECT books " + "FROM '" + user.getUserID() + "'";

    Statement statement;
    ResultSet resultSet;

    try {

      statement = connection.createStatement();
      resultSet = statement.executeQuery(query);

      while (resultSet.next()) {
        for (int i = 0; i < Library.bookList.size(); i++) {
          if (resultSet.getString("books").equals(Library.bookList.get(i).getBookID())) {
            userBooks.add(Library.bookList.get(i));
          }
        }
      }

      resultSet.close();
      statement.close();

    } catch (SQLException e) {
      e.printStackTrace();
    }

    return userBooks;
  }

  /**
   * Gets the fine rate of the given account type
   *
   * @param accountType the {@link AccountType}
   * @return the fine rate of the {@link AccountType}
   */
  public double getFineRate(AccountType accountType) {

    Statement statement;
    ResultSet resultSet;

    String query;
    double fineRate = 0;

    query = "SELECT student, teacher FROM Rules";

    try {

      statement = connection.createStatement();
      resultSet = statement.executeQuery(query);

      while (resultSet.next()) {
        if (accountType == AccountType.TEACHER) {
          fineRate = resultSet.getDouble("teacher");
        } else if (accountType == AccountType.STUDENT) {
          fineRate = resultSet.getDouble("teacher");
        }
      }

      resultSet.close();
      statement.close();

    } catch (SQLException e) {
      e.printStackTrace();
    }

    return fineRate;
  }

  /**
   * Adds a user to the database and current session from a given {@link User} object
   *
   * @param user {@link User} you want to add to the program/database
   */
  public void addUser(User user) {
    String accountType;

    Statement statement;

    accountType = user.getAccountTypeString();

    String addToUsersQuery = "INSERT INTO Users (firstName, lastName, id, accountType) " +
        "VALUES ('" + user.getFirstName() +
        "', '" + user.getLastName() +
        "', '" + user.getUserID() +
        "', '" + accountType + "')";

    String createTableQuery = "CREATE TABLE '" + user.getUserID() + "' (books TEXT)";

    String insertBlank = "INSERT INTO '" + user.getUserID() + "' (books) VALUES ('space')";

    try {

      statement = connection.createStatement();

      statement.executeUpdate(addToUsersQuery);
      statement.executeUpdate(createTableQuery);
      statement.executeUpdate(insertBlank);

      statement.close();
    } catch (SQLException e) {
      e.printStackTrace();
    }

    Library.logging.writeToLog(LogType.USER_ACTION, "New User: " + user.getUserID());
  }

  /**
   * Used to change a given element of a user
   *
   * @param user {@link User} you want to change the info for
   * @param column Information you want to change
   * @param value Value you want to change it to
   */
  public void editUser(User user, String column, String value) {
    String query =
        "UPDATE Users SET " + column + "='" + value + "' WHERE id='" + user.getUserID() + "'";

    if (column.equals("firstName")) {
      user.setFirstName(value);
    } else if (column.equals("lastName")) {
      user.setLastName(value);
    }

    Statement statement;

    try {
      statement = connection.createStatement();

      statement.executeUpdate(query);

      statement.close();
    } catch (SQLException e) {
      e.printStackTrace();
    }

    Library.logging.writeToLog(LogType.USER_ACTION, "Edit made to User: " + user.getUserID());
  }

  /**
   * Deletes a user from the system/database
   *
   * @param user {@link User} you wish to delete
   */
  public void deleteUser(User user) {

    String deleteUserTable = "DROP TABLE '" + user.getUserID() + "'";
    String deleteUserFromLibrary = "DELETE FROM Users WHERE id='" + user.getUserID() + "'";

    Statement statement;

    System.out.println(TAG + "User " + (user.getUserBooks().size() - 2));
    if (user.getUserBooks().size() == 0) {
      Library.logging.writeToLog(LogType.USER_ACTION, "Deleted User: " + user.getUserID());
      Library.userList.remove(user);
      try {
        statement = connection.createStatement();

        statement.executeUpdate(deleteUserTable);
        statement.executeUpdate(deleteUserFromLibrary);

        statement.close();
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }
  }

  /**
   * Adds a given {@link Book} object to the system/database
   *
   * @param book {@link Book} you wish to add
   */
  public void addBook(Book book) {

    String title = book.getBookTitle();
    String author = book.getBookAuthor();
    String id = book.getBookID();
    Date dateOut = Book.storeDate;

    Statement statement;

    String query = "INSERT INTO LibraryBooks (title, author, id, isOut, isLate, dateOut) " +
        "VALUES ('" + title +
        "', '" + author +
        "', '" + id +
        "', " + 0 +
        ", " + 0 +
        ", '" + dateFormat.format(dateOut) + "')";

    try {
      statement = connection.createStatement();
      statement.executeUpdate(query);

      statement.close();
    } catch (SQLException e) {
      e.printStackTrace();
    }

    Library.logging.writeToLog(LogType.BOOK_ACTION, "Created Book: " + book.getBookID());
  }

  /**
   * Removes a given book from the system/database
   *
   * @param book {@link Book} you wish to delete
   */
  public void deleteBook(Book book) {
    System.out.println(TAG + "Book ID: " + book.getBookID());
    String query = "DELETE FROM LibraryBooks WHERE id='" + book.getBookID() + "'";

    Statement statement;

    try {
      statement = connection.createStatement();
      statement.executeUpdate(query);

      statement.close();
    } catch (SQLException e) {
      e.printStackTrace();
    }

    Library.logging.writeToLog(LogType.BOOK_ACTION, "Deleted Book: " + book.getBookID());
  }

  /**
   * Gets a complete list of users in the library
   *
   * @return Complete {@link List<User>}
   */
  public List<User> getCurrentUsers() {

    List<User> users = new ArrayList<>();

    User user;

    String firstName, lastName, id;
    AccountType accountType = null;

    Statement statement;
    ResultSet resultSet;

    String query = "SELECT firstName, lastName, id, accountType FROM Users";

    try {

      statement = connection.createStatement();
      resultSet = statement.executeQuery(query);

      while (resultSet.next()) {

        if (resultSet.getString("accountType").equals("teacher")) {
          accountType = AccountType.TEACHER;
        } else if (resultSet.getString("accountType").equals("student")) {
          accountType = AccountType.STUDENT;
        }

        firstName = resultSet.getString("firstName");
        lastName = resultSet.getString("lastName");
        id = resultSet.getString("id");

        if (accountType == AccountType.TEACHER) {
          user = new Teacher(firstName, lastName, id);
          users.add(user);
        } else if (accountType == AccountType.STUDENT) {
          user = new Student(firstName, lastName, id);
          user.setUserBooks();
          users.add(user);
        }
      }

      resultSet.close();
      statement.close();

    } catch (SQLException e) {
      e.printStackTrace();
    }

    return users;
  }

  /**
   * Gets the max days a selected user is able to have a book checked out
   *
   * @param user selected {@link User}
   * @return Number of days a user is able to have a book out
   */
  public int getMaxDays(User user) {
    int days;

    String query = null;
    String type = null;

    if (user.getAccountType() == AccountType.TEACHER) {
      query = "SELECT teacher FROM Rules WHERE rule='maxDays'";
      type = "teacher";
    } else if (user.getAccountType() == AccountType.STUDENT) {
      query = "SELECT student FROM Rules WHERE rule='maxDays'";
      type = "teacher";
    }

    days = getMaxRule(query, type);
    return days;
  }

  /**
   * Gets the max number of books a selected user is able to check out at a time
   *
   * @param user Selected {@link User}
   * @return Maximum number of books a user can have out at a time
   */
  public int getMaxBooks(User user) {
    int books;

    String query = null;
    String type = null;

    if (user.getAccountType() == AccountType.TEACHER) {
      query = "SELECT teacher FROM Rules WHERE rule='maxBooks'";
      type = "teacher";
    } else if (user.getAccountType() == AccountType.STUDENT) {
      query = "SELECT student FROM Rules WHERE rule='maxBooks'";
      type = "student";
    }

    books = getMaxRule(query, type);
    return books;
  }

  /**
   * Gets value of selected rule
   *
   * @param query Selected rule
   * @param type Account type the rule is for
   * @return Selected rule for the given account type
   */
  private int getMaxRule(String query, String type) {
    Statement statement;
    ResultSet resultSet;
    int max = 0;
    try {
      statement = connection.createStatement();
      resultSet = statement.executeQuery(query);

      while (resultSet.next()) {
        max = (int) resultSet.getDouble(type);
      }

      resultSet.close();
      statement.close();

    } catch (SQLException e) {
      e.printStackTrace();
    }
    return max;
  }

  /**
   * Checks a book out to a given user
   *
   * @param user Selected user
   * @param book Selected book
   * @return if the action was successful
   */
  public boolean userCheckOut(User user, Book book) {
    String query;

    Statement statement;

    Library.logging.writeToLog(LogType.CHECKOUT,
        "User: " + user.getUserID() + ", checked out Book: " + book.getBookID());

    if (user.getUserBooks().size() + 1 <= getMaxBooks(user)) {
      query = "INSERT INTO '" + user.getUserID() + "' (books) VALUES ('" + book.getBookID() + "')";
      String setOut = "UPDATE LibraryBooks SET isOut=1 WHERE id='" + book.getBookID() + "'";
      String setDate =
          "UPDATE LibraryBooks SET dateOut='" + dateFormat.format(Calendar.getInstance().getTime())
              + "' WHERE id='" + book.getBookID() + "'";
      book.setIsOut(IsOut.OUT);
      try {
        book.setDateOut(
            DateUtils.parseDate(dateFormat.format(Calendar.getInstance().getTime()), "yyyy-MM-dd"));
      } catch (ParseException e) {
        e.printStackTrace();
      }
      System.out.println(TAG + dateFormat.format(Calendar.getInstance().getTime()));

      try {
        statement = connection.createStatement();
        statement.executeUpdate(query);
        statement.executeUpdate(setOut);
        statement.executeUpdate(setDate);

        statement.close();
        return true;
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }
    return false;
  }

  /**
   * Returns a selected book
   *
   * @param book selected {@link Book}
   */
  public void userReturnBook(Book book) {
    String query;

    Statement statement;
    ResultSet resultSet;

    boolean pass = false;
    System.out.println(TAG + " Got in");
    do {
      for (User user : Library.userList) {
        System.out.println(TAG + "Cycling users");
        try {
          statement = connection.createStatement();
          resultSet = statement.executeQuery("SELECT books FROM '" + user.getUserID() + "'");

          while (resultSet.next()) {
            if (book.getBookID().equals(resultSet.getString("books"))) {

              Library.logging.writeToLog(LogType.RETURN,
                  "User: " + user.getUserID() + ", returned Book: " + book.getBookID());
              query =
                  "DELETE FROM '" + user.getUserID() + "' WHERE books='" + book.getBookID() + "'";
              String setIn =
                  "UPDATE LibraryBooks SET isOut=0 WHERE id='" + book.getBookID() + "'";
              String setDate =
                  "UPDATE LibraryBooks SET dateOut='" + dateFormat.format(Book.storeDate)
                      + "' WHERE id='" + book.getBookID() + "'";
              book.setIsOut(IsOut.IN);
              book.setIsLate(IsLate.SAFE);
              book.setDateOut(Book.storeDate);
              String setIsLate =
                  "UPDATE LibraryBooks SET isLate=0 WHERE id='" + book.getBookID() + "'";
              try {
                statement = connection.createStatement();
                statement.executeUpdate(query);
                statement.executeUpdate(setIn);
                statement.executeUpdate(setDate);
                statement.executeUpdate(setIsLate);

                pass = true;
              } catch (SQLException e) {
                e.printStackTrace();
              }
            }
          }

          resultSet.close();
          statement.close();

        } catch (SQLException e) {
          e.printStackTrace();
        }
      }
    } while (!pass);
  }

  /**
   * Gets selected rule value for a given account type
   *
   * @param accountType Account type
   * @param rule Selected rule
   * @return Value of selected rule for the given account type
   */
  public double getRule(AccountType accountType, String rule) {
    String query = null;

    Statement statement;
    ResultSet resultSet;

    String type = null;

    double rRule = 0;

    if (accountType == AccountType.TEACHER) {
      type = "teacher";
      query = "SELECT teacher FROM Rules WHERE rule='" + rule + "'";
    } else if (accountType == AccountType.STUDENT) {
      type = "student";
      query = "SELECT student FROM Rules WHERE rule='" + rule + "'";
    }

    try {
      statement = connection.createStatement();
      resultSet = statement.executeQuery(query);

      while (resultSet.next()) {
        rRule = resultSet.getDouble(type);
      }

      resultSet.close();
      statement.close();

    } catch (SQLException e) {
      e.printStackTrace();
    }

    return rRule;
  }

  /**
   * Sets the value of a rule for a given {@link AccountType}
   *
   * @param accountType given {@link AccountType}
   * @param rule selected rule
   * @param value new value
   */
  public void setRule(AccountType accountType, String rule, double value) {
    String query = null;
    String type = null;

    Statement statement;

    if (accountType == AccountType.TEACHER) {
      query = "UPDATE Rules SET teacher=" + value + " where rule='" + rule + "'";
    } else if (accountType == AccountType.STUDENT) {
      query = "UPDATE Rules SET student=" + value + " where rule='" + rule + "'";
    }

    try {
      statement = connection.createStatement();
      statement.executeUpdate(query);

      statement.close();

    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  /**
   * Checks to see if an ID is already in user throughout the system
   *
   * @param string ID value
   * @return true if the value is not taken
   */
  public boolean checkValidID(String string) {
    String query;

    Statement statement;
    ResultSet resultSet;

    boolean pass = true;

    try {
      query = "SELECT id FROM Users";

      statement = connection.createStatement();
      resultSet = statement.executeQuery(query);

      while (resultSet.next()) {
        if (string.equals(resultSet.getString("id"))) {
          pass = false;
        }
      }

      query = "SELECT id FROM LibraryBooks";

      statement = connection.createStatement();
      resultSet = statement.executeQuery(query);

      while (resultSet.next()) {
        if (string.equals(resultSet.getString("id"))) {
          pass = false;
        }
      }

      resultSet.close();
      statement.close();

    } catch (SQLException e) {
      e.printStackTrace();
    }

    return pass;
  }

  /**
   * Gets the date of the most recent log in the database
   *
   * @return value of the last log date
   */
  public String getLastLogDate() {
    String query = "SELECT LastLogDate FROM LogDate";

    Statement statement;
    ResultSet resultSet;

    try {
      statement = connection.createStatement();
      resultSet = statement.executeQuery(query);

      while (resultSet.next()) {
        return resultSet.getString("LastLogDate");
      }

      resultSet.close();
      statement.close();

    } catch (SQLException e) {
      e.printStackTrace();
    }

    return null;
  }

  /**
   * Sets the log date to today
   */
  public void setLastLogDate() {
    String query =
        "UPDATE LogDate SET LastLogDate='" + dateFormat.format(Calendar.getInstance().getTime())
            + "'";

    Statement statement;

    try {
      statement = connection.createStatement();
      statement.executeUpdate(query);

      statement.close();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  /**
   * Compares log dates
   *
   * @return false if the current date is less than one week out from the last log
   */
  public boolean checkLogDate() {
    String query = "SELECT LastLogDate FROM LogDate";
    Date today = null;
    Date lastLog = null;

    Statement statement;
    ResultSet resultSet;

    try {
      today = DateUtils
          .parseDate(dateFormat.format(Calendar.getInstance().getTime()), "yyyy-MM-dd");
    } catch (ParseException e) {
      e.printStackTrace();
    }

    try {
      statement = connection.createStatement();
      resultSet = statement.executeQuery(query);

      while (resultSet.next()) {
        lastLog = dateFormat.parse(resultSet.getString("LastLogDate"));
      }

      statement.close();

    } catch (SQLException | ParseException e) {
      e.printStackTrace();
    }

    if (DateUtils.addWeeks(lastLog, 1).before(today)) {
      return true;
    }

    return false;
  }
}
