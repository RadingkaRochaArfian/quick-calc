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
import io.github.radingkarochaarfian.quickcalc.model.MathEvaluator;
import io.github.radingkarochaarfian.quickcalc.service.HistoryBackupService;
import io.github.radingkarochaarfian.quickcalc.util.CalculatorUtils;
import io.github.radingkarochaarfian.quickcalc.view.CalculatorView;

public class CalculatorController {
  private final CalculatorModel model;
  private final HistoryModel hModel;
  private final CalculatorView view;
  private final MathEvaluator evaluator;
  private final DatabaseConfig dbConfig;
  private final HistoryBackupService backupService;
  private boolean statusCtrlHold;
  private Timer testTimer;

  public CalculatorController(CalculatorView iView,
      CalculatorModel iModel,
      HistoryModel iHModel,
      MathEvaluator iEvaluator,
      DatabaseConfig iDbConfig,
      HistoryBackupService bService) {
    model = iModel;
    hModel = iHModel;
    view = iView;
    evaluator = iEvaluator;
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
    setPlusMinusButtonLogic();
  }

  private void setPlusMinusButtonLogic() {
    JTextField tfInput = view.getTfInput();
    JButton btnPlusMinus = view.getMapButton().get("+/-");
    btnPlusMinus.addActionListener(e -> {
      String currText = tfInput.getText();
      int caretPos = (tfInput.isFocusOwner()) ? tfInput.getCaretPosition() : currText.length();

    });
  }

  private void setOperatorButtonLogic() {
    JTextField tfInput = view.getTfInput();
    for (String textLabel : getListOfOperator()) {
      JButton btnNum = view.getMapButton().get(textLabel);
      btnNum.addActionListener(e -> {
        model.truncateBelow();
        String currText = tfInput.getText();
        int caretPos = tfInput.isFocusOwner() ? tfInput.getCaretPosition() : currText.length();
        if (isCaretInsideBracket(currText, caretPos)) {
          if (tfInput.isFocusOwner()) {
            tfInput.replaceSelection(textLabel);
          } else {
            tfInput.setText(tfInput.getText() + textLabel);
          }
        } else if (CalculatorUtils.isOperator(currText)) {
          tfInput.setText(textLabel);
        } else {
          model.addInput(tfInput.getText());
          tfInput.setText(textLabel);
        }
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
        if (CalculatorUtils.isOperator(currentText)) {
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
