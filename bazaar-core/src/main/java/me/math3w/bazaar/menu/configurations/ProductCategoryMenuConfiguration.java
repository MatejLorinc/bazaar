package me.math3w.bazaar.menu.configurations;

import me.math3w.bazaar.api.bazaar.Product;
import me.math3w.bazaar.api.bazaar.ProductCategory;
import me.math3w.bazaar.api.menu.ClickActionManager;
import me.math3w.bazaar.menu.ConfigurableMenuItem;
import me.math3w.bazaar.menu.MenuConfiguration;
import me.zort.containr.Component;
import me.zort.containr.GUI;
import me.zort.containr.internal.util.ItemBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class ProductCategoryMenuConfiguration extends MenuConfiguration {
    private final List<Integer> productSlots;

    public ProductCategoryMenuConfiguration(String name, int rows, List<ConfigurableMenuItem> items, List<Integer> productSlots) {
        super(name, rows, items);
        this.productSlots = productSlots;
    }


    public static ProductCategoryMenuConfiguration createDefaultProductCategoryConfiguration(String name, int products) {
        List<ConfigurableMenuItem> items = new ArrayList<>();

        items.add(new ConfigurableMenuItem(30,
                ItemBuilder.newBuilder(Material.ARROW)
                        .withName(ChatColor.GREEN + "Go Back")
                        .appendLore(ChatColor.GRAY + "To Bazaar")
                        .build(),
                "back"));
        items.add(new ConfigurableMenuItem(31,
                ItemBuilder.newBuilder(Material.BARRIER)
                        .withName(ChatColor.RED + "Close")
                        .build(),
                "close"));
        items.add(new ConfigurableMenuItem(32,
                ItemBuilder.newBuilder(Material.BOOK)
                        .withName(ChatColor.GREEN + "Manage Orders")
                        .appendLore(ChatColor.GRAY + "You don't have any ongoing")
                        .appendLore(ChatColor.GRAY + "orders.")
                        .appendLore("")
                        .appendLore(ChatColor.YELLOW + "Click to manage!")
                        .build(),
                "manage-orders"));

        int rows = 4;
        List<Integer> productSlots = new ArrayList<>();

        switch (products) {
            case 2:
                productSlots = Arrays.asList(12, 14);
                break;
            case 3:
                productSlots = Arrays.asList(11, 13, 15);
                break;
            case 4:
                productSlots = Arrays.asList(10, 12, 14, 16);
                break;
        }

        ItemStack glass = ItemBuilder.newBuilder(Material.STAINED_GLASS_PANE).withData((short) 15).withName(ChatColor.WHITE.toString()).build();
        for (int i = 0; i < rows * 9; i++) {
            int finalI = i;
            if (items.stream().anyMatch(configurableMenuItem -> configurableMenuItem.getSlot() == finalI)) continue;
            items.add(new ConfigurableMenuItem(i, glass, ""));
        }

        return new ProductCategoryMenuConfiguration(name, rows, items, productSlots);
    }

    public static ProductCategoryMenuConfiguration deserialize(Map<String, Object> args) {
        return new ProductCategoryMenuConfiguration((String) args.get("name"),
                (Integer) args.get("rows"),
                (List<ConfigurableMenuItem>) args.get("items"),
                (List<Integer>) args.get("slots"));
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> args = super.serialize();

        args.put("slots", productSlots);

        return args;
    }

    public GUI getMenu(ProductCategory selectedCategory) {
        ClickActionManager clickActionManager = selectedCategory.getCategory().getBazaar().getBazaarApi().getClickActionManager();

        return getMenuBuilder().prepare((gui, player) -> {
            super.loadItems(gui, clickActionManager);

            List<Product> products = selectedCategory.getProducts();

            for (int i = 0; i < productSlots.size(); i++) {
                if (i >= products.size()) break;

                int slot = productSlots.get(i);
                Product product = products.get(i);

                gui.setElement(slot, Component.element()
                        .click(element -> {
                            //TODO Open product menu
                        })
                        .item(product.getIcon())
                        .build());
            }
        }).build();
    }
}
