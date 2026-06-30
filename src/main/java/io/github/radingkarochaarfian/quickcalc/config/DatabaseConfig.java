package io.github.radingkarochaarfian.quickcalc.config;

import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import javax.swing.JOptionPane;

public class DatabaseConfig {
  private static final String HOST = "localhost";
  private static final String PORT = "1433";
  private static final String DATABASE_NAME = "QuickCalcDB";

  private String username = "sa";
  private String password = "insert_password_here";

  public Connection getConnection() throws SQLException, ClassNotFoundException {
    Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
    String url = String.format(
        "jdbc:sqlserver://%s:%s;databaseName=%s;encrypt=true;trustServerCertificate=true;",
        HOST, PORT, DATABASE_NAME);
    return DriverManager.getConnection(url, username, password);
  }

  public Connection getMasterConnection() throws SQLException, ClassNotFoundException {
    Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
    String url = String.format(
        "jdbc:sqlserver://%s:%s;encrypt=true;trustServerCertificate=true;",
        HOST, PORT);
    return DriverManager.getConnection(url, username, password);
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String iUsername) {
    username = iUsername;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String iPassword) {
    password = iPassword;
  }

  public String getDatabaseName() {
    return DATABASE_NAME;
  }

  public void ExportConfigToFile(String newUser, String newPass) {
    setUsername(newUser);
    setPassword(newPass);
    Properties prop = new Properties();
    String fileName = "db_config.properties";
    try (FileOutputStream output = new FileOutputStream(fileName)) {
      prop.setProperty("db.user", newUser);
      prop.setProperty("db.password", newPass);
      prop.store(output, DATABASE_NAME + " configuration (Auto-Generated)");
    } catch (IOException e) {
      JOptionPane.showMessageDialog(
          null,
          "Failed to write properties file: " + e.getMessage(),
          "Error",
          JOptionPane.ERROR_MESSAGE);
    }
  }
}
