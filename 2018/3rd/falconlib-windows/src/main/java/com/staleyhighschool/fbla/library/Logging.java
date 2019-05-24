package com.staleyhighschool.fbla.library;

import com.staleyhighschool.fbla.util.enums.LogType;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Logging {

  private final String TAG = (this.getClass().getName() + ": ");

  private String userName;
  private String logSaveDirPath;
  private File logSaveDirectory;
  private FileWriter log;
  private DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
  private DateFormat dateTime = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
  private BufferedWriter logWriter;
  private PrintWriter writer;

  public Logging() {
    userName = System.getProperty("user.name");
    logSaveDirPath = getDocumentsDirectory();
    createDirectory();
    log = checkLogCreation();
    logWriter = new BufferedWriter(log);
    writer = new PrintWriter(logWriter);
  }

  private void createDirectory() {

    logSaveDirectory = new File(logSaveDirPath);

    if (!logSaveDirectory.exists()) {
      boolean pass = logSaveDirectory.mkdirs();

      if (!pass) {
        System.out.println("Failed to create directory " + logSaveDirPath);
      }
    }
  }

  public String getDocumentsDirectory() {
    String os = System.getProperty("os.name");
    String username = System.getProperty("user.name");
    String documents = null;
    if (os.contains("Mac")) {
      documents = "/Users/" + username + "/Documents/falcon-lib-logs/";
    } else if (os.contains("Windows")) {
      documents =
          "C:\\Users\\" + username + "\\Documents\\falcon-lib-logs\\";
    }
    return documents;
  }

  public void writeToLog(LogType logType, String message) {
    if (logType == LogType.USER_ACTION) {
      writer.println(
          dateTime.format(Calendar.getInstance().getTime()) + " [ USER EVENT ]: " + message);
      System.out.println(
          TAG + dateTime.format(Calendar.getInstance().getTime()) + " [ USER EVENT ]: " + message);
    } else if (logType == LogType.BOOK_ACTION) {
      writer.println(
          dateTime.format(Calendar.getInstance().getTime()) + " [ BOOK EVENT ]: " + message);
      System.out.println(
          TAG + dateTime.format(Calendar.getInstance().getTime()) + " [ BOOK EVENT ]: " + message);
    } else if (logType == LogType.CHECKOUT) {
      writer.println(
          dateTime.format(Calendar.getInstance().getTime()) + " [ BOOK CHECKED OUT ]: " + message);
      System.out.println(
          TAG + dateTime.format(Calendar.getInstance().getTime()) + " [ BOOK CHECKED OUT ]: "
              + message);
    } else if (logType == LogType.RETURN) {
      writer.println(
          dateTime.format(Calendar.getInstance().getTime()) + " [ BOOK RETURNED ]: " + message);
      System.out.println(
          TAG + dateTime.format(Calendar.getInstance().getTime()) + " [ BOOK RETURNED ]: "
              + message);
    }
  }

  public void closeLog() {
    System.out.println(TAG + "closing");
    writer.close();
  }

  private FileWriter checkLogCreation() {
    if (Library.connection.checkLogDate()) {
      Library.connection.setLastLogDate();
      try {
        return new FileWriter(
            ((new File(logSaveDirPath, format.format(Calendar.getInstance().getTime())))) + ".txt",
            true);
      } catch (IOException e) {
        e.printStackTrace();
      }
    } else {
      try {
        return new FileWriter(
            new File(logSaveDirPath, Library.connection.getLastLogDate()) + ".txt", true);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    return null;
  }
}
