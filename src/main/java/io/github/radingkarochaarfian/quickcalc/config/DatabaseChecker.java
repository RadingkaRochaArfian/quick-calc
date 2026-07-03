package io.github.radingkarochaarfian.quickcalc.config;

public interface DatabaseChecker {
  public int checkDatabaseStatus(String driverClass, String url, String username, String pass);
}
