package io.github.radingkarochaarfian.quickcalc.config;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;

import javax.swing.JOptionPane;

import io.github.radingkarochaarfian.quickcalc.view.DatabaseCredentialDialog;

public class DatabaseInit {
  private final DatabaseConfig dbConfig;

  public DatabaseInit(DatabaseConfig iDbConfig) {
    dbConfig = iDbConfig;
  }

  public void initializeDatabase() {
    boolean connected = false;
    DatabaseProvider dbProvider = DatabaseProviderFactory.getProvider(dbConfig);
    while (!connected) {
      int dbStatus = dbProvider.checkDatabaseStatus(
          dbConfig.getDriverClass(),
          dbConfig.getMasterUrl(),
          dbConfig.getUsername(),
          dbConfig.getPassword());
      switch (dbStatus) {
        case 1:
          try {
            dbProvider.createDatabaseIfNotExist(dbConfig);
            try (Connection conn = dbConfig.getConnection()) {
              dbProvider.createaTableIfNotExist(conn);
              connected = true;
            }
          } catch (SQLException | ClassNotFoundException e) {
            showError("Failed to create database.");
          }
          break;
        case 2:
          showCredentialError();
          break;
        case 3:
          showError("Failed to read JDBC.");
          break;
        case 0:
        default:
          showError("Database is offline.");
          break;
      }
    }
  }

  private void showError(String message) {
    JOptionPane.showMessageDialog(
        null,
        message,
        "Error",
        JOptionPane.ERROR_MESSAGE);
  }

  private void showCredentialError() {
    DatabaseCredentialDialog dcDialog = new DatabaseCredentialDialog(dbConfig);
    if (dcDialog.showDialog()) {
      String newUsername = dcDialog.getUsernameInput();
      String newPassword = dcDialog.getPasswordInput();
      if (dcDialog.isRememberChecked()) {
        dbConfig.ExportConfigToFile(newUsername, newPassword);
      } else {
        dbConfig.setUsername(newUsername);
        dbConfig.setPassword(newPassword);
        File fileProp = new File(dbConfig.getConfigFileName());
        if (fileProp.exists()) {
          fileProp.delete();
        }
      }
    } else {
      System.exit(0);// to do: use backup from local
    }
  }

}
