package io.github.radingkarochaarfian.quickcalc.service;

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

import io.github.radingkarochaarfian.quickcalc.model.HistoryModel.HistoryEntry;
import io.github.radingkarochaarfian.quickcalc.repository.HistoryRepository;
import io.github.radingkarochaarfian.quickcalc.util.CalculatorUtils;

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
    try {
      File folder = new File(FOLDER_BACKUP);
      if (!folder.exists()) {
        folder.mkdirs();
      }
      File jsonFile = new File(FILE_JSON);
      if (!jsonFile.exists()) {
        jsonFile.createNewFile();
      }
      File csvFile = new File(FILE_CSV);
      if (!csvFile.exists()) {
        csvFile.createNewFile();
      }
    } catch (IOException e) {
      showError("Failed to create backup file(s)");
    }
  }

  public void syncLocalToDatabase() {
    if (offlineStatus)
      return;
    List<HistoryEntry> listLocal = loadFromJson();
    List<HistoryEntry> listFullUpdated = new ArrayList<>();
    boolean updatedStatus = false;
    for (HistoryEntry entry : listLocal) {
      if (entry.getId() == -1) {
        int generatedId = historyRepo.save(entry.getExpression(), entry.getResult());
        if (generatedId != -1) {
          HistoryEntry syncEntry = new HistoryEntry(
              generatedId,
              entry.getExpression(),
              entry.getResult(),
              entry.getListHistoryInput());
          listFullUpdated.add(syncEntry);
          updatedStatus = true;
        } else {
          listFullUpdated.add(entry);
        }
      } else {
        listFullUpdated.add(entry);
      }
    }
    if (updatedStatus) {
      saveToJson(listFullUpdated);
    }
  }

  public List<HistoryEntry> loadHistory() {
    if (offlineStatus) {
      return loadFromJson();
    } else {
      List<HistoryEntry> lHistoryEntry = historyRepo.loadAllEntry();
      if (!lHistoryEntry.isEmpty()) {
        saveToJson(lHistoryEntry);
      }
      return lHistoryEntry;
    }
  }

  public void saveHistory(String expression, String result) {
    folderCheck();
    int id = -1;
    if (!offlineStatus) {
      id = historyRepo.save(expression, result);
    }
    List<String> listInput = CalculatorUtils.parseToInput(CalculatorUtils.getEquation(expression, result));
    HistoryEntry entry = new HistoryEntry(id, expression, result, listInput);
    saveToJson(entry);
    saveToCsv(expression, result);
  }

  private void saveToJson(HistoryEntry entry) {
    folderCheck();
    List<HistoryEntry> lHistoryEntry = loadFromJson();
    lHistoryEntry.add(entry);
    saveToJson(lHistoryEntry);
  }

  private void saveToJson(List<HistoryEntry> lHistoryEntry) {
    folderCheck();
    try (FileWriter writer = new FileWriter(FILE_JSON)) {
      gson.toJson(lHistoryEntry, writer);
    } catch (IOException e) {
      showError("Failed to save json backup.");
    }
  }

  private List<HistoryEntry> loadFromJson() {
    File jsonFile = new File(FILE_JSON);
    if (!jsonFile.exists())
      return new ArrayList<>();
    try (FileReader reader = new FileReader(jsonFile)) {
      Type listType = new TypeToken<ArrayList<HistoryEntry>>() {
      }.getType();
      List<HistoryEntry> listEquation = gson.fromJson(reader, listType);
      return (listEquation != null) ? listEquation : new ArrayList<>();
    } catch (IOException e) {
      showError("Failed to load json file.");
      return new ArrayList<>();
    }

  }

  private void saveToCsv(String expression, String result) {
    folderCheck();
    File csvFile = new File(FILE_CSV);
    boolean isNewFile = !csvFile.exists();
    try (PrintWriter printer = new PrintWriter(new FileWriter(FILE_CSV, true))) {
      if (isNewFile) {
        printer.println("Expression,Result");
      }
      printer.println(expression.trim() + "," + result.trim());
    } catch (IOException e) {
      showError("Failed to save csv backup.");
    }
  }

  public void deleteAllHistoryBackup() {
    if (!offlineStatus) {
      historyRepo.deleteAll();
    }
    saveToJson(new ArrayList<>());
  }

  public void deleteHistoryBackupAt(int idx, int id) {
    if (!offlineStatus && id != -1) {
      historyRepo.deleteById(id);
    }
    List<HistoryEntry> lHistoryEntry = loadFromJson();
    if (idx >= 0 && idx < lHistoryEntry.size()) {
      lHistoryEntry.remove(idx);
      saveToJson(lHistoryEntry);
    }
  }

  public void setOfflineMode(boolean status) {
    offlineStatus = status;
  }

  public boolean isOfflineMode() {
    return offlineStatus;
  }

  private void showError(String message) {
    JOptionPane.showMessageDialog(
        null,
        message,
        "Error",
        JOptionPane.ERROR_MESSAGE);
  }
}
