package me.math3w.bazaar.menu;

import me.math3w.bazaar.api.menu.ClickActionManager;
import me.zort.containr.Component;
import me.zort.containr.ContainerComponent;
import me.zort.containr.builder.SimpleGUIBuilder;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class MenuConfiguration implements ConfigurationSerializable {
    protected final String name;
    protected final int rows;
    protected final List<ConfigurableMenuItem> items;

    public MenuConfiguration(String name, int rows, List<ConfigurableMenuItem> items) {
        this.name = name;
        this.rows = rows;
        this.items = items;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> args = new HashMap<>();
        args.put("name", name);
        args.put("rows", rows);
        args.put("items", items);
        return args;
    }

    protected SimpleGUIBuilder getMenuBuilder() {
        return Component.gui()
                .title(name)
                .rows(rows);
    }

    public String getName() {
        return name;
    }

    public int getRows() {
        return rows;
    }

    public List<ConfigurableMenuItem> getItems() {
        return items;
    }

    protected void loadItems(ContainerComponent containerComponent, ClickActionManager clickActionManager) {
        for (ConfigurableMenuItem item : items) {
            item.setItem(containerComponent, clickActionManager);
        }
    }
}
