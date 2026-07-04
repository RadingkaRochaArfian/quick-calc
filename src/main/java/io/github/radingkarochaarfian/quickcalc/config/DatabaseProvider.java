package io.github.radingkarochaarfian.quickcalc.config;

import java.sql.Connection;

public interface DatabaseProvider {
  int checkDatabaseStatus(String driverClass, String url, String username, String pass);

  void createDatabaseIfNotExist(DatabaseConfig dbConfig);

  void createaTableIfNotExist(Connection conn);
}
