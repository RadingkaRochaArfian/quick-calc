package io.github.radingkarochaarfian.quickcalc.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import javax.swing.JOptionPane;

public class DatabaseConfig {
  private final Properties prop;
  private static final String CONFIG_FILE_NAME = "db_config.properties";

  public DatabaseConfig() {
    prop = new Properties();
    loadConfirationFromProperties();
  }

  private void setDefaultValue() {
    prop.setProperty("db.provider.class", "io.github.radingkarochaarfian.quickcalc.config.SqlServerProvider");
    prop.setProperty("db.driver", "com.microsoft.sqlserver.jdbc.SQLServerDriver");
    prop.setProperty("db.url",
        "jdbc:sqlserver://localhost:1433;databaseName=QuickCalcDB;encrypt=true;trustServerCertificate=true;");
    prop.setProperty("db.masterUrl", "jdbc:sqlserver://localhost:1433;encrypt=true;trustServerCertificate=true;");
    prop.setProperty("db.name", "QuickCalcDB");
    prop.setProperty("db.username", "sa");
    prop.setProperty("db.password", "insertPassword");
  }

  private void loadConfirationFromProperties() {
    File file = new File(CONFIG_FILE_NAME);
    if (file.exists()) {
      try (FileInputStream input = new FileInputStream(file)) {
        prop.load(input);
      } catch (IOException e) {
        setDefaultValue();
      }
    } else {
      setDefaultValue();
    }
  }

  public String getProviderClass() {
    return prop.getProperty("db.provider.class");
  }

  public String getDriverClass() {
    return prop.getProperty("db.driver");
  }

  public String getUrl() {
    return prop.getProperty("db.url");
  }

  public String getMasterUrl() {
    return prop.getProperty("db.masterUrl");
  }

  public String getDatabaseName() {
    return prop.getProperty("db.name");
  }

  public String getUsername() {
    return prop.getProperty("db.username");
  }

  public String getPassword() {
    return prop.getProperty("db.password");
  }

  public void setUsername(String username) {
    prop.setProperty("db.username", username);
  }

  public void setPassword(String password) {
    prop.setProperty("db.password", password);
  }

  public Connection getConnection() throws SQLException, ClassNotFoundException {
    Class.forName(getDriverClass());
    return DriverManager.getConnection(getUrl(), getUsername(), getPassword());
  }

  public Connection getMasterConnection() throws SQLException, ClassNotFoundException {
    Class.forName(getDriverClass());
    return DriverManager.getConnection(getMasterUrl(), getUsername(), getPassword());
  }

  public void ExportConfigToFile(String newUser, String newPass) {
    setUsername(newUser);
    setPassword(newPass);
    try (FileOutputStream output = new FileOutputStream(CONFIG_FILE_NAME)) {
      prop.store(output, "QuickCalc Database Configuration (Auto-Generated)");
    } catch (IOException e) {
      JOptionPane.showMessageDialog(
          null,
          "Failed to write properties file.",
          "Error",
          JOptionPane.ERROR_MESSAGE);
    }
  }
}
