package me.math3w.bazaar.bazaar.productcategory;

import me.math3w.bazaar.bazaar.product.ProductConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductCategoryConfiguration implements ConfigurationSerializable {
    private final List<ProductConfiguration> products;
    private ItemStack icon;
    private String name;

    public ProductCategoryConfiguration(ItemStack icon, String name, List<ProductConfiguration> products) {
        this.icon = icon;
        this.name = name;
        this.products = products;
    }

    public static ProductCategoryConfiguration deserialize(Map<String, Object> args) {
        return new ProductCategoryConfiguration((ItemStack) args.get("icon"),
                (String) args.get("name"),
                (List<ProductConfiguration>) args.get("products"));
    }

    public ItemStack getIcon() {
        return icon;
    }

    public void setIcon(ItemStack icon) {
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ProductConfiguration> getProducts() {
        return products;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> args = new HashMap<>();
        args.put("icon", icon);
        args.put("name", name);
        args.put("products", products);
        return args;
    }
}
