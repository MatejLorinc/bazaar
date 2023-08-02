package me.math3w.bazaar.bazaar.product;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class ProductConfiguration implements ConfigurationSerializable {
    private ItemStack item;
    private ItemStack icon;
    private String name;

    public ProductConfiguration(ItemStack item, ItemStack icon, String name) {
        this.item = item;
        this.icon = icon;
        this.name = name;
    }

    public static ProductConfiguration deserialize(Map<String, Object> args) {
        return new ProductConfiguration((ItemStack) args.get("item"),
                (ItemStack) args.get("icon"),
                (String) args.get("name"));
    }

    public ItemStack getItem() {
        return item;
    }

    public void setItem(ItemStack item) {
        this.item = item;
    }

    public ItemStack getIcon() {
        return icon;
    }

    public void setIcon(ItemStack icon) {
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> args = new HashMap<>();
        args.put("item", item);
        args.put("icon", icon);
        args.put("name", name);
        return args;
    }
}
