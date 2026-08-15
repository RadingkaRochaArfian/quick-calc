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

  public boolean initializeDatabase() {
    if (dbConfig.isUseLocalOnly()) {
      return false;
    }
    DatabaseProvider dbProvider = DatabaseProviderFactory.getProvider(dbConfig);
    while (true) {
      int dbStatus = dbProvider.checkDatabaseStatus(
          dbConfig.getMasterUrl(),
          dbConfig.getUsername(),
          dbConfig.getPassword());
      switch (dbStatus) {
        case 1:
          try {
            dbProvider.createDatabaseIfNotExist(dbConfig);
            try (Connection conn = dbConfig.getMasterConnection()) {
              dbProvider.createaTableIfNotExist(conn);
              return true;
            }
          } catch (SQLException e) {
            showNotification("Failed to setup database. Switching to offline mode.");
            return false;
          }
        case 2:
          showNotification("Incorrect username or password. Please try again.");
          boolean isUserTryAgain = handleCredentialInput();
          if (!isUserTryAgain) {
            return false;
          }
          break;
        case 0:
        default:
          showNotification("Database server unreachable. Activating local backup mode...");
          return false;
      }
    }
  }

  private void showNotification(String message) {
    JOptionPane.showMessageDialog(
        null,
        message,
        "Information",
        JOptionPane.INFORMATION_MESSAGE);
  }

  private boolean handleCredentialInput() {
    DatabaseCredentialDialog dcDialog = new DatabaseCredentialDialog(dbConfig);
    if (dcDialog.showDialog()) {
      String newUsername = dcDialog.getUsernameInput();
      String newPassword = dcDialog.getPasswordInput();
      if (dcDialog.isRememberChecked()) {
        dbConfig.exportConfigToFile(newUsername, newPassword);
      } else {
        dbConfig.setPassword(newPassword);
        dbConfig.setUsername(newUsername);
        File fileProp = new File(dbConfig.getConfigFileName());
        if (fileProp.exists()) {
          fileProp.delete();
        }
      }
      return true;
    } else {
      return false;
    }
  }

}
