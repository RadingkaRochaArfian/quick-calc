package io.github.radingkarochaarfian.quickcalc.controller;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.Timer;

import io.github.radingkarochaarfian.quickcalc.config.DatabaseConfig;
import io.github.radingkarochaarfian.quickcalc.model.CalculatorModel;
import io.github.radingkarochaarfian.quickcalc.model.HistoryModel;
import io.github.radingkarochaarfian.quickcalc.service.HistoryBackupService;
import io.github.radingkarochaarfian.quickcalc.view.CalculatorView;

public class CalculatorController {
  private final CalculatorModel model;
  private final HistoryModel hModel;
  private final CalculatorView view;
  private final DatabaseConfig dbConfig;
  private final HistoryBackupService backupService;
  private boolean statusCtrlHold;
  private Timer testTimer;

  public CalculatorController(CalculatorView iView,
      CalculatorModel iModel,
      HistoryModel iHModel,
      DatabaseConfig iDbConfig,
      HistoryBackupService bService) {
    model = iModel;
    hModel = iHModel;
    view = iView;
    dbConfig = iDbConfig;
    backupService = bService;
    initController();
  }

  private void initController() {
    setButtonLogic();
  }

  private void setButtonLogic() {
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
      JTextField tfInput = view.getTfInput();
      if (tfInput.getText().isEmpty()) {
        tfInput.setText("0");
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
    setNumberButtonLogic();
    setOperatorButtonLogic();
    setPlusMinusButton();
  }

  private void setPlusMinusButton() {
    JTextField tfInput = view.getTfInput();

  }

  private void setOperatorButtonLogic() {
    JTextField tfInput = view.getTfInput();
    for (String textLabel : getListOfOperator()) {
      JButton btnNum = view.getMapButton().get(textLabel);
      btnNum.addActionListener(e -> {
        model.truncateBelow();
        String currText = tfInput.getText();
        int caretPos;
        if (tfInput.isFocusOwner())
          caretPos = tfInput.getCaretPosition();
        else
          caretPos = currText.length();
        if (isCaretInsideBracket(currText, caretPos)) {
          if (tfInput.isFocusOwner()) {
            tfInput.replaceSelection(textLabel);
          } else {
            tfInput.setText(tfInput.getText() + textLabel);
          }
        }
        model.addInput(tfInput.getText());
        tfInput.setText(textLabel);
      });
    }
  }

  private void setNumberButtonLogic() {
    JTextField tfInput = view.getTfInput();
    for (String textLabel : getListOfNum()) {
      JButton btnNum = view.getMapButton().get(textLabel);
      btnNum.addActionListener(e -> {
        model.truncateBelow();
        String currentText = tfInput.getText();
        if (isOperator(currentText)) {
          model.addInput(currentText);
          tfInput.setText("0");
        }
        if (tfInput.isFocusOwner()) {
          tfInput.replaceSelection(textLabel);
        } else {
          if (currentText.equals("0")) {
            tfInput.setText(textLabel);
          } else {
            tfInput.setText(currentText + textLabel);
          }
        }
      });
    }
  }

  private void setButtonText(List<String> listTextBefore, List<String> listTextAfter) {
    for (int i = 0; i < listTextBefore.size(); i++) {
      view.getMapButton().get(listTextBefore.get(i)).setText(listTextAfter.get(i));
    }
  }

  private List<String> getListOfNum() {
    return List.of(".", "0", "1", "2", "3", "4", "5", "6", "7", "8", "9");
  }

  private List<String> getListOfOperator() {
    return List.of("%", "÷", "×", "-", "+");
  }

  private List<String> getListOfPlusMinus() {
    return List.of("+/-");
  }

  private boolean isNum(String input) {
    if (getListOfNum().contains(input)) {
      return true;
    }
    return false;
  }

  private boolean isOperator(String input) {
    if (getListOfOperator().contains(input)) {
      return true;
    }
    return false;
  }

  private boolean isCaretInsideBracket(String text, int caretPosition) {
    int openCount = 0;
    int closeCount = 0;
    for (int i = 0; i < caretPosition; i++) {
      char ch = text.charAt(i);
      if (ch == '(')
        openCount++;
      else if (ch == ')')
        closeCount++;
    }
    return openCount > closeCount;
  }
}
