package me.math3w.bazaar.menu;

import me.math3w.bazaar.api.BazaarAPI;
import me.math3w.bazaar.api.bazaar.Bazaar;
import me.math3w.bazaar.api.menu.ConfigurableMenuItem;
import me.math3w.bazaar.api.menu.MenuInfo;
import me.math3w.bazaar.utils.MenuUtils;
import me.zort.containr.Component;
import me.zort.containr.ContainerComponent;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class DefaultConfigurableMenuItem implements ConfigurableMenuItem {
    private int slot;
    private ItemStack item;
    private String action;

    public DefaultConfigurableMenuItem(int slot, ItemStack item, String action) {
        this.slot = slot;
        this.item = item;
        this.action = action;
    }

    public static DefaultConfigurableMenuItem deserialize(Map<String, Object> args) {
        return new DefaultConfigurableMenuItem((Integer) args.get("slot"),
                (ItemStack) args.get("item"),
                (String) args.get("action"));
    }

    @Override
    public int getSlot() {
        return slot;
    }

    @Override
    public void setSlot(Bazaar bazaar, int slot) {
        this.slot = slot;
        bazaar.saveConfig();
    }

    @Override
    public ItemStack getItem() {
        return item;
    }

    @Override
    public void setItem(Bazaar bazaar, ItemStack item) {
        this.item = item.clone();
        bazaar.saveConfig();
    }

    @Override
    public String getAction() {
        return action;
    }

    @Override
    public void setAction(Bazaar bazaar, String action) {
        this.action = action;
        bazaar.saveConfig();
    }

    @Override
    public void putItem(ContainerComponent containerComponent, BazaarAPI bazaarApi, Player player, MenuInfo info, boolean editMenu) {
        containerComponent.setElement(slot, Component.element(bazaarApi.getItemPlaceholders().replaceItemPlaceholders(containerComponent, MenuUtils.appendEditLore(item, editMenu), slot, player, info))
                .click(bazaarApi.getClickActionManager().getClickAction(this, info, editMenu))
                .build());
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
