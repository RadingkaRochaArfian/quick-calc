package io.github.radingkarochaarfian.quickcalc.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JOptionPane;

public class SqlServerProvider implements DatabaseProvider {
  public int checkDatabaseStatus(String url, String username, String password) {
    try {
      try (Connection conn = DriverManager.getConnection(url, username, password)) {
        if (conn != null && conn.isValid(2)) {
          return 1;
        }
      }
    } catch (SQLException e) {
      if (e.getErrorCode() == 18456) {
        return 2;
      }
    }
    return 0;
  }

  private void showError(String message) {
    JOptionPane.showMessageDialog(
        null,
        message,
        "Error",
        JOptionPane.ERROR_MESSAGE);
  }

  public void createDatabaseIfNotExist(DatabaseConfig dbConfig) {
    String query = "IF NOT EXISTS (SELECT * FROM sys.databases WHERE name='" + dbConfig.getDatabaseName() + "') " +
        "BEGIN CREATE DATABASE " + dbConfig.getDatabaseName() + " END";
    try (Connection conn = dbConfig.getConnection()) {
      Statement stmt = conn.createStatement();
      stmt.executeUpdate(query);
    } catch (Exception e) {
      showError("Failed to create SQL Server database.");
    }
  }

  public void createaTableIfNotExist(Connection conn) {
    String query = "IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='history' AND xtype='U') " +
        "CREATE TABLE history (" +
        "id INT IDENTITY(1,1) PRIMARY KEY, " +
        "expression NVARCHAR(500) NOT NULL, " +
        "result NVARCHAR(255) NOT NULL, " +
        ");";
    try (Statement stmt = conn.createStatement()) {
      stmt.executeUpdate(query);
    } catch (SQLException e) {
      showError("Failed to create history table in SQL Server.");
    }
  }
}
