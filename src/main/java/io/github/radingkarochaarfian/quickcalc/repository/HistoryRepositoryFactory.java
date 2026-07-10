package io.github.radingkarochaarfian.quickcalc.repository;

import io.github.radingkarochaarfian.quickcalc.config.DatabaseConfig;

public class HistoryRepositoryFactory {
  public static HistoryRepository getRepository(DatabaseConfig dbConfig) {
    try {
      String className = dbConfig.getRepoClass();
      Class<?> dynamicClass = Class.forName(className);
      return (HistoryRepository) dynamicClass.getConstructor(DatabaseConfig.class).newInstance(dbConfig);
    } catch (Exception e) {
      throw new RuntimeException("Invalid key or value on repository properties.");
    }
  }
}
