package me.math3w.bazaar.bazaar.productcategory;

import me.math3w.bazaar.api.bazaar.Category;
import me.math3w.bazaar.api.bazaar.Product;
import me.math3w.bazaar.api.bazaar.ProductCategory;
import me.math3w.bazaar.api.config.MessagePlaceholder;
import me.math3w.bazaar.bazaar.product.ProductImpl;
import me.math3w.bazaar.utils.Utils;
import me.zort.containr.GUI;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.stream.Collectors;

public class ProductCategoryImpl implements ProductCategory {
    private final Category category;
    private final ProductCategoryConfiguration config;
    private final List<Product> products;

    public ProductCategoryImpl(Category category, ProductCategoryConfiguration config) {
        this.category = category;
        this.config = config;
        products = config.getProducts().stream()
                .map(productConfiguration -> new ProductImpl(this, productConfiguration))
                .collect(Collectors.toList());
    }

    @Override
    public ItemStack getIcon() {
        return category.getBazaar().getBazaarApi().getMenuConfig().replaceLorePlaceholders(config.getIcon(),
                "productcategory-lore",
                new MessagePlaceholder("products", String.valueOf(products.size())));
    }

    @Override
    public void setIcon(ItemStack icon) {
        config.setIcon(icon);
        category.getBazaar().saveConfig();
    }

    @Override
    public ItemStack getRawIcon() {
        return config.getIcon().clone();
    }

    @Override
    public String getName() {
        return Utils.colorize(config.getName());
    }

    @Override
    public void setName(String name) {
        config.setName(name);
        category.getBazaar().saveConfig();
    }

    @Override
    public List<Product> getProducts() {
        return products;
    }

    @Override
    public void addProduct(Product product) {
        config.getProducts().add(((ProductImpl) product).getConfig());
        products.add(product);
        category.getBazaar().saveConfig();
    }

    @Override
    public void removeProduct(Product product) {
        config.getProducts().remove(((ProductImpl) product).getConfig());
        products.remove(product);
        category.getBazaar().saveConfig();
    }

    @Override
    public GUI getMenu() {
        return config.getMenuConfig().getMenu(this, false);
    }

    @Override
    public GUI getEditMenu() {
        return config.getMenuConfig().getMenu(this, true);
    }

    @Override
    public Category getCategory() {
        return category;
    }

    public ProductCategoryConfiguration getConfig() {
        return config;
    }
}
