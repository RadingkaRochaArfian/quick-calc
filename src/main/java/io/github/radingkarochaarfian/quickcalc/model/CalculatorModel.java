package io.github.radingkarochaarfian.quickcalc.model;

import java.util.ArrayList;
import java.util.List;

import io.github.radingkarochaarfian.quickcalc.util.CalculatorUtils;

public class CalculatorModel {
  private List<String> listHistoryInput = new ArrayList<>();
  private int currIndex = -1;

  public void restoreInput(List<String> listInput) {
    if (listInput == null || listInput.isEmpty()) {
      clearState();
    }
    listHistoryInput = listInput;
    currIndex = listHistoryInput.size() - 1;
  }

  public String moveIndexUp() {
    if (currIndex > 0) {
      currIndex--;
      return listHistoryInput.get(currIndex);
    }
    return null;// check if null later
  }

  public String moveIndexDown() {
    if (currIndex < listHistoryInput.size() - 1) {
      currIndex++;
      return listHistoryInput.get(currIndex);
    }
    return null;// check if null later
  }

  public void truncateBelow() {
    if (currIndex > -1) {
      listHistoryInput = new ArrayList<>(listHistoryInput.subList(0, currIndex + 1));
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

  public String togglePlusMinusAt(String text, int caretPosition) {
    if (text.isEmpty() || text == null || text.equals("0")) {
      return text;
    }
    int start = caretPosition;
    char currChar = text.charAt(start - 1);
    while (start > 0 && (Character.isDigit(currChar) || currChar == '.')) {
      start--;
      currChar = text.charAt(start - 1);
    }
    int end = caretPosition;
    while (end < text.length() && (Character.isDigit(text.charAt(end)) || text.charAt(end) == '.')) {
      end++;
    }
    if (start == end)
      return text;
    String targetNum = text.substring(start, end);
    String beforeTarget = text.substring(0, start);
    String afterTarget = text.substring(end);
    if (beforeTarget.endsWith("(-") && afterTarget.startsWith(")")) {
      beforeTarget = beforeTarget.substring(0, beforeTarget.length() - 2);
      afterTarget = afterTarget.substring(1);
      return beforeTarget + targetNum + afterTarget;
    } else {
      return beforeTarget + "(-" + targetNum + ")" + afterTarget;
    }
  }
}
