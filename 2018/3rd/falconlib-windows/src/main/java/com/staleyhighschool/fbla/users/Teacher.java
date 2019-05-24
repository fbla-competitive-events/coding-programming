package com.staleyhighschool.fbla.users;

import com.staleyhighschool.fbla.library.Book;
import com.staleyhighschool.fbla.library.Library;
import com.staleyhighschool.fbla.util.enums.AccountType;
import java.util.List;

public class Teacher extends User {

  private String TAG = (this.getClass().getName() + ": ");

  private double fineRate;
  private int lateBooks;
  private List<Book> userBooks;

  public Teacher(String sFirstName, String sLastName, String tID) {
    super(sFirstName, sLastName, tID, AccountType.TEACHER);
    fineRate = Library.grabFineRate(this);
  }

  @Override
  public List<Book> getUserBooks() {
    setUserBooks();
    return userBooks;
  }

  @Override
  public void setUserBooks() {
    userBooks = Library.connection.getUserBooks(this);
  }

  @Override
  public void setLateBooks() {
    lateBooks = Library.lateBookCount(this);
  }

  @Override
  public double calculateFine() {
    return lateBooks * fineRate;
  }

  @Override
  public String getAccountTypeString() {
    return "teacher";
  }
}
