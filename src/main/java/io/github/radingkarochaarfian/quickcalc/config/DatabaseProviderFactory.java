package io.github.radingkarochaarfian.quickcalc.config;

public class DatabaseProviderFactory {
  public static DatabaseProvider getProvider(DatabaseConfig dbConfig) {
    try {
      String className = dbConfig.getProviderClass();
      Class<?> dynamicClass = Class.forName(className);
      return (DatabaseProvider) dynamicClass.getDeclaredConstructor().newInstance();
    } catch (Exception e) {
      return new SqlServerProvider();
    }
  }
}
