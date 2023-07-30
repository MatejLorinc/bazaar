package me.math3w.bazaar.config;

import me.math3w.bazaar.api.config.MenuConfig;
import me.math3w.bazaar.api.config.MessagePlaceholder;
import me.math3w.bazaar.utils.Utils;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class DefaultMenuConfig extends CustomConfig implements MenuConfig {
    public DefaultMenuConfig(JavaPlugin plugin) {
        super(plugin, "menu");
    }

    public static List<String> formatStringList(List<String> strings, MessagePlaceholder... placeholders) {
        for (MessagePlaceholder placeholder : placeholders) {
            strings.replaceAll(string -> Utils.colorize(string.replaceAll("%" + placeholder.getPlaceholder() + "%", placeholder.getValue())));
        }

        return strings;
    }

    @Override
    protected void addDefaults() {
        addDefault("productcategory-lore", Collections.singletonList("&8%products% products"));
        addDefault("product-lore", Arrays.asList("&7Buy price: &6%buy-price% coins", "&7Sell price: &6%sell-price% coins"));
    }

    private List<String> getLorePlaceholder(String path, MessagePlaceholder... placeholders) {
        return formatStringList(getConfig().getStringList(path), placeholders);
    }

    @Override
    public ItemStack replaceLorePlaceholders(ItemStack icon, String placeholder, MessagePlaceholder... innerPlaceholders) {
        ItemStack newIcon = icon.clone();
        ItemMeta itemMeta = icon.getItemMeta();
        List<String> lore = new ArrayList<>(itemMeta.getLore());

        List<String> placeholderLore = this.getLorePlaceholder(placeholder, innerPlaceholders);

        for (int i = 0; i < lore.size(); i++) {
            String line = lore.get(i);
            if (!line.equals("%" + placeholder + "%")) continue;
            lore.remove(i);

            for (int j = 0; j < placeholderLore.size(); j++) {
                String placeholderLine = placeholderLore.get(j);
                lore.add(i + j, placeholderLine);
            }

        }

        itemMeta.setLore(lore);
        newIcon.setItemMeta(itemMeta);

        return newIcon;
    }
}
