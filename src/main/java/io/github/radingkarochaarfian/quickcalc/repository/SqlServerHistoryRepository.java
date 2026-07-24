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
import io.github.radingkarochaarfian.quickcalc.model.HistoryModel.HistoryEntry;
import io.github.radingkarochaarfian.quickcalc.util.CalculatorUtils;

public class SqlServerHistoryRepository implements HistoryRepository {
  private final DatabaseConfig dbconfig;

  public SqlServerHistoryRepository(DatabaseConfig iDbConfig) {
    dbconfig = iDbConfig;
  }

  public List<HistoryEntry> loadAllEntry() {
    List<HistoryEntry> lHistoryEntry = new ArrayList<>();
    String query = "SELECT id, expression, result FROM history ORDER BY id DESC";
    try (Connection conn = dbconfig.getConnection();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(query)) {
      while (rs.next()) {
        int id = rs.getInt("id");
        String expression = rs.getString("expression");
        String result = rs.getString("result");
        List<String> listInput = CalculatorUtils.parseToInput(expression + "=" + result);
        HistoryEntry hEntry = new HistoryEntry(id, expression, result, listInput);
        lHistoryEntry.add(hEntry);
      }
    } catch (SQLException e) {
      JOptionPane.showMessageDialog(
          null,
          "Failed to load data.",
          "Error",
          JOptionPane.ERROR_MESSAGE);
    }
    return lHistoryEntry;
  }

  public void deleteAll() {
    String query = "TRUNCATE TABLE history";
    try (Connection conn = dbconfig.getConnection();
        Statement stmt = conn.createStatement()) {
      stmt.executeUpdate(query);
    } catch (SQLException e) {
      JOptionPane.showMessageDialog(
          null,
          "Failed to delete all data.",
          "Error",
          JOptionPane.ERROR_MESSAGE);
    }
  }

  public void save(String expression, String result) {
    String query = "INSERT INTO history (expression, result) VALUES (?, ?)";
    try (Connection conn = dbconfig.getConnection();
        PreparedStatement ps = conn.prepareStatement(query)) {
      ps.setString(1, expression);
      ps.setString(2, result);
      ps.executeUpdate();
    } catch (SQLException e) {
      JOptionPane.showMessageDialog(
          null,
          "Failed to save data.",
          "Error",
          JOptionPane.ERROR_MESSAGE);
    }
  }
}
