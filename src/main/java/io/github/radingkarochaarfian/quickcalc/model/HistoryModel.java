package io.github.radingkarochaarfian.quickcalc.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HistoryModel {
  public static class HistoryEntry {
    private final int id;
    private final String expression;
    private final String result;
    private final List<String> listToken;

    public HistoryEntry(int id, String expression, String result, List<String> listToken) {
      this.id = id;
      this.expression = expression;
      this.result = result;
      this.listToken = new ArrayList<>(listToken);
    }

    public int getId() {
      return id;
    }

    public String getExpression() {
      return expression;
    }

    public String getResult() {
      return result;
    }

    public List<String> getListToken() {
      return Collections.unmodifiableList(listToken);
    }

    public String getDisplayString() {
      return expression + "=" + result;
    }
  }

  private final List<HistoryEntry> listHistoryEntry = new ArrayList<>();
  private int idCount;

  public void addHistory(String expression, String result, List<String> listToken) {
    HistoryEntry newEntry = new HistoryEntry(idCount++, expression, result, listToken);
    listHistoryEntry.add(newEntry);
  }

  public List<String> getListTokenAt(int rowTableIndex) {
    if (rowTableIndex >= 0 && rowTableIndex < listHistoryEntry.size()) {
      return listHistoryEntry.get(rowTableIndex).listToken;
    }
    return new ArrayList<>();
  }

  public HistoryEntry getHistoryEntryAt(int rowTableIndex) {
    if (rowTableIndex >= 0 && rowTableIndex < listHistoryEntry.size()) {
      return listHistoryEntry.get(rowTableIndex);
    }
    return null;
  }

  public int getListHistoryEntrySize() {
    return listHistoryEntry.size();
  }

  public void clearListHistoryEntry() {
    listHistoryEntry.clear();
    idCount = 1;
  }
}
