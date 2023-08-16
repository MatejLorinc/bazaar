package me.math3w.bazaar.menu.configurations;

import me.math3w.bazaar.BazaarPlugin;
import me.math3w.bazaar.api.BazaarAPI;
import me.math3w.bazaar.api.bazaar.Bazaar;
import me.math3w.bazaar.api.bazaar.Category;
import me.math3w.bazaar.api.bazaar.Product;
import me.math3w.bazaar.api.bazaar.ProductCategory;
import me.math3w.bazaar.api.edit.EditManager;
import me.math3w.bazaar.bazaar.product.ProductConfiguration;
import me.math3w.bazaar.bazaar.productcategory.ProductCategoryConfiguration;
import me.math3w.bazaar.bazaar.productcategory.ProductCategoryImpl;
import me.math3w.bazaar.config.BazaarConfig;
import me.math3w.bazaar.menu.DefaultConfigurableMenuItem;
import me.math3w.bazaar.menu.MenuConfiguration;
import me.math3w.bazaar.utils.MenuUtils;
import me.math3w.bazaar.utils.Utils;
import me.zort.containr.Component;
import me.zort.containr.GUI;
import me.zort.containr.PagedContainer;
import me.zort.containr.internal.util.ItemBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CategoryMenuConfiguration extends MenuConfiguration {
    public CategoryMenuConfiguration(String name, int rows, List<DefaultConfigurableMenuItem> items) {
        super(name, rows, items);
    }

    public static CategoryMenuConfiguration createDefaultConfiguration(String name, ItemStack glass) {
        return new CategoryMenuConfiguration(name, 6, getDefaultCategoryMenuItems(glass));
    }

    public static List<DefaultConfigurableMenuItem> getDefaultCategoryMenuItems(ItemStack glass) {
        int[] glassSlots = new int[]{1, 2, 3, 4, 5, 6, 7, 8, 10, 17, 19, 26, 28, 35, 37, 44, 46, 48, 51, 52, 53};

        List<DefaultConfigurableMenuItem> items = new ArrayList<>();
        for (int glassSlot : glassSlots) {
            items.add(new DefaultConfigurableMenuItem(glassSlot, ItemBuilder.newBuilder(glass).withName(ChatColor.WHITE.toString()).build(), ""));
        }

        items.add(new DefaultConfigurableMenuItem(45,
                ItemBuilder.newBuilder(Material.SIGN)
                        .withName(ChatColor.GREEN + "Search")
                        .appendLore(ChatColor.GRAY + "Find products by name!")
                        .appendLore("")
                        .appendLore(ChatColor.YELLOW + "Click to search!")
                        .build(),
                "search"));
        items.add(new DefaultConfigurableMenuItem(47,
                ItemBuilder.newBuilder(Material.CHEST)
                        .withName(ChatColor.GREEN + "Sell Inventory Now")
                        .appendLore(ChatColor.GRAY + "Instantly sell any items in your")
                        .appendLore(ChatColor.GRAY + "inventory that can be sold on")
                        .appendLore(ChatColor.GRAY + "the Bazaar.")
                        .appendLore("")
                        .appendLore("%sell-inventory%")
                        .build(),
                "sell-inventory"));
        items.add(new DefaultConfigurableMenuItem(49,
                ItemBuilder.newBuilder(Material.BARRIER)
                        .withName(ChatColor.RED + "Close")
                        .build(),
                "close"));
        items.add(new DefaultConfigurableMenuItem(50,
                ItemBuilder.newBuilder(Material.BOOK)
                        .withName(ChatColor.GREEN + "Manage Orders")
                        .appendLore(ChatColor.GRAY + "You don't have any ongoing")
                        .appendLore(ChatColor.GRAY + "orders.")
                        .appendLore("")
                        .appendLore(ChatColor.YELLOW + "Click to manage!")
                        .build(),
                "manage-orders"));
        return items;
    }

    public static CategoryMenuConfiguration deserialize(Map<String, Object> args) {
        return new CategoryMenuConfiguration((String) args.get("name"),
                (Integer) args.get("rows"),
                (List<DefaultConfigurableMenuItem>) args.get("items"));
    }

    public GUI getMenu(Category selectedCategory, boolean edit) {
        BazaarAPI bazaarApi = selectedCategory.getBazaar().getBazaarApi();

        return getMenuBuilder().prepare((gui, player) -> {
            super.loadItems(gui, bazaarApi, player, selectedCategory, edit);

            Bazaar bazaar = selectedCategory.getBazaar();
            EditManager editManager = bazaarApi.getEditManager();

            gui.setContainer(0, Component.staticContainer()
                    .size(1, 5)
                    .init(container -> {
                        for (Category category : bazaar.getCategories()) {
                            ItemStack categoryIcon = category.equals(selectedCategory) ? Utils.getGlowedItem(category.getIcon()) : category.getIcon();
                            container.appendElement(Component.element()
                                    .click(editManager.createEditableItemClickAction(clickInfo -> bazaar.open(player, category),
                                            clickInfo -> bazaar.openEdit(player, category),
                                            clickInfo -> editManager.openCategoryEdit(player, category),
                                            edit))
                                    .item(MenuUtils.appendEditLore(categoryIcon, edit))
                                    .build());
                        }
                    })
                    .build());

            PagedContainer productCategoriesContainer = Component.pagedContainer()
                    .size(6, 4)
                    .init(container -> {
                        for (ProductCategory productCategory : selectedCategory.getProductCategories()) {
                            List<Product> products = productCategory.getProducts();
                            if (products.size() == 1 && !edit) {
                                Product product = products.get(0);
                                ItemStack productIcon = product.getIcon(container, MenuUtils.getNextFreeSlot(container), player);
                                container.appendElement(Component.element()
                                        .click(clickInfo -> bazaar.openProduct(player, product))
                                        .item(productIcon)
                                        .build());
                                continue;
                            }

                            container.appendElement(Component.element()
                                    .click(editManager.createEditableItemClickAction(clickInfo -> productCategory.getMenu().open(clickInfo.getPlayer()),
                                            clickInfo -> productCategory.getEditMenu().open(player),
                                            clickInfo -> editManager.openProductCategoryEdit(player, productCategory),
                                            clickInfo -> selectedCategory.removeProductCategory(productCategory),
                                            clickInfo -> getMenu(selectedCategory, true).open(player),
                                            edit)).item(MenuUtils.appendEditLore(productCategory.getIcon(), edit, true))
                                    .build());
                        }

                        if (edit) {
                            container.appendElement(Component.element((MenuUtils.getPlusSkull(ChatColor.GREEN + "Add Category")))
                                    .click(clickInfo -> {
                                        BazaarConfig bazaarConfig = ((BazaarPlugin) bazaar.getBazaarApi()).getBazaarConfig();
                                        ProductCategoryConfiguration productCategoryConfiguration = bazaarConfig.getProductCategoryConfiguration(new ItemStack(Material.COAL), "Not set!", new ProductConfiguration[]{}, ChatColor.RED);
                                        ProductCategoryImpl productCategory = new ProductCategoryImpl(selectedCategory, productCategoryConfiguration);
                                        selectedCategory.addProductCategory(productCategory);
                                        editManager.openProductCategoryEdit(player, productCategory);
                                    }).build());
                        }
                    })
                    .build();

            gui.setContainer(11, productCategoriesContainer);
            MenuUtils.setPagingArrows(this, gui, productCategoriesContainer, bazaarApi, player, 48, 51, edit);
        }).build();
    }
}
