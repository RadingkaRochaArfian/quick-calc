package io.github.radingkarochaarfian.quickcalc.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.HashMap;
import java.util.List;

import javax.swing.*;
import javax.swing.plaf.basic.BasicSplitPaneUI;
import javax.swing.table.DefaultTableModel;

import com.formdev.flatlaf.extras.FlatSVGIcon;

public class CalculatorView extends JFrame {
  private final int MIN_WIDTH = 330;
  private final int MIN_HEIGHT = 360;
  private JTextField tfInput;
  private HashMap<String, JButton> mapButton;

  private boolean isHistoryOpen;
  private JPanel pHistory;

  private JSplitPane spMain;
  private JTable tHistory;
  private DefaultTableModel tModelHistory;
  private JTextField tfDisplay;

  private JMenuBar mbMain;
  private HashMap<String, JMenuItem> mapMenuItem;

  public CalculatorView() {
    setMinimumSize(new Dimension(MIN_WIDTH, MIN_HEIGHT));
    setLayout(new BorderLayout(10, 10));
    setComponent();
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setTitle("0 - QuickCalc");
    getRootPane().setFocusable(true);
    setLocationRelativeTo(null);
    addComponentListener(new ComponentAdapter() {
      public void componentResized(ComponentEvent e) {
        updateButtonFontSize();
      }
    });
  }

