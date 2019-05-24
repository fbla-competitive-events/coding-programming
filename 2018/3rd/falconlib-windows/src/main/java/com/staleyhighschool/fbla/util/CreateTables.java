package com.staleyhighschool.fbla.util;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class CreateTables {

  private final String TAG = this.getClass().getName() + ": ";

  Connection connection;

  public CreateTables(Connection connection) {
    this.connection = connection;

    List<String> tables = new ArrayList<>();
    DatabaseMetaData metaData;

    try {
      metaData = connection.getMetaData();

      ResultSet rs = metaData.getTables(null, null, "%", null);
      while (rs.next()) {
        tables.add(rs.getString(3));
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }

    if (!tables.contains("LibraryBooks")) {
      createLibraryBooksTable();
    }
    if (!tables.contains("Users")) {
      createUsersTable();
    }
    if (!tables.contains("Rules")) {
      createRulesTable();
    }
    if (!tables.contains("LogDate")) {
      createLogDateTable();
    }
    if (!tables.contains("781450-OQOZE")) {
      createSampleUserTable();
    }
  }

  private void createUsersTable() {
    Statement statement;

    try {
      statement = connection.createStatement();

      statement.executeUpdate("CREATE TABLE Users "
          + "(firstName TEXT, lastName TEXT, id TEXT, accountType TEXT)");
      statement.executeUpdate("INSERT INTO Users (firstName, lastName, id, accountType) VALUES "
          + "('Connor', 'Davis', '781450-OQOZE', 'student')");
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  private void createRulesTable() {
    Statement statement;

    try {
      statement = connection.createStatement();

      statement.executeUpdate("CREATE TABLE Rules (rule TEXT, teacher DOUBLE, student DOUBLE)");
      statement.executeUpdate("INSERT INTO Rules (rule, teacher, student) VALUES "
          + "('maxBooks', 60, 4), "
          + "('maxDays', 200, 28), "
          + "('fineRate', 0.1, 0.25)");
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  private void createLogDateTable() {
    Statement statement;

    try {
      statement = connection.createStatement();

      statement.executeUpdate("CREATE TABLE LogDate (LastLogDate TEXT)");
      statement.executeUpdate("INSERT INTO LogDate (LastLogDate) VALUES "
          + "('2018-04-04')");
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  private void createLibraryBooksTable() {
    Statement statement;

    try {
      statement = connection.createStatement();

      statement.executeUpdate("CREATE TABLE LibraryBooks "
          + "(title TEXT, author TEXT, id TEXT, isOut INTEGER, isLate INTEGER, dateOut DATE)");
      statement.executeUpdate("INSERT INTO LibraryBooks "
          + "(title, author, id, isOut, isLate, dateOut) VALUES "
          + "('Prince of Thorns', 'Mark Lawrence', '658213-2BOM2', 0, 0, '2000-01-01')");
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  private void createSampleUserTable() {
    Statement statement;

    try {
      statement = connection.createStatement();

      statement.executeUpdate("CREATE TABLE '781450-OQOZE' (books TEXT)");
      statement.executeUpdate("INSERT into '781450-OQOZE' (books) VALUES "
          + "('sample')");
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }
}
