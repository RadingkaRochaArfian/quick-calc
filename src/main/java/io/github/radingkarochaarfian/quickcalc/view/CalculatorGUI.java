package io.github.radingkarochaarfian.quickcalc.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.HashMap;

import javax.swing.*;

public class CalculatorGUI extends JFrame {
  private final int MIN_WIDTH = 290;
  private final int MIN_HEIGHT = 330;
  private JTextField tfInput;
  private JButton bUp;
  private JButton bDown;
  private HashMap<String, JButton> mapButton;

  public CalculatorGUI() {
    setMinimumSize(new Dimension(MIN_WIDTH, MIN_HEIGHT));
    setLayout(new BorderLayout(10, 10));
    setNorthComponent();
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setSouthComponent();
    setTitle("0 - QuickCalc");
    getRootPane().setFocusable(true);
  }

  private void setNorthComponent() {
    tfInput = new JTextField();
    tfInput.setText("0");
    tfInput.setHorizontalAlignment(JTextField.RIGHT);
    tfInput.setFont(new Font("Arial", Font.BOLD, 25));
    tfInput.setBackground(new Color(240, 240, 240));
    bUp = new JButton("▲");
    bDown = new JButton("▼");
    JPanel northPanel = new JPanel();
    JPanel northPanelWest = new JPanel();
    northPanel.setLayout(new BorderLayout(10, 10));
    northPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    northPanelWest.setLayout(new BorderLayout(0, 3));
    northPanelWest.add(bUp, BorderLayout.NORTH);
    northPanelWest.add(bDown, BorderLayout.SOUTH);
    northPanel.add(northPanelWest, BorderLayout.WEST);
    northPanel.add(tfInput, BorderLayout.CENTER);
    add(northPanel, BorderLayout.NORTH);
  }

  private void setSouthComponent() {
    JPanel region = new JPanel(new BorderLayout());
    JPanel southPanel = new JPanel(new GridBagLayout());
    region.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
    region.add(southPanel, BorderLayout.CENTER);
    add(region, BorderLayout.CENTER);
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.fill = GridBagConstraints.BOTH;
    mapButton = new HashMap<>();
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
        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
        button.setFocusable(false);
        southPanel.add(button, gbc);
        mapButton.put(listButtonText[i][j], button);
      }
    }
  }

  public JButton getBUp() {
    return bUp;
  }

  public JButton getBDown() {
    return bDown;
  }

  public HashMap<String, JButton> getMapButton() {
    return mapButton;
  }

  public JTextField getTfInput() {
    return tfInput;
  }
}
