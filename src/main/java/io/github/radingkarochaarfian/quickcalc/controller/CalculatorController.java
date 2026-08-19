package io.github.radingkarochaarfian.quickcalc.controller;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JTextField;
import javax.swing.Timer;
import javax.swing.table.DefaultTableModel;

import io.github.radingkarochaarfian.quickcalc.config.DatabaseConfig;
import io.github.radingkarochaarfian.quickcalc.config.DatabaseInit;
import io.github.radingkarochaarfian.quickcalc.controller.handler.OfflineOnlyMenuItemHandler;
import io.github.radingkarochaarfian.quickcalc.controller.handler.ResetDatabaseCredentialsMenuItemHandler;
import io.github.radingkarochaarfian.quickcalc.controller.handler.ShortcutMenuItemHandler;
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
  private final DatabaseInit dbInit;
  private final HistoryBackupService backupService;
  private boolean isResultState;

  private static final List<String> listBeforeCtrl = List.of("÷", "=", "×", "+/-", "C", "AC");
  private static final List<String> listAfterCtrl = List.of("/", "↲", "*", "\\", "ESC", "DEL");

  public CalculatorController(CalculatorView iView,
      CalculatorModel iModel,
      HistoryModel iHModel,
      MathEvaluator iEvaluator,
      DatabaseConfig iDbConfig,
      DatabaseInit iDbInit,
      HistoryBackupService bService) {
    model = iModel;
    hModel = iHModel;
    view = iView;
    evaluator = iEvaluator;
    dbConfig = iDbConfig;
    dbInit = iDbInit;
    backupService = bService;

    initController();
  }

  private void initController() {
    setRootPaneLogic();
    setButtonLogic();
    setHistoryTableContent();
    setTfInputLogic();
    setTfDisplayLogic();
    setMenuItemLogic();
  }

  private void setMenuItemLogic() {
    HashMap<String, JMenuItem> mapMenuItem = view.getMapMenuItem();
    setUserMenuLogic(mapMenuItem);
    setUtilityMenuLogic(mapMenuItem);
    setHelpMenuLogic(mapMenuItem);
  }

  private void setHelpMenuLogic(HashMap<String, JMenuItem> mapMenuItem) {
    JMenuItem miShortcut = mapMenuItem.get("SHORTCUT");
    miShortcut.addActionListener(new ShortcutMenuItemHandler(view));
  }

  private void setUtilityMenuLogic(HashMap<String, JMenuItem> mapMenuItem) {
    JMenuItem miClearAll = mapMenuItem.get("CLEAR_ALL");
    miClearAll.addActionListener(e -> {
      performAllClear();
    });
  }

  private void setUserMenuLogic(HashMap<String, JMenuItem> mapMenuItem) {
    JMenuItem miResetDbCred = mapMenuItem.get("RESET_DB_CREDENTIALS");
    miResetDbCred.addActionListener(new ResetDatabaseCredentialsMenuItemHandler(view, dbConfig));
    JCheckBoxMenuItem miOfflineOnly = (JCheckBoxMenuItem) mapMenuItem.get("USE_OFFLINE_ONLY");
    miOfflineOnly.setSelected(dbConfig.isUseLocalOnly());
    miOfflineOnly.addActionListener(new OfflineOnlyMenuItemHandler(view, dbConfig, dbInit));
  }

  private void setTfDisplayLogic() {
    JTextField tfDisplay = view.getTfDisplay();
    tfDisplay.addKeyListener(new KeyAdapter() {
      public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
          case KeyEvent.VK_ESCAPE:
            view.getRootPane().requestFocus();
            break;

        }
      }
    });
  }

  private void setTfInputLogic() {
    JTextField tfInput = view.getTfInput();
    HashMap<String, JButton> mapButton = view.getMapButton();
    tfInput.addKeyListener(new KeyAdapter() {

      public void keyPressed(KeyEvent e) {
        if (e.isControlDown() && e.getKeyCode() != KeyEvent.VK_CONTROL) {
          return;
        }
        String currText = tfInput.getText();
        switch (e.getKeyCode()) {
          case KeyEvent.VK_CONTROL:
            setButtonText(listBeforeCtrl, listAfterCtrl);
            break;
          case KeyEvent.VK_BACK_SPACE:
            int caretPos = tfInput.getCaretPosition();
            String newText = currText.substring(0, caretPos - 1) + currText.substring(caretPos);
            tfInput.setText(newText);
            break;
          case KeyEvent.VK_H:
            mapButton.get("H").doClick();
            break;
          case KeyEvent.VK_F11:
            view.toggleFullscreen();
            break;
          case KeyEvent.VK_BACK_SLASH:
            mapButton.get("+/-").doClick();
            break;
          case KeyEvent.VK_ESCAPE:
            if (currText.isEmpty()) {
              tfInput.setText("0");
            }
            view.getRootPane().requestFocus();
            break;
          case KeyEvent.VK_DELETE:
            mapButton.get("AC").doClick();
            break;
          case KeyEvent.VK_DIVIDE:
          case KeyEvent.VK_SLASH:
            mapButton.get("÷").doClick();
            break;
          case KeyEvent.VK_MULTIPLY:
            mapButton.get("×").doClick();
            break;
          case KeyEvent.VK_8:
            if (e.isShiftDown()) {
              mapButton.get("×").doClick();
            } else {
              mapButton.get("8").doClick();
            }
            break;
          case KeyEvent.VK_ENTER:
            mapButton.get("=").doClick();
            break;
          case KeyEvent.VK_SUBTRACT:
          case KeyEvent.VK_MINUS:
            mapButton.get("-").doClick();
            break;
          case KeyEvent.VK_ADD:
          case KeyEvent.VK_EQUALS:
            if (e.isShiftDown() || e.getKeyCode() == KeyEvent.VK_ADD) {
              mapButton.get("+").doClick();
            }
            break;
          case KeyEvent.VK_UP:
            mapButton.get("▲").doClick();
            break;
          case KeyEvent.VK_DOWN:
            mapButton.get("▼").doClick();
            break;
          case KeyEvent.VK_9:
            if (e.isShiftDown()) {
              mapButton.get("(").doClick();
            }
            break;
          case KeyEvent.VK_0:
            if (e.isShiftDown()) {
              mapButton.get(")").doClick();
            }
            break;
          case KeyEvent.VK_DECIMAL:
          case KeyEvent.VK_PERIOD:
            mapButton.get(".").doClick();
            break;
          case KeyEvent.VK_ALT:
            JMenuBar mbMain = view.getMbMain();
            boolean complement = mbMain.isVisible() ? false : true;
            view.getMbMain().setVisible(complement);
            break;
        }
        List<String> numList = List.of(
            "0", "1", "2", "3", "4", "5", "6", "7", "8", "9");
        int keyCode = e.getKeyCode();
        if (keyCode >= KeyEvent.VK_0 && keyCode <= KeyEvent.VK_9 && keyCode != KeyEvent.VK_8) {
          mapButton.get(numList.get(keyCode - KeyEvent.VK_0)).doClick();
        } else if (keyCode >= KeyEvent.VK_NUMPAD0 && keyCode <= KeyEvent.VK_NUMPAD9) {
          mapButton.get(numList.get(keyCode - KeyEvent.VK_NUMPAD0)).doClick();
        }
      }

      public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_CONTROL) {
          setButtonText(listBeforeCtrl, listBeforeCtrl);
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
          entry.getDisplayString()
      };
      tModelHistory.addRow(rowData);
      hModel.addHistory(entry.getId(), entry.getExpression(), entry.getResult(), entry.getListHistoryInput());
    }
  }

  private void setRootPaneLogic() {
    JTextField tfInput = view.getTfInput();
    view.getRootPane().addKeyListener(new KeyAdapter() {

      public void keyPressed(KeyEvent e) {
        if (e.isControlDown() && e.getKeyCode() != KeyEvent.VK_CONTROL) {
          return;
        }
        HashMap<String, JButton> mapButton = view.getMapButton();
        switch (e.getKeyCode()) {
          case KeyEvent.VK_CONTROL:
            setButtonText(listBeforeCtrl, listAfterCtrl);
            break;
          case KeyEvent.VK_BACK_SPACE:
            if (isResultState) {
              break;
            }
            String currText = tfInput.getText();
            String newText = currText.substring(0, currText.length() - 1);
            if (newText.isEmpty()) {
              newText = "0";
            }
            tfInput.setText(newText);
            break;
          case KeyEvent.VK_F11:
            view.toggleFullscreen();
            break;
          case KeyEvent.VK_H:
            mapButton.get("H").doClick();
            break;
          case KeyEvent.VK_DELETE:
            mapButton.get("AC").doClick();
            break;
          case KeyEvent.VK_UP:
            mapButton.get("▲").doClick();
            break;
          case KeyEvent.VK_DOWN:
            mapButton.get("▼").doClick();
            break;
          case KeyEvent.VK_LEFT:
            mapButton.get("←").doClick();
            break;
          case KeyEvent.VK_RIGHT:
            mapButton.get("→").doClick();
            break;
          case KeyEvent.VK_ENTER:
            mapButton.get("=").doClick();
            break;
          case KeyEvent.VK_MULTIPLY:
            mapButton.get("×").doClick();
            break;
          case KeyEvent.VK_8:
            if (e.isShiftDown()) {
              mapButton.get("×").doClick();
            } else {
              mapButton.get("8").doClick();
            }
            break;
          case KeyEvent.VK_ADD:
          case KeyEvent.VK_EQUALS:
            if (e.isShiftDown() || e.getKeyCode() == KeyEvent.VK_ADD) {
              mapButton.get("+").doClick();
            }
            break;
          case KeyEvent.VK_SUBTRACT:
          case KeyEvent.VK_MINUS:
            mapButton.get("-").doClick();
            break;
          case KeyEvent.VK_DIVIDE:
          case KeyEvent.VK_SLASH:
            mapButton.get("÷").doClick();
            break;
          case KeyEvent.VK_BACK_SLASH:
            mapButton.get("+/-").doClick();
            break;
          case KeyEvent.VK_ESCAPE:
            mapButton.get("C").doClick();
            break;
          case KeyEvent.VK_DECIMAL:
          case KeyEvent.VK_PERIOD:
            mapButton.get(".").doClick();
            break;
          case KeyEvent.VK_ALT:
            JMenuBar mbMain = view.getMbMain();
            boolean complement = mbMain.isVisible() ? false : true;
            view.getMbMain().setVisible(complement);
            break;
        }
        List<String> numList = List.of(
            "0", "1", "2", "3", "4", "5", "6", "7", "8", "9");
        int keyCode = e.getKeyCode();
        if (keyCode >= KeyEvent.VK_0 && keyCode <= KeyEvent.VK_9 && keyCode != KeyEvent.VK_8) {
          mapButton.get(numList.get(keyCode - KeyEvent.VK_0)).doClick();
        } else if (keyCode >= KeyEvent.VK_NUMPAD0 && keyCode <= KeyEvent.VK_NUMPAD9) {
          mapButton.get(numList.get(keyCode - KeyEvent.VK_NUMPAD0)).doClick();
        }
      }

      public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_CONTROL) {
          setButtonText(listBeforeCtrl, listBeforeCtrl);
        }
      }

    });
  }

  private void setButtonLogic() {
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
    setRestoreHistoryButtonLogic();
    setLeftButtonLogic();
    setRightButtonLogic();
  }

  private void setRestoreHistoryButtonLogic() {
    JButton bRestore = view.getMapButton().get("Restore");
  }

  private void setRightButtonLogic() {
    JTextField tfInput = view.getTfInput();
    JButton bRight = view.getMapButton().get("→");
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
    JButton bLeft = view.getMapButton().get("←");
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
    bDHistory.addActionListener(e -> {
      int selectedRow = view.getTHistory().getSelectedRow();
      if (selectedRow == -1) {
        CalculatorUtils.showInformation(view, "Select a row first.");
        return;
      }
      HistoryEntry selectedEntry = hModel.getHistoryEntryAt(selectedRow);
      hModel.removeHistoryEntryAt(selectedRow);
      backupService.deleteHistoryBackupAt(selectedRow, selectedEntry.getId());
      view.getTModelHistory().removeRow(selectedRow);
    });
  }

  private void setClearHistoryButtonLogic() {
    JButton bCHistory = view.getMapButton().get("Clear");
    DefaultTableModel tMHistory = view.getTModelHistory();
    bCHistory.addActionListener(e -> {
      hModel.clearListHistoryEntry();
      backupService.deleteAllHistoryBackup();
      tMHistory.setRowCount(0);
    });
  }

  private void setDownButtonLogic() {
    JButton bDown = view.getMapButton().get("▼");
    JTextField tfInput = view.getTfInput();
    bDown.addActionListener(e -> {
      String token = model.moveIndexDown();
      if (token != null && !token.isEmpty()) {
        tfInput.setText(token);
      }
    });
  }

  private void setUpButtonLogic() {
    JButton bUp = view.getMapButton().get("▲");
    JTextField tfInput = view.getTfInput();
    bUp.addActionListener(e -> {
      String token = model.moveIndexUp();
      if (token != null && !token.isEmpty()) {
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
    JTextField tfDisplay = view.getTfDisplay();
    JButton bEqual = view.getMapButton().get("=");
    bEqual.addActionListener(e -> {
      if (isResultState) {
        return;
      }
      String currText = tfInput.getText();
      if (CalculatorUtils.isOperator(currText)) {
        return;
      }
      model.addInput(tfInput.getText());
      try {
        String expression = model.getExpressionOnString();
        double evalResult = evaluator.evaluate(expression);
        String formattedResult = (evalResult % 1 == 0)
            ? String.valueOf((long) evalResult)
            : String.valueOf(evalResult);
        tfDisplay.setText(expression + " = " + formattedResult);

        HistoryEntry savedEntry = backupService.saveHistory(expression, formattedResult);
        List<String> listToken = model.getListHistoryInput();
        hModel.addHistory(savedEntry.getId(), expression, formattedResult, listToken);

        DefaultTableModel tModel = view.getTModelHistory();
        tModel.addRow(new Object[] {
            expression + " = " + formattedResult
        });

        tfInput.setText(formattedResult);
        isResultState = true;
      } catch (Exception ex) {
        tfInput.setText("Error");
        tfDisplay.setText(model.getExpressionOnString() + " = Error");
        model.clearState();
        isResultState = true;
        Timer errorTimer = new Timer(200, eT -> {
          tfInput.setText("0");
          tfDisplay.setText("0");
        });
        errorTimer.setRepeats(false);
        errorTimer.start();
      }
    });
  }

  private void performAllClear() {
    model.clearState();
    view.getTfInput().setText("0");
    view.getTfDisplay().setText("0");
  }

  private void setAllClearButtonLogic() {
    JButton bAllClear = view.getMapButton().get("AC");
    bAllClear.addActionListener(e -> {
      performAllClear();
    });
  }

  private void setClearButtonLogic() {
    JTextField tfInput = view.getTfInput();
    JButton bClear = view.getMapButton().get("C");
    bClear.addActionListener(e -> {
      model.truncateBelow();
      tfInput.setText("0");
      updateTfDisplay();
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
      JButton btnOp = view.getMapButton().get(textLabel);
      btnOp.addActionListener(e -> {
        model.truncateBelow();
        String currText = tfInput.getText();
        if (isResultState) {
          model.clearState();
          model.addInput(currText);
          tfInput.setText(textLabel);
          isResultState = false;
          updateTfDisplay();
          return;
        }
        int caretPos = tfInput.isFocusOwner() ? tfInput.getCaretPosition() : currText.length();
        if (isCaretInsideBracket(currText, caretPos)) {
          insertTfInput(textLabel);
        } else if (CalculatorUtils.isOperator(currText)) {
          tfInput.setText(textLabel);
          model.replaceLastInput(textLabel);
        } else {
          model.addInput(tfInput.getText());
          tfInput.setText(textLabel);
        }
        updateTfDisplay();
      });
    }
  }

  private void setNumberButtonLogic() {
    JTextField tfInput = view.getTfInput();
    for (String textLabel : getListOfNum()) {
      JButton btnNum = view.getMapButton().get(textLabel);
      btnNum.addActionListener(e -> {
        model.truncateBelow();
        if (isResultState) {
          model.clearState();
          tfInput.setText(textLabel.equals(".") ? "0." : textLabel);
          isResultState = false;
          updateTfDisplay();
          return;
        }
        String currentText = tfInput.getText();
        if (CalculatorUtils.isOperator(currentText)) {
          model.addInput(currentText);
          tfInput.setText(textLabel.equals(".") ? "0." : textLabel);
          updateTfDisplay();
          return;
        }
        if (currentText.equals("0") && !textLabel.equals(".")) {
          tfInput.setText(textLabel);
        } else {
          if (currentText.equals(".") && textLabel.equals(".")) {
            return;
          }
          tfInput.setText(currentText + textLabel);
        }
        updateTfDisplay();
      });
    }
  }

  private void setButtonText(List<String> listTextBefore, List<String> listTextAfter) {
    for (int i = 0; i < listTextBefore.size(); i++) {
      view.getMapButton().get(listTextBefore.get(i)).setText(listTextAfter.get(i));
    }
  }

  private void updateTfDisplay() {
    JTextField tfInput = view.getTfInput();
    JTextField tfDisplay = view.getTfDisplay();
    String currentExpression = model.getExpressionOnString();
    if (currentExpression.isEmpty()) {
      tfDisplay.setText(tfInput.getText());
    } else {
      tfDisplay.setText(currentExpression + " " + tfInput.getText());
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
