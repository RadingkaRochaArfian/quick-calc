package io.github.radingkarochaarfian.quickcalc.repository;

import java.util.List;

public interface HistoryRepository {
  void save(String expression, String result);

  List<String> loadAll();

  void deleteAll();
}
