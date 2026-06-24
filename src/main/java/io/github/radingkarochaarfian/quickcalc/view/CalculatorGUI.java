package io.github.radingkarochaarfian.quickcalc.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.*;

public class CalculatorGUI extends JFrame {
  private final int MIN_WIDTH = 270;
  private final int MIN_HEIGHT = 300;
  private JTextField tfInput;
  private JButton bUp;
  private JButton bDown;

  private JButton bMod;
  private JButton bDiv;
  private JButton bMult;
  private JButton bSub;
  private JButton bAdd;
  private JButton bEqual;
  private JButton bOpen;
  private JButton bClose;
  private JButton bPlusMin;
  private JButton bClear;
  private JButton bAllClear;
  private JButton bHistory;
  private JButton bDot;
  private JButton b1;
  private JButton b2;
  private JButton b3;
  private JButton b4;
  private JButton b5;
  private JButton b6;
  private JButton b7;
  private JButton b8;
  private JButton b9;
  private JButton b0;

  public CalculatorGUI() {
    setMinimumSize(new Dimension(MIN_WIDTH, MIN_HEIGHT));
    setLayout(new BorderLayout(10, 10));
    setNorthComponent();
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setSouthComponent();
    setTitle("0 - QuickCalc");
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
    JPanel southPanel = new JPanel(new GridBagLayout());
    JPanel region = new JPanel(new BorderLayout());
    region.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
    region.add(southPanel, BorderLayout.CENTER);
    add(region, BorderLayout.CENTER);
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.fill = GridBagConstraints.BOTH;
    gbc.weightx = 1.0;
    gbc.weighty = 1.0;
    gbc.insets = new Insets(2, 2, 2, 2);

    bMod = new JButton("%");
    bDiv = new JButton("÷");
    bMult = new JButton("×");
    bSub = new JButton("−");
    bClear = new JButton("C");
    b7 = new JButton("7");
    b8 = new JButton("8");
    b9 = new JButton("9");
    bAdd = new JButton("+");
    bAllClear = new JButton("AC");
    b4 = new JButton("4");
    b5 = new JButton("5");
    b6 = new JButton("6");
    bHistory = new JButton("H");
    bOpen = new JButton("(");
    b1 = new JButton("1");
    b2 = new JButton("2");
    b3 = new JButton("3");
    bEqual = new JButton("=");
    bClose = new JButton(")");
    b0 = new JButton("0");
    bDot = new JButton(".");
    bPlusMin = new JButton("+/-");

    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.gridwidth = 1;
    gbc.gridheight = 1;
    southPanel.add(bMod, gbc);

    gbc.gridx = 1;
    gbc.gridy = 0;
    gbc.gridwidth = 1;
    gbc.gridheight = 1;
    southPanel.add(bDiv, gbc);

    gbc.gridx = 2;
    gbc.gridy = 0;
    gbc.gridwidth = 1;
    gbc.gridheight = 1;
    southPanel.add(bMult, gbc);

    gbc.gridx = 3;
    gbc.gridy = 0;
    gbc.gridwidth = 1;
    gbc.gridheight = 1;
    southPanel.add(bSub, gbc);

    gbc.insets = new Insets(2, 5, 2, 2);
    gbc.gridx = 4;
    gbc.gridy = 0;
    gbc.gridwidth = 1;
    gbc.gridheight = 1;
    southPanel.add(bClear, gbc);

    gbc.insets = new Insets(2, 2, 2, 2);
    gbc.gridx = 0;
    gbc.gridy = 1;
    gbc.gridwidth = 1;
    gbc.gridheight = 1;
    southPanel.add(b7, gbc);

    gbc.gridx = 1;
    gbc.gridy = 1;
    gbc.gridwidth = 1;
    gbc.gridheight = 1;
    southPanel.add(b8, gbc);

    gbc.gridx = 2;
    gbc.gridy = 1;
    gbc.gridwidth = 1;
    gbc.gridheight = 1;
    southPanel.add(b9, gbc);

    gbc.gridx = 3;
    gbc.gridy = 1;
    gbc.gridwidth = 1;
    gbc.gridheight = 1;
    southPanel.add(bAdd, gbc);

    gbc.insets = new Insets(2, 5, 2, 2);
    gbc.gridx = 4;
    gbc.gridy = 1;
    gbc.gridwidth = 1;
    gbc.gridheight = 1;
    southPanel.add(bAllClear, gbc);

    gbc.insets = new Insets(2, 2, 2, 2);
    gbc.gridx = 0;
    gbc.gridy = 2;
    gbc.gridwidth = 1;
    gbc.gridheight = 1;
    southPanel.add(b4, gbc);

    gbc.gridx = 1;
    gbc.gridy = 2;
    gbc.gridwidth = 1;
    gbc.gridheight = 1;
    southPanel.add(b5, gbc);

    gbc.gridx = 2;
    gbc.gridy = 2;
    gbc.gridwidth = 1;
    gbc.gridheight = 1;
    southPanel.add(b6, gbc);

    gbc.gridx = 3;
    gbc.gridy = 2;
    gbc.gridwidth = 1;
    gbc.gridheight = 1;
    southPanel.add(bHistory, gbc);

    gbc.insets = new Insets(2, 5, 2, 2);
    gbc.gridx = 4;
    gbc.gridy = 2;
    gbc.gridwidth = 1;
    gbc.gridheight = 1;
    southPanel.add(bOpen, gbc);

    gbc.insets = new Insets(2, 2, 2, 2);
    gbc.gridx = 0;
    gbc.gridy = 3;
    gbc.gridwidth = 1;
    gbc.gridheight = 1;
    southPanel.add(b1, gbc);

    gbc.gridx = 1;
    gbc.gridy = 3;
    gbc.gridwidth = 1;
    gbc.gridheight = 1;
    southPanel.add(b2, gbc);

    gbc.gridx = 2;
    gbc.gridy = 3;
    gbc.gridwidth = 1;
    gbc.gridheight = 1;
    southPanel.add(b3, gbc);

    gbc.gridx = 3;
    gbc.gridy = 3;
    gbc.gridwidth = 1;
    gbc.gridheight = 2;
    southPanel.add(bEqual, gbc);

    gbc.insets = new Insets(2, 5, 2, 2);
    gbc.gridx = 4;
    gbc.gridy = 3;
    gbc.gridwidth = 1;
    gbc.gridheight = 1;
    southPanel.add(bClose, gbc);

    gbc.insets = new Insets(2, 2, 2, 2);
    gbc.gridx = 0;
    gbc.gridy = 4;
    gbc.gridwidth = 2;
    gbc.gridheight = 1;
    southPanel.add(b0, gbc);

    gbc.gridx = 2;
    gbc.gridy = 4;
    gbc.gridwidth = 1;
    gbc.gridheight = 1;
    southPanel.add(bDot, gbc);

    gbc.insets = new Insets(2, 5, 2, 2);
    gbc.gridx = 4;
    gbc.gridy = 4;
    gbc.gridwidth = 1;
    gbc.gridheight = 1;
    southPanel.add(bPlusMin, gbc);

  }
}
