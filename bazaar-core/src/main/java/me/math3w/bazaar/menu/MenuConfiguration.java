package me.math3w.bazaar.menu;

import me.math3w.bazaar.api.BazaarAPI;
import me.math3w.bazaar.api.menu.MenuInfo;
import me.zort.containr.Component;
import me.zort.containr.ContainerComponent;
import me.zort.containr.builder.SimpleGUIBuilder;
import me.zort.containr.internal.util.ItemBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

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

    public static void fillWithGlass(int rows, List<ConfigurableMenuItem> items) {
        ItemStack glass = ItemBuilder.newBuilder(Material.STAINED_GLASS_PANE).withData((short) 15).withName(ChatColor.WHITE.toString()).build();
        for (int i = 0; i < rows * 9; i++) {
            int finalI = i;
            if (items.stream().anyMatch(configurableMenuItem -> configurableMenuItem.getSlot() == finalI)) continue;
            items.add(new ConfigurableMenuItem(i, glass, ""));
        }
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

    protected void loadItems(ContainerComponent containerComponent, BazaarAPI bazaarApi, Player player, MenuInfo info) {
        for (ConfigurableMenuItem item : items) {
            item.setItem(containerComponent, bazaarApi, player, info);
        }
    }
}
