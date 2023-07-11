package me.math3w.bazaar.config;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

public class CustomConfig {
    private final JavaPlugin plugin;
    private final Map<String, Object> defaults = new LinkedHashMap<>();
    private File file;
    private FileConfiguration config;

    public CustomConfig(JavaPlugin plugin, String name) {
        this.plugin = plugin;
        createCustomConfigPlugin(name);
    }

    protected void createCustomConfigPlugin(String name) {
        String fullName = name + ".yml";
        file = new File(plugin.getDataFolder(), fullName);

        if (!file.exists()) {
            file.getParentFile().mkdirs();
            if (plugin.getResource(fullName) != null) {
                plugin.saveResource(fullName, false);
            }
            config = YamlConfiguration.loadConfiguration(file);
            saveDefault();
            return;
        }

        config = YamlConfiguration.loadConfiguration(file);
        saveDefaults();
    }

    protected void set(String path, Object value) {
        getConfig().set(path, value);
        save();
    }

    public File getFile() {
        return file;
    }

    public FileConfiguration getConfig() {
        return this.config;
    }

    public void save() {
        try {
            config.save(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected void addDefaults() {

    }

    public void addDefault(String path, Object object) {
        defaults.put(path, object);
    }

    private void saveDefaults() {
        addDefaults();
        for (Map.Entry<String, Object> defaultConfig : defaults.entrySet()) {
            String path = defaultConfig.getKey();
            if (getConfig().contains(path)) continue;

            getConfig().set(path, defaultConfig.getValue());
        }
        save();
    }

    protected void saveDefault() {
        saveDefaults();
    }

    public JavaPlugin getPlugin() {
        return plugin;
    }
}
