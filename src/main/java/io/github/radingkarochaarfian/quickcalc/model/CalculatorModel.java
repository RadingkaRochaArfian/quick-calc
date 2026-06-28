package io.github.radingkarochaarfian.quickcalc.model;

import java.util.ArrayList;
import java.util.List;

public class CalculatorModel {
  private List<String> listCurrToken = new ArrayList<>();
  private int currIndex = -1;

  public String restoreState(List<String> listToken) {
    if (listToken == null || listToken.isEmpty()) {
      clearState();
      return null;// check if null later
    }
    listCurrToken = listToken;
    currIndex = listCurrToken.size() - 1;
    return listCurrToken.get(currIndex);
  }

  public String moveIndexUp() {
    if (currIndex > 0) {
      currIndex--;
      return listCurrToken.get(currIndex);
    }
    return null;// check if null later
  }

  public String moveIndexDown() {
    if (currIndex < listCurrToken.size() - 1) {
      currIndex++;
      return listCurrToken.get(currIndex);
    }
    return null;// check if null later
  }

  public void truncateBelow() {
    if (currIndex > -1) {
      listCurrToken = new ArrayList<>(listCurrToken.subList(0, currIndex + 1));
    }
  }

  public void clearState() {
    listCurrToken.clear();
    currIndex = -1;
  }

  public void setListCurrToken(List<String> listToken) {
    if (listToken != null) {
      listCurrToken = new ArrayList<>(listToken);
      currIndex = listCurrToken.size() - 1;
    }
  }

  public List<String> getListCurrToken() {
    return listCurrToken;
  }

  public int getCurrentIndex() {
    return currIndex;
  }

  public String getExpressionOnString() {
    StringBuilder sb = new StringBuilder();
    for (String token : listCurrToken) {
      sb.append(token);
    }
    return sb.toString();
  }
}
