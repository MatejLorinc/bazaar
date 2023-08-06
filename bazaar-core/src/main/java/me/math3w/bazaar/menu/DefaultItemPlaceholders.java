package me.math3w.bazaar.menu;

import me.math3w.bazaar.BazaarPlugin;
import me.math3w.bazaar.api.bazaar.Product;
import me.math3w.bazaar.api.bazaar.orders.BazaarOrder;
import me.math3w.bazaar.api.config.MenuConfig;
import me.math3w.bazaar.api.config.MessagePlaceholder;
import me.math3w.bazaar.api.menu.ItemPlaceholderFunction;
import me.math3w.bazaar.api.menu.ItemPlaceholders;
import me.math3w.bazaar.api.menu.MenuInfo;
import me.math3w.bazaar.utils.MenuUtils;
import me.math3w.bazaar.utils.Utils;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class DefaultItemPlaceholders implements ItemPlaceholders {
    private final BazaarPlugin bazaarPlugin;
    private final Set<ItemPlaceholderFunction> itemPlaceholders = new HashSet<>();

    public DefaultItemPlaceholders(BazaarPlugin bazaarPlugin) {
        this.bazaarPlugin = bazaarPlugin;

        MenuConfig menuConfig = bazaarPlugin.getMenuConfig();

        addItemPlaceholder((item, player, info) -> {
            ItemStack newItem = item.clone();
            ItemMeta itemMeta = newItem.getItemMeta();

            if (itemMeta == null || !itemMeta.hasLore()) return item;

            List<String> lore = new ArrayList<>(itemMeta.getLore());

            String placeholder = "sell-inventory";

            int sellInventoryLineIndex = getPlaceholderLoreLineIndex(lore, placeholder);
            if (sellInventoryLineIndex == -1) return item;

            lore.remove(sellInventoryLineIndex);

            Map<Product, Integer> productsInInventory = bazaarPlugin.getBazaar().getProductsInInventory(player);

            if (productsInInventory.isEmpty()) {
                String noneItemsPlaceholder = "sell-inventory-none";
                lore.add(sellInventoryLineIndex, "%" + noneItemsPlaceholder + "%");
                itemMeta.setLore(lore);
                newItem.setItemMeta(itemMeta);
                return menuConfig.replaceLorePlaceholders(newItem, noneItemsPlaceholder);
            }

            newItem = menuConfig.replaceLorePlaceholders(newItem, placeholder);
            itemMeta = newItem.getItemMeta();
            lore = new ArrayList<>(itemMeta.getLore());

            double totalEarnedCoins = 0;

            int itemsLineIndex = getPlaceholderLoreLineIndex(lore, "items");
            lore.remove(itemsLineIndex);

            int i = 0;
            for (Map.Entry<Product, Integer> productAmountEntry : productsInInventory.entrySet()) {
                Product product = productAmountEntry.getKey();
                int amount = productAmountEntry.getValue();

                //TODO Need to check if there's enough stock to sell

                double totalItemPrice = amount * product.getHighestSellPrice(); //TODO This needs to calculate real price from buy orders
                List<String> itemAsLore = menuConfig.getStringList("item",
                        new MessagePlaceholder("item-amount", String.valueOf(amount)),
                        new MessagePlaceholder("item-name", product.getName()),
                        new MessagePlaceholder("item-coins", Utils.getTextPrice(totalItemPrice)));

                for (int j = 0; j < itemAsLore.size(); j++) {
                    String loreLine = itemAsLore.get(j);
                    lore.add(itemsLineIndex + i + j, loreLine);
                }

                i += itemAsLore.size();

                totalEarnedCoins += totalItemPrice;
            }

            itemMeta.setLore(lore);
            newItem.setItemMeta(itemMeta);

            newItem = MenuUtils.replaceLorePlaceholders(newItem, new MessagePlaceholder("earned-coins", Utils.getTextPrice(totalEarnedCoins)));

            return newItem;
        });

        addItemPlaceholder((item, player, info) -> {
            if (info instanceof Product) {
                Product product = (Product) info;
                return menuConfig.replaceLorePlaceholders(item, "product", new MessagePlaceholder("product", product.getName()));
            } else if (info instanceof BazaarOrder) {
                BazaarOrder order = (BazaarOrder) info;
                return menuConfig.replaceLorePlaceholders(item, "product", new MessagePlaceholder("product", order.getProduct().getName()));
            } else {
                return item;
            }
        });

        addItemPlaceholder((item, player, info) -> {
            if (!(info instanceof Product)) return item;
            Product product = (Product) info;
            return menuConfig.replaceLorePlaceholders(item,
                    "buy-instantly",
                    new MessagePlaceholder("buy-price", Utils.getTextPrice(product.getLowestBuyPrice())),
                    new MessagePlaceholder("stack-buy-price", Utils.getTextPrice(product.getLowestBuyPrice() * 64))); //TODO Calculate real price from current stock
        });

        addItemPlaceholder((item, player, info) -> {
            if (!(info instanceof Product)) return item;
            Product product = (Product) info;
            return menuConfig.replaceLorePlaceholders(item,
                    "sell-instantly",
                    new MessagePlaceholder("item-amount", Utils.getTextPrice(bazaarPlugin.getBazaar().getProductAmountInInventory(product, player))),
                    new MessagePlaceholder("coins", Utils.getTextPrice(product.getHighestSellPrice())));
        });
    }

    @Override
    public void addItemPlaceholder(ItemPlaceholderFunction action) {
        itemPlaceholders.add(action);
    }

    @Override
    public ItemStack replaceItemPlaceholders(ItemStack item, Player player, MenuInfo info) {
        ItemStack newItem = item.clone();
        for (ItemPlaceholderFunction itemPlaceholder : itemPlaceholders) {
            newItem = itemPlaceholder.apply(newItem, player, info);
        }
        return newItem;
    }

    private int getPlaceholderLoreLineIndex(List<String> lore, String placeholder) {
        int sellInventoryLineIndex = -1;
        for (int i = 0; i < lore.size(); i++) {
            String line = lore.get(i);
            if (!line.equals("%" + placeholder + "%")) continue;
            sellInventoryLineIndex = i;
            break;
        }
        return sellInventoryLineIndex;
    }
}
