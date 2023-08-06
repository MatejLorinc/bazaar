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
            strings.replaceAll(string -> string.replaceAll("%" + placeholder.getPlaceholder() + "%", placeholder.getValue()));
        }

        strings.replaceAll(Utils::colorize);

        return strings;
    }

    @Override
    protected void addDefaults() {
        addDefault("productcategory-lore", Collections.singletonList("&8%products% products"));
        addDefault("product-lore", Arrays.asList("&7Buy price: &6%buy-price% coins", "&7Sell price: &6%sell-price% coins"));
        addDefault("product", Collections.singletonList("&8%product%"));
        addDefault("sell-inventory", Arrays.asList("%items%", "", "&7You earn: &6%earned-coins% coins", "", "&eClick to sell!"));
        addDefault("sell-inventory-none", Arrays.asList("&cYou don't have anything to", "&csell!"));
        addDefault("item", Collections.singletonList(" &a%item-amount%&7x &f%item-name% &7for &6%item-coins% coins"));
        addDefault("buy-instantly", Arrays.asList("&7Price per unit: &6%buy-price% coins", "&7Stack price: &6%stack-buy-price% coins"));
        addDefault("sell-instantly", Arrays.asList("&7Inventory: &a%item-amount% items", "", "&7Amount: &a%item-amount%&7x", "&7Total: &6%coins% coins"));
        addDefault("buy-order", Collections.singletonList("&8- &6%coins% coins&7 each | &a%item%&7x in &f%orders% orders"));
        addDefault("buy-order-amount-sign", Arrays.asList("", "^^^^^^^^^^^^^^^", "Enter amount", "to order"));
        addDefault("buy-order-price-sign", Arrays.asList("", "^^^^^^^^^^^^^^^", "Enter price", "per unit"));
        addDefault("sell-offer", Collections.singletonList("&8- &6%coins% coins&7 each | &a%item%&7x from &f%offers% offers"));
        addDefault("sell-offer-amount-sign", Arrays.asList("", "^^^^^^^^^^^^^^^", "Enter amount", "to sell"));
        addDefault("sell-offer-price-sign", Arrays.asList("", "^^^^^^^^^^^^^^^", "Enter price", "per unit"));
        addDefault("confirm-lore", Arrays.asList("", "&7Price per unit: &6%unit-price% coins", "", "&7Order: &a%amount%&7x &f%product%", "&7Total price: &6%total-price% coins"));
        addDefault("search-sign", Arrays.asList("", "^^^^^^^^^^^^^^^", "Enter Query", ""));
    }

    @Override
    public List<String> getStringList(String path, MessagePlaceholder... placeholders) {
        return formatStringList(getConfig().getStringList(path), placeholders);
    }

    @Override
    public ItemStack replaceLorePlaceholders(ItemStack icon, String placeholder, MessagePlaceholder... innerPlaceholders) {
        ItemStack newIcon = icon.clone();
        ItemMeta itemMeta = icon.getItemMeta();

        if (itemMeta == null || !itemMeta.hasLore()) return icon;

        List<String> lore = new ArrayList<>(itemMeta.getLore());

        List<String> placeholderLore = this.getStringList(placeholder, innerPlaceholders);

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
