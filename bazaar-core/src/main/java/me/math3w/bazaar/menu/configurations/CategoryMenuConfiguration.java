package me.math3w.bazaar.menu.configurations;

import me.math3w.bazaar.api.BazaarAPI;
import me.math3w.bazaar.api.bazaar.Bazaar;
import me.math3w.bazaar.api.bazaar.Category;
import me.math3w.bazaar.api.bazaar.Product;
import me.math3w.bazaar.api.bazaar.ProductCategory;
import me.math3w.bazaar.menu.ConfigurableMenuItem;
import me.math3w.bazaar.menu.MenuConfiguration;
import me.math3w.bazaar.utils.Utils;
import me.zort.containr.Component;
import me.zort.containr.GUI;
import me.zort.containr.PagedContainer;
import me.zort.containr.internal.util.ItemBuilder;
import me.zort.containr.internal.util.Items;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CategoryMenuConfiguration extends MenuConfiguration {
    public CategoryMenuConfiguration(String name, int rows, List<ConfigurableMenuItem> items) {
        super(name, rows, items);
    }

    public static CategoryMenuConfiguration createDefaultConfiguration(String name, ItemStack glass) {
        return new CategoryMenuConfiguration(name, 6, getDefaultCategoryMenuItems(glass));
    }

    public static List<ConfigurableMenuItem> getDefaultCategoryMenuItems(ItemStack glass) {
        int[] glassSlots = new int[]{1, 2, 3, 4, 5, 6, 7, 8, 10, 17, 19, 26, 28, 35, 37, 44, 46, 48, 51, 52, 53};

        List<ConfigurableMenuItem> items = new ArrayList<>();
        for (int glassSlot : glassSlots) {
            items.add(new ConfigurableMenuItem(glassSlot, ItemBuilder.newBuilder(glass).withName(ChatColor.WHITE.toString()).build(), ""));
        }

        items.add(new ConfigurableMenuItem(45,
                ItemBuilder.newBuilder(Material.SIGN)
                        .withName(ChatColor.GREEN + "Search")
                        .appendLore(ChatColor.GRAY + "Find products by name!")
                        .appendLore("")
                        .appendLore(ChatColor.YELLOW + "Click to search!")
                        .build(),
                "search"));
        items.add(new ConfigurableMenuItem(47,
                ItemBuilder.newBuilder(Material.CHEST)
                        .withName(ChatColor.GREEN + "Sell Inventory Now")
                        .appendLore(ChatColor.GRAY + "Instantly sell any items in your")
                        .appendLore(ChatColor.GRAY + "inventory that can be sold on")
                        .appendLore(ChatColor.GRAY + "the Bazaar.")
                        .appendLore("")
                        .appendLore("%sell-inventory%")
                        .build(),
                "sell-inventory"));
        items.add(new ConfigurableMenuItem(49,
                ItemBuilder.newBuilder(Material.BARRIER)
                        .withName(ChatColor.RED + "Close")
                        .build(),
                "close"));
        items.add(new ConfigurableMenuItem(50,
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
                (List<ConfigurableMenuItem>) args.get("items"));
    }

    public static void setPagingArrows(MenuConfiguration menuConfiguration, GUI gui, PagedContainer productCategoriesContainer, BazaarAPI bazaarApi, Player player) {
        if (productCategoriesContainer.getCurrentPageIndex() > 0) {
            gui.setElement(48, Component.element()
                    .click(info -> {
                        productCategoriesContainer.previousPage();
                        setPagingArrows(menuConfiguration, gui, productCategoriesContainer, bazaarApi, player);
                        gui.update(info.getPlayer());
                    })
                    .item(Items.create(Material.ARROW, ChatColor.GREEN + "Previous page"))
                    .build());
        } else {
            menuConfiguration.getItems().stream().filter(item -> item.getSlot() == 48).findAny().ifPresent(glassItem -> {
                glassItem.setItem(gui, bazaarApi, player, null);
            });
        }

        if (productCategoriesContainer.getCurrentPageIndex() < productCategoriesContainer.getMaxPageIndex()) {
            gui.setElement(51, Component.element()
                    .click(info -> {
                        productCategoriesContainer.nextPage();
                        setPagingArrows(menuConfiguration, gui, productCategoriesContainer, bazaarApi, player);
                        gui.update(info.getPlayer());
                    })
                    .item(Items.create(Material.ARROW, ChatColor.GREEN + "Next page"))
                    .build());
        } else {
            menuConfiguration.getItems().stream().filter(item -> item.getSlot() == 51).findAny().ifPresent(glassItem -> {
                glassItem.setItem(gui, bazaarApi, player, null);
            });
        }
    }

    public GUI getMenu(Category selectedCategory) {
        BazaarAPI bazaarApi = selectedCategory.getBazaar().getBazaarApi();

        return getMenuBuilder().prepare((gui, player) -> {
            super.loadItems(gui, bazaarApi, player, selectedCategory);

            Bazaar bazaar = selectedCategory.getBazaar();

            gui.setContainer(0, Component.staticContainer()
                    .size(1, 5)
                    .init(container -> {
                        for (Category category : bazaar.getCategories()) {
                            container.appendElement(Component.element()
                                    .click(element -> {
                                        bazaar.open(player, category);
                                    })
                                    .item(category.equals(selectedCategory) ? Utils.getGlowedItem(category.getIcon()) : category.getIcon())
                                    .build());
                        }
                    })
                    .build());

            PagedContainer productCategoriesContainer = Component.pagedContainer()
                    .size(6, 4)
                    .init(container -> {
                        for (ProductCategory productCategory : selectedCategory.getProductCategories()) {
                            List<Product> products = productCategory.getProducts();
                            if (products.size() == 1) {
                                Product product = products.get(0);
                                container.appendElement(Component.element()
                                        .click(element -> bazaar.openProduct(player, product))
                                        .item(product.getIcon(container, container.getEmptyElementSlots()[0], player))
                                        .build());
                                continue;
                            }

                            container.appendElement(Component.element()
                                    .click(element -> productCategory.getMenu().open(element.getPlayer()))
                                    .item(productCategory.getIcon())
                                    .build());
                        }
                    })
                    .build();

            gui.setContainer(11, productCategoriesContainer);
            setPagingArrows(this, gui, productCategoriesContainer, bazaarApi, player);
        }).build();
    }
}
