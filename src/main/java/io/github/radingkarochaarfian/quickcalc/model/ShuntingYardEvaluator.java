package io.github.radingkarochaarfian.quickcalc.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class ShuntingYardEvaluator implements MathEvaluator {
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

  public double evaluate(String expression) {
    List<String> listToken = tokenize(expression);
    List<String> listPostFix = shuntingYard(listToken);
    return evaluateListPostFix(listPostFix);
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

  private double evaluateListPostFix(List<String> listPostFix) {
    Stack<Double> stackNum = new Stack<>();
    for (String token : listPostFix) {
      if (isNumber(token)) {
        stackNum.push(Double.parseDouble(token));
      } else if (isOperator(token)) {
        if (stackNum.size() < 2) {
          throw new IllegalArgumentException("Expression Invalid.");
        }
        double rightNum = stackNum.pop();
        double leftNum = stackNum.pop();
        if (List.of("/", "÷").contains(token) && rightNum == 0) {
          throw new IllegalArgumentException("Cannot divide by zero.");
        }
        double result = evaluateMath(leftNum, rightNum, token);
        stackNum.push(result);
      }
    }
    if (stackNum.size() != 1) {
      throw new IllegalArgumentException("Expression format is incomplete.");
    }
    return stackNum.pop();
  }

  public boolean isOperator(String token) {
    return (List.of("%", "/", "*", "-", "+", "×", "÷").contains(token));
  }

  public boolean isNumber(String token) {
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

  private double evaluateMath(double leftNum, double rightNum, String operator) {
    switch (operator) {
      case ("+"):
        return leftNum + rightNum;
      case ("-"):
        return leftNum - rightNum;
      case ("/"):
      case ("÷"):
        return leftNum / rightNum;
      case ("×"):
      case ("*"):
        return leftNum * rightNum;
      case ("%"):
        return leftNum % rightNum;
      default:
        throw new IllegalArgumentException("Operator is undefined: " + operator + ".");
    }
  }
}
