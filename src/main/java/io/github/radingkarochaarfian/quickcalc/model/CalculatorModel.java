package io.github.radingkarochaarfian.quickcalc.model;

import java.util.ArrayList;
import java.util.List;

public class CalculatorModel {
  private List<String> listHistoryInput = new ArrayList<>();
  private int currIndex = -1;

  public List<String> restoreInput(List<String> listInput) {
    if (listInput == null || listInput.isEmpty()) {
      clearState();
    }
    listHistoryInput = new ArrayList<>(listInput);
    currIndex = listHistoryInput.size() - 1;
    return listHistoryInput;
  }

  public String moveIndexUp() {
    if (currIndex > 0) {
      currIndex--;
      return listHistoryInput.get(currIndex);
    }
    return null;
  }

  public String moveIndexDown() {
    if (currIndex < listHistoryInput.size() - 1) {
      currIndex++;
      return listHistoryInput.get(currIndex);
    }
    return null;
  }

  public void truncateBelow() {
    if (currIndex > -1) {
      listHistoryInput = new ArrayList<>(listHistoryInput.subList(0, currIndex + 1));
    } else if (currIndex == -1) {
      listHistoryInput.clear();
    }
  }

  public void clearState() {
    listHistoryInput.clear();
    currIndex = -1;
  }

  public void addInput(String text) {
    listHistoryInput.add(text);
    currIndex++;
  }

  public void replaceLastInput(String input) {
    listHistoryInput.set(listHistoryInput.size() - 1, input);
  }

  public void setListHistoryInput(List<String> listInput) {
    if (listInput != null) {
      listHistoryInput = new ArrayList<>(listInput);
      currIndex = listHistoryInput.size() - 1;
    }
  }

  public List<String> getListHistoryInput() {
    return listHistoryInput;
  }

  public int getCurrentIndex() {
    return currIndex;
  }

  public String getExpressionOnString() {
    StringBuilder sb = new StringBuilder();
    for (String token : listHistoryInput) {
      sb.append(token);
    }
    return sb.toString();
  }

}
