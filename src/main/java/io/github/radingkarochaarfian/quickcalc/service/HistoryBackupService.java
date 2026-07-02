package io.github.radingkarochaarfian.quickcalc.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import io.github.radingkarochaarfian.quickcalc.repository.HistoryRepository;

public class HistoryBackupService {
  private final HistoryRepository historyRepo;
  private final Gson gson;

  private static final String FOLDER_BACKUP = "backup";
  private static final String FILE_CSV = FOLDER_BACKUP + "/history.csv";
  private static final String FILE_JSON = FOLDER_BACKUP + "/history.json";

  public HistoryBackupService(HistoryRepository historyRepository) {
    historyRepo = historyRepository;
    gson = new GsonBuilder().setPrettyPrinting().create();
  }

  private void folderCheck() {
    File folder = new File(FOLDER_BACKUP);
    if (!folder.exists()) {
      folder.mkdirs();
    }
  }

  public void backupToJson() {
    folderCheck();
    List<String> listHistory = historyRepo.loadAll();
    try (FileWriter writer = new FileWriter(FILE_JSON)) {
      gson.toJson(listHistory, writer);
    } catch (IOException e) {
      JOptionPane.showMessageDialog(
          null,
          "Failed to save json backup.",
          "Error",
          JOptionPane.ERROR_MESSAGE);
    }
  }

  public void backupToCsv() {
    folderCheck();
    List<String> listHistory = historyRepo.loadAll();
    try (PrintWriter printer = new PrintWriter(new FileWriter(FILE_CSV))) {
      printer.println("Expression,Result");
      for (String line : listHistory) {
        String[] splitLine = line.split(" ");
        if (splitLine.length == 2) {
          String expression = splitLine[0];
          String result = splitLine[1];
          printer.println(expression + "," + result);
        }
      }
    } catch (IOException e) {
      JOptionPane.showMessageDialog(
          null,
          "Failed to save csv backup.",
          "Error",
          JOptionPane.ERROR_MESSAGE);
    }
  }

  public void restoreFromJson() {
    File file = new File(FILE_JSON);
    if (!file.exists())
      return;
    try (FileReader reader = new FileReader(file)) {
      Type targetType = new TypeToken<ArrayList<String>>() {
      }.getType();
      List<String> listHistory = gson.fromJson(reader, targetType);
      if (listHistory != null) {
        for (String line : listHistory) {
          String[] splitLine = line.split(" = ");
          if (splitLine.length == 2) {
            historyRepo.save(splitLine[0].trim(), splitLine[1].trim(), "");
          }
        }
      }
    } catch (IOException e) {
      JOptionPane.showMessageDialog(
          null,
          "Failed to load json backup",
          "Error",
          JOptionPane.ERROR_MESSAGE);
    }
  }

  public void restoreFromCsv() {
    File file = new File(FILE_CSV);
    if (!file.exists())
      return;
    try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
      String line;
      boolean firstLine = true;
      while ((line = reader.readLine()) != null) {
        if (firstLine) {
          firstLine = false;
          continue;
        }
        String[] splitLine = line.split(" ");
        historyRepo.save(splitLine[0].trim(), splitLine[1].trim(), "");
      }
    } catch (IOException e) {
      JOptionPane.showMessageDialog(
          null,
          "Failed to load csv backup",
          "Error",
          JOptionPane.ERROR_MESSAGE);
    }
  }
}
