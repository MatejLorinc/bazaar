package me.math3w.bazaar.bazaar.product;

import me.math3w.bazaar.api.bazaar.Product;
import me.math3w.bazaar.api.bazaar.ProductCategory;
import me.math3w.bazaar.utils.Utils;
import org.bukkit.inventory.ItemStack;

public class ProductImpl implements Product {
    private final ProductCategory productCategory;
    private final ProductConfiguration config;

    public ProductImpl(ProductCategory productCategory, ProductConfiguration config) {
        this.productCategory = productCategory;
        this.config = config;
    }

    @Override
    public ItemStack getIcon() {
        return config.getIcon();
    }

    @Override
    public void setIcon(ItemStack icon) {
        config.setIcon(icon);
        productCategory.getCategory().getBazaar().saveConfig();
    }

    @Override
    public String getName() {
        return Utils.colorize(config.getName());
    }

    @Override
    public void setName(String name) {
        config.setName(name);
        productCategory.getCategory().getBazaar().saveConfig();
    }

    @Override
    public ProductCategory getProductCategory() {
        return productCategory;
    }
}
