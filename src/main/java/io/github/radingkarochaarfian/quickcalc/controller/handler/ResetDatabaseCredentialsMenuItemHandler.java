package io.github.radingkarochaarfian.quickcalc.controller.handler;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import io.github.radingkarochaarfian.quickcalc.config.DatabaseConfig;

public class ResetDatabaseCredentialsMenuItemHandler implements ActionListener {
  private Component parent;
  private DatabaseConfig dbConfig;

  public ResetDatabaseCredentialsMenuItemHandler(Component parent, DatabaseConfig dbConfig) {
    this.parent = parent;
    this.dbConfig = dbConfig;
  }

  public void actionPerformed(ActionEvent e) {
    int choice = JOptionPane.showConfirmDialog(
        parent,
        "Are you sure want to reset the database credentials?",
        "Reset Database Credentials",
        JOptionPane.WARNING_MESSAGE);
    if (choice == JOptionPane.YES_OPTION) {
      dbConfig.resetDatabaseCredential();
    }
  }
}
