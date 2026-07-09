package io.github.radingkarochaarfian.quickcalc.service;

import java.io.BufferedWriter;
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

  private boolean offlineStatus;

  public HistoryBackupService(HistoryRepository historyRepository) {
    historyRepo = historyRepository;
    gson = new GsonBuilder().setPrettyPrinting().create();
    offlineStatus = false;
  }

  private void folderCheck() {
    File folder = new File(FOLDER_BACKUP);
    if (!folder.exists()) {
      folder.mkdirs();
    }
  }

  public void syncLocalToDatabase() {
    File jsonFile = new File(FILE_JSON);
    if (!jsonFile.exists())
      return;
    List<String> listLocalEquation = loadFromJson();
    List<String> listDbEquation = historyRepo.loadAll();
    int sizeListLocal = listLocalEquation.size();
    int sizeListDb = listDbEquation.size();
    if (sizeListLocal > sizeListDb) {
      for (int i = sizeListDb; i < sizeListLocal; i++) {
        String equation = listLocalEquation.get(i);
        String[] splitLine = equation.split(" = ");
        if (splitLine.length == 2) {
          historyRepo.save(splitLine[0].trim(), splitLine[1].trim());
        }
      }
    }
    offlineStatus = false;
  }

  public List<String> loadHistory() {
    if (offlineStatus) {
      return loadFromJson();
    } else {
      return historyRepo.loadAll();
    }
  }

  public void saveHistory(String expression, String result) {
    folderCheck();
    String equation = expression + " = " + result;
    saveToJson(equation);
    saveToCsv(expression, result);
    if (!offlineStatus) {
      historyRepo.save(expression, result);
    }
  }

  private void saveToJson(String equation) {
    folderCheck();
    List<String> listHistory = loadFromJson();
    listHistory.add(equation);
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

  private List<String> loadFromJson() {
    File jsonFile = new File(FILE_JSON);
    if (!jsonFile.exists())
      return new ArrayList<>();
    try (FileReader reader = new FileReader(jsonFile)) {
      Type listType = new TypeToken<ArrayList<String>>() {
      }.getType();
      List<String> listEquation = gson.fromJson(reader, listType);
      return (listEquation != null) ? listEquation : new ArrayList<>();
    } catch (IOException e) {
      JOptionPane.showMessageDialog(
          null,
          "Failed to load json file.",
          "Error",
          JOptionPane.ERROR_MESSAGE);
      return new ArrayList<>();
    }

  }

  private void saveToCsv(String expression, String result) {
    folderCheck();
    File csvFile = new File(FILE_CSV);
    boolean isNewFile = csvFile.exists();
    try (PrintWriter printer = new PrintWriter(new FileWriter(FILE_CSV))) {
      if (isNewFile) {
        printer.println("Expression,Result");
      }
      printer.println(expression.trim() + "," + result.trim());
    } catch (IOException e) {
      JOptionPane.showMessageDialog(
          null,
          "Failed to save csv backup.",
          "Error",
          JOptionPane.ERROR_MESSAGE);
    }
  }

  public void deleteAllHistory() {
    if (!offlineStatus) {
      historyRepo.deleteAll();
    }
    try (FileWriter writer = new FileWriter(FILE_JSON)) {
      gson.toJson(new ArrayList<>(), writer);
    } catch (IOException e) {
      JOptionPane.showMessageDialog(
          null,
          "Failed to delete json backup.",
          "Error",
          JOptionPane.ERROR_MESSAGE);
    }
  }

  public void setOfflineMode(boolean status) {
    offlineStatus = status;
  }

  public boolean isOfflineMode() {
    return offlineStatus;
  }
}
