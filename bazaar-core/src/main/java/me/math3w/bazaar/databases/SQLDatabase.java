package me.math3w.bazaar.databases;

import java.sql.Connection;

public interface SQLDatabase {
    Connection getConnection();

    boolean isConnected();

    Type getType();

    enum Type {
        SQLITE("file", SQLiteDatabase.class),
        MYSQL("mysql", MySQLDatabase.class);

        private final String configName;
        private final Class<? extends SQLDatabase> databaseClass;

        Type(String configName, Class<? extends SQLDatabase> databaseClass) {
            this.configName = configName;
            this.databaseClass = databaseClass;
        }

        public String getConfigName() {
            return configName;
        }

        public Class<? extends SQLDatabase> getDatabaseClass() {
            return databaseClass;
        }
    }
}
