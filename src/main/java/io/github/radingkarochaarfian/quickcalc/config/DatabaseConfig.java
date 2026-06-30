package io.github.radingkarochaarfian.quickcalc.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

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

}
