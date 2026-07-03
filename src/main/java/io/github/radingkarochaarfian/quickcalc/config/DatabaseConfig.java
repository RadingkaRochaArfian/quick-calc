package io.github.radingkarochaarfian.quickcalc.config;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import javax.swing.JOptionPane;

public class DatabaseConfig {
  private String driverClass = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
  private String masterUrl = "jdbc:sqlserver://localhost:1433;encrypt=true;trustServerCertificate=true;";
  private String url = "jdbc:sqlserver://localhost:1433;databaseName=QuickCalcDB;encrypt=true;trustServerCertificate=true;";
  private String databaseName = "QuickCalcDB";
  private String username = "sa";
  private String password = "insert_password_here";

  public DatabaseConfig() {
    loadConfirationFromProperties();
  }

  private void loadConfirationFromProperties() {
    Properties prop = new Properties();
    String fileName = "db_config.properties";
    try (FileInputStream input = new FileInputStream(fileName)) {
      prop.load(input);
      driverClass = prop.getProperty("db.driver", driverClass);
      url = prop.getProperty("db.url", url);
      masterUrl = prop.getProperty("db.masterUrl", masterUrl);
      username = prop.getProperty("db.username", username);
      password = prop.getProperty("db.password", password);
      databaseName = prop.getProperty("db.name", databaseName);
    } catch (IOException e) {

    }
  }

  public String getMasterUrl() {
    return masterUrl;
  }

  public String getUrl() {
    return url;
  }

  public Connection getConnection() throws SQLException, ClassNotFoundException {
    Class.forName(driverClass);
    return DriverManager.getConnection(getUrl(), username, password);
  }

  public Connection getMasterConnection() throws SQLException, ClassNotFoundException {
    Class.forName(driverClass);
    return DriverManager.getConnection(getMasterUrl(), username, password);
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
    return databaseName;
  }

  public String getDriverClass() {
    return driverClass;
  }

  public void ExportConfigToFile(String newUser, String newPass) {
    setUsername(newUser);
    setPassword(newPass);
    Properties prop = new Properties();
    String fileName = "db_config.properties";
    try (FileOutputStream output = new FileOutputStream(fileName)) {
      prop.setProperty("db.driver", driverClass);
      prop.setProperty("db.name", databaseName);
      prop.setProperty("db.url", url);
      prop.setProperty("db.masterUrl", masterUrl);
      prop.setProperty("db.username", newUser);
      prop.setProperty("db.password", newPass);
      prop.store(output, "QuickCalc Multi-Database configuration (Auto-Generated)");
    } catch (IOException e) {
      JOptionPane.showMessageDialog(
          null,
          "Failed to write properties file.",
          "Error",
          JOptionPane.ERROR_MESSAGE);
    }
  }
}
