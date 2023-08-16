package me.math3w.bazaar.utils;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.text.DecimalFormat;

public class Utils {
    private Utils() {
        throw new IllegalStateException("Utility class cannot be instantiated");
    }

    public static String colorize(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public static ItemStack getGlowedItem(ItemStack item) {
        ItemStack glowedItem = item.clone();

        glowedItem.addUnsafeEnchantment(Enchantment.DURABILITY, 1);

        ItemMeta itemMeta = glowedItem.getItemMeta();
        itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        glowedItem.setItemMeta(itemMeta);

        return glowedItem;
    }

    public static String getTextPrice(double price) {
        return new DecimalFormat("#.#").format(price);
    }

    public static TextComponent createClickableText(String text, String command) {
        TextComponent component = new TextComponent(text);
        component.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/" + command));
        return component;
    }
}
