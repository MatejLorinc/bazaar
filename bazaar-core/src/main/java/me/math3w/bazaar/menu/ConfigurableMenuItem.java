package me.math3w.bazaar.menu;

import me.math3w.bazaar.api.BazaarAPI;
import me.math3w.bazaar.api.menu.MenuInfo;
import me.zort.containr.Component;
import me.zort.containr.ContainerComponent;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class ConfigurableMenuItem implements ConfigurationSerializable {
    private final int slot;
    private final ItemStack item;
    private final String action;

    public ConfigurableMenuItem(int slot, ItemStack item, String action) {
        this.slot = slot;
        this.item = item;
        this.action = action;
    }

    public static ConfigurableMenuItem deserialize(Map<String, Object> args) {
        return new ConfigurableMenuItem((Integer) args.get("slot"),
                (ItemStack) args.get("item"),
                (String) args.get("action"));
    }

    public int getSlot() {
        return slot;
    }

    public ItemStack getItem() {
        return item;
    }

    public String getAction() {
        return action;
    }

    public void setItem(ContainerComponent containerComponent, BazaarAPI bazaarApi, Player player, MenuInfo info) {
        containerComponent.setElement(slot, Component.element(bazaarApi.getItemPlaceholders().replaceItemPlaceholders(item, player, info)).click(bazaarApi.getClickActionManager().getClickAction(action)).build());
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> args = new HashMap<>();
        args.put("slot", slot);
        args.put("item", item);
        args.put("action", action);
        return args;
    }
}
