package com.staleyhighschool.fbla.library;

import com.staleyhighschool.fbla.users.User;
import com.staleyhighschool.fbla.util.enums.IsLate;
import com.staleyhighschool.fbla.util.enums.IsOut;
import java.text.ParseException;
import java.util.Date;
import org.apache.commons.lang3.time.DateUtils;

/**
 * Per Book object with access and generation methods
 */
public class Book {

  private String TAG = (this.getClass().getName() + ": ");

  private String bookTitle;
  private String bookAuthor;
  private String bookID;
  private IsOut isOut;
  private IsLate isLate;
  private Date dateOut;
  public static Date storeDate;

  static {
    try {
      storeDate = DateUtils.parseDate("2000-01-01", "yyyy-MM-dd");
    } catch (ParseException e) {
      e.printStackTrace();
    }
  }

  /**
   * Generates a new book
   *
   * @param bookTitle {@link String} holding the title of the {@link Book}
   * @param bookAuthor {@link String} holding the author of the {@link Book}
   * @param bookID {@link String} holding the generated ID of the {@link Book}
   * @param isLate {@link com.staleyhighschool.fbla.util.enums.IsLate} if the {@link Book} is
   * overdue or not
   * @param isOut {@link com.staleyhighschool.fbla.util.enums.IsOut} if the {@link Book} is out of
   * the library or not
   */
  public Book(String bookTitle, String bookAuthor, String bookID, IsLate isLate,
      IsOut isOut, Date dateOut) {
    this.bookTitle = bookTitle;
    this.bookAuthor = bookAuthor;
    this.bookID = bookID;
    this.isLate = isLate;
    this.isOut = isOut;
    this.dateOut = dateOut;
  }

  /**
   * Gets the title of the {@link Book}
   *
   * @return {@link String} holding the title of the {@link Book}
   */
  public String getBookTitle() {
    return bookTitle;
  }

  /**
   * Gets the author of the {@link Book}
   *
   * @return {@link String} holding the author of the {@link Book}
   */
  public String getBookAuthor() {
    return bookAuthor;
  }

  /**
   * Gets the ID of the {@link Book}
   *
   * @return {@link String} holding the ID of the {@link Book}
   */
  public String getBookID() {
    return bookID;
  }

  /**
   * Returns boolean stating if book is late or not
   *
   * @return {@link Boolean} stating if the book is late
   */
  public boolean isLate() {
    boolean late = false;

    if (isLate == IsLate.LATE) {
      late = true;
    } else if (isLate == IsLate.SAFE) {
      late = false;
    }
    return late;
  }

  public void markLate(User user) {
    Date due;

    due = DateUtils.addDays(dateOut, Library.connection.getMaxDays(user));
    if (dateOut.after(due) && !dateOut.equals(storeDate)) {
      isLate = IsLate.LATE;
    } else {
      isLate = IsLate.SAFE;
    }
  }

  public boolean isOut() {
    boolean out = false;

    if (isOut == IsOut.OUT) {
      out = true;
    } else if (isOut == IsOut.IN) {
      out = false;
    }
    return out;
  }

  public void setIsOut(IsOut isOut) {
    this.isOut = isOut;
  }

  public void setIsLate(IsLate isLate) {
    this.isLate = isLate;
  }

  public void setDateOut(Date date) {
    dateOut = date;
  }

  public Date getDateOut() {
    return dateOut;
  }
}
