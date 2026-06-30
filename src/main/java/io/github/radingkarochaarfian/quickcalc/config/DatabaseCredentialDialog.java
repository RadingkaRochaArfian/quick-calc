package io.github.radingkarochaarfian.quickcalc.config;

import java.awt.GridLayout;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class DatabaseCredentialDialog {
  private final DatabaseConfig dbConfig;
  private JTextField tfUser;
  private JPasswordField tfPass;
  private JCheckBox cbRemember;

  public DatabaseCredentialDialog(DatabaseConfig iDbConfig) {
    dbConfig = iDbConfig;
    initComponent();
  }

  private void initComponent() {
    tfUser = new JTextField(dbConfig.getUsername());
    tfPass = new JPasswordField(dbConfig.getPassword());
    cbRemember = new JCheckBox("Remember Password");
    cbRemember.setSelected(true);
  }

  public boolean showDialog() {
    JPanel pInput = new JPanel(new GridLayout(3, 2, 5, 5));
    pInput.add(new JLabel("SQL Server Username:"));
    pInput.add(tfUser);
    pInput.add(new JLabel("SQL Server Password"));
    pInput.add(tfPass);
    pInput.add(new JLabel(""));
    pInput.add(cbRemember);
    int selectedChoice = JOptionPane.showConfirmDialog(
        null,
        pInput,
        "Please insert SQL Server Credentials",
        JOptionPane.OK_CANCEL_OPTION,
        JOptionPane.QUESTION_MESSAGE);
    return selectedChoice == JOptionPane.OK_OPTION;
  }

  public String getUsernameInput() {
    return tfUser.getText().trim();
  }

  public String getPasswordInput() {
    return new String(tfPass.getPassword());
  }

  public boolean isRememberChecked() {
    return cbRemember.isSelected();
  }
}
