package io.github.radingkarochaarfian.quickcalc.controller.handler;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import io.github.radingkarochaarfian.quickcalc.util.CalculatorUtils;

public class ShortcutMenuItemHandler implements ActionListener {
  private final Component parent;

  public ShortcutMenuItemHandler(Component parent) {
    this.parent = parent;
  }

  public void actionPerformed(ActionEvent e) {
    String shortcutHtml = """
        <html>

        <head>
          <style>
            body {
              width: 320px;
              padding: 5px;
            }

            h3 {
              text-align: center;
            }

            table {
              width: 100%;
              border-spacing: 0 10px;
            }

            .keybind {
              display: inline-block;
              padding: 3px 8px;
              border: 1px solid darkslategrey;
              border-radius: 5px;
              background: ghostwhite;
              font-weight: bold;
            }
          </style>
        </head>

        <body>
          <h3>Action and Input Keys</h3>
          <table>
            <tr>
              <td>Clear (C)</td>
              <td align="right"><span class="keybind">Esc</span></td>
            </tr>
            <tr>
              <td>All Clear (AC)</td>
              <td align="right"><span class="keybind">Del</span></td>
            </tr>
            <tr>
              <td>MOD (%)</td>
              <td align="right"><span class="keybind">Shift</span> + <span class="keybind">5</span></td>
            </tr>
            <tr>
              <td>Divide (/)</td>
              <td align="right"><span class="keybind">/</span></td>
            </tr>
            <tr>
              <td>Multiply (*)</td>
              <td align="right"><span class="keybind">Shift</span> + <span class="keybind">8</span></td>
            </tr>
            <tr>
              <td>Subtract (-)</td>
              <td align="right"><span class="keybind">-</span></td>
            </tr>
            <tr>
              <td>Add (+)</td>
              <td align="right"><span class="keybind">Shift</span> + <span class="keybind">=</span></td>
            </tr>
            <tr>
              <td>Show History Panel (H)</td>
              <td align="right"><span class="keybind">h</span></td>
            </tr>
            <tr>
              <td>Open Parenthesis ("(")</td>
              <td align="right"><span class="keybind">Shift</span> + <span class="keybind">9</span></td>
            </tr>
            <tr>
              <td>Close Parenthesis (")")</td>
              <td align="right"><span class="keybind">Shift</span> + <span class="keybind">0</span></td>
            </tr>
            <tr>
              <td>Modifier (+/-)</td>
              <td align="right"><span class="keybind">\\</span></td>
            </tr>
            <tr>
              <td>Calculate (=)</td>
              <td align="right"><span class="keybind">Enter</span></td>
            </tr>
          </table>
          <h3>Navigation</h3>
          <table>
            <tr>
              <td>Show Keybind on Buttons</td>
              <td align="right"><span class="keybind">Ctrl</span></td>
            </tr>
            <tr>
              <td>Go to previous Expression</td>
              <td align="right"><span class="keybind">↑</span></td>
            </tr>
            <tr>
              <td>Go to next Expression</td>
              <td align="right"><span class="keybind">↓</span></td>
            </tr>
            <tr>
              <td>Go to next Character</td>
              <td align="right"><span class="keybind">→</span></td>
            </tr>
            <tr>
              <td>Go to previous Character</td>
              <td align="right"><span class="keybind">←</span></td>
            </tr>
          </table>
        </body>

        </html>
            """;
    CalculatorUtils.showPlainMessage(parent, shortcutHtml, "Quick Calc Shortcut");
  }
}
