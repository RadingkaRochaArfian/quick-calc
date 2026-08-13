package io.github.radingkarochaarfian.quickcalc.controller.handler;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JCheckBoxMenuItem;

import io.github.radingkarochaarfian.quickcalc.config.DatabaseConfig;
import io.github.radingkarochaarfian.quickcalc.config.DatabaseInit;
import io.github.radingkarochaarfian.quickcalc.util.CalculatorUtils;

public class OfflineOnlyMenuItemHandler implements ActionListener {
  private final Component parent;
  private final DatabaseConfig dbConfig;
  private final DatabaseInit dbInit;

  public OfflineOnlyMenuItemHandler(Component parent, DatabaseConfig dbConfig, DatabaseInit dbInit) {
    this.parent = parent;
    this.dbConfig = dbConfig;
    this.dbInit = dbInit;
  }

  public void actionPerformed(ActionEvent e) {
    JCheckBoxMenuItem miOfflineOnly = (JCheckBoxMenuItem) e.getSource();
    boolean isOfflineOnlyModeSelected = miOfflineOnly.isSelected();
    if (isOfflineOnlyModeSelected) {
      dbConfig.setUseLocalOnly(true);
      CalculatorUtils.showInformation(parent, "Application is now running on Offline Mode Only.");
    } else {
      boolean isConnected = dbInit.initializeDatabase();
      if (isConnected) {
        miOfflineOnly.setSelected(false);
        CalculatorUtils.showInformation(parent, "Application is connected to database.");
      } else {
        miOfflineOnly.setSelected(true);
        CalculatorUtils.showWarning(parent, "Failed to connect to database.");
      }
    }
  }

}
