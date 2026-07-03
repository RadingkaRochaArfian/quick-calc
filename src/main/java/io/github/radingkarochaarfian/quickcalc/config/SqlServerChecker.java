package io.github.radingkarochaarfian.quickcalc.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SqlServerChecker implements DatabaseChecker {
  public int checkDatabaseStatus(String driverClass, String url, String username, String password) {
    try {
      Class.forName(driverClass);
      try (Connection conn = DriverManager.getConnection(url, username, password)) {
        if (conn != null && conn.isValid(2)) {
          return 1;
        }
      }
    } catch (SQLException e) {
      if (e.getErrorCode() == 18456) {
        return 2;
      }
    } catch (ClassNotFoundException e) {
      return 3;
    }
    return 0;
  }
}
