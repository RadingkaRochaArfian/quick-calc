package io.github.radingkarochaarfian.quickcalc.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseCheck {
  public static int checkDatabaseStatus(String driverClass, String url, String user, String pass) {
    try {
      Class.forName(driverClass);
      try (Connection conn = DriverManager.getConnection(url, user, pass)) {
        if (conn != null && conn.isValid(2)) {
          return 1;
        }
      }
    } catch (SQLException e) {
      int errorCode = e.getErrorCode();
      if (errorCode == 18456) {
        return 2;
      }
    } catch (ClassNotFoundException e) {
      return 3;
    }
    return 0;
  }
}
