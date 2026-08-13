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
  public static final String KEY_PROVIDER_CLASS = "db.provider-class";
  public static final String KEY_REPO_CLASS = "db.repo-class";
  public static final String KEY_URL = "db.url";
  public static final String KEY_MASTER_URL = "db.master-url";
  public static final String KEY_NAME = "db.name";
  public static final String KEY_USERNAME = "db.username";
  public static final String KEY_PASSWORD = "db.password";
  public static final String KEY_USE_LOCAL = "db.use.local";

  public DatabaseConfig() {
    prop = new Properties();
    loadConfirationFromProperties();
  }

  private void setDefaultValue() {
    prop.setProperty(KEY_PROVIDER_CLASS, "io.github.radingkarochaarfian.quickcalc.config.SqlServerProvider");
    prop.setProperty(KEY_REPO_CLASS, "io.github.radingkarochaarfian.quickcalc.repository.SqlServerHistoryRepository");
    prop.setProperty(KEY_URL,
        "jdbc:sqlserver://localhost:1433;databaseName=QuickCalcDB;encrypt=true;trustServerCertificate=true;");
    prop.setProperty(KEY_MASTER_URL, "jdbc:sqlserver://localhost:1433;encrypt=true;trustServerCertificate=true;");
    prop.setProperty(KEY_NAME, "QuickCalcDB");
    prop.setProperty(KEY_USERNAME, "sa");
    prop.setProperty(KEY_PASSWORD, "insertPassword");
    prop.setProperty(KEY_USE_LOCAL, "false");
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

  public String getConfigFileName() {
    return CONFIG_FILE_NAME;
  }

  public String getRepoClass() {
    return prop.getProperty(KEY_REPO_CLASS);
  }

  public String getProviderClass() {
    return prop.getProperty(KEY_PROVIDER_CLASS);
  }

  public String getUrl() {
    return prop.getProperty(KEY_URL);
  }

  public String getMasterUrl() {
    return prop.getProperty(KEY_MASTER_URL);
  }

  public String getDatabaseName() {
    return prop.getProperty(KEY_NAME);
  }

  public String getUsername() {
    return prop.getProperty(KEY_USERNAME);
  }

  public String getPassword() {
    return prop.getProperty(KEY_PASSWORD);
  }

  public void setUsername(String username) {
    prop.setProperty(KEY_USERNAME, username);
  }

  public void setPassword(String password) {
    prop.setProperty(KEY_PASSWORD, password);
  }

  public boolean isUseLocalOnly() {
    return Boolean.parseBoolean(prop.getProperty(KEY_USE_LOCAL));
  }

  public void resetDatabaseCredential() {
    prop.setProperty(KEY_USERNAME, "sa");
    prop.setProperty(KEY_PASSWORD, "insertPassword");
  }

  public void setUseLocalOnly(boolean useLocal) {
    prop.setProperty(KEY_USE_LOCAL, String.valueOf(useLocal));
    savePropertiesToFile();
  }

  public Connection getConnection() throws SQLException {
    return DriverManager.getConnection(getUrl(), getUsername(), getPassword());
  }

  public Connection getMasterConnection() throws SQLException {
    return DriverManager.getConnection(getMasterUrl(), getUsername(), getPassword());
  }

  public void exportConfigToFile(String newUser, String newPass) {
    setUsername(newUser);
    setPassword(newPass);
    savePropertiesToFile();
  }

  private void savePropertiesToFile() {
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
