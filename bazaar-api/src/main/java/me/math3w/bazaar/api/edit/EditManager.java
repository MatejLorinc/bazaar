package me.math3w.bazaar.api.edit;

import me.math3w.bazaar.api.bazaar.Category;
import me.math3w.bazaar.api.bazaar.Product;
import me.math3w.bazaar.api.bazaar.ProductCategory;
import me.math3w.bazaar.api.menu.ConfigurableMenuItem;
import me.zort.containr.ContextClickInfo;
import org.bukkit.entity.Player;

import java.util.function.Consumer;

public interface EditManager {
    void openItemEdit(Player player, ConfigurableMenuItem configurableMenuItem);

    void openCategoryEdit(Player player, Category category);

    void openProductCategoryEdit(Player player, ProductCategory productCategory);

    void openProductEdit(Player player, Product product);

    Consumer<ContextClickInfo> createEditableItemClickAction(Consumer<ContextClickInfo> defaultClickAction, Consumer<ContextClickInfo> defaultEditClickAction, Consumer<ContextClickInfo> editClickAction, Consumer<ContextClickInfo> removeClickAction, Consumer<ContextClickInfo> updateMenu, boolean editing);

    Consumer<ContextClickInfo> createEditableItemClickAction(Consumer<ContextClickInfo> defaultClickAction, Consumer<ContextClickInfo> defaultEditClickAction, Consumer<ContextClickInfo> editClickAction, boolean editing);
}
