package io.github.radingkarochaarfian.quickcalc.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HistoryModel {
  public static class HistoryEntry {
    private final int id;
    private final String expression;
    private final String result;
    private final List<String> listHistoryInput;

    public HistoryEntry(int id, String expression, String result, List<String> listInput) {
      this.id = id;
      this.expression = expression;
      this.result = result;
      this.listHistoryInput = new ArrayList<>(listInput);
    }

    public HistoryEntry(String expression, String result, List<String> listInput) {
      this(-1, expression, result, listInput);
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

    public List<String> getListHistoryInput() {
      return Collections.unmodifiableList(listHistoryInput);
    }

    public String getDisplayString() {
      return expression + "=" + result;
    }
  }

  private final List<HistoryEntry> listHistoryEntry = new ArrayList<>();

  public void addHistory(String expression, String result, List<String> listToken) {
    HistoryEntry newEntry = new HistoryEntry(expression, result, listToken);
    listHistoryEntry.add(newEntry);
  }

  public void addHistory(int id, String expression, String result, List<String> listToken) {
    HistoryEntry newEntry = new HistoryEntry(id, expression, result, listToken);
    listHistoryEntry.add(newEntry);
  }

  public List<String> getListTokenAt(int rowTableIndex) {
    if (rowTableIndex >= 0 && rowTableIndex < listHistoryEntry.size()) {
      return listHistoryEntry.get(rowTableIndex).listHistoryInput;
    }
    return new ArrayList<>();
  }

  public HistoryEntry getHistoryEntryAt(int rowTableIndex) {
    if (rowTableIndex >= 0 && rowTableIndex < listHistoryEntry.size()) {
      return listHistoryEntry.get(rowTableIndex);
    }
    return null;
  }

  public boolean removeHistoryEntryAt(int rowTableIndex) {
    if (rowTableIndex >= 0 && rowTableIndex < listHistoryEntry.size()) {
      listHistoryEntry.remove(rowTableIndex);
      return true;
    }
    return false;
  }

  public int getListHistoryEntrySize() {
    return listHistoryEntry.size();
  }

  public void clearListHistoryEntry() {
    listHistoryEntry.clear();
  }
}
