package me.math3w.bazaar.databases;

import org.bukkit.Bukkit;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLiteDatabase implements SQLDatabase {
    private final File folder;
    private final String fileName;
    private Connection connection;

    public SQLiteDatabase(File folder, String fileName) {
        this.folder = folder;
        this.fileName = fileName;
        connect();
        if (!isConnected()) {
            Bukkit.getLogger().severe("Cannot connect to sqlite database!");
        }
    }

    public void connect() {
        if (isConnected()) return;

        File file = new File(folder, fileName + ".db");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        String url = "jdbc:sqlite:" + file;

        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection(url);
        } catch (SQLException | ClassNotFoundException exception) {
            throw new RuntimeException(exception);
        }
    }

    @Override
    public Connection getConnection() {
        return connection;
    }

    @Override
    public boolean isConnected() {
        return connection != null;
    }

    @Override
    public Type getType() {
        return Type.SQLITE;
    }
}
