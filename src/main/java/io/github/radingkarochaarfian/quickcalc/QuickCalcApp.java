package io.github.radingkarochaarfian.quickcalc;

import io.github.radingkarochaarfian.quickcalc.controller.CalculatorController;
import io.github.radingkarochaarfian.quickcalc.model.CalculatorModel;
import io.github.radingkarochaarfian.quickcalc.view.*;

import javax.swing.SwingUtilities;

public class QuickCalcApp {
  public static void main(String[] args) {
    SwingUtilities.invokeLater(() -> {
      CalculatorGUI view = new CalculatorGUI();
      CalculatorModel model = new CalculatorModel();
      CalculatorController controller = new CalculatorController(view, model);
      view.setVisible(true);
    });
  }
}
