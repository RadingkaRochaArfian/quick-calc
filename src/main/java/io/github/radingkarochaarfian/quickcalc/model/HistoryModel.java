package io.github.radingkarochaarfian.quickcalc.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HistoryModel {
  public static class HistoryEntry {
    private final String id;
    private final String expression;
    private final String result;
    private final List<String> listToken;

    public HistoryEntry(String id, String expression, String result, List<String> listToken) {
      this.id = id;
      this.expression = expression;
      this.result = result;
      this.listToken = new ArrayList<>(listToken);
    }

    public String getId() {
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
}
