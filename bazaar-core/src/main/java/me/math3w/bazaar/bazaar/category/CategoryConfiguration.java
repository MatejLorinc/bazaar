package me.math3w.bazaar.bazaar.category;

import me.math3w.bazaar.bazaar.productcategory.ProductCategoryConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CategoryConfiguration implements ConfigurationSerializable {
    private final List<ProductCategoryConfiguration> productCategories;
    private ItemStack icon;
    private String name;

    public CategoryConfiguration(ItemStack icon, String name, List<ProductCategoryConfiguration> productCategories) {
        this.icon = icon;
        this.name = name;
        this.productCategories = productCategories;
    }

    public static CategoryConfiguration deserialize(Map<String, Object> args) {
        return new CategoryConfiguration((ItemStack) args.get("icon"),
                (String) args.get("name"),
                (List<ProductCategoryConfiguration>) args.get("product-categories"));
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

    public List<ProductCategoryConfiguration> getProductCategories() {
        return productCategories;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> args = new HashMap<>();
        args.put("icon", icon);
        args.put("name", name);
        args.put("product-categories", productCategories);
        return args;
    }
}
