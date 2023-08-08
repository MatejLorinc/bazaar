package me.math3w.bazaar.menu;

import me.math3w.bazaar.BazaarPlugin;
import me.math3w.bazaar.api.bazaar.Product;
import me.math3w.bazaar.api.bazaar.orders.BazaarOrder;
import me.math3w.bazaar.api.bazaar.orders.OrderType;
import me.math3w.bazaar.api.config.MenuConfig;
import me.math3w.bazaar.api.config.MessagePlaceholder;
import me.math3w.bazaar.api.menu.ItemPlaceholderFunction;
import me.math3w.bazaar.api.menu.ItemPlaceholders;
import me.math3w.bazaar.api.menu.MenuInfo;
import me.math3w.bazaar.utils.MenuUtils;
import me.math3w.bazaar.utils.Utils;
import me.zort.containr.Component;
import me.zort.containr.ContainerComponent;
import me.zort.containr.Element;
import me.zort.containr.GUIRepository;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class DefaultItemPlaceholders implements ItemPlaceholders {
    public static final int ORDER_LIMIT = 5;
    private final BazaarPlugin bazaarPlugin;
    private final List<ItemPlaceholderFunction> itemPlaceholders = new ArrayList<>();

    public DefaultItemPlaceholders(BazaarPlugin bazaarPlugin) {
        this.bazaarPlugin = bazaarPlugin;

        MenuConfig menuConfig = bazaarPlugin.getMenuConfig();

        addItemPlaceholder((containerComponent, item, itemSlot, player, info) -> {
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

        addItemPlaceholder((containerComponent, item, itemSlot, player, info) -> {
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

        addItemPlaceholder((containerComponent, item, itemSlot, player, info) -> {
            if (!(info instanceof Product)) return item;
            Product product = (Product) info;
            return menuConfig.replaceLorePlaceholders(item,
                    "buy-instantly",
                    new MessagePlaceholder("buy-price", Utils.getTextPrice(product.getLowestBuyPrice())),
                    new MessagePlaceholder("stack-buy-price", Utils.getTextPrice(product.getLowestBuyPrice() * 64))); //TODO Calculate real price from current stock
        });

        addItemPlaceholder((containerComponent, item, itemSlot, player, info) -> {
            if (!(info instanceof Product)) return item;
            Product product = (Product) info;
            return menuConfig.replaceLorePlaceholders(item,
                    "sell-instantly",
                    new MessagePlaceholder("item-amount", Utils.getTextPrice(bazaarPlugin.getBazaar().getProductAmountInInventory(product, player))),
                    new MessagePlaceholder("coins", Utils.getTextPrice(product.getHighestSellPrice())));
        });

        addItemPlaceholder((containerComponent, item, itemSlot, player, info) -> {
            if (!(info instanceof Product)) return item;
            Product product = (Product) info;

            return replaceMultiLinePlaceholder(menuConfig,
                    containerComponent,
                    item,
                    itemSlot,
                    player,
                    "buy-orders",
                    bazaarPlugin.getOrderManager().getCompressedOrders(product, OrderType.BUY, ORDER_LIMIT)
                            .thenApply(orders -> orders.stream()
                                    .flatMap(order -> menuConfig.getStringList("buy-order",
                                            new MessagePlaceholder("coins", Utils.getTextPrice(order.getUnitPrice())),
                                            new MessagePlaceholder("amount", String.valueOf(order.getAmount())),
                                            new MessagePlaceholder("orders", String.valueOf(order.getOrderAmount()))).stream())
                                    .collect(Collectors.toList())),
                    "buy-order-loading",
                    "buy-order-none");
        });

        addItemPlaceholder((containerComponent, item, itemSlot, player, info) -> {
            if (!(info instanceof Product)) return item;
            Product product = (Product) info;

            return replaceMultiLinePlaceholder(menuConfig,
                    containerComponent,
                    item,
                    itemSlot,
                    player,
                    "sell-offers",
                    bazaarPlugin.getOrderManager().getCompressedOrders(product, OrderType.SELL, ORDER_LIMIT)
                            .thenApply(orders -> orders.stream()
                                    .flatMap(order -> menuConfig.getStringList("sell-offer",
                                            new MessagePlaceholder("coins", Utils.getTextPrice(order.getUnitPrice())),
                                            new MessagePlaceholder("amount", String.valueOf(order.getAmount())),
                                            new MessagePlaceholder("offers", String.valueOf(order.getOrderAmount()))).stream())
                                    .collect(Collectors.toList())),
                    "sell-offer-loading",
                    "sell-offer-none");
        });
    }

    @Override
    public void addItemPlaceholder(ItemPlaceholderFunction action) {
        itemPlaceholders.add(action);
    }

    @Override
    public ItemStack replaceItemPlaceholders(ContainerComponent containerComponent, ItemStack item, int itemSlot, Player player, MenuInfo info) {
        ItemStack newItem = item.clone();
        for (ItemPlaceholderFunction itemPlaceholder : itemPlaceholders) {
            newItem = itemPlaceholder.apply(containerComponent, newItem, itemSlot, player, info);
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

    private ItemStack replaceMultiLinePlaceholder(MenuConfig menuConfig,
                                                  ContainerComponent container,
                                                  ItemStack item,
                                                  int itemSlot,
                                                  Player player,
                                                  String placeholder,
                                                  CompletableFuture<List<String>> linesFuture,
                                                  String loadingPlaceholder,
                                                  String nonePlaceholder) {
        ItemStack newItem = item.clone();
        ItemMeta itemMeta = newItem.getItemMeta();

        if (itemMeta == null || !itemMeta.hasLore()) return item;

        List<String> lore = new ArrayList<>(itemMeta.getLore());

        int placeholderLineIndex = getPlaceholderLoreLineIndex(lore, placeholder);
        if (placeholderLineIndex == -1) return item;

        lore.remove(placeholderLineIndex);

        List<String> loadingPlaceholderValue = menuConfig.getStringList(loadingPlaceholder);
        addLinesToLore(loadingPlaceholderValue, lore, placeholderLineIndex);

        itemMeta.setLore(lore);
        newItem.setItemMeta(itemMeta);

        linesFuture.thenAccept(lines -> {
            for (int i = 0; i < loadingPlaceholderValue.size(); i++) {
                lore.remove(placeholderLineIndex + i);
            }

            if (lines.isEmpty()) {
                List<String> nonePlaceholderValue = menuConfig.getStringList(nonePlaceholder);
                addLinesToLore(nonePlaceholderValue, lore, placeholderLineIndex);
            } else {
                addLinesToLore(lines, lore, placeholderLineIndex);
            }

            itemMeta.setLore(lore);
            newItem.setItemMeta(itemMeta);

            for (Map.Entry<Integer, Element> slotElementEntry : container.content(null).entrySet()) {
                int slot = slotElementEntry.getKey();
                Element element = slotElementEntry.getValue();

                if (slot != itemSlot) continue;

                Bukkit.getScheduler().runTaskLater(bazaarPlugin, () -> {
                    container.setElement(itemSlot, Component.element(newItem).click(element::click).build());
                    GUIRepository.reopenCurrent(player);
                }, 5);
                break;
            }
        });

        return newItem;
    }

    private void addLinesToLore(List<String> placeholderValue, List<String> lore, int index) {
        for (int i = 0; i < placeholderValue.size(); i++) {
            String placeholderLine = placeholderValue.get(i);
            lore.add(index + i, placeholderLine);
        }
    }
}
