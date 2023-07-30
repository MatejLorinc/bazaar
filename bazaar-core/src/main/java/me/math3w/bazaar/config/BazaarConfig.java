package me.math3w.bazaar.config;

import me.math3w.bazaar.bazaar.category.CategoryConfiguration;
import me.math3w.bazaar.bazaar.product.ProductConfiguration;
import me.math3w.bazaar.bazaar.productcategory.ProductCategoryConfiguration;
import me.math3w.bazaar.menu.configurations.CategoryMenuConfiguration;
import me.math3w.bazaar.menu.configurations.ProductCategoryMenuConfiguration;
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

        categories.add(createDefaultCategory(Material.GOLD_HOE, "&eFarming", ItemBuilder.newBuilder(Material.STAINED_GLASS_PANE).withData((short) 4).build(), getDefaultFarmingProductCategories()));
        categories.add(createDefaultCategory(Material.DIAMOND_PICKAXE, "&bMining", ItemBuilder.newBuilder(Material.STAINED_GLASS_PANE).withData((short) 9).build(), getDefaultMiningProductCategories()));
        categories.add(createDefaultCategory(Material.IRON_SWORD, "&cCombat", ItemBuilder.newBuilder(Material.STAINED_GLASS_PANE).withData((short) 14).build(), getDefaultCombatProductCategories()));
        categories.add(createDefaultCategory(Material.FISHING_ROD, "&6Woods & Fishes", ItemBuilder.newBuilder(Material.STAINED_GLASS_PANE).withData((short) 1).build(), getDefaultWoodsFishesProductCategories()));
        categories.add(createDefaultCategory(Material.ENCHANTMENT_TABLE, "&dOddities", ItemBuilder.newBuilder(Material.STAINED_GLASS_PANE).withData((short) 6).build(), getDefaultOdditiesProductCategories()));

        addDefault("categories", categories);
    }

    private CategoryConfiguration createDefaultCategory(Material icon, String name, ItemStack glass, List<ProductCategoryConfiguration> productCategories) {
        String colorizedName = Utils.colorize(name);
        return new CategoryConfiguration(getDefaultMenuConfiguration(colorizedName, glass),
                ItemBuilder.newBuilder(icon)
                        .withName(colorizedName)
                        .appendLore(ChatColor.DARK_GRAY + "Category", "", ChatColor.YELLOW + "Click to view!")
                        .build(),
                name,
                productCategories);
    }

    private CategoryMenuConfiguration getDefaultMenuConfiguration(String categoryName, ItemStack glass) {
        return CategoryMenuConfiguration.createDefaultConfiguration(ChatColor.getLastColors(categoryName) + "Bazaar ➜ " + categoryName, glass);
    }

    private List<ProductCategoryConfiguration> getDefaultFarmingProductCategories() {
        return Arrays.asList(
                getFarmingProductCategoryConfiguration(Material.WHEAT,
                        "Wheat & Seeds",
                        getProductConfiguration(Material.SEEDS, "Seeds"),
                        getProductConfiguration(Material.WHEAT, "Wheat"),
                        getProductConfiguration(Material.BREAD, "Bread")),
                getFarmingProductCategoryConfiguration(Material.CARROT_ITEM,
                        "Carrot",
                        getProductConfiguration(Material.CARROT_ITEM, "Carrot"),
                        getProductConfiguration(Material.GOLDEN_CARROT, "Golden Carrot")),
                getFarmingProductCategoryConfiguration(Material.POTATO_ITEM,
                        "Potato",
                        getProductConfiguration(Material.POTATO_ITEM, "Potato"),
                        getProductConfiguration(Material.BAKED_POTATO, "Baked Potato")),
                getFarmingProductCategoryConfiguration(Material.PUMPKIN,
                        "Pumpkin",
                        getProductConfiguration(Material.PUMPKIN, "Pumpkin"),
                        getProductConfiguration(Material.PUMPKIN_PIE, "Pumpkin Pie")),
                getFarmingProductCategoryConfiguration(Material.MELON,
                        "Melon",
                        getProductConfiguration(Material.MELON, "Melon"),
                        getProductConfiguration(Material.MELON_BLOCK, "Melon Block")),
                getFarmingProductCategoryConfiguration(Material.RED_MUSHROOM,
                        "Mushrooms",
                        getProductConfiguration(Material.RED_MUSHROOM, "Red Mushroom"),
                        getProductConfiguration(Material.BROWN_MUSHROOM, "Brown Mushroom")),
                getFarmingProductCategoryConfiguration(new ItemStack(Material.INK_SACK, 1, (short) 3),
                        "Cocoa Beans",
                        getProductConfiguration(new ItemStack(Material.INK_SACK, 1, (short) 3), "Cocoa Beans")),
                getFarmingProductCategoryConfiguration(Material.CACTUS,
                        "Cactus",
                        getProductConfiguration(Material.CACTUS, "Cactus"),
                        getProductConfiguration(new ItemStack(Material.INK_SACK, 1, (short) 3), "Cactus Green")),
                getFarmingProductCategoryConfiguration(Material.SUGAR_CANE,
                        "Sugar Cane",
                        getProductConfiguration(Material.SUGAR_CANE, "Sugar Cane"),
                        getProductConfiguration(Material.SUGAR, "Sugar")),
                getFarmingProductCategoryConfiguration(Material.LEATHER,
                        "Leather & Beef",
                        getProductConfiguration(Material.LEATHER, "Leather"),
                        getProductConfiguration(Material.RAW_BEEF, "Beef"),
                        getProductConfiguration(Material.COOKED_BEEF, "Steak")),
                getFarmingProductCategoryConfiguration(Material.PORK,
                        "Pork",
                        getProductConfiguration(Material.PORK, "Pork"),
                        getProductConfiguration(Material.PORK, "Grilled Pork")),
                getFarmingProductCategoryConfiguration(Material.RAW_CHICKEN,
                        "Chicken & Feather",
                        getProductConfiguration(Material.RAW_CHICKEN, "Chicken"),
                        getProductConfiguration(Material.COOKED_CHICKEN, "Cooked Chicken"),
                        getProductConfiguration(Material.FEATHER, "Feather"),
                        getProductConfiguration(Material.EGG, "Egg")),
                getFarmingProductCategoryConfiguration(Material.MUTTON,
                        "Mutton & Wool",
                        getProductConfiguration(Material.MUTTON, "Mutton"),
                        getProductConfiguration(Material.COOKED_MUTTON, "Cooked Mutton"),
                        getProductConfiguration(Material.WOOL, "Wool")),
                getFarmingProductCategoryConfiguration(Material.NETHER_STALK,
                        "Nether Warts",
                        getProductConfiguration(Material.NETHER_STALK, "Nether Warts"))
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
                getMiningProductCategoryConfiguration(new ItemStack(Material.INK_SACK, 1, (short) 4), "Lapis",
                        getProductConfiguration(new ItemStack(Material.INK_SACK, 1, (short) 4), "Lapis"),
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
                getCombatProductCategoryConfiguration(Material.SULPHUR, "Gunpowder",
                        getProductConfiguration(Material.SULPHUR, "Gunpowder")),
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
                getWoodsFishesProductCategoryConfiguration(new ItemStack(Material.LOG), "Oak",
                        getProductConfiguration(Material.LOG, "Oak Wood")),
                getWoodsFishesProductCategoryConfiguration(new ItemStack(Material.LOG, 1, (short) 1), "Spruce",
                        getProductConfiguration(new ItemStack(Material.LOG, 1, (short) 2), "Spruce Wood")),
                getWoodsFishesProductCategoryConfiguration(new ItemStack(Material.LOG, 1, (short) 2), "Birch",
                        getProductConfiguration(new ItemStack(Material.LOG, 1, (short) 2), "Birch Wood")),
                getWoodsFishesProductCategoryConfiguration(new ItemStack(Material.LOG_2, 1, (short) 1), "Dark Oak",
                        getProductConfiguration(new ItemStack(Material.LOG_2, 1, (short) 1), "Dark Oak Wood")),
                getWoodsFishesProductCategoryConfiguration(new ItemStack(Material.LOG_2), "Acacia",
                        getProductConfiguration(new ItemStack(Material.LOG_2), "Acacia Wood")),
                getWoodsFishesProductCategoryConfiguration(new ItemStack(Material.LOG, 1, (short) 3), "Jungle",
                        getProductConfiguration(new ItemStack(Material.LOG, 1, (short) 3), "Jungle Wood")),
                getWoodsFishesProductCategoryConfiguration(new ItemStack(Material.RAW_FISH), "Fish",
                        getProductConfiguration(new ItemStack(Material.RAW_FISH, 1), "Raw Fish")),
                getWoodsFishesProductCategoryConfiguration(new ItemStack(Material.RAW_FISH, 1, (short) 1), "Salmon",
                        getProductConfiguration(new ItemStack(Material.RAW_FISH, 1, (short) 1), "Raw Salmon")),
                getWoodsFishesProductCategoryConfiguration(new ItemStack(Material.RAW_FISH, 1, (short) 2), "Clownfish",
                        getProductConfiguration(new ItemStack(Material.RAW_FISH, 1, (short) 2), "Clownfish")),
                getWoodsFishesProductCategoryConfiguration(new ItemStack(Material.RAW_FISH, 1, (short) 3), "Pufferfish",
                        getProductConfiguration(new ItemStack(Material.RAW_FISH, 1, (short) 3), "Pufferfish")),
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
                getOdditiesProductCategoryConfiguration(Material.EXP_BOTTLE, "EXP Bottles",
                        getProductConfiguration(Material.EXP_BOTTLE, "Experience Bottle"))
        );
    }

    private ProductCategoryConfiguration getOdditiesProductCategoryConfiguration(Material icon, String name, ProductConfiguration... products) {
        return getOdditiesProductCategoryConfiguration(new ItemStack(icon), name, products);
    }

    private ProductCategoryConfiguration getOdditiesProductCategoryConfiguration(ItemStack icon, String name, ProductConfiguration... products) {
        return getProductCategoryConfiguration(icon, name, products, ChatColor.LIGHT_PURPLE);
    }

    private ProductCategoryConfiguration getProductCategoryConfiguration(ItemStack icon, String name, ProductConfiguration[] products, ChatColor color) {
        return new ProductCategoryConfiguration(ProductCategoryMenuConfiguration.createDefaultProductCategoryConfiguration(name, products.length),
                getDefaultProductCategoryIcon(icon, color + name),
                name,
                Arrays.asList(products));
    }

    private ProductConfiguration getProductConfiguration(ItemStack item, String name) {
        return new ProductConfiguration(ItemBuilder.newBuilder(item)
                .appendLore("")
                .appendLore("%product-lore%")
                .appendLore("")
                .appendLore(ChatColor.YELLOW + "Click to view details!")
                .build(), name);
    }

    private ProductConfiguration getProductConfiguration(Material material, String name) {
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

    public List<CategoryConfiguration> getCategories() {
        return (List<CategoryConfiguration>) getConfig().getList("categories", new ArrayList<>());
    }
}
