package me.math3w.bazaar.bazaar.category;

import me.math3w.bazaar.api.bazaar.Bazaar;
import me.math3w.bazaar.api.bazaar.Category;
import me.math3w.bazaar.api.bazaar.ProductCategory;
import me.math3w.bazaar.bazaar.BazaarImpl;
import me.math3w.bazaar.bazaar.productcategory.ProductCategoryImpl;
import me.math3w.bazaar.utils.Utils;
import me.zort.containr.GUI;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.stream.Collectors;

public class CategoryImpl implements Category {
    private final Bazaar bazaar;
    private final CategoryConfiguration config;
    private final List<ProductCategory> productCategories;

    public CategoryImpl(BazaarImpl bazaar, CategoryConfiguration config) {
        this.bazaar = bazaar;
        this.config = config;
        productCategories = config.getProductCategories().stream()
                .map(productCategoryConfiguration -> new ProductCategoryImpl(this, productCategoryConfiguration))
                .collect(Collectors.toList());
    }

    @Override
    public ItemStack getIcon() {
        return config.getIcon();
    }

    @Override
    public void setIcon(ItemStack icon) {
        config.setIcon(icon);
        bazaar.saveConfig();
    }

    @Override
    public String getName() {
        return Utils.colorize(config.getName());
    }

    @Override
    public void setName(String name) {
        config.setName(name);
        bazaar.saveConfig();
    }

    @Override
    public List<ProductCategory> getProductCategories() {
        return productCategories;
    }

    @Override
    public void addProductCategory(ProductCategory productCategory) {
        config.getProductCategories().add(((ProductCategoryImpl) productCategory).getConfig());
        productCategories.add(productCategory);
        bazaar.saveConfig();
    }

    @Override
    public void removeProductCategory(ProductCategory productCategory) {
        config.getProductCategories().remove(((ProductCategoryImpl) productCategory).getConfig());
        productCategories.remove(productCategory);
        bazaar.saveConfig();
    }

    @Override
    public GUI getMenu() {
        return config.getMenuConfig().getMenu(this, false);
    }

    @Override
    public void setTitle(String name) {
        config.getMenuConfig().setName(bazaar, name);
    }

    @Override
    public GUI getEditMenu() {
        return config.getMenuConfig().getMenu(this, true);
    }

    @Override
    public Bazaar getBazaar() {
        return bazaar;
    }
}
