package io.github.radingkarochaarfian.quickcalc;

import io.github.radingkarochaarfian.quickcalc.view.*;

import javax.swing.SwingUtilities;

public class QuickCalcApp {
  public static void main(String[] args) {
    SwingUtilities.invokeLater(() -> {
      CalculatorGUI app = new CalculatorGUI();
      app.setVisible(true);
    });
  }
}
