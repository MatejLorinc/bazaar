package me.math3w.bazaar.bazaar.product;

import me.math3w.bazaar.api.bazaar.Product;
import me.math3w.bazaar.api.bazaar.ProductCategory;
import me.math3w.bazaar.api.config.MessagePlaceholder;
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
    public ItemStack getItem() {
        return config.getItem();
    }

    @Override
    public void setItem(ItemStack item) {
        config.setItem(item);
        productCategory.getCategory().getBazaar().saveConfig();
    }

    @Override
    public ItemStack getIcon() {
        return productCategory.getCategory().getBazaar().getBazaarApi().getMenuConfig().replaceLorePlaceholders(config.getIcon(),
                "product-lore",
                new MessagePlaceholder("buy-price", Utils.getTextPrice(getLowestBuyPrice())),
                new MessagePlaceholder("sell-price", Utils.getTextPrice(getHighestSellPrice())));
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

    @Override
    public double getLowestBuyPrice() {
        return 0;
    }

    @Override
    public double getHighestSellPrice() {
        return 0;
    }
}