  private void setComponent() {
    JPanel pMain = new JPanel(new BorderLayout(10, 10));
    pHistory = new JPanel(new BorderLayout(10, 10));
    pMain.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    pHistory.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    pHistory.setVisible(false);
    mapButton = new HashMap<>();
    setNorthComponentMain(pMain);
    setSouthComponentMain(pMain);
    setCenterComponentHistory(pHistory);
    setSouthComponentHistory(pHistory);
    setNorthComponentHistory(pHistory);
    spMain = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, pMain, pHistory);
    spMain.setDividerLocation(1.0);
    spMain.setContinuousLayout(true);
    add(spMain, BorderLayout.CENTER);
    setSpMainDivider();
    setMenuComponent();
    setAppIcon();
  }

  private void setAppIcon() {
    try {
      FlatSVGIcon appIcon = new FlatSVGIcon("icons/AppIcon.svg");
      setIconImage(appIcon.getImage());
    } catch (Exception e) {
      JOptionPane.showMessageDialog(
          this,
          "Failed to load app icon.",
          "Error",
          JOptionPane.ERROR_MESSAGE);
    }
  }

  private void setSpMainDivider() {
    if (spMain.getUI() instanceof BasicSplitPaneUI) {
      BasicSplitPaneUI ui = (BasicSplitPaneUI) spMain.getUI();
      ui.getDivider().setEnabled(false);
    }
  }

  private void setMenuComponent() {
    mbMain = new JMenuBar();
    mbMain.setVisible(false);
    setJMenuBar(mbMain);
    mapMenuItem = new HashMap<>();
    setUserMenuItem();
    setUtilityMenuItem();
    setAboutMenuItem();
  }

  private void setAboutMenuItem() {
    JMenu mAbout = new JMenu("Help");
    JMenuItem miShortcut = new JMenuItem("Shortcut");
    mbMain.add(mAbout);
    mAbout.add(miShortcut);
    mapMenuItem.put("SHORTCUT", mAbout);
  }

  private void setUserMenuItem() {
    JMenu mUser = new JMenu("User");
    mbMain.add(mUser);
    JMenuItem miResetDbCred = new JMenuItem("Reset Database Credential");
    mUser.add(miResetDbCred);
    mapMenuItem.put("RESET_DB_CREDENTIALS", miResetDbCred);
    JCheckBoxMenuItem miOfflineOnly = new JCheckBoxMenuItem("Use Offline Only");
    mUser.add(miOfflineOnly);
    mapMenuItem.put("USE_OFFLINE_ONLY", miOfflineOnly);
  }

  private void setUtilityMenuItem() {
    JMenu mUtility = new JMenu("Utility");
    JMenuItem miClearAll = new JMenuItem("Clear All");
    mbMain.add(mUtility);
    mUtility.add(miClearAll);
    mapMenuItem.put("CLEAR_ALL", miClearAll);
  }

  private void setCenterComponentHistory(JPanel panel) {
    tModelHistory = new DefaultTableModel(new Object[] { "Operation History" }, 0) {
      public boolean isCellEditable(int row, int column) {
        return false;
      }
    };
    tHistory = new JTable(tModelHistory);
    tHistory.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    JScrollPane spHistory = new JScrollPane(tHistory);
    panel.add(spHistory, BorderLayout.CENTER);
  }

  private void setSouthComponentHistory(JPanel panel) {
    JPanel pSouth = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
    for (String s : List.of("←", "→", "Clear", "Restore", "Delete")) {
      JButton btn = new JButton(s);
      btn.setFocusable(false);
      pSouth.add(btn);
      mapButton.put(s, btn);
    }
    panel.add(pSouth, BorderLayout.SOUTH);
  }

  private void setNorthComponentHistory(JPanel panel) {
    tfDisplay = new JTextField("0");
    tfDisplay.setEditable(false);
    panel.add(tfDisplay, BorderLayout.NORTH);
  }

  private void setNorthComponentMain(JPanel panel) {
    tfInput = new JTextField();
    tfInput.setText("0");
    tfInput.setHorizontalAlignment(JTextField.RIGHT);
    tfInput.setFont(new Font("Arial", Font.BOLD, 25));
    tfInput.setBackground(new Color(240, 240, 240));
    tfInput.setEditable(false);
    JPanel northPanel = new JPanel();
    JPanel northPanelWest = new JPanel();
    northPanel.setLayout(new BorderLayout(10, 10));
    northPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    northPanelWest.setLayout(new BorderLayout(0, 3));
    for (String s : List.of("▲", "▼")) {
      JButton btn = new JButton(s);
      if (s.equals("▲"))
        northPanelWest.add(btn, BorderLayout.NORTH);
      else
        northPanelWest.add(btn, BorderLayout.SOUTH);
      mapButton.put(s, btn);

    }
    northPanel.add(northPanelWest, BorderLayout.WEST);
    northPanel.add(tfInput, BorderLayout.CENTER);
    panel.add(northPanel, BorderLayout.NORTH);
  }

  private void setSouthComponentMain(JPanel panel) {
    JPanel region = new JPanel(new BorderLayout());
    JPanel southPanel = new JPanel(new GridBagLayout());
    region.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
    region.add(southPanel, BorderLayout.CENTER);
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.fill = GridBagConstraints.BOTH;
    gbc.weightx = 1.0;
    gbc.weighty = 1.0;
    String[][] listButtonText = {
        { "%", "÷", "×", "-", "C" },
        { "7", "8", "9", "+", "AC" },
        { "4", "5", "6", "H", "(" },
        { "1", "2", "3", "=", ")" },
        { "0", "", ".", "", "+/-" }
    };
    for (int i = 0; i < 5; i++) {
      for (int j = 0; j < 5; j++) {
        if (listButtonText[i][j].isEmpty()) {
          continue;
        }
        gbc.gridx = j;
        gbc.gridy = i;
        if (j == 4) {
          gbc.insets = new Insets(2, 5, 2, 2);
        } else {
          gbc.insets = new Insets(2, 2, 2, 2);
        }
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        if (listButtonText[i][j].equals("=")) {
          gbc.gridheight = 2;
        } else if (listButtonText[i][j].equals("0")) {
          gbc.gridwidth = 2;
        } else {
        }
        JButton button = new JButton(listButtonText[i][j]);
        button.setFont(new Font("Roboto", Font.BOLD, 13));
        button.setFocusable(false);
        button.setMargin(new Insets(0, 0, 0, 0));
        button.setMinimumSize(new Dimension(30, 30));
        southPanel.add(button, gbc);
        mapButton.put(listButtonText[i][j], button);
      }
    }
    panel.add(region, BorderLayout.CENTER);
  }

  public void toggleHistoryPanel() {
    int currentWidth = getWidth();
    int currentHeight = getHeight();
    if (isHistoryOpen) {
      pHistory.setVisible(false);
      int targetWidth = Math.max(currentWidth / 2, MIN_WIDTH);
      setSize(targetWidth, currentHeight);
      isHistoryOpen = false;
    } else {
      pHistory.setVisible(true);
      double maxScreenWidth = getGraphicsConfiguration().getBounds().getWidth();
      int targetWidth = Math.min((int) maxScreenWidth, currentWidth * 2);
      setSize(targetWidth, currentHeight);
      SwingUtilities.invokeLater(() -> {
        spMain.setDividerLocation(0.5);
      });
      isHistoryOpen = true;
    }
  }

  private void updateButtonFontSize() {
    float newSize = Math.max(13f, getWidth() / 50f);
    for (JButton btn : mapButton.values()) {
      btn.setFont(btn.getFont().deriveFont(newSize));
    }
  }

  public HashMap<String, JButton> getMapButton() {
    return mapButton;
  }

  public JTextField getTfInput() {
    return tfInput;
  }

  public JTextField getTfDisplay() {
    return tfDisplay;
  }

  public JTable getTHistory() {
    return tHistory;
  }

  public DefaultTableModel getTModelHistory() {
    return tModelHistory;
  }

  public JSplitPane getSpMain() {
    return spMain;
  }

  public HashMap<String, JMenuItem> getMapMenuItem() {
    return mapMenuItem;
  }

  public JMenuBar getMbMain() {
    return mbMain;
  }
}
