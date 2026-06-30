package io.github.radingkarochaarfian.quickcalc.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class MathEvaluator {
  public List<String> tokenize(String expression) {
    List<String> listToken = new ArrayList<>();
    int i = 0;
    int n = expression.length();
    while (i < n) {
      char c = expression.charAt(i);
      if (Character.isWhitespace(c)) {
        i++;
        continue;
      }
      if (Character.isDigit(c) || c == '.') {
        StringBuilder sb = new StringBuilder();
        while (i < n && (Character.isDigit(expression.charAt(i)) || expression.charAt(i) == '.')) {
          sb.append(expression.charAt(i));
          i++;
        }
        listToken.add(sb.toString());
      } else {
        listToken.add(String.valueOf(c));
        i++;
      }
    }
    return listToken;
  }

  public double evaluate(JFrame window, String expression) {
    List<String> listToken = tokenize(expression);
    List<String> listPostFix = shuntingYard(listToken);
    return evaluateListPostFix(window, listPostFix);
  }

  public List<String> shuntingYard(List<String> listToken) {
    List<String> listPostFix = new ArrayList<>();
    Stack<String> stackOperator = new Stack<>();
    for (String token : listToken) {
      if (isNumber(token)) {
        listPostFix.add(token);
      } else if (token.equals("(")) {
        stackOperator.push(token);
      } else if (token.equals(")")) {
        while (!stackOperator.isEmpty() && !stackOperator.peek().equals("(")) {
          listPostFix.add(stackOperator.pop());
        }
        if (!stackOperator.isEmpty()) {
          stackOperator.pop();
        }
      } else if (isOperator(token)) {
        while (!stackOperator.isEmpty() &&
            isOperator(stackOperator.peek()) &&
            getPrecedence(stackOperator.peek()) >= getPrecedence(token)) {
          listPostFix.add(stackOperator.pop());
        }
        stackOperator.push(token);
      }
    }
    while (!stackOperator.isEmpty()) {
      listPostFix.add(stackOperator.pop());
    }
    return listPostFix;
  }

  private double evaluateListPostFix(JFrame window, List<String> listPostFix) {
    try {
      Stack<Double> stackNum = new Stack<>();
      for (String token : listPostFix) {
        if (isNumber(token)) {
          stackNum.push(Double.parseDouble(token));
        } else if (isOperator(token)) {
          if (stackNum.size() < 2) {
            throw new IllegalArgumentException("Expression Invalid");
          }
          double rightNum = stackNum.pop();
          double leftNum = stackNum.pop();
          double result = evaluateMath(window, leftNum, rightNum, token);
          stackNum.push(result);
        }
      }
      if (stackNum.size() != 1) {
        throw new IllegalArgumentException("Expression Invalid");
      }
      return stackNum.pop();
    } catch (Exception e) {
      JOptionPane.showMessageDialog(
          window,
          "Expression Invalid!",
          "Error",
          JOptionPane.ERROR_MESSAGE);
      return 0;
    }
  }

  private boolean isOperator(String token) {
    return (List.of("%", "/", "*", "-", "+", "×", "÷").contains(token));
  }

  private boolean isNumber(String token) {
    try {
      Double.parseDouble(token);
      return true;
    } catch (Exception e) {
      return false;
    }
  }

  private int getPrecedence(String token) {
    if (List.of("+", "-").contains(token))
      return 1;
    if (List.of("%", "×", "÷", "/", "*").contains(token))
      return 2;
    return 0;
  }

  private double evaluateMath(JFrame window, double leftNum, double rightNum, String operator) {
    try {
      switch (operator) {
        case ("+"):
          return leftNum + rightNum;
        case ("-"):
          return leftNum - rightNum;
        case ("/"):
        case ("÷"):
          if (rightNum == 0)
            throw new ArithmeticException("Cant divide by zero");
          return leftNum / rightNum;
        case ("×"):
        case ("*"):
          return leftNum * rightNum;
        case ("%"):
          return leftNum % rightNum;
        default:
          return 0;
      }

    } catch (Exception e) {
      JOptionPane.showMessageDialog(
          window,
          "Expresssion Invalid",
          "Error",
          JOptionPane.ERROR_MESSAGE);
      return 0;
    }
  }
}
