package me.math3w.bazaar.config;

import com.cryptomorin.xseries.XMaterial;
import me.math3w.bazaar.api.bazaar.orders.OrderType;
import me.math3w.bazaar.bazaar.category.CategoryConfiguration;
import me.math3w.bazaar.bazaar.product.ProductConfiguration;
import me.math3w.bazaar.bazaar.productcategory.ProductCategoryConfiguration;
import me.math3w.bazaar.menu.configurations.*;
import me.math3w.bazaar.utils.Utils;
import me.zort.containr.internal.util.ItemBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BazaarConfig extends CustomConfig {
    public BazaarConfig(JavaPlugin plugin) {
        super(plugin, "bazaar");
    }

    @Override
    protected void addDefaults() {
        addDefaultCategories();
    }

    private void addDefaultCategories() {
        ArrayList<CategoryConfiguration> categories = new ArrayList<>();

        categories.add(createDefaultCategory(XMaterial.GOLDEN_HOE.parseItem(), "&eFarming", XMaterial.YELLOW_STAINED_GLASS_PANE.parseItem(), getDefaultFarmingProductCategories()));
        categories.add(createDefaultCategory(Material.DIAMOND_PICKAXE, "&bMining", XMaterial.CYAN_STAINED_GLASS_PANE.parseItem(), getDefaultMiningProductCategories()));
        categories.add(createDefaultCategory(Material.IRON_SWORD, "&cCombat", XMaterial.RED_STAINED_GLASS_PANE.parseItem(), getDefaultCombatProductCategories()));
        categories.add(createDefaultCategory(Material.FISHING_ROD, "&6Woods & Fishes", XMaterial.ORANGE_STAINED_GLASS_PANE.parseItem(), getDefaultWoodsFishesProductCategories()));
        categories.add(createDefaultCategory(XMaterial.ENCHANTING_TABLE.parseItem(), "&dOddities", XMaterial.PINK_STAINED_GLASS_PANE.parseItem(), getDefaultOdditiesProductCategories()));

        addDefault("categories", categories);
        addDefault("search-menu", SearchMenuConfiguration.createDefaultConfiguration(ChatColor.GREEN + "Bazaar ➜ Search", XMaterial.LIME_STAINED_GLASS_PANE.parseItem()));
        addDefault("product-menu", ProductMenuConfiguration.createDefaultProductConfiguration());
        for (OrderType orderType : OrderType.values()) {
            addDefault("confirm-" + orderType.name().toLowerCase() + "-menu", ConfirmationMenuConfiguration.createDefaultConfirmationConfiguration(orderType));
        }
        addDefault("orders", OrdersMenuConfiguration.createDefaultConfiguration());
    }

    private CategoryConfiguration createDefaultCategory(ItemStack icon, String name, ItemStack glass, List<ProductCategoryConfiguration> productCategories) {
        String colorizedName = Utils.colorize(name);
        return new CategoryConfiguration(getDefaultMenuConfiguration(colorizedName, glass),
                ItemBuilder.newBuilder(icon)
                        .withName(colorizedName)
                        .appendLore(ChatColor.DARK_GRAY + "Category", "", ChatColor.YELLOW + "Click to view!")
                        .build(),
                name,
                productCategories);
    }

    private CategoryConfiguration createDefaultCategory(Material icon, String name, ItemStack glass, List<ProductCategoryConfiguration> productCategories) {
        return createDefaultCategory(new ItemStack(icon), name, glass, productCategories);
    }

    private CategoryMenuConfiguration getDefaultMenuConfiguration(String categoryName, ItemStack glass) {
        return CategoryMenuConfiguration.createDefaultConfiguration(ChatColor.getLastColors(categoryName) + "Bazaar ➜ " + categoryName, glass);
    }

    private List<ProductCategoryConfiguration> getDefaultFarmingProductCategories() {
        return Arrays.asList(
                getFarmingProductCategoryConfiguration(Material.WHEAT,
                        "Wheat & Seeds",
                        getProductConfiguration(XMaterial.WHEAT_SEEDS.parseItem(), "Seeds"),
                        getProductConfiguration(Material.WHEAT, "Wheat"),
                        getProductConfiguration(Material.BREAD, "Bread")),
                getFarmingProductCategoryConfiguration(XMaterial.CARROT.parseMaterial(),
                        "Carrot",
                        getProductConfiguration(XMaterial.CARROT.parseMaterial(), "Carrot"),
                        getProductConfiguration(Material.GOLDEN_CARROT, "Golden Carrot")),
                getFarmingProductCategoryConfiguration(XMaterial.POTATO.parseMaterial(),
                        "Potato",
                        getProductConfiguration(XMaterial.POTATO.parseMaterial(), "Potato"),
                        getProductConfiguration(Material.BAKED_POTATO, "Baked Potato")),
                getFarmingProductCategoryConfiguration(Material.PUMPKIN,
                        "Pumpkin",
                        getProductConfiguration(Material.PUMPKIN, "Pumpkin"),
                        getProductConfiguration(Material.PUMPKIN_PIE, "Pumpkin Pie")),
                getFarmingProductCategoryConfiguration(Material.MELON,
                        "Melon",
                        getProductConfiguration(Material.MELON, "Melon")),
                getFarmingProductCategoryConfiguration(Material.RED_MUSHROOM,
                        "Mushrooms",
                        getProductConfiguration(Material.RED_MUSHROOM, "Red Mushroom"),
                        getProductConfiguration(Material.BROWN_MUSHROOM, "Brown Mushroom")),
                getFarmingProductCategoryConfiguration(XMaterial.COCOA_BEANS.parseItem(),
                        "Cocoa Beans",
                        getProductConfiguration(XMaterial.COCOA_BEANS.parseItem(), "Cocoa Beans")),
                getFarmingProductCategoryConfiguration(Material.CACTUS,
                        "Cactus",
                        getProductConfiguration(Material.CACTUS, "Cactus"),
                        getProductConfiguration(XMaterial.GREEN_DYE.parseItem(), "Cactus Green")),
                getFarmingProductCategoryConfiguration(Material.SUGAR_CANE,
                        "Sugar Cane",
                        getProductConfiguration(Material.SUGAR_CANE, "Sugar Cane"),
                        getProductConfiguration(Material.SUGAR, "Sugar")),
                getFarmingProductCategoryConfiguration(Material.LEATHER,
                        "Leather & Beef",
                        getProductConfiguration(Material.LEATHER, "Leather"),
                        getProductConfiguration(XMaterial.BEEF.parseMaterial(), "Beef"),
                        getProductConfiguration(Material.COOKED_BEEF, "Steak")),
                getFarmingProductCategoryConfiguration(XMaterial.PORKCHOP.parseMaterial(),
                        "Pork",
                        getProductConfiguration(XMaterial.PORKCHOP.parseMaterial(), "Pork"),
                        getProductConfiguration(XMaterial.COOKED_PORKCHOP.parseMaterial(), "Grilled Pork")),
                getFarmingProductCategoryConfiguration(XMaterial.CHICKEN.parseItem(),
                        "Chicken & Feather",
                        getProductConfiguration(XMaterial.CHICKEN.parseMaterial(), "Chicken"),
                        getProductConfiguration(Material.COOKED_CHICKEN, "Cooked Chicken"),
                        getProductConfiguration(Material.FEATHER, "Feather"),
                        getProductConfiguration(Material.EGG, "Egg")),
                getFarmingProductCategoryConfiguration(Material.MUTTON,
                        "Mutton & Wool",
                        getProductConfiguration(Material.MUTTON, "Mutton"),
                        getProductConfiguration(Material.COOKED_MUTTON, "Cooked Mutton"),
                        getProductConfiguration(XMaterial.WHITE_WOOL.parseMaterial(), "Wool")),
                getFarmingProductCategoryConfiguration(XMaterial.NETHER_WART.parseItem(),
                        "Nether Warts",
                        getProductConfiguration(XMaterial.NETHER_WART.parseMaterial(), "Nether Warts"))
        );
    }

    private ProductCategoryConfiguration getFarmingProductCategoryConfiguration(Material icon, String name, ProductConfiguration... products) {
        return getFarmingProductCategoryConfiguration(new ItemStack(icon), name, products);
    }

    private ProductCategoryConfiguration getFarmingProductCategoryConfiguration(ItemStack icon, String name, ProductConfiguration... products) {
        return getProductCategoryConfiguration(icon, name, products, ChatColor.YELLOW);
    }

    private List<ProductCategoryConfiguration> getDefaultMiningProductCategories() {
        return Arrays.asList(
                getMiningProductCategoryConfiguration(Material.COBBLESTONE, "Cobblestone",
                        getProductConfiguration(Material.COBBLESTONE, "Cobblestone"),
                        getProductConfiguration(Material.STONE, "Stone")),
                getMiningProductCategoryConfiguration(Material.COAL, "Coal",
                        getProductConfiguration(Material.COAL, "Coal"),
                        getProductConfiguration(Material.COAL_BLOCK, "Coal Block")),
                getMiningProductCategoryConfiguration(Material.IRON_INGOT, "Iron",
                        getProductConfiguration(Material.IRON_INGOT, "Iron Ingot"),
                        getProductConfiguration(Material.IRON_BLOCK, "Iron Block")),
                getMiningProductCategoryConfiguration(Material.GOLD_INGOT, "Gold",
                        getProductConfiguration(Material.GOLD_INGOT, "Gold Ingot"),
                        getProductConfiguration(Material.GOLD_BLOCK, "Gold Block")),
                getMiningProductCategoryConfiguration(Material.DIAMOND, "Diamond",
                        getProductConfiguration(Material.DIAMOND, "Diamond"),
                        getProductConfiguration(Material.DIAMOND_BLOCK, "Diamond Block")),
                getMiningProductCategoryConfiguration(XMaterial.LAPIS_LAZULI.parseItem(), "Lapis",
                        getProductConfiguration(XMaterial.LAPIS_LAZULI.parseItem(), "Lapis"),
                        getProductConfiguration(Material.LAPIS_BLOCK, "Lapis Block")),
                getMiningProductCategoryConfiguration(Material.EMERALD, "Emerald",
                        getProductConfiguration(Material.EMERALD, "Emerald"),
                        getProductConfiguration(Material.EMERALD_BLOCK, "Emerald Block")),
                getMiningProductCategoryConfiguration(Material.REDSTONE, "Redstone",
                        getProductConfiguration(Material.REDSTONE, "Redstone"),
                        getProductConfiguration(Material.REDSTONE_BLOCK, "Redstone Block")),
                getMiningProductCategoryConfiguration(Material.QUARTZ, "Quartz",
                        getProductConfiguration(Material.QUARTZ, "Quartz"),
                        getProductConfiguration(Material.QUARTZ_BLOCK, "Quartz Block")),
                getMiningProductCategoryConfiguration(Material.OBSIDIAN, "Obsidian",
                        getProductConfiguration(Material.OBSIDIAN, "Obsidian")),
                getMiningProductCategoryConfiguration(Material.GLOWSTONE_DUST, "Glowstone",
                        getProductConfiguration(Material.GLOWSTONE_DUST, "Glowstone Dust"),
                        getProductConfiguration(Material.GLOWSTONE, "Glowstone")),
                getMiningProductCategoryConfiguration(Material.FLINT, "Flint & Gravel",
                        getProductConfiguration(Material.FLINT, "Flint"),
                        getProductConfiguration(Material.GRAVEL, "Gravel"))
        );
    }

    private ProductCategoryConfiguration getMiningProductCategoryConfiguration(Material icon, String name, ProductConfiguration... products) {
        return getMiningProductCategoryConfiguration(new ItemStack(icon), name, products);
    }

    private ProductCategoryConfiguration getMiningProductCategoryConfiguration(ItemStack icon, String name, ProductConfiguration... products) {
        return getProductCategoryConfiguration(icon, name, products, ChatColor.AQUA);
    }

    private List<ProductCategoryConfiguration> getDefaultCombatProductCategories() {
        return Arrays.asList(
                getCombatProductCategoryConfiguration(Material.ROTTEN_FLESH, "Rotten Flesh",
                        getProductConfiguration(Material.ROTTEN_FLESH, "Rotten Flesh")),
                getCombatProductCategoryConfiguration(Material.BONE, "Bone",
                        getProductConfiguration(Material.BONE, "Bone"),
                        getProductConfiguration(Material.BONE, "Bone Meal")),
                getCombatProductCategoryConfiguration(Material.STRING, "String",
                        getProductConfiguration(Material.STRING, "String"),
                        getProductConfiguration(Material.SPIDER_EYE, "Spider Eye")),
                getCombatProductCategoryConfiguration(XMaterial.GUNPOWDER.parseMaterial(), "Gunpowder",
                        getProductConfiguration(XMaterial.GUNPOWDER.parseMaterial(), "Gunpowder")),
                getCombatProductCategoryConfiguration(Material.ENDER_PEARL, "Ender Pearl",
                        getProductConfiguration(Material.ENDER_PEARL, "Ender Pearl")),
                getCombatProductCategoryConfiguration(Material.GHAST_TEAR, "Ghast Tear",
                        getProductConfiguration(Material.GHAST_TEAR, "Ghast Tear")),
                getCombatProductCategoryConfiguration(Material.SLIME_BALL, "Slime Drops",
                        getProductConfiguration(Material.SLIME_BALL, "Slime Ball"),
                        getProductConfiguration(Material.MAGMA_CREAM, "Magma Cream")),
                getCombatProductCategoryConfiguration(Material.BLAZE_ROD, "Blaze Rod",
                        getProductConfiguration(Material.BLAZE_ROD, "Blaze Rod"),
                        getProductConfiguration(Material.BLAZE_POWDER, "Blaze Powder"))
        );
    }

    private ProductCategoryConfiguration getCombatProductCategoryConfiguration(Material icon, String name, ProductConfiguration... products) {
        return getCombatProductCategoryConfiguration(new ItemStack(icon), name, products);
    }

    private ProductCategoryConfiguration getCombatProductCategoryConfiguration(ItemStack icon, String name, ProductConfiguration... products) {
        return getProductCategoryConfiguration(icon, name, products, ChatColor.RED);
    }

    private List<ProductCategoryConfiguration> getDefaultWoodsFishesProductCategories() {
        return Arrays.asList(
                getWoodsFishesProductCategoryConfiguration(XMaterial.OAK_LOG.parseItem(), "Oak",
                        getProductConfiguration(XMaterial.OAK_LOG.parseItem(), "Oak Wood")),
                getWoodsFishesProductCategoryConfiguration(XMaterial.SPRUCE_LOG.parseItem(), "Spruce",
                        getProductConfiguration(XMaterial.SPRUCE_LOG.parseItem(), "Spruce Wood")),
                getWoodsFishesProductCategoryConfiguration(XMaterial.BIRCH_LOG.parseItem(), "Birch",
                        getProductConfiguration(XMaterial.BIRCH_LOG.parseItem(), "Birch Wood")),
                getWoodsFishesProductCategoryConfiguration(XMaterial.DARK_OAK_LOG.parseItem(), "Dark Oak",
                        getProductConfiguration(XMaterial.DARK_OAK_LOG.parseItem(), "Dark Oak Wood")),
                getWoodsFishesProductCategoryConfiguration(XMaterial.ACACIA_LOG.parseItem(), "Acacia",
                        getProductConfiguration(XMaterial.ACACIA_LOG.parseItem(), "Acacia Wood")),
                getWoodsFishesProductCategoryConfiguration(XMaterial.JUNGLE_LOG.parseItem(), "Jungle",
                        getProductConfiguration(XMaterial.JUNGLE_LOG.parseItem(), "Jungle Wood")),
                getWoodsFishesProductCategoryConfiguration(XMaterial.COD.parseItem(), "Fish",
                        getProductConfiguration(XMaterial.COD.parseItem(), "Raw Fish")),
                getWoodsFishesProductCategoryConfiguration(XMaterial.SALMON.parseItem(), "Salmon",
                        getProductConfiguration(XMaterial.SALMON.parseItem(), "Raw Salmon")),
                getWoodsFishesProductCategoryConfiguration(XMaterial.TROPICAL_FISH.parseItem(), "Clownfish",
                        getProductConfiguration(XMaterial.TROPICAL_FISH.parseItem(), "Clownfish")),
                getWoodsFishesProductCategoryConfiguration(XMaterial.PUFFERFISH.parseItem(), "Pufferfish",
                        getProductConfiguration(XMaterial.PUFFERFISH.parseItem(), "Pufferfish")),
                getWoodsFishesProductCategoryConfiguration(Material.PRISMARINE_SHARD, "Prismarine",
                        getProductConfiguration(Material.PRISMARINE_SHARD, "Prismarine Shard"),
                        getProductConfiguration(Material.PRISMARINE_CRYSTALS, "Prismarine Crystals")),
                getWoodsFishesProductCategoryConfiguration(Material.CLAY, "Clay",
                        getProductConfiguration(Material.CLAY, "Clay"))
        );
    }

    private ProductCategoryConfiguration getWoodsFishesProductCategoryConfiguration(Material icon, String name, ProductConfiguration... products) {
        return getWoodsFishesProductCategoryConfiguration(new ItemStack(icon), name, products);
    }

    private ProductCategoryConfiguration getWoodsFishesProductCategoryConfiguration(ItemStack icon, String name, ProductConfiguration... products) {
        return getProductCategoryConfiguration(icon, name, products, ChatColor.GOLD);
    }

    private List<ProductCategoryConfiguration> getDefaultOdditiesProductCategories() {
        return Arrays.asList(
                getOdditiesProductCategoryConfiguration(XMaterial.EXPERIENCE_BOTTLE.parseMaterial(), "EXP Bottles",
                        getProductConfiguration(XMaterial.EXPERIENCE_BOTTLE.parseMaterial(), "Experience Bottle"))
        );
    }

    private ProductCategoryConfiguration getOdditiesProductCategoryConfiguration(Material icon, String name, ProductConfiguration... products) {
        return getOdditiesProductCategoryConfiguration(new ItemStack(icon), name, products);
    }

    private ProductCategoryConfiguration getOdditiesProductCategoryConfiguration(ItemStack icon, String name, ProductConfiguration... products) {
        return getProductCategoryConfiguration(icon, name, products, ChatColor.LIGHT_PURPLE);
    }

    public ProductCategoryConfiguration getProductCategoryConfiguration(ItemStack icon, String name, ProductConfiguration[] products, ChatColor color) {
        return new ProductCategoryConfiguration(ProductCategoryMenuConfiguration.createDefaultProductCategoryConfiguration(name, products.length),
                getDefaultProductCategoryIcon(icon, color + name),
                name,
                Arrays.asList(products));
    }

    public ProductConfiguration getProductConfiguration(ItemStack item, String name) {
        return new ProductConfiguration(item,
                ItemBuilder.newBuilder(item)
                        .appendLore("")
                        .appendLore("%product-lore%")
                        .appendLore("")
                        .appendLore(ChatColor.YELLOW + "Click to view details!")
                        .build(),
                name);
    }

    public ProductConfiguration getProductConfiguration(Material material, String name) {
        return getProductConfiguration(new ItemStack(material), name);
    }

    private ItemStack getDefaultProductCategoryIcon(ItemStack icon, String name) {
        return ItemBuilder.newBuilder(icon)
                .withName(name)
                .appendLore("%productcategory-lore%")
                .appendLore("")
                .appendLore(ChatColor.YELLOW + "Click to view products!")
                .build();
    }

    public SearchMenuConfiguration getSearchMenuConfiguration() {
        return (SearchMenuConfiguration) getConfig().get("search-menu");
    }

    public ProductMenuConfiguration getProductMenuConfiguration() {
        return (ProductMenuConfiguration) getConfig().get("product-menu");
    }

    public ConfirmationMenuConfiguration getConfirmationMenuConfiguration(OrderType orderType) {
        return (ConfirmationMenuConfiguration) getConfig().get("confirm-" + orderType.name().toLowerCase() + "-menu");
    }

    public OrdersMenuConfiguration getOrdersMenuConfiguration() {
        return (OrdersMenuConfiguration) getConfig().get("orders");
    }

    public List<CategoryConfiguration> getCategories() {
        return (List<CategoryConfiguration>) getConfig().getList("categories", new ArrayList<>());
    }
}
