package io.github.radingkarochaarfian.quickcalc.repository;

import java.util.List;

import io.github.radingkarochaarfian.quickcalc.model.HistoryModel.HistoryEntry;

public interface HistoryRepository {
  void save(String expression, String result);

  List<HistoryEntry> loadAllEntry();

  void deleteAll();
}
