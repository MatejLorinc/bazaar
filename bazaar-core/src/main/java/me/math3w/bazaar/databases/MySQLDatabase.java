package me.math3w.bazaar.databases;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQLDatabase implements SQLDatabase {
    private final ConfigurationSection config;
    private Connection connection;

    public MySQLDatabase(ConfigurationSection config) {
        this.config = config;
        connect();
        if (!isConnected()) {
            Bukkit.getLogger().severe("Cannot connect to mysql database! Please check the databases config");
        }
    }

    private void connect() {
        if (isConnected()) return;

        String url = "jdbc:mysql://" + config.getString("hostname") + ":" + config.getInt("port") + "/" + config.getString("database") + "?autoReconnect=true";

        try {
            connection = DriverManager.getConnection(url, config.getString("username"), config.getString("password"));
        } catch (SQLException exception) {
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
        return Type.MYSQL;
    }
}
