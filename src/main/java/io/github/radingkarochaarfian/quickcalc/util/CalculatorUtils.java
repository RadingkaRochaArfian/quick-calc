package io.github.radingkarochaarfian.quickcalc.util;

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
}
