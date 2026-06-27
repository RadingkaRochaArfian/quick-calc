package io.github.radingkarochaarfian.quickcalc.controller;

import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.KeyStroke;
import javax.swing.Timer;

import io.github.radingkarochaarfian.quickcalc.model.CalculatorModel;
import io.github.radingkarochaarfian.quickcalc.view.CalculatorGUI;

public class CalculatorController {
  private final CalculatorModel model;
  private final CalculatorGUI view;
  private boolean statusCtrlHold;
  private Timer testTimer;

  public CalculatorController(CalculatorGUI iView, CalculatorModel iModel) {
    model = iModel;
    view = iView;
    initController();
  }

  private void initController() {
    setButtonTextLogic();
  }

  private void setButtonTextLogic() {
    view.getTfInput().addKeyListener(new KeyAdapter() {
      public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE)
          view.getRootPane().requestFocusInWindow();
      }
    });
    testTimer = new Timer(100, e -> {
      List<String> listBeforeCtrl = List.of("÷", "=", "×", "+/-", "C", "AC");
      List<String> listAfterCtrl = List.of("/", "↲", "*", "\\", "ESC", "DEL");
      if (!statusCtrlHold) {
        setButtonText(listBeforeCtrl, listBeforeCtrl);
      } else {
        setButtonText(listBeforeCtrl, listAfterCtrl);
      }
    });
    testTimer.start();
    view.getRootPane().addKeyListener(new KeyAdapter() {

      public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_CONTROL) {
          statusCtrlHold = true;
        }
      }

      public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_CONTROL) {
          statusCtrlHold = false;
        }
      }
    });
  }

  private void setButtonText(List<String> listTextBefore, List<String> listTextAfter) {
    for (int i = 0; i < listTextBefore.size(); i++) {
      view.getMapButton().get(listTextBefore.get(i)).setText(listTextAfter.get(i));
    }
  }

  private void onNumberClick(String num) {
    if (model.isInsideBracket()) {
      String newText = model.getInputOnBracket() + num;
      view.getTfInput().setText(newText);
    } else {
      String screenText = view.getTfInput().getText();
      if (isOperator(screenText)) {
        model.commitInputOperator();
        view.getTfInput().setText(num);
      } else {
        if (view.getTfInput().getText().equals("0"))
          view.getTfInput().setText("");
        view.getTfInput().setText(screenText + num);
      }
    }
  }

  private void onOperatorClick(String op) {
    if (model.isInsideBracket()) {
      String currText = model.getInputOnBracket();
      char lastCharacter = currText.charAt(currText.length() - 1);
      String newText = currText;
      if (isOperator(String.valueOf(lastCharacter)) || lastCharacter == '(') {
        newText += "0" + op;
      } else {
        newText += op;
      }
      model.setInputOnBracket(newText);
      view.getTfInput().setText(newText);
    } else {
      String currText = view.getTfInput().getText();
      if (!currText.isEmpty() && !isOperator(currText)) {
        model.addToken(currText);
        ;
      }
      model.setInputOperator(op);
      view.getTfInput().setText(op);
    }
  }

  private boolean isNum(String input) {
    if (List.of(".", "0", "1", "2", "3", "4", "5", "6", "7", "8", "9").contains(input)) {
      return true;
    }
    return false;
  }

  private boolean isOperator(String input) {
    if (List.of("%", "/", "÷", "*", "×", "-", "+").contains(input)) {
      return true;
    }
    return false;
  }
}
