package io.github.radingkarochaarfian.quickcalc.config;

import java.awt.GridLayout;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class DatabaseInit {
  private final DatabaseConfig dbConfig;

  public DatabaseInit(DatabaseConfig iDbConfig) {
    dbConfig = iDbConfig;
  }

  public void initializeDatabase() {
    boolean connected = false;
    while (!connected) {
      try{
        createDatabaseIfNotExist();
        try (Connection conn = dbConfig.getConnection()) {
          connected = true;
          createTableIfNotExist(conn);
        }
      } catch (SQLException | ClassNotFoundException e) {
        int selectedChoice = showErrorAndChoice(e.getMessage());
        if (selectedChoice == JOptionPane.YES_OPTION) {
          askNewCredential();
        } else {
          JOptionPane.showMessageDialog(
              null,
              "Application cannot run without database. Closing program...");
          System.exit(0);
        }
      }

    }
  }

  private int showErrorAndChoice(String message) {
    String fullMessage = "Failed connecting to SQL Server!\n\n" +
        "Error detail: " + message + "\n\n" +
        "Re-enter database username and password?";
    return JOptionPane.showConfirmDialog(
        null,
        fullMessage,
        "Database Connection Error",
        JOptionPane.YES_NO_OPTION,
        JOptionPane.ERROR_MESSAGE);
  }

  private void askNewCredential() {
    JTextField tfUser = new JTextField(dbConfig.getUsername());
    JPasswordField tfPass = new JPasswordField(dbConfig.getPassword());
    JPanel pInput = new JPanel(new GridLayout(2, 2, 5, 5));
    pInput.add(new JLabel("SQL Server Username:"));
    pInput.add(tfUser);
    pInput.add(new JLabel("SQL Server Password:"));
    pInput.add(tfPass);
    int selectedChoice = JOptionPane.showConfirmDialog(
        null,
        pInput,
        "Please insert SQL Server Credentials",
        JOptionPane.OK_CANCEL_OPTION,
        JOptionPane.QUESTION_MESSAGE);
    if (selectedChoice == JOptionPane.OK_OPTION) {
      dbConfig.setUsername(tfUser.getText().trim());
      dbConfig.setPassword(new String(tfPass.getPassword()));
    } else {
      System.exit(0);
    }
  }

  private void createTableIfNotExist(Connection conn) {
    String query = "use "+dbConfig.getDatabaseName()+" go;" +
        "IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='history' AND xtype='U')" +
        "CREATE TABLE history (" +
        "id INT IDENTITY(1,1) PRIMARY KEY, " +
        "expression NVARCHAR(500) NOT NULL, " +
        "result NVARCHAR(255) NOT NULL, " +
        "tokens NVARCHAR(MAX) NOT NULL" +
        ");";
    try (Statement stmt = conn.createStatement()) {
      stmt.executeUpdate(query);
    } catch (SQLException e) {
      JOptionPane.showMessageDialog(
          null,
          "Failed to initialize table.",
          "Error",
          JOptionPane.ERROR_MESSAGE);
    }
  }

  private void createDatabaseIfNotExist(){
    try(Connection masterConn=dbConfig.getMasterConnection()){
      Statement stmt=masterConn.createStatement();
      String query="IF NOT EXISTS (SELECT * FROM sys.database WHERE name='"+dbConfig.getDatabaseName()+"') "+
      "BEGIN "+
      "CREATE DATABASE "+dbConfig.getDatabaseName()+" "+
      "END";
      stmt.executeUpdate(query);
    }catch(SQLException|ClassNotFoundException e){
      JOptionPane.showMessageDialog(
        null,
        "Failed to initialize database.",
        "Error",
        JOptionPane.ERROR_MESSAGE
      );
    }
  }
}
