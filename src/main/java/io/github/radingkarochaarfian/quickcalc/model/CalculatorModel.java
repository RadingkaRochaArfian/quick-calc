package io.github.radingkarochaarfian.quickcalc.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class CalculatorModel {
  private final List<String> listToken = new ArrayList<>();
  private String inputOPerator = "";
  private boolean statusInsideBracker = false;
  private String inputOnBracket = "";

  public void addToken(String token) {
    if (token != null && !token.trim().isEmpty()) {
      listToken.add(token);
    }
  }

  public void commitInputOperator() {
    if (!inputOPerator.isEmpty()) {
      listToken.add(inputOPerator);
      inputOPerator = "";
    }
  }

  public void commitInputOnBracket() {
    if (!inputOnBracket.isEmpty()) {
      listToken.add(inputOnBracket);
      inputOnBracket = "";
      statusInsideBracker = false;
    }
  }

  public void clearAll() {
    listToken.clear();
    inputOPerator = "";
    inputOnBracket = "";
    statusInsideBracker = false;
  }

  public List<String> getListToken() {
    return listToken;
  }

  public String getInputOperator() {
    return inputOPerator;
  }

  public void setInputOperator(String ip) {
    inputOPerator = ip;
  }

  public boolean isInsideBracket() {
    return statusInsideBracker;
  }

  public void setInsideBracket(boolean status) {
    statusInsideBracker = status;
  }

  public String getInputOnBracket() {
    return inputOnBracket;
  }

  public void setInputOnBracket(String ib) {
    inputOnBracket = ib;
  }

  public int getListTokenSize() {
    return listToken.size();
  }

  public String getTokenAt(int idx) {
    if (idx >= 0 && idx < listToken.size()) {
      return listToken.get(idx);
    }
    return "";
  }

  public double evaluate(JFrame window) {
    if (listToken.isEmpty())
      return 0;
    try {
      List<String> formattedTokenList = new ArrayList<>();
      for (String token : listToken) {
        StringBuilder fullSingleNumber = new StringBuilder();
        for (int i = 0; i < token.length(); i++) {
          char c = token.charAt(i);
          if (Character.isDigit(c) || c == '.') {
            fullSingleNumber.append(c);
          } else if (c == '-' && (i == 0 || token.charAt(i - 1) == '(')) {
            fullSingleNumber.append(c);
          } else {
            if (fullSingleNumber.length() > 0) {
              formattedTokenList.add(fullSingleNumber.toString());
              fullSingleNumber.setLength(0);
            }
            formattedTokenList.add(String.valueOf(c));
          }
        }
        if (fullSingleNumber.length() > 0)
          formattedTokenList.add(fullSingleNumber.toString());
      }

      List<String> postFixTokens = convertToPostFix(formattedTokenList);

      Stack<Double> numberStack = new Stack<>();
      for (String token : postFixTokens) {
        if (Character.isDigit(token.charAt(0)) || (token.length() > 1 && token.charAt(0) == '-')) {
          numberStack.push(Double.parseDouble(token));
        } else {
          double rightNum = numberStack.pop();
          double leftNum = numberStack.isEmpty() ? 0 : numberStack.pop();
          double result = 0;
          switch (token) {
            case ("+"):
              result = rightNum + leftNum;
            case ("×"):
            case ("*"):
              result = rightNum * leftNum;
            case ("-"):
              result = leftNum - rightNum;
            case ("/"):
            case ("÷"):
              result = leftNum / rightNum;
              break;
          }
          numberStack.push(result);
        }
      }
      return numberStack.isEmpty() ? 0 : numberStack.pop();
    } catch (Exception e) {
      JOptionPane.showMessageDialog(
          window,
          "Input is invalid!",
          "Error",
          JOptionPane.ERROR_MESSAGE);
      return 0;
    }
  }

  private List<String> convertToPostFix(List<String> tokens) {
    List<String> listPosFix = new ArrayList<>();
    Stack<String> operatorStack = new Stack<>();
    for (String token : tokens) {
      if (Character.isDigit(token.charAt(0)) || (token.length() > 1 && token.charAt(0) == ('-'))) {
        listPosFix.add(token);
      } else if (token.equals("(")) {
        operatorStack.push(token);
      } else if (token.equals(")")) {
        while (!operatorStack.isEmpty() && operatorStack.peek().equals("(")) {
          listPosFix.add(operatorStack.pop());
        }
        if (!operatorStack.isEmpty()) {
          operatorStack.pop();
        }
      } else {
        while (!operatorStack.isEmpty() && getPriority(token) <= getPriority(operatorStack.peek())) {
          listPosFix.add(operatorStack.pop());
        }
        operatorStack.push(token);
      }
    }
    while (!operatorStack.isEmpty()) {
      listPosFix.add(operatorStack.pop());
    }
    return listPosFix;
  }

  private int getPriority(String token) {
    if (List.of("+", "-").contains(token)) {
      return 1;
    }
    if (List.of("*", "/", "×", "÷").contains(token)) {
      return 2;
    }
    return -1;
  }
}
