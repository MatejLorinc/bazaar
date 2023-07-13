package me.math3w.bazaar.bazaar.productcategory;

import me.math3w.bazaar.api.bazaar.Category;
import me.math3w.bazaar.api.bazaar.Product;
import me.math3w.bazaar.api.bazaar.ProductCategory;
import me.math3w.bazaar.bazaar.product.ProductImpl;
import me.math3w.bazaar.utils.Utils;
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
        return config.getIcon();
    }

    @Override
    public void setIcon(ItemStack icon) {
        config.setIcon(icon);
        category.getBazaar().saveConfig();
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
    public Category getCategory() {
        return category;
    }
}
