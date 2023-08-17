package me.math3w.bazaar.utils;

import me.math3w.bazaar.api.bazaar.orders.BazaarOrder;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.text.DecimalFormat;
import java.util.List;

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

    public static double getTotalPrice(List<BazaarOrder> orders, int amount) {
        double price = 0;
        for (int i = 0; i < orders.size(); i++) {
            BazaarOrder order = orders.get(i);

            if (i == orders.size() - 1) {
                price += order.getUnitPrice() * getLastOrderFillAmount(orders, amount);
                break;
            }

            price += order.getUnitPrice() * order.getOrderableItems();
        }

        return price;
    }

    public static int getLastOrderFillAmount(List<BazaarOrder> orders, int amount) {
        int currentAmount = 0;
        for (int i = 0; i < orders.size(); i++) {
            BazaarOrder order = orders.get(i);

            if (i == orders.size() - 1) {
                return Math.min(amount - currentAmount, order.getOrderableItems());
            }

            currentAmount += order.getOrderableItems();
        }

        return orders.get(orders.size() - 1).getOrderableItems();
    }
}
