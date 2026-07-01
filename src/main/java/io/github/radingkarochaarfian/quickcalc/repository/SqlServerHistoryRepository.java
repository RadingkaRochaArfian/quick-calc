package io.github.radingkarochaarfian.quickcalc.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import io.github.radingkarochaarfian.quickcalc.config.DatabaseConfig;

public class SqlServerHistoryRepository implements HistoryRepository {
  private final DatabaseConfig dbconfig;

  public SqlServerHistoryRepository(DatabaseConfig iDbConfig) {
    dbconfig = iDbConfig;
  }

  public List<String> loadAll() {
    List<String> listHistory = new ArrayList<>();
    String query = "SELECT expression, result FROM history ORDER BY id DESC";
    try (Connection conn = dbconfig.getConnection();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(query)) {
      while (rs.next()) {
        String line = rs.getString("expression") + " = " + rs.getString("result");
        listHistory.add(line);
      }
    } catch (SQLException | ClassNotFoundException e) {
      JOptionPane.showMessageDialog(
          null,
          "Failed to load data.",
          "Error",
          JOptionPane.ERROR_MESSAGE);
    }
    return listHistory;
  }

  public void deleteAll() {
    String query = "TRUNCATE TABLE history";
    try (Connection conn = dbconfig.getConnection();
        Statement stmt = conn.createStatement()) {
      stmt.executeUpdate(query);
    } catch (SQLException | ClassNotFoundException e) {
      JOptionPane.showMessageDialog(
          null,
          "Failed to delete all data.",
          "Error",
          JOptionPane.ERROR_MESSAGE);
    }
  }

  public void save(String expression, String result, String tokens) {
    String query = "USE " + dbconfig.getDatabaseName() + " GO;" +
        "INSERT INTO history (expression, result, tokens) VALUES (?, ?, ?)";
    try (Connection conn = dbconfig.getConnection();
        PreparedStatement ps = conn.prepareStatement(query)) {
      ps.setString(1, expression);
      ps.setString(2, result);
      ps.setString(3, tokens);
      ps.executeUpdate();
    } catch (SQLException | ClassNotFoundException e) {
      JOptionPane.showMessageDialog(
          null,
          "Failed to save data.",
          "Error",
          JOptionPane.ERROR_MESSAGE);
    }
  }
}
