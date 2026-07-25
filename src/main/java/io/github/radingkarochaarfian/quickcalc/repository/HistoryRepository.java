package io.github.radingkarochaarfian.quickcalc.repository;

import java.util.List;

import io.github.radingkarochaarfian.quickcalc.model.HistoryModel.HistoryEntry;

public interface HistoryRepository {
  int save(String expression, String result);

  void deleteById(int id);

  List<HistoryEntry> loadAllEntry();

  void deleteAll();
}
