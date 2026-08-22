package io.github.radingkarochaarfian.quickcalc.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.HashMap;
import java.util.List;

import javax.swing.*;
import javax.swing.plaf.basic.BasicSplitPaneUI;
import javax.swing.table.DefaultTableModel;

import com.formdev.flatlaf.extras.FlatSVGIcon;

public class CalculatorView extends JFrame {
  private int minWidth = 490;
  private int minHeight = 540;
  private JTextField tfInput;
  private HashMap<String, JButton> mapButton;

  private boolean isHistoryOpen;
  private JPanel pHistory;
  private JPanel pMain;

  private JSplitPane spMain;
  private JTable tHistory;
  private DefaultTableModel tModelHistory;
  private JTextField tfDisplay;

  private JMenuBar mbMain;
  private HashMap<String, JMenuItem> mapMenuItem;

  public CalculatorView() {
    setMinimumSize(new Dimension(minWidth, minHeight));
    setLayout(new BorderLayout(10, 10));
    setComponent();
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setTitle("0 - QuickCalc");
    getRootPane().setFocusable(true);
    setLocationRelativeTo(null);
    setRootPaneListeners();
  }

  private void setRootPaneListeners() {
    addComponentListener(new ComponentAdapter() {
      public void componentResized(ComponentEvent e) {
        SwingUtilities.invokeLater(() -> {
          if (isHistoryOpen) {
            spMain.setResizeWeight(0.5);
            spMain.setDividerLocation(.5);
          } else {
            spMain.setResizeWeight(1.);
            spMain.setDividerLocation(1.);
          }
          updateUIFonts();
          updateButtonGap();
        });
      }
    });
  }

  private void setComponent() {
    pMain = new JPanel(new BorderLayout(10, 10));
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
    spMain.setResizeWeight(1.);
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
    mapMenuItem.put("SHORTCUT", miShortcut);
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
      btn.setFocusable(false);
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
        gbc.insets = new Insets(2, 2, 2, 2);
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

  public void toggleFullscreen() {
    boolean isMaximized = (getExtendedState() & JFrame.MAXIMIZED_BOTH) == JFrame.MAXIMIZED_BOTH;
    if (isMaximized) {
      setExtendedState(JFrame.NORMAL);
    } else {
      setExtendedState(JFrame.MAXIMIZED_BOTH);
    }
  }

  public void toggleHistoryPanel() {
    boolean isMaximized = (getExtendedState() & JFrame.MAXIMIZED_BOTH) == JFrame.MAXIMIZED_BOTH;
    int currentWidth = getWidth();
    int currentHeight = getHeight();
    if (isHistoryOpen) {
      minWidth /= 2;
      setMinimumSize(new Dimension(minWidth, minHeight));
      pHistory.setVisible(false);
      if (!isMaximized) {
        int targetWidth = Math.max(currentWidth / 2, minWidth);
        setSize(targetWidth, currentHeight);
      }
      isHistoryOpen = false;
      spMain.setDividerLocation(1.0);
    } else {
      minWidth *= 2;
      setMinimumSize(new Dimension(minWidth, minHeight));
      pHistory.setVisible(true);
      double maxScreenWidth = getGraphicsConfiguration().getBounds().getWidth();
      int targetWidth = Math.min((int) maxScreenWidth, currentWidth * 2);
      setSize(targetWidth, currentHeight);
      isHistoryOpen = true;
      SwingUtilities.invokeLater(() -> {
        spMain.setDividerLocation(0.5);
      });
    }
  }

  private void updateUIFonts() {
    int mainWidth = pMain.getWidth();
    int mainHeight = pMain.getHeight();
    int historyWidth = pHistory.getWidth();
    int historyHeight = pHistory.getHeight();
    float mainScale = (float) Math.sqrt(mainWidth * mainHeight);
    float historyScale = (float) Math.sqrt(historyHeight * historyWidth);
    float btnMainSize = Math.max(13f, mainScale / 22f);
    float btnHistorySize = Math.max(11f, historyScale / 24f);
    float tfInputFontSize = Math.max(20, mainScale / 16f);
    float tfDisplayFontSize = Math.max(13f, historyScale / 24f);
    float tHistoryFontSize = Math.max(12f, historyScale / 28f);
    int tHistoryRowHeight = Math.max(22, (int) (tHistoryFontSize * 1.5));
    tfInput.setFont(tfInput.getFont().deriveFont(Font.BOLD, tfInputFontSize));
    tfDisplay.setFont(tfDisplay.getFont().deriveFont(tfDisplayFontSize));
    tHistory.setFont(tHistory.getFont().deriveFont(tHistoryFontSize));
    tHistory.setRowHeight(tHistoryRowHeight);
    tHistory.getTableHeader().setFont(
        tHistory.getTableHeader().getFont().deriveFont(Font.BOLD, tHistoryFontSize));
    List<String> historyButtons = List.of("←", "→", "Clear", "Restore", "Delete");
    for (var entry : mapButton.entrySet()) {
      JButton btn = entry.getValue();
      if (historyButtons.contains(entry.getKey())) {
        btn.setFont(btn.getFont().deriveFont(btnHistorySize));
      } else {
        btn.setFont(btn.getFont().deriveFont(btnMainSize));
      }
    }
  }

  private void updateButtonGap() {
    int mainWidth = pMain.getWidth();
    int mainHeight = pMain.getHeight();
    float mainScale = (float) Math.sqrt(mainWidth * mainHeight);
    int mainGap = Math.max(2, Math.round(mainScale / 90f));
    for (var entry : mapButton.entrySet()) {
      JButton btn = entry.getValue();
      LayoutManager layoutCheck = btn.getParent().getLayout();
      if (layoutCheck instanceof GridBagLayout) {
        GridBagLayout layout = (GridBagLayout) btn.getParent().getLayout();
        GridBagConstraints gbc = layout.getConstraints(btn);
        gbc.insets = new Insets(mainGap, mainGap, mainGap, mainGap);
        layout.setConstraints(btn, gbc);
      }
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
