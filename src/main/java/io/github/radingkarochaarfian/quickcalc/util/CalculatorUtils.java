package io.github.radingkarochaarfian.quickcalc.util;

import java.util.ArrayList;
import java.util.List;

public final class CalculatorUtils {
  private CalculatorUtils() {
  }

  public static boolean isOperator(String token) {
    return (List.of("%", "/", "*", "-", "+", "×", "÷").contains(token));
  }

  public static boolean isNumber(String token) {
    if (token == null)
      return false;
    try {
      Double.parseDouble(token);
      return true;
    } catch (NumberFormatException e) {
      return false;
    }
  }

  public static List<String> parseToInput(String equation) {
    List<String> listInput = new ArrayList<>();
    if (equation == null || equation.trim().isEmpty())
      return listInput;
    char[] charEq = equation.toCharArray();
    int len = charEq.length;
    int i = 0;
    while (i < len) {
      char c = charEq[i];
      if (Character.isWhitespace(c)) {
        i++;
        continue;
      }
      if (c == '(') {
        StringBuilder sb = new StringBuilder();
        int open = 0;
        while (i < len) {
          sb.append(charEq[i]);
          if (charEq[i] == '(')
            open++;
          else if (charEq[i] == ')')
            open--;
          i++;
          if (open == 0)
            break;
        }
        listInput.add(sb.toString());
      } else if (Character.isDigit(c) || c == '.') {
        StringBuilder sb = new StringBuilder();
        while (i < len && (Character.isDigit(c) || charEq[i] == '.')) {
          sb.append(charEq[i]);
          i++;
        }
        listInput.add(sb.toString());
      } else if (CalculatorUtils.isOperator(String.valueOf(c))) {
        listInput.add(String.valueOf(c));
        i++;
      } else {
        i++;
      }
    }
    return listInput;
  }
}
