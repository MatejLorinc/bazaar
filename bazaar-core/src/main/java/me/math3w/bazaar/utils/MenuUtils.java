package me.math3w.bazaar.utils;

import me.math3w.bazaar.api.config.MessagePlaceholder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class MenuUtils {
    private MenuUtils() {
        throw new IllegalStateException("Utility class cannot be instantiated");
    }

    public static ItemStack replaceLorePlaceholders(ItemStack icon, MessagePlaceholder... placeholders) {
        ItemStack newIcon = icon.clone();
        ItemMeta itemMeta = icon.getItemMeta();
        List<String> lore = new ArrayList<>(itemMeta.getLore());

        for (int i = 0; i < lore.size(); i++) {
            String line = lore.get(i);
            for (MessagePlaceholder placeholder : placeholders) {
                line = Utils.colorize(line.replaceAll("%" + placeholder.getPlaceholder() + "%", placeholder.getValue()));
            }
            lore.set(i, line);
        }

        itemMeta.setLore(lore);
        newIcon.setItemMeta(itemMeta);

        return newIcon;
    }
}
