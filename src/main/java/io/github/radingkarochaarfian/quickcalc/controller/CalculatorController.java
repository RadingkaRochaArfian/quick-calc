package io.github.radingkarochaarfian.quickcalc.controller;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.Timer;
import javax.swing.table.DefaultTableModel;

import io.github.radingkarochaarfian.quickcalc.config.DatabaseConfig;
import io.github.radingkarochaarfian.quickcalc.model.CalculatorModel;
import io.github.radingkarochaarfian.quickcalc.model.HistoryModel;
import io.github.radingkarochaarfian.quickcalc.model.MathEvaluator;
import io.github.radingkarochaarfian.quickcalc.model.HistoryModel.HistoryEntry;
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
    setHistoryTableContent();
    setTextFieldLogic();
  }

  private void setTextFieldLogic() {
    JTextField tfInput = view.getTfInput();
    HashMap<String, JButton> mapButton = view.getMapButton();
    tfInput.addKeyListener(new KeyAdapter() {

      public void keyPressed(KeyEvent e) {

        if (e.getKeyCode() == KeyEvent.VK_CONTROL) {
          statusCtrlHold = true;
        }

        if (statusCtrlHold) {
          switch (e.getKeyCode()) {
            case KeyEvent.VK_DELETE:
              JButton bAllClear = mapButton.get("AC");
              bAllClear.doClick();
              e.consume();
              break;
            case KeyEvent.VK_BACK_SPACE:
              JButton bClear = mapButton.get("C");
              bClear.doClick();
              e.consume();
              break;
          }
        }

      }

      public void keyReleased(KeyEvent e) {

        if (e.getKeyCode() == KeyEvent.VK_CONTROL) {
          statusCtrlHold = false;
        }

      }

    });
  }

  private void setHistoryTableContent() {
    backupService.syncLocalToDatabase();
    List<HistoryEntry> lHistoryEntry = backupService.loadHistory();
    DefaultTableModel tModelHistory = view.getTModelHistory();
    tModelHistory.setRowCount(0);
    for (HistoryEntry entry : lHistoryEntry) {
      Object[] rowData = new Object[] {
          entry.getId(),
          entry.getExpression(),
          entry.getResult()
      };
      tModelHistory.addRow(rowData);
    }
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
      List<String> listAfterCtrl = List.of("/", "↲", "*", "\\", "⌫", "DEL");
      if (!statusCtrlHold) {
        setButtonText(listBeforeCtrl, listBeforeCtrl);
      } else {
        setButtonText(listBeforeCtrl, listAfterCtrl);
      }
      JTextField tfInput = view.getTfInput();
      if (tfInput.getText().isEmpty() || tfInput.getText().equals("Error")) {
        tfInput.setText("0");
      }
    });
    testTimer.start();
    view.getRootPane().addKeyListener(new KeyAdapter() {

      public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_CONTROL) {
          statusCtrlHold = true;
        }

        if (statusCtrlHold) {
          HashMap<String, JButton> mapButton = view.getMapButton();
          switch (e.getKeyCode()) {
            case KeyEvent.VK_DELETE:
              mapButton.get("AC").doClick();
              break;
            case KeyEvent.VK_ENTER:
              mapButton.get("=").doClick();
              break;
            case KeyEvent.VK_8:
              mapButton.get("×").doClick();
              break;
            case KeyEvent.VK_SLASH:
              mapButton.get("÷").doClick();
              break;
            case KeyEvent.VK_BACK_SLASH:
              mapButton.get("+/-").doClick();
              break;
            case KeyEvent.VK_BACK_SPACE:
              mapButton.get("C").doClick();
              break;
          }
        }
      }

      public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_CONTROL) {
          statusCtrlHold = false;
        }
      }

    });
    setButtonUnfocusable();
    setNumberButtonLogic();
    setOperatorButtonLogic();
    setPlusMinusButtonLogic();
    setOpenBracketButtonLogic();
    setCloseBracketButtonLogic();
    setClearButtonLogic();
    setAllClearButtonLogic();
    setEqualButtonLogic();
    setHistoryButtonLogic();
    setUpButtonLogic();
    setDownButtonLogic();
    setClearHistoryButtonLogic();
    setDeleteHistoryButtonLogic();
    setLeftButtonLogic();
    setRightButtonLogic();
  }

  private void setButtonUnfocusable() {
    view.getMapButton().values().forEach(btn -> {
      btn.setFocusable(false);
    });
  }

  private void setRightButtonLogic() {
    JTextField tfInput = view.getTfInput();
    JButton bRight = view.getMapButton().get("▶");
    bRight.addActionListener(e -> {
      tfInput.requestFocus();
      int caretPos = tfInput.getCaretPosition();
      int inputLength = tfInput.getText().length();
      if (caretPos < inputLength) {
        tfInput.setCaretPosition(caretPos + 1);
      }
    });
  }

  private void setLeftButtonLogic() {
    JTextField tfInput = view.getTfInput();
    JButton bLeft = view.getMapButton().get("◀");
    bLeft.addActionListener(e -> {
      tfInput.requestFocus();
      int caretPos = tfInput.getCaretPosition();
      if (caretPos > 0) {
        tfInput.setCaretPosition(caretPos - 1);
      }
    });
  }

  private void setDeleteHistoryButtonLogic() {
    JButton bDHistory = view.getMapButton().get("Delete");
    int selectedRow = view.getTHistory().getSelectedRow();
    if (selectedRow == -1) {
      CalculatorUtils.showInformation(view, "Select a row first.");
      return;
    }
    bDHistory.addActionListener(e -> {
      view.getTModelHistory().removeRow(selectedRow);
      HistoryEntry selectedEntry = hModel.getHistoryEntryAt(selectedRow);
      hModel.removeHistoryEntryAt(selectedRow);
      backupService.deleteHistoryBackupAt(selectedRow, selectedEntry.getId());
    });
  }

  private void setClearHistoryButtonLogic() {
    JButton bCHistory = view.getMapButton().get("Clear");
    DefaultTableModel tMHistory = view.getTModelHistory();
    bCHistory.addActionListener(e -> {
      backupService.deleteAllHistoryBackup();
      tMHistory.setColumnCount(0);
    });
  }

  private void setDownButtonLogic() {
    JButton bDown = view.getMapButton().get("▼");
    JTextField tfInput = view.getTfInput();
    bDown.addActionListener(e -> {
      String token = model.moveIndexDown();
      if (!token.isEmpty() || token != null) {
        tfInput.setText(token);
      }
    });
  }

  private void setUpButtonLogic() {
    JButton bUp = view.getMapButton().get("▲");
    JTextField tfInput = view.getTfInput();
    bUp.addActionListener(e -> {
      String token = model.moveIndexUp();
      if (!token.isEmpty() || token != null) {
        tfInput.setText(token);
      }
    });
  }

  private void setHistoryButtonLogic() {
    JButton bHistory = view.getMapButton().get("H");
    bHistory.addActionListener(e -> {
      view.toggleHistoryPanel();
    });
  }

  private void setEqualButtonLogic() {
    JTextField tfInput = view.getTfInput();
    JButton bEqual = view.getMapButton().get("=");
    bEqual.addActionListener(e -> {
      model.addInput(tfInput.getText());
      try {
        String expression = model.getExpressionOnString();
        double evalResult = evaluator.evaluate(expression);
        String formattedResult = (evalResult % 1 == 0) ? String.valueOf((long) evalResult) : String.valueOf(evalResult);

        backupService.saveHistory(expression, formattedResult);
        List<String> listToken = model.getListHistoryInput();
        hModel.addHistory(expression, formattedResult, listToken);

        DefaultTableModel tModel = view.getTModelHistory();
        tModel.addRow(new Object[] {
            expression + " = " + formattedResult
        });

        tfInput.setText(formattedResult);
      } catch (Exception ex) {
        tfInput.setText("Error");
      }
    });
  }

  private void setAllClearButtonLogic() {
    JTextField tfInput = view.getTfInput();
    JButton bAllClear = view.getMapButton().get("AC");
    bAllClear.addActionListener(e -> {
      model.clearState();
      tfInput.setText("0");
    });
  }

  private void setClearButtonLogic() {
    JTextField tfInput = view.getTfInput();
    JButton bClear = view.getMapButton().get("C");
    bClear.addActionListener(e -> {
      model.truncateBelow();
      tfInput.setText("0");
    });
  }

  private void setCloseBracketButtonLogic() {
    JTextField tfInput = view.getTfInput();
    JButton bCBracket = view.getMapButton().get(")");
    bCBracket.addActionListener(e -> {
      model.truncateBelow();
      String currText = tfInput.getText();
      int caretPos = (tfInput.isFocusOwner()) ? tfInput.getCaretPosition() : currText.length();
      if (isCaretInsideBracket(currText, caretPos)) {
        return;
      }
      String charBefore = String.valueOf(currText.charAt(caretPos - 1));
      if (CalculatorUtils.isOperator(charBefore) || charBefore.equals("(")) {
        return;
      }
      insertTfInput(")");
    });
  }

  private void setOpenBracketButtonLogic() {
    JTextField tfInput = view.getTfInput();
    JButton bOBracket = view.getMapButton().get("(");
    bOBracket.addActionListener(e -> {
      model.truncateBelow();
      String currText = tfInput.getText();
      int caretPos = (tfInput.isFocusOwner()) ? tfInput.getCaretPosition() : currText.length();
      if (currText.isEmpty() || currText.equals("0")) {
        insertTfInput("(");
        return;
      }
      String charBefore = String.valueOf(currText.charAt(caretPos - 1));
      if (CalculatorUtils.isNumber(charBefore) || charBefore.equals(")")) {
        insertTfInput("×(");
      } else {
        insertTfInput("(");
      }
    });
  }

  private void setPlusMinusButtonLogic() {
    JTextField tfInput = view.getTfInput();
    JButton btnPlusMinus = view.getMapButton().get("+/-");
    btnPlusMinus.addActionListener(e -> {
      String currText = tfInput.getText();
      if (currText.isEmpty())
        return;
      int caretPos = (tfInput.isFocusOwner()) ? tfInput.getCaretPosition() : currText.length();
      String resultText = togglePlusMinusAt(currText, caretPos);
      tfInput.setText(resultText);
      model.truncateBelow();
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
          insertTfInput(textLabel);
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
          tfInput.setText(textLabel);
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

  private void insertTfInput(String newText) {
    JTextField tfInput = view.getTfInput();
    if (tfInput.isFocusOwner()) {
      tfInput.replaceSelection(newText);
    } else {
      String currText = tfInput.getText();
      tfInput.setText(currText + newText);
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
    boolean insideBracket = isCaretInsideBracket(text, caretPosition);
    if (!insideBracket && text.endsWith(")") && caretPosition == text.length()) {
      if (text.startsWith("(-") && text.endsWith(")")) {
        return text.substring(2, text.length() - 1);
      }
      return "(-" + text + ")";
    }
    int start = caretPosition;
    while (start > 0) {
      char currChar = text.charAt(start - 1);
      if (Character.isDigit(currChar) || currChar == '.') {
        start--;
      } else {
        break;
      }
    }
    int end = caretPosition;
    while (end < text.length()) {
      char currChar = text.charAt(end);
      if (Character.isDigit(currChar) || currChar == '.') {
        end++;
      } else {
        break;
      }
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
