package io.github.radingkarochaarfian.quickcalc;

import io.github.radingkarochaarfian.quickcalc.config.DatabaseConfig;
import io.github.radingkarochaarfian.quickcalc.config.DatabaseInit;
import io.github.radingkarochaarfian.quickcalc.controller.CalculatorController;
import io.github.radingkarochaarfian.quickcalc.model.CalculatorModel;
import io.github.radingkarochaarfian.quickcalc.model.HistoryModel;
import io.github.radingkarochaarfian.quickcalc.repository.HistoryRepository;
import io.github.radingkarochaarfian.quickcalc.repository.HistoryRepositoryFactory;
import io.github.radingkarochaarfian.quickcalc.service.HistoryBackupService;
import io.github.radingkarochaarfian.quickcalc.view.*;

import javax.swing.SwingUtilities;

public class QuickCalcApp {
  public static void main(String[] args) {
    SwingUtilities.invokeLater(() -> {
      DatabaseConfig dbConfig = new DatabaseConfig();// hold key and value of database config
      DatabaseInit dbInit = new DatabaseInit(dbConfig);// hold initiation of database
      HistoryRepository historyRepo = HistoryRepositoryFactory.getRepository(dbConfig);// hold action to database
      HistoryBackupService backupService = new HistoryBackupService(historyRepo);// hold control of historyrepo
      boolean isDatabaseOnline = dbInit.initializeDatabase();
      if (isDatabaseOnline) {
        backupService.syncLocalToDatabase();
        backupService.setOfflineMode(false);
      } else {
        backupService.setOfflineMode(true);
      }
      CalculatorView view = new CalculatorView();
      CalculatorModel model = new CalculatorModel();
      HistoryModel hModel = new HistoryModel();
      CalculatorController controller = new CalculatorController(view, model, hModel, dbConfig, backupService);
      view.setVisible(true);
    });
  }
}
