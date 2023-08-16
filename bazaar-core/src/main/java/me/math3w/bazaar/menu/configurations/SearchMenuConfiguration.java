package me.math3w.bazaar.menu.configurations;

import me.math3w.bazaar.api.BazaarAPI;
import me.math3w.bazaar.api.bazaar.Bazaar;
import me.math3w.bazaar.api.bazaar.Category;
import me.math3w.bazaar.api.bazaar.Product;
import me.math3w.bazaar.menu.DefaultConfigurableMenuItem;
import me.math3w.bazaar.menu.MenuConfiguration;
import me.math3w.bazaar.utils.MenuUtils;
import me.zort.containr.Component;
import me.zort.containr.GUI;
import me.zort.containr.PagedContainer;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Map;

public class SearchMenuConfiguration extends MenuConfiguration {
    public SearchMenuConfiguration(String name, int rows, List<DefaultConfigurableMenuItem> items) {
        super(name, rows, items);
    }

    public static SearchMenuConfiguration createDefaultConfiguration(String name, ItemStack glass) {
        return new SearchMenuConfiguration(name, 6, CategoryMenuConfiguration.getDefaultCategoryMenuItems(glass));
    }

    public static SearchMenuConfiguration deserialize(Map<String, Object> args) {
        return new SearchMenuConfiguration((String) args.get("name"),
                (Integer) args.get("rows"),
                (List<DefaultConfigurableMenuItem>) args.get("items"));
    }

    public GUI getMenu(Bazaar bazaar, String filter, boolean edit) {
        BazaarAPI bazaarApi = bazaar.getBazaarApi();

        return getMenuBuilder().prepare((gui, player) -> {
            super.loadItems(gui, bazaarApi, player, null, edit);

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
                                    .click(element -> bazaar.openProduct(player, product))
                                    .item(product.getIcon(container, MenuUtils.getNextFreeSlot(container), player))
                                    .build());
                        }
                    })
                    .build();

            gui.setContainer(11, productCategoriesContainer);
            MenuUtils.setPagingArrows(this, gui, productCategoriesContainer, bazaarApi, player, 48, 51, edit);
        }).build();
    }

}
