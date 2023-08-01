package me.math3w.bazaar.menu.configurations;

import me.math3w.bazaar.api.BazaarAPI;
import me.math3w.bazaar.api.bazaar.Bazaar;
import me.math3w.bazaar.api.bazaar.Category;
import me.math3w.bazaar.api.bazaar.Product;
import me.math3w.bazaar.menu.ConfigurableMenuItem;
import me.math3w.bazaar.menu.MenuConfiguration;
import me.zort.containr.Component;
import me.zort.containr.GUI;
import me.zort.containr.PagedContainer;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Map;

public class SearchMenuConfiguration extends MenuConfiguration {
    public SearchMenuConfiguration(String name, int rows, List<ConfigurableMenuItem> items) {
        super(name, rows, items);
    }

    public static SearchMenuConfiguration createDefaultConfiguration(String name, ItemStack glass) {
        return new SearchMenuConfiguration(name, 6, CategoryMenuConfiguration.getDefaultCategoryMenuItems(glass));
    }

    public static SearchMenuConfiguration deserialize(Map<String, Object> args) {
        return new SearchMenuConfiguration((String) args.get("name"),
                (Integer) args.get("rows"),
                (List<ConfigurableMenuItem>) args.get("items"));
    }

    public GUI getMenu(Bazaar bazaar, String filter) {
        BazaarAPI bazaarApi = bazaar.getBazaarApi();

        return getMenuBuilder().prepare((gui, player) -> {
            super.loadItems(gui, bazaarApi, player);

            gui.setContainer(0, Component.staticContainer()
                    .size(1, 5)
                    .init(container -> {
                        for (Category category : bazaar.getCategories()) {
                            container.appendElement(Component.element()
                                    .click(element -> {
                                        bazaar.open(player, category);
                                    })
                                    .item(category.getIcon())
                                    .build());
                        }
                    })
                    .build());

            PagedContainer productCategoriesContainer = Component.pagedContainer()
                    .size(6, 4)
                    .init(container -> {
                        List<Product> products = bazaar.getProducts(product -> product.getName().toLowerCase().contains(filter.toLowerCase()));
                        for (Product product : products) {
                            container.appendElement(Component.element()
                                    .click(element -> {
                                        //TODO Open product menu
                                    })
                                    .item(product.getIcon())
                                    .build());
                        }
                    })
                    .build();

            gui.setContainer(11, productCategoriesContainer);
            CategoryMenuConfiguration.setPagingArrows(this, gui, productCategoriesContainer, bazaarApi, player);
        }).build();
    }
}
