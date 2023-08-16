package me.math3w.bazaar.menu.configurations;

import me.math3w.bazaar.BazaarPlugin;
import me.math3w.bazaar.api.BazaarAPI;
import me.math3w.bazaar.api.bazaar.Product;
import me.math3w.bazaar.api.bazaar.ProductCategory;
import me.math3w.bazaar.api.edit.EditManager;
import me.math3w.bazaar.bazaar.product.ProductConfiguration;
import me.math3w.bazaar.bazaar.product.ProductImpl;
import me.math3w.bazaar.config.BazaarConfig;
import me.math3w.bazaar.menu.DefaultConfigurableMenuItem;
import me.math3w.bazaar.menu.MenuConfiguration;
import me.math3w.bazaar.utils.MenuUtils;
import me.zort.containr.Component;
import me.zort.containr.Element;
import me.zort.containr.GUI;
import me.zort.containr.internal.util.ItemBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class ProductCategoryMenuConfiguration extends MenuConfiguration {
    private final List<Integer> productSlots;

    public ProductCategoryMenuConfiguration(String name, int rows, List<DefaultConfigurableMenuItem> items, List<Integer> productSlots) {
        super(name, rows, items);
        this.productSlots = productSlots;
    }


    public static ProductCategoryMenuConfiguration createDefaultProductCategoryConfiguration(String name, int products) {
        List<DefaultConfigurableMenuItem> items = new ArrayList<>();

        items.add(new DefaultConfigurableMenuItem(30,
                ItemBuilder.newBuilder(Material.ARROW)
                        .withName(ChatColor.GREEN + "Go Back")
                        .appendLore(ChatColor.GRAY + "To Bazaar")
                        .build(),
                "back"));
        items.add(new DefaultConfigurableMenuItem(31,
                ItemBuilder.newBuilder(Material.BARRIER)
                        .withName(ChatColor.RED + "Close")
                        .build(),
                "close"));
        items.add(new DefaultConfigurableMenuItem(32,
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
            case 1:
                productSlots = Collections.singletonList(13);
                break;
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

        fillWithGlass(rows, items);

        return new ProductCategoryMenuConfiguration(name, rows, items, productSlots);
    }

    public static ProductCategoryMenuConfiguration deserialize(Map<String, Object> args) {
        return new ProductCategoryMenuConfiguration((String) args.get("name"),
                (Integer) args.get("rows"),
                (List<DefaultConfigurableMenuItem>) args.get("items"),
                (List<Integer>) args.get("slots"));
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> args = super.serialize();

        args.put("slots", productSlots);

        return args;
    }

    public GUI getMenu(ProductCategory selectedCategory, boolean edit) {
        BazaarAPI bazaarApi = selectedCategory.getCategory().getBazaar().getBazaarApi();
        EditManager editManager = bazaarApi.getEditManager();

        return getMenuBuilder().prepare((gui, player) -> {
            super.loadItems(gui, bazaarApi, player, selectedCategory, edit);

            if (edit) {
                for (Map.Entry<Integer, Element> slotElementEntry : gui.content(null).entrySet()) {
                    int slot = slotElementEntry.getKey();
                    Element element = slotElementEntry.getValue();
                    ItemStack originalItem = element.item(player);
                    if (originalItem == null) continue;

                    ItemStack item = ItemBuilder.newBuilder(originalItem)
                            .appendLore(ChatColor.DARK_AQUA + "Middle-Click to set item here")
                            .build();

                    gui.setElement(slot, Component.element(item).click(clickInfo -> {
                        if (clickInfo.getClickType() == ClickType.MIDDLE) {
                            BazaarConfig bazaarConfig = ((BazaarPlugin) bazaarApi).getBazaarConfig();
                            ProductConfiguration productConfiguration = bazaarConfig.getProductConfiguration(ItemBuilder.newBuilder(Material.COAL).withName(ChatColor.RED + "Not set!").build(), ChatColor.RED + "Not set!");
                            ProductImpl product = new ProductImpl(selectedCategory, productConfiguration);

                            productSlots.add(slot);
                            selectedCategory.addProduct(product);
                            editManager.openProductEdit(player, product);

                            return;
                        }
                        element.click(clickInfo);
                    }).build());
                }
            }

            List<Product> products = selectedCategory.getProducts();

            for (int i = 0; i < productSlots.size(); i++) {
                if (i >= products.size()) break;

                int slot = productSlots.get(i);
                Product product = products.get(i);

                int finalI = i;
                gui.setElement(slot, Component.element()
                        .click(editManager.createEditableItemClickAction(clickInfo -> bazaarApi.getBazaar().openProduct(player, product),
                                clickInfo -> bazaarApi.getBazaar().openProductEdit(player, product),
                                clickInfo -> editManager.openProductEdit(player, product),
                                clickInfo -> {
                                    productSlots.remove(finalI);
                                    selectedCategory.removeProduct(product);
                                },
                                clickInfo -> getMenu(selectedCategory, edit).open(player), edit))
                        .item(MenuUtils.appendEditLore(product.getIcon(gui, slot, player), edit, true))
                        .build());
            }
        }).build();
    }
}
